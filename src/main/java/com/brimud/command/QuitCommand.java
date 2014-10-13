/**
 * 
 */
package com.brimud.command;

import com.brimud.model.Player;
import com.brimud.session.Session;
import com.brimud.session.SessionManager;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
class QuitCommand implements Command {

  static final String QUIT = "quit";

  private final SessionManager sessionManager;
  
  @Inject
  public QuitCommand(SessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.session.Session, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    Session session = sessionManager.get(player);
    player.getRoom().exit(player);
    session.quit("Thanks for playing!\n");
  }

}
