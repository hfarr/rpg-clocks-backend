package net.hfarr.clocks.clockserver.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.hfarr.clocks.clockserver.model.ClockModel;
import net.hfarr.clocks.clockserver.model.ClockRowModel;
import net.hfarr.clocks.clockserver.service.SseTable;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/rows")
public class ClockRowsController {
  
  private List<ClockRowModel> globalRows;
  private final SseTable tableEventGenerator; // TODO do I like this name

  private final File database;
  private ObjectMapper objectMapper;

  @Autowired
  public ClockRowsController(SseTable tableEventEmitter) {

    tableEventGenerator = tableEventEmitter;
    database = new File("data/clocks.json");

    try {
      loadFromFile();
    } catch (IOException ioe) {
      log.error("Failed to load from file {}", ioe.getMessage());
    }

    log.info("initialized");

  }

  // Roughshod persistence. Slipknick. Lickspittle.
  private void loadFromFile() throws IOException {
    
    log.info("File path {}", database.getAbsolutePath());
    objectMapper = new ObjectMapper();

    if (!database.exists()) {

      database.createNewFile();

      File seedFile = new File("data/seed-clocks.json");
      Scanner sc = new Scanner(seedFile);
      BufferedWriter bw = new BufferedWriter(new FileWriter(database));
      bw.write(sc.nextLine());
      sc.close();
      bw.close();

    }

    globalRows = List.of( objectMapper.readValue(database, ClockRowModel[].class ) );

  }

  private synchronized void commitToFile() throws IOException {
    objectMapper.writeValue(database, globalRows);
  }

  private void emit() {
    tableEventGenerator.sendUpdatedTable(globalRows);
  }

  @PostMapping("/{id}/add")
  public ResponseEntity<String> addClock(@PathVariable Integer id, @RequestBody ClockModel clock) throws IOException {
    if ( id >= globalRows.size() || id < 0 ) {
      return ResponseEntity.badRequest().body("Row does not exist.");
    }

    ClockRowModel appendee = globalRows.get(id);

    appendee.addClock(clock);
    // TODO don't make response success dependent on DB saving?
    commitToFile();
    emit();

    return ResponseEntity.ok().build();
  }

  // TODO check what happens with controller methods when throwing checked exception
  @PostMapping("/add")
  public void addRow() throws IOException {

    globalRows.add(new ClockRowModel());
    commitToFile();
    emit();
  }

  @PutMapping("/set")
  public void setRows(@RequestBody List<ClockRowModel> newRows) throws IOException {
    if ( Objects.isNull(newRows) ) {
      // Is this possible?
      return;
    }

    this.globalRows = newRows;
    commitToFile();
    emit();
  }

  @GetMapping
  public List<ClockRowModel> getGlobalRows() {
    return globalRows;
  }

}
