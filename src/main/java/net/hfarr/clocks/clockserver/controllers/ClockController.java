package net.hfarr.clocks.clockserver.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.hfarr.clocks.clockserver.model.ClockModel;

@Slf4j
@CrossOrigin
@RestController
public class ClockController {

  private ClockModel clock;

  public ClockController() {

    this.clock = ClockModel.builder()
      .segments(8)
      .progress(3)
      .build();
  }

  @GetMapping(path = "/clock")
  public ClockModel getClock() {

    log.info("Request received: GET /clock");
    
    return this.clock;
  }

  @PutMapping(path = "/clock")
  public ClockModel putClock(ClockModel newClock) {
    log.info("Request received: PUT /clock");
    this.clock = newClock;
    return this.clock;
  }
  
}
  
  
