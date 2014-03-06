/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.command.Direction;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.service.MessageService;
import com.brimud.util.StringUtil;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
class RoomScanDescCommand implements Command {

  static final String ROOM_SCAN_DESC = "rscandesc";
  
  private final int DESCRIPTION_MAX_LENGTH = 256;
  private final MessageService messageService;
  
  @Inject
  RoomScanDescCommand(MessageService messageService) {
    this.messageService = messageService;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    arguments = StringUtil.trimToNull(arguments);
    if (arguments == null) {
      messageService.sendMessage(player, "Expected '" + ROOM_SCAN_DESC + " <direction> <exit description>'");
      return;
    }
    
    if (arguments.length() > DESCRIPTION_MAX_LENGTH) {
      messageService.sendMessage(player, "Room exit descriptions cannot exceed " + DESCRIPTION_MAX_LENGTH + " characters.");
      return;
    }
    
    int firstSpace = arguments.indexOf(' ');
    if (firstSpace < 0) {
      messageService.sendMessage(player, "Expected '" + ROOM_SCAN_DESC + " <direction> <exit description>'");
      return;
    }
    
    String[] splitArgs = new String[2];
    splitArgs[0] = arguments.substring(0, firstSpace);
    
    if (arguments.length() < firstSpace + 1) {
      messageService.sendMessage(player, "Expected '" + ROOM_SCAN_DESC + " <direction> <exit description>'");
      return;
    }
    
    splitArgs[1] = arguments.substring(firstSpace + 1);
    
    Direction d = Direction.fromName(splitArgs[0]);
    if (d == null) {
      messageService.sendMessage(player, "Expected '" + ROOM_SCAN_DESC + " <direction> <exit description>'");
      return;
    }
    Room room = player.getRoom();
    
    if ("clear".equals(splitArgs[1])) {
      room.removeScanDescription(d);
    } else {
      room.addScanDescription(d, splitArgs[1]);
    }
  }

}
