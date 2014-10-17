/**
 * 
 */
package com.brimud.command;

import javax.inject.Inject;

import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.service.MessageService;

/**
 * @author dan
 * 
 */
class LookCommand implements Command {

  static final String LOOK = "look";

  private final MessageService messageService;

  @Inject
  public LookCommand(MessageService messageService) {
    this.messageService = messageService;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.brimud.command.Command#doCommand(com.brimud.session.Session,
   * java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    Room room = player.getRoom();
    if (arguments != null) {
      Direction d = Direction.fromName(arguments);
      String description;
      if (d != null) {
        description = createDirectionDescription(d, player, arguments, room);
      } else {
        String extraDesc = room.getExtraDescription(arguments);
        if (extraDesc != null) {
          description = extraDesc;
        } else {
          description = "What are you looking at?";
        }
      }

      messageService.sendMessage(player, description);

    } else {
      String roomDescription = room.look(player);
      messageService.sendMessage(player, roomDescription);
    }
  }

  private String createDirectionDescription(Direction d, Player player, String arguments, Room room) {
    String scanDescription = room.getScanDescription(d);
    Room exit = room.getExit(d);
    StringBuilder description = new StringBuilder();
    if (scanDescription != null) {
      description.append(scanDescription + "\n");

      // FIXME similar logic in scan command, clean this up
      if (exit != null) {
        description.append(exit.buildPlayerDescriptions(player));
      }
    } else if (exit != null) {
      description.append(exit.scan(player));
    } else {
      description.append("You see nothing of interest.\n");
    }
    return description.toString();
  }

}
