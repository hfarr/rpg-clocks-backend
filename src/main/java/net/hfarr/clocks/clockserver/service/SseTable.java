package net.hfarr.clocks.clockserver.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;
import net.hfarr.clocks.clockserver.model.ClockRowModel;
import net.hfarr.clocks.clockserver.service.SSE.EventSource;

@Slf4j
@Service  // Global Table Emitter (TODO for now)
public class SseTable implements EventSource {

  private List<SseEmitter> emitters;
  private Set<SseEmitter> completeEmitters = new HashSet<>();

  private static final String EVENT_NAME = "tableUpdate";
  
  public SseTable() {
    emitters = new ArrayList<>();
  }

  private void emit(SseEmitter emitter, List<ClockRowModel> table) throws IOException {
    if (completeEmitters.contains(emitter)) {
      return;
    }
    // build sent object first?
    emitter.send(
      SseEmitter.event()
        .name(EVENT_NAME)
        .data(table)
        .build()
    );
  }
  
  private void tryEmit(SseEmitter emitter, List<ClockRowModel> table) {
    try {

      emit(emitter, table);
      // emitters.forEach(emitter -> emitter.send("Ping") );
    } catch (IOException ioe) {
      log.error("Error sending event (Expected?) {}", ioe);
    } catch (Exception e) {
      log.error("Unexpected error! {}", e.getMessage());
      // e.printStackTrace();
    }
  }

  public void sendUpdatedTable(List<ClockRowModel> table) {
    for (final SseEmitter emitter : emitters) {
      tryEmit(emitter, table);
    }

    // housekeeping
    completeEmitters.forEach(emitters::remove);
    completeEmitters.clear();

  }

  public void markComplete(SseEmitter emitter) {

    completeEmitters.add(emitter);

  }

  // TODO temp
  public void addEmitter(SseEmitter emitter) {
    emitters.add(emitter);
  }

}
