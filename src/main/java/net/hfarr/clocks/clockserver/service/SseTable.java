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
    emitter.send(
      SseEmitter.event()
        .name(EVENT_NAME)
        .data(table)
        .build()
    );
  }

  public void sendUpdatedTable(List<ClockRowModel> table) {
    try {

      for (final SseEmitter emitter : emitters) {
        if (completeEmitters.contains(emitter))
          continue;
        emit(emitter, table);
      }

      completeEmitters.forEach(emitters::remove);
      completeEmitters.clear();

      // emitters.forEach(emitter -> emitter.send("Ping") );
    } catch (IOException ioe) {
      log.error("Error sending event (Expected?) {}", ioe);
    } catch (Exception e) {
      log.error("Unexpected error! {}", e);
    }

  }

  public void markComplete(SseEmitter emitter) {

    completeEmitters.add(emitter);

  }

  // TODO temp
  public void addEmitter(SseEmitter emitter) {
    emitters.add(emitter);
  }

}
