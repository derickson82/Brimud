package com.brimud.statemachine;

@InitialState("first")
@EndState("last")
public class SimpleStateMachine {
  
  String value = null;
  boolean executedFirst = false;
  boolean executedSecond = false;
  
  @State("first")
  public String first(String value) {
    this.value = value;
    executedFirst = true;
    return "second";
  }
  
  @State("second")
  public String second(String value) {
    this.value = value;
    executedSecond = true;
    return "last";
  }
  
}
