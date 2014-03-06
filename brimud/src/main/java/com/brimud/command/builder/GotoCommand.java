/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.db.PlayerDao;
import com.brimud.db.RoomDao;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.service.MessageService;
import com.brimud.util.Preconditions;
import com.brimud.util.StringUtil;
import com.google.inject.Inject;

/**
 * @author dan
 * 
 */
class GotoCommand implements Command {

  static final String GOTO = "goto";
	
  private final MessageService messageService;

  private final RoomDao roomDao;

  private final PlayerDao playerDao;

  @Inject
  GotoCommand(MessageService messageService, RoomDao roomDao, PlayerDao playerDao) {
    this.messageService = messageService;
    this.roomDao = roomDao;
    this.playerDao = playerDao;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player,
   * java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    Preconditions.checkNotNull(player, "player");
    Preconditions.checkNotNull(command, "command");
    arguments = StringUtil.trimToNull(arguments);
    if (arguments == null) {
      messageService.sendMessage(player, "Goto where? who?");
      return;
    }
    
    // includes the zone id as well
    RoomId roomId = RoomId.fromString(arguments);
    Room currentRoom = player.getRoom();
    if (roomId == null) {
      roomId = new RoomId(currentRoom.getId().getZone(), arguments);
    }
    Room gotoRoom = roomDao.getById(roomId);
    if (gotoRoom == null) {
      Player gotoPlayer = playerDao.getById(arguments);
      if (gotoPlayer != null) {
        gotoRoom = gotoPlayer.getRoom();
      }
    }
    

    if (gotoRoom != null) {
      currentRoom.exit(player);
      gotoRoom.enter(player);
      messageService.sendMessage(player, gotoRoom.look(player));
    } else {
      messageService.sendMessage(player, "You can't go there!");
    }

  }

}
