/**
 * 
 */
package com.brimud.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.brimud.model.Player;
import com.google.inject.Singleton;

/**
 * @author dan
 * 
 */
@Singleton
public class SessionManager {

  private static final Logger logger = Logger.getLogger(SessionManager.class);

  private final Map<String, Session> sessions = Collections.synchronizedMap(new HashMap<String, Session>());

  public void sendMessage(Player p, String message) {
    sendMessage(p.getName(), message);
  }

  public void sendMessage(String playerName, String message) {
    Session s = sessions.get(playerName);
    if (s != null) {
      s.sendMessage(message);
    } else {
      logger.warn("No session associated with player: " + playerName);
    }
  }

  public Session get(Player player) {
    return sessions.get(player.getName());
  }

  public Session get(String playerName) {
    return sessions.get(playerName);
  }

  public static interface SessionVisitor {
    void visit(Session session);
  }

  public void each(SessionVisitor sessionVisitor) {
    synchronized (sessions) {
      for (Session session : sessions.values()) {
        sessionVisitor.visit(session);
      }
    }
  }

  public void init(Session session) {
    if (session.getAccount().getPlayer() != null) {
      sessions.put(session.getAccount().getPlayer().getName(), session);
    } else {
      logger.warn("Attempted to add session with null player name! " + session);
    }
  }

  public void closing(Session session) {
    sessions.remove(session);
  }
}
