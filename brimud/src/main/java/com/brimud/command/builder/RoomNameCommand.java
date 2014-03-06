/**
 * 
 */
package com.brimud.command.builder;

import com.brimud.command.Command;
import com.brimud.model.Player;
import com.brimud.service.MessageService;
import com.brimud.util.StringUtil;
import com.google.inject.Inject;

/**
 * @author dan
 * 
 */
class RoomNameCommand implements Command {

  static final String ROOM_NAME = "rname";
  static final int NAME_MAX_LENGTH = 256;
  private final MessageService messageService;

  @Inject
  RoomNameCommand(MessageService messageService) {
    this.messageService = messageService;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player,
   * java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    arguments = StringUtil.trimToNull(arguments);
    if (arguments == null || arguments.length() > NAME_MAX_LENGTH) {
      messageService.sendMessage(player, "Room names cannot exceed " + NAME_MAX_LENGTH + " characters.");
    } else {
      player.getRoom().setName(arguments);
      messageService.sendMessage(player, player.getRoom().look(player));
    }
  }

}
