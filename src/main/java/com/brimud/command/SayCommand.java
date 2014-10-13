/**
 * 
 */
package com.brimud.command;

import java.util.Set;

import com.brimud.model.Player;
import com.brimud.service.MessageService;
import com.google.inject.Inject;

/**
 * @author dan
 * 
 */
class SayCommand implements Command {

  static final String SAY = "say";

  private final MessageService messageService;

  @Inject
  public SayCommand(MessageService messageService) {
    this.messageService = messageService;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.brimud.command.Command#doCommand(com.brimud.session.Session,
   * java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(final Player speakingPlayer, final String command, final String arguments) {
    if (arguments == null) {
      messageService.sendMessage(speakingPlayer, "Say what?");
    } else {
      Set<Player> players = speakingPlayer.getRoom().getPlayers();
      for (Player p : players) {
        if (!speakingPlayer.equals(p)) {
          messageService.sendMessage(p, speakingPlayer.getName() + " says '" + arguments + "'\n");
        } else {
          messageService.sendMessage(speakingPlayer, "You say '" + arguments + "'\n");
        }
      }
    }
  }

}
