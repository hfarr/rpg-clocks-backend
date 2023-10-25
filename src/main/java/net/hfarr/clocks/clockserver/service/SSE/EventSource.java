package net.hfarr.clocks.clockserver.service.SSE;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EventSource {
  
  public void addEmitter(SseEmitter emitter);

  public void markComplete(SseEmitter emitter);
}
