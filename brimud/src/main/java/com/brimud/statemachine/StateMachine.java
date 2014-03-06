package com.brimud.statemachine;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class StateMachine {

  private Map<String, Method> stateMap = new HashMap<String, Method>();

  private String currentState;

  // execute is a no-op when in the done state (optional)
  private String doneState = null;

  private final Object stateMachine;

  public StateMachine(Object obj) {
    this.stateMachine = obj;
    Class<? extends Object> smClass = obj.getClass();

    EndState endState = smClass.getAnnotation(EndState.class);
    if (endState != null) {
      doneState = endState.value();
    }
    InitialState initState = smClass.getAnnotation(InitialState.class);
    if (initState == null) {
      throw new IllegalArgumentException(
          "obj must be annotated with a class level InitialState annotation");
    }

    currentState = initState.value();

    // load all state methods

    for (Method method : smClass.getMethods()) {
      State state = method.getAnnotation(State.class);
      if (state != null) {
        String stateValue = state.value();
        stateMap.put(stateValue, method);
      }
    }
  }

  public void execute(Object... args) {
    // dynamically pass through the arguments to the method.
    // All state methods must have the same signature
    if (!isDone()) {
      Method method = stateMap.get(currentState);
      if (method != null) {
        try {
          // expecting a string that is the next state backs
          currentState = (String)method.invoke(stateMachine, args);
        } catch (Throwable e) {
          throw new IllegalStateException("Error invoking method: " + method
              + " on " + stateMachine + " with args: " + args, e);

        }
      }
    }
  }
  
  public boolean isDone() {
    return currentState != null ? currentState.equals(doneState) : doneState == null;
  }
}
