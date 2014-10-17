/**
 * 
 */
package com.brimud.command.builder;

import javax.inject.Inject;

import com.brimud.command.Command;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.service.MessageService;

/**
 * @author dan
 *
 */
class RoomShortCommand implements Command {

  static final String ROOM_SHORT_DESCRIPTION = "rshort";
  
  private final int SHORT_MAX_LENGTH = 256;
  private final MessageService messageService;
  
  @Inject
  RoomShortCommand(MessageService messageService) {
    this.messageService = messageService;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    if (arguments.length() > SHORT_MAX_LENGTH) {
      messageService.sendMessage(player, "Room short descriptions cannot be longer than " + SHORT_MAX_LENGTH + " characters.");
    } else {
      Room room = player.getRoom();
      room.setShortDescription(arguments);
      messageService.sendMessage(player, room.look(player));
    }
  }

}
