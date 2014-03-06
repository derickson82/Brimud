/**
 * 
 */
package com.brimud.command.builder;

import java.util.HashMap;
import java.util.Map;

import com.brimud.command.Command;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * @author dan
 *
 */
public class BuilderCommandModule extends AbstractModule {


  /* (non-Javadoc)
   * @see com.google.inject.AbstractModule#configure()
   */
  @Override
  protected void configure() {
    // TODO Auto-generated method stub

  }
  
  @Singleton
  @Provides
  @BuilderCommands
  Map<String, Command> builderCommands(DigCommand digCommand,
      RoomIdCommand roomIdCommand,
      RoomDescCommand descCommand,
      RoomNameCommand roomNameCommand,
      RoomLinkCommand roomLinkCommand,
      RoomUnlinkCommand roomUnlinkCommand,
      RoomCreateCommand roomCreateCommand,
      RoomDeleteCommand roomDeleteCommand,
      RoomShortCommand roomShortCommand,
      RoomScanDescCommand roomScanDescCommand,
      RoomExtraDescriptionCommand roomExtraCommand,
      GotoCommand gotoCommand,
      ZoneStartingRoomCommand zoneStartingRoomCommand,
      ZoneCreateCommand zoneCreateCommand) {
    Map<String, Command> commands = new HashMap<String, Command>();

    // builder commands
    commands.put(RoomDeleteCommand.ROOM_DELETE, roomDeleteCommand);
    commands.put(DigCommand.DIG, digCommand);
    commands.put(RoomIdCommand.ROOM_ID, roomIdCommand);
    commands.put(RoomDescCommand.ROOM_DESCRIPTION, descCommand);
    commands.put(RoomShortCommand.ROOM_SHORT_DESCRIPTION, roomShortCommand);
    commands.put(RoomScanDescCommand.ROOM_SCAN_DESC, roomScanDescCommand);
    commands.put(RoomNameCommand.ROOM_NAME, roomNameCommand);
    commands.put(RoomLinkCommand.ROOM_LINK, roomLinkCommand);
    commands.put(RoomUnlinkCommand.ROOM_UNLINK, roomUnlinkCommand);
    commands.put(RoomCreateCommand.ROOM_CREATE, roomCreateCommand);
    commands.put(RoomExtraDescriptionCommand.ROOM_EXTRA, roomExtraCommand);
    commands.put(GotoCommand.GOTO, gotoCommand);
    commands.put(ZoneStartingRoomCommand.ZONE_STARTING_ROOM, zoneStartingRoomCommand);
    commands.put(ZoneCreateCommand.ZONE_CREATE, zoneCreateCommand);

    return commands;
  }

}
