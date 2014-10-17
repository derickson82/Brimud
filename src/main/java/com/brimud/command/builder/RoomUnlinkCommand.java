/**
 * 
 */
package com.brimud.command.builder;

import javax.inject.Inject;

import com.brimud.command.Command;
import com.brimud.command.Direction;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.service.MessageService;

/**
 * @author dan
 *
 */
class RoomUnlinkCommand implements Command {

 static final String ROOM_UNLINK = "runlink";
  
  private final MessageService messageService;
  
  @Inject
  RoomUnlinkCommand(MessageService messageService) {
    this.messageService = messageService;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    if (!ROOM_UNLINK.equalsIgnoreCase(command)) {
      messageService.sendMessage(player, "Something odd happened. runlink, but not runlink");
      return;
    }
    
    if (arguments == null) {
      messageService.sendMessage(player, "Unlink which direction?");
      return;
    }
    
    Direction d = Direction.fromName(arguments);
    
    if (d == null) {
      messageService.sendMessage(player, "Expected 'runlink <direction>' (e.g. runlink w)");
      return;
    }
    
    Room currentRoom = player.getRoom();
    currentRoom.removeExit(d);
  }

}
