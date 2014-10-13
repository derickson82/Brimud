/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.command.Direction;
import com.brimud.command.MoveCommand;
import com.brimud.db.RoomDao;
import com.brimud.db.ZoneDao;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.model.World;
import com.brimud.model.Zone;
import com.brimud.service.MessageService;
import com.google.inject.Inject;

/**
 * @author dan
 * 
 */
class DigCommand implements Command {

  static final String DIG = "dig";

  private final MessageService messageService;
  private final World world;
  private final MoveCommand moveCommand;

  @Inject
  DigCommand(MessageService messageService, World world, MoveCommand moveCommand) {
    this.messageService = messageService;
    this.world = world;
    this.moveCommand = moveCommand;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player,
   * java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {

    if (!DIG.equalsIgnoreCase(command)) {
      messageService.sendMessage(player, "Something odd happened. Dig, but not dig");
      return;
    }

    if (arguments == null) {
      messageService.sendMessage(player, "Dig where?");
      return;
    }

    String[] splitArgs = arguments.split(" ");
    if (splitArgs.length != 2) {
      messageService.sendMessage(player,
          "Expected 'dig <direction> <room name>' Room name should not have any spaces. (e.g. w roomName)");
      return;
    }

    Direction d = Direction.fromName(splitArgs[0]);

    if (d == null) {
      messageService.sendMessage(player, "Expected 'dig <direction> <zone_name>.<room_name>' (e.g. dig w zone.room or just dig w room if digging in the same zone)");
      return;
    }

    Room currentRoom = player.getRoom();
    RoomId roomId = RoomId.fromString(splitArgs[1]);
    if (roomId == null) {
      roomId = new RoomId(currentRoom.getId().getZone(), splitArgs[1]);
    } else {
      // TODO might need to lock down digging to other zones by role
      Zone zone = world.getZoneById(roomId.getZoneId());
      if (zone == null) {
        messageService.sendMessage(player, "Zone " + roomId.getZoneId() + " must be created first.");
        return;
      }
    }
    
    Room room = world.getRoomById(roomId);
    if (room == null) {
      room = new Room();
      room.setId(roomId);
    }
    currentRoom.addExit(d, room);
    room.addExit(d.opposite(), currentRoom);
    moveCommand.doCommand(player, d.getName(), null);
  }
}
