package net.hfarr.clocks.clockserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.hfarr.clocks.clockserver.model.ClockModel;
import net.hfarr.clocks.clockserver.model.ClockRowModel;

// @Slf4j
@CrossOrigin
@RestController
@RequestMapping("/rows")
public class ClockRowsController {
  
  private List<ClockRowModel> globalRows;

  public ClockRowsController() {

    // TODO load from file
    globalRows = new ArrayList<ClockRowModel>();

  }

  @PostMapping("/{id}/add")
  public ResponseEntity<String> addClock(@RequestParam Integer id, @RequestBody ClockModel clock) {
    if ( id >= globalRows.size() || id < 0 ) {
      return ResponseEntity.badRequest().body("Row does not exist.");
    }

    ClockRowModel appendee = globalRows.get(id);

    appendee.addClock(clock);

    // TODO send SSE
    return ResponseEntity.ok().build();
  }

  @PostMapping("/add")
  public void addRow(@RequestBody ClockModel clock) {

    globalRows.add(new ClockRowModel());
    // TODO send SSE
  }

  @PutMapping("/set")
  public void setRows(@RequestBody List<ClockRowModel> newRows) {
    if ( Objects.isNull(newRows) ) {
      // Is this possible?
      return;
    }

    this.globalRows = newRows;

    // TODO send SSE
  }

  @GetMapping
  public List<ClockRowModel> getGlobalRows() {
    return globalRows;
  }

}
