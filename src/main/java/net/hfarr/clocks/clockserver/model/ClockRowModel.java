package net.hfarr.clocks.clockserver.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClockRowModel {
  
  public String name;
  public List<ClockModel> clocks;

  public ClockRowModel() {
    name = "";
    clocks = new ArrayList<>();
    
    // clocks.
  }

  public static ClockRowModel createEmptyRow(String name) {
    final ClockRowModel crm = new ClockRowModel();
    crm.name = name;
    
    return crm;
  }


  /**
   * Add a clock to the clock row
   * @param clock
   */
  public void addClock(ClockModel clock) {
    clocks.add(clock);
  }


  public void rearrangeClock() {
    // TODO implement
  }

}
