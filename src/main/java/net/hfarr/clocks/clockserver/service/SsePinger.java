package net.hfarr.clocks.clockserver.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SsePinger {
  
  private List<SseEmitter> emitters;
  private Set<SseEmitter> completeEmitters = new HashSet<>();
  private Thread pingThread;
  
  public SsePinger() {
    emitters = new ArrayList<>();
    this.pingThread = new Thread(this::doPings);
    pingThread.setName("Pinger");
    pingThread.start();
  }

  private void doPings() {
    while (true) {
      try {
        
        Thread.sleep(1000);
        // log.info("Ping!");
        for (final SseEmitter emitter : emitters) {

          if (completeEmitters.contains(emitter)) continue;

          emitter.send("Ping");
        }

        completeEmitters.forEach(emitters::remove);
        completeEmitters.clear();

        // emitters.forEach(emitter -> emitter.send("Ping") );
      } catch (IOException ioe) {
        log.error("Error sending event", ioe);
      } catch (InterruptedException ie) {
        log.error("Pinger thread interrupted", ie);
        break;
      } catch (Exception e) {
        log.error("Ping thread crashed for unexpected reason", e);
      }
    }

    log.info("Pinger stopped");

  }

  // TODO temp
  public void addEmitter(SseEmitter emitter) {
    // emitter.onCompletion( () -> completeEmitters.add(emitter) );
    emitter.onCompletion( () -> { 
      completeEmitters.add(emitter);
      log.info("Emitter complete", emitter);
    });
    emitters.add(emitter);
  }

  public boolean isRunning() {
    return pingThread.isAlive();
  }

  private void sendMessage(SseEmitter sse, String message) throws IOException {
    if (completeEmitters.contains(sse)) return;
    sse.send(message);
  }

  public void sendMessage(String message) {
    for (final SseEmitter emitter : emitters) {
      try {
        sendMessage(emitter, message);
      } catch (IOException ioe) {
        log.error("Error sending message :(", ioe);
      }
    }
  }

}
