/**
 * 
 */
package com.brimud.command.builder;

import javax.inject.Inject;

import com.brimud.command.Command;
import com.brimud.model.Player;
import com.brimud.service.MessageService;

/**
 * @author dan
 * 
 */
public class RoomExtraDescriptionCommand implements Command {

  static final String ROOM_EXTRA = "rextra";

  private final MessageService messageService;

  @Inject
  RoomExtraDescriptionCommand(MessageService messageService) {
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
    if (arguments == null) {
      expectedSyntax(player);
      return;
    }

    int keywordIndex = arguments.indexOf(' ');
    if (keywordIndex < 0) {
      expectedSyntax(player);
      return;
    }

    String keyword = arguments.substring(0, keywordIndex);

    if (arguments.length() < keywordIndex + 1) {
      expectedSyntax(player);
      return;
    }

    String description = arguments.substring(keywordIndex + 1);

    if ("clear".equalsIgnoreCase(description)) {
      player.getRoom().removeExtraDescription(keyword);
    } else {
      player.getRoom().addExtraDescription(keyword, description);
    }
  }

  private void expectedSyntax(Player player) {
    messageService.sendMessage(player, "Expected " + ROOM_EXTRA + " <keyword> <Description>, or " + ROOM_EXTRA
        + " <keyword> clear to remove the description.");
  }

}
