/**
 * 
 */
package com.brimud.service;

import javax.inject.Inject;

import com.brimud.model.Player;
import com.brimud.session.SessionManager;

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
