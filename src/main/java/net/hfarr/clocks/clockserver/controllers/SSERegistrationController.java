package net.hfarr.clocks.clockserver.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;
import net.hfarr.clocks.clockserver.service.SsePinger;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/sse")
public class SSERegistrationController {
  
  private final SsePinger pinger;
  
  @Autowired
  public SSERegistrationController(SsePinger pinger) {
    this.pinger = pinger;
  }

  
  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter registerSSE(HttpEntity<Object> entity) {
    SseEmitter emitter = new SseEmitter();
    pinger.addEmitter(emitter);

    log.info("Stub implementation");
    return emitter;
  }
}
