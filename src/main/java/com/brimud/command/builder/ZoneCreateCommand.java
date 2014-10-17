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
class ZoneCreateCommand implements Command {

  static final String ZONE_CREATE = "zcreate";

  static final int MAX_ZONE_NAME_LENGTH = 30;

  private final MessageService messageService;
  
  private final World world;

  @Inject
  ZoneCreateCommand(MessageService messageService, World world) {
    this.messageService = messageService;
    this.world = world;
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

    Zone zone = world.getZoneById(arguments);
    if (zone != null) {
      messageService.sendMessage(player, "Zone " + arguments + " already exists!");
      return;
    }

    zone = new Zone(arguments, "Zone needs a name", "Zone needs a description");

    RoomId roomId = new RoomId(arguments, "room1");
    Room room = new Room();
    room.setId(roomId);

    player.setRoom(room);
  }
}
