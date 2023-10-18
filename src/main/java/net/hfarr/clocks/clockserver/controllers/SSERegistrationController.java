package net.hfarr.clocks.clockserver.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/sse")
public class SSERegistrationController {
  
  private List<SseEmitter> emitters = new ArrayList<>();

  
  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter registerSSE(HttpEntity<Object> entity) {
    SseEmitter emitter = new SseEmitter();
    emitters.add(emitter);

    log.info("Stub implementation");
    return emitter;
  }
}
