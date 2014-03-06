/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.command.Direction;
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
class RoomLinkCommand implements Command {

  static final String ROOM_LINK = "rlink";
  
  private final MessageService messageService;
  private final RoomDao roomDao;
  
  @Inject
  RoomLinkCommand(MessageService messageService, RoomDao roomDao) {
    this.messageService = messageService;
    this.roomDao = roomDao;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    if (!ROOM_LINK.equalsIgnoreCase(command)) {
      messageService.sendMessage(player, "Something odd happened. rlink, but not rlink");
      return;
    }
    
    if (arguments == null) {
      messageService.sendMessage(player, "Link what?");
      return;
    }
    
    String[] splitArgs = arguments.split(" ");
    if (splitArgs.length != 2) {
      messageService.sendMessage(player, "Expected 'rlink <direction> <room name>' Room name should not have any spaces. (e.g. w roomName)");
      return;
    }
    
    Direction d = Direction.fromName(splitArgs[0]);
    
    if (d == null) {
      messageService.sendMessage(player, "Expected 'rlink <direction> <room name>' (e.g. rlink w room)");
      return;
    }
    
    RoomId roomId = RoomId.fromString(splitArgs[1]);
    Room currentRoom = player.getRoom();
    if (roomId == null) {
      roomId = new RoomId(currentRoom.getId().getZone(), splitArgs[1]);
    }
    
    Room room = roomDao.getById(roomId);
    if (room == null) {
      messageService.sendMessage(player, "Can only link to rooms that already exist. " + roomId + " does not exist.");
      return;
    }
    currentRoom.addExit(d, room);
  }

}
