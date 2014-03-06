package com.brimud.character.race;


public class Race {
  // TODO the subclasses of race should probably just be prototype objects, not necessarily enums
  // although, it might depend more on some of the other racial traits. Wait and see what to do.
  
  private String name;
  
  public Race(String name) {
    this.name = name;
  }
  
  public String toString() {
    return name;
  }
}
