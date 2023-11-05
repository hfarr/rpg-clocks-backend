package net.hfarr.clocks.clockserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClockModel {

  private String name;

  private int segments;
  private int progress;

}
