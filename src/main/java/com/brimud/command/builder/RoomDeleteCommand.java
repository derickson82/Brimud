/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.model.World;
import com.brimud.service.BuilderException;
import com.brimud.service.MessageService;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
class RoomDeleteCommand implements Command {
  
  static final String ROOM_DELETE = "rdelete";
  
  private final MessageService messageService;
  private final World world;
  
  @Inject
  RoomDeleteCommand(MessageService messageService, World world) {
    this.messageService = messageService;
    this.world = world;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    // TODO Auto-generated method stub
    if (!ROOM_DELETE.equalsIgnoreCase(command)) {
      messageService.sendMessage(player, "Something strange happened. rdelete, but not rdelete");
      return;
    }
    
    Room currentRoom = player.getRoom();
    RoomId idToDelete = RoomId.fromString(arguments);
    if (idToDelete == null) {
      idToDelete = new RoomId(currentRoom.getId().getZone(), arguments);
    }
    
    try {
    	world.deleteRoom(idToDelete);
    } catch (BuilderException e) {
    	messageService.sendMessage(player, e.getMessage());
    }
  }

}
