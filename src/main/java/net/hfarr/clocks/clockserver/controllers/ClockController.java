package net.hfarr.clocks.clockserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.hfarr.clocks.clockserver.model.ClockModel;

@Slf4j
@RestController
public class ClockController {

  @GetMapping(path = "/clock")
  public ClockModel getClock() {

    log.info("Request received: /clock");
    final ClockModel result = ClockModel.builder()
      .segments(8)
      .progress(3)
      .build();
    

    return result;
  }

  
  
}
  
  
