/**
 * 
 */
package com.brimud.command;

import javax.inject.Inject;

import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.service.MessageService;

/**
 * @author dan
 *
 */
// FIXME This class is public because it is reused in the Dig command. Move common functionality to a service.
public class MoveCommand implements Command {

  static final String MOVE = "move";
  
  private final MessageService messageService;
  
  @Inject
  MoveCommand(MessageService messageService) {
    this.messageService = messageService;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    Direction direction = Direction.fromName(command);
    Room room = player.getRoom();
    Room exit = room.getExit(direction);
    if (exit == null) {
      messageService.sendMessage(player, "You can't go that way!");
    } else {
      room.exit(player);
      for (Player p : room.getPlayers()) {
        if (!p.equals(player)) {
          messageService.sendMessage(p, player.getName() + " leaves " + direction.getName()  + ".\n");
        }
      }
      
      exit.enter(player);
      messageService.sendMessage(player, exit.look(player));
      for (Player p : exit.getPlayers()) {
        if (!p.equals(player)) {
          // FIXME these descriptions don't work perfectly
          messageService.sendMessage(p, player.getName() + " enters from the " + direction.opposite().getName() + "\n");
        }
      }
    }
  }
  
  

}
