package net.hfarr.clocks.clockserver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClockModel {

  private String name;

  private int segments;
  private int progress;

}
