/**
 * 
 */
package com.brimud.session;


/**
 * @author dan
 * 
 */
public class LoginContext implements State {

  private boolean loggedIn = false;
  private State currentState;

  LoginContext() {
    currentState = ACCOUNTNAME;
  }

  @Override
  public void handleCommand(Session session, String command) {
    currentState.handleCommand(session, command);
  }

  @Override
  public String getMessage() {
    return currentState.getMessage();
  }
  
  boolean isLoggedIn() {
    return loggedIn;
  }

  private final State ACCOUNTNAME = new State() {
    @Override
    public String getMessage() {
      return "Type 'register' to create a new account.\nWhat is your login id? ";
    }

    @Override
    public void handleCommand(Session session, String command) {
      final State nextState;
      if ("register".equals(command)) {
        nextState = REGISTER;
      } else {
        nextState = ACCOUNTNAME;
      }
      currentState = nextState;
    }
  };

  private final State REGISTER = new State() {
    @Override
    public String getMessage() {
      return "What will be your login id? ";
    }
    
    @Override
    public void handleCommand(Session session, String command) {
      currentState = PASSWORD;
    }
  };

  private final State PASSWORD = new State() {
    public String getMessage() {
      return "Password? ";
    };
    
    @Override
    public void handleCommand(Session session, String command) {
      currentState = LOGGED_IN;
    }
  };
  
  private final State LOGGED_IN = new State() {
    public String getMessage() {
      return "Logged in";
    }
    
    public void handleCommand(Session session, String command) {
    };
  };
}
