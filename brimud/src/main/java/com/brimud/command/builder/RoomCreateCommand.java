/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.db.RoomDao;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.service.MessageService;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
class RoomCreateCommand implements Command {

  static final String ROOM_CREATE = "rcreate";
  
  private final MessageService messageService;
  private final RoomDao roomDao;
  
  @Inject
  RoomCreateCommand(MessageService messageService, RoomDao roomDao) {
    this.messageService = messageService;
    this.roomDao = roomDao;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    if (!ROOM_CREATE.equalsIgnoreCase(command)) {
      messageService.sendMessage(player, "Something odd happened. rcreate, but not rcreate");
      return;
    }
    
    if (arguments == null) {
      messageService.sendMessage(player, "I need a name!");
      return;
    }
    
    Room currentRoom = player.getRoom();
    RoomId roomId = new RoomId(currentRoom.getId().getZone(), arguments);
    
    Room newRoom = roomDao.getById(roomId);
    if (newRoom != null) {
      messageService.sendMessage(player, "Room " + roomId + " already exists!");
      return;
    }
    newRoom = new Room();
    newRoom.setId(roomId);
    roomDao.saveOrUpdate(newRoom);
    player.setRoom(newRoom);
  }
}
