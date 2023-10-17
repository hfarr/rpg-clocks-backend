package net.hfarr.clocks.clockserver.model;

import lombok.Data;

@Data
public class ClockModel {

  private String name;

  private int segments;
  private int progress;

}
