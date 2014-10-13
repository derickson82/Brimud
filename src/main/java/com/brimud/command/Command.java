/**
 * 
 */
package com.brimud.command;

import com.brimud.model.Player;

/**
 * @author dan
 *
 */
public interface Command {
  void doCommand(Player player, String command, String arguments);
}
