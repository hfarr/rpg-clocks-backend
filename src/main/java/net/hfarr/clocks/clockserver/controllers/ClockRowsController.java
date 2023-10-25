package net.hfarr.clocks.clockserver.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.hfarr.clocks.clockserver.model.ClockModel;
import net.hfarr.clocks.clockserver.model.ClockRowModel;

@Slf4j
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
  public void addClock(@RequestParam Integer id, @RequestBody ClockModel clock) {

    ClockRowModel appendee = globalRows.get(id);

    appendee.addClock(clock);

    // TODO send SSE
  }

  @PostMapping("/add")
  public void addRow(@RequestBody ClockModel clock) {
    globalRows.add(new ClockRowModel());
    // TODO send SSE
  }

  @PutMapping("/set")
  public void setRows(@RequestBody List<ClockRowModel> newRows) {
    this.globalRows = newRows;

    // TODO send SSE
  }

  @GetMapping
  public List<ClockRowModel> getGlobalRows() {
    return globalRows;
  }

}
