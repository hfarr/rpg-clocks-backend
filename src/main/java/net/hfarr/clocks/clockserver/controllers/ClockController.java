package net.hfarr.clocks.clockserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClockController {

  @GetMapping(path = "/clock")
  public String getClock() {
    return "Hello, world!";
  }
  
}
  
  
