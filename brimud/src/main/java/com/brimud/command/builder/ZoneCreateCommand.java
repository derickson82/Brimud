/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.db.RoomDao;
import com.brimud.db.ZoneDao;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.model.Zone;
import com.brimud.service.MessageService;
import com.google.inject.Inject;

/**
 * @author dan
 * 
 */
class ZoneCreateCommand implements Command {

  static final String ZONE_CREATE = "zcreate";

  static final int MAX_ZONE_NAME_LENGTH = 30;

  private final MessageService messageService;

  private final ZoneDao zoneDao;

  private final RoomDao roomDao;

  @Inject
  ZoneCreateCommand(MessageService messageService, ZoneDao zoneDao, RoomDao roomDao) {
    this.messageService = messageService;
    this.zoneDao = zoneDao;
    this.roomDao = roomDao;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player,
   * java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    if (!ZONE_CREATE.equalsIgnoreCase(command)) {
      messageService.sendMessage(player, "Something odd happened. zcreate, but not zcreate");
      return;
    }

    if (arguments == null) {
      messageService.sendMessage(player, "I need a name!");
      return;
    }

    if (arguments.length() > MAX_ZONE_NAME_LENGTH) {
      messageService.sendMessage(player, "Max lenght on zone names is " + MAX_ZONE_NAME_LENGTH);
      return;
    }

    Zone zone = zoneDao.getById(arguments);
    if (zone != null) {
      messageService.sendMessage(player, "Zone " + arguments + " already exists!");
      return;
    }

    zone = new Zone(arguments, "Zone needs a name", "Zone needs a description");
    zoneDao.saveOrUpdate(zone);

    RoomId roomId = new RoomId(arguments, "room1");
    Room room = new Room();
    room.setId(roomId);

    roomDao.saveOrUpdate(room);
    player.setRoom(room);
  }
}
