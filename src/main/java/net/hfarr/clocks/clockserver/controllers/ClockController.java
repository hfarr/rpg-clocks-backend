package net.hfarr.clocks.clockserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.hfarr.clocks.clockserver.model.ClockModel;
import net.hfarr.clocks.clockserver.service.SsePinger;

// @RequestMapping("/api/clocks")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/clock")
public class ClockController {

  private ClockModel clock;

  // temp
  private SsePinger pinger;

  @Autowired
  public ClockController(SsePinger pinger) {

    this.clock = ClockModel.builder()
      .segments(8)
      .progress(3)
      .build();

    this.pinger = pinger;
  }

  // @GetMapping(path = "/clock")
  @GetMapping
  public ClockModel getClock() {

    log.info("Request received: GET /clock");
    
    return this.clock;
  }

  // @PutMapping(path = "/clock")
  @PutMapping
  public ClockModel putClock(@RequestBody ClockModel newClock) {
    log.info("Request received: PUT /clock");
    this.clock = newClock;
    // very proper json serialization
    pinger.sendMessage("{\"segments\":" + clock.getSegments() + 
      ", \"progress\": " + clock.getProgress() + "}");
    return this.clock;
  }
  
}
