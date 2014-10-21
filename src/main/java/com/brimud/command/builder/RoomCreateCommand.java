/**
 * 
 */
package com.brimud.command.builder;

import javax.inject.Inject;

import com.brimud.command.Command;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.model.World;
import com.brimud.model.Zone;
import com.brimud.service.MessageService;

/**
 * @author dan
 *
 */
class RoomCreateCommand implements Command {

  static final String ROOM_CREATE = "rcreate";
  
  private final MessageService messageService;
  private final World world;
  
  @Inject
  RoomCreateCommand(MessageService messageService, World world) {
    this.messageService = messageService;
    this.world = world;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String roomName) {
    if (!ROOM_CREATE.equalsIgnoreCase(command)) {
      messageService.sendMessage(player, "Something odd happened. rcreate, but not rcreate");
      return;
    }
    
    if (roomName == null) {
      messageService.sendMessage(player, "I need a name! Give me a name!");
      return;
    }
    
    Room currentRoom = player.getRoom();
    Zone currentZone = currentRoom.getId().getZone();
    RoomId roomId = new RoomId(currentZone, roomName);
    
    Room newRoom = world.getRoomById(roomId);
    if (newRoom != null) {
      messageService.sendMessage(player, "Room " + roomId + " already exists!");
      return;
    }
    newRoom = new Room();
    newRoom.setId(roomId);
    currentZone.addRoom(newRoom);
    player.setRoom(newRoom);
  }
}
