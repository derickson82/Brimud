/**
 * 
 */
package com.brimud.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.brimud.command.Direction;
import com.brimud.db.PlayerDao;
import com.brimud.model.Player;
import com.brimud.session.Session;
import com.google.inject.Inject;

/**
 * This filter is intended to go at the end of the filter chain, so the prompt will be output last
 * @author dan
 */
public class PromptFilter implements Filter {

  private final PlayerDao playerDao;
  
  @Inject
  PromptFilter(PlayerDao playerDao) {
    this.playerDao = playerDao;
  }
  
  @Override
  public void doFilter(Session session, String command, FilterChain filterChain) {
    filterChain.doFilter(session, command);
    if (session.isAuthenticated() 
        && !session.isQuit() && session.getAccount().getPlayer() != null) {
      StringBuilder b = new StringBuilder();
      b.append("\n");
      Player player = playerDao.getById(session.getAccount().getPlayer().getName());
      b.append(player.getRoom().getId()).append(" ");
      List<Direction> exits = new ArrayList<Direction>(player.getRoom().getExits().keySet());
      Collections.sort(exits);
      for (Direction d : exits) {
        b.append(d.name().charAt(0));
      }
      b.append(">");
      session.addMessage(b.toString());
    }
  }

}
