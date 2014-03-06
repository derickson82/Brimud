/**
 * 
 */
package com.brimud.session;

public interface State {
  String getMessage();
  void handleCommand(Session session, String command);
}