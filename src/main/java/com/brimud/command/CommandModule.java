/**
 * 
 */
package com.brimud.command;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * @author dan
 * 
 */
public class CommandModule extends AbstractModule {

  /*
   * (non-Javadoc)
   * 
   * @see com.google.inject.AbstractModule#configure()
   */
  @Override
  protected void configure() {

  }
  
  @Singleton
  @Provides
  @GeneralCommands
  Map<String, Command> commands(
      SayCommand sayCommand,
      LookCommand lookCommand,
      ScanCommand scanCommand,
      QuitCommand quitCommand,
      MoveCommand moveCommand
      ) {
    Map<String, Command> commands = new HashMap<String, Command>();
    
    commands.put(SayCommand.SAY, sayCommand);
    commands.put(LookCommand.LOOK, lookCommand);
    commands.put(ScanCommand.SCAN, scanCommand);
    commands.put(QuitCommand.QUIT, quitCommand);
    commands.put(Direction.NORTH.getName(), moveCommand);
    commands.put(Direction.SOUTH.getName(), moveCommand);
    commands.put(Direction.EAST.getName(), moveCommand);
    commands.put(Direction.WEST.getName(), moveCommand);
    commands.put(Direction.UP.getName(), moveCommand);
    commands.put(Direction.DOWN.getName(), moveCommand);
    
    return commands;
  }

}
