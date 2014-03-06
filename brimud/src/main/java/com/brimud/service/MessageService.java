/**
 * 
 */
package com.brimud.service;

import com.brimud.model.Player;
import com.brimud.session.SessionManager;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
public class MessageService {

  private final SessionManager sessionManager;
  
  @Inject
  public MessageService(SessionManager world) {
    this.sessionManager = world;
  }
  
  public void sendMessage(Player player, String message) {
    sessionManager.sendMessage(player, message);
  }
  
  public void sendMessage(String playerName, String message) {
    sessionManager.sendMessage(playerName, message);
  }
}
