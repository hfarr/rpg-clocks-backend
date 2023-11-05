package net.hfarr.clocks.clockserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @Autowired
  public ClockRowsController(SseTable tableEventEmitter) {

    // TODO load from file
    globalRows = new ArrayList<ClockRowModel>();
    globalRows.add(ClockRowModel.createEmptyRow("Row 1"));
    globalRows.add(ClockRowModel.createEmptyRow("Row 2"));
    globalRows.get(0).addClock(ClockModel.builder()
        .segments(8)
        .progress(2)
        .build());
    globalRows.get(0).addClock(ClockModel.builder()
        .segments(6)
        .progress(2)
        .build());
    
    tableEventGenerator = tableEventEmitter;

    log.info("initialized");

  }

  private void emit() {
    tableEventGenerator.sendUpdatedTable(globalRows);
  }

  @PostMapping("/{id}/add")
  public ResponseEntity<String> addClock(@PathVariable Integer id, @RequestBody ClockModel clock) {
    if ( id >= globalRows.size() || id < 0 ) {
      return ResponseEntity.badRequest().body("Row does not exist.");
    }

    ClockRowModel appendee = globalRows.get(id);

    appendee.addClock(clock);
    emit();

    return ResponseEntity.ok().build();
  }

  @PostMapping("/add")
  public void addRow() {

    globalRows.add(new ClockRowModel());
    emit();
  }

  @PutMapping("/set")
  public void setRows(@RequestBody List<ClockRowModel> newRows) {
    if ( Objects.isNull(newRows) ) {
      // Is this possible?
      return;
    }

    this.globalRows = newRows;
    emit();
  }

  @GetMapping
  public List<ClockRowModel> getGlobalRows() {
    return globalRows;
  }

}
