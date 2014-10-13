/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.service.MessageService;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
class RoomDescCommand implements Command {

  static final String ROOM_DESCRIPTION = "rdesc";
  
  private final int DESCRIPTION_MAX_LENGTH = Integer.MAX_VALUE;
  private final MessageService messageService;

  @Inject
  RoomDescCommand(MessageService messageService) {
    this.messageService = messageService;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    if (arguments.length() > DESCRIPTION_MAX_LENGTH) {
      messageService.sendMessage(player, "Room descriptions cannot exceed " + DESCRIPTION_MAX_LENGTH + " characters.");
    } else {
      Room room = player.getRoom();
      room.setDescription(arguments);
      messageService.sendMessage(player, room.look(player));
    }
  }

}
