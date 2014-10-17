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
class ScanCommand implements Command {
  
  static final String SCAN = "scan";
  
  private final MessageService messageService;
  
  @Inject
  ScanCommand(MessageService messageService) {
    this.messageService = messageService;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String arguments) {
    Room currentRoom = player.getRoom();
    
    StringBuilder scans = new StringBuilder();
    for (Direction d : Direction.values()) {
      scans.append("Looking " + d.getName() + ":\n");
      String scanDescription = currentRoom.getScanDescription(d);
      Room exit = currentRoom.getExit(d);
      if (scanDescription != null) {
        scans.append("\t" + scanDescription + "\n");
        
        // FIXME clean up how scan descriptions are built
        if (exit != null) {
          scans.append(exit.buildPlayerDescriptions(player));
        }
      } else if (exit != null) {
        scans.append("\t" + exit.scan(player));
      } else {
        scans.append("\tYou see nothing of interest.\n");
      }
    }
    
    messageService.sendMessage(player, scans.toString());
  }
}
