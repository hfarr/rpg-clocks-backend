package net.hfarr.clocks.clockserver.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SsePinger {
  
  private List<SseEmitter> emitters;
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
        log.info("Ping!");
        for (final SseEmitter emitter : emitters) {
          emitter.send("Ping");
        }
        // emitters.forEach(emitter -> emitter.send("Ping") );
      } catch (IOException ioe) {
        log.error("Error sending event", ioe);
      } catch (InterruptedException ie) {
        log.error("Pinger thread interrupted", ie);
        break;
      }
    }
  }

  // TODO temp
  public void addEmitter(SseEmitter emitter) {
    emitters.add(emitter);
  }

  public boolean isRunning() {
    return pingThread.isAlive();
  }

}
