/**
 * 
 */
package com.brimud.character;

import com.brimud.session.Session;
import com.brimud.session.State;

/**
 * @author dan
 * 
 */
public class NewCharacterMenu implements State {
  private final CharacterBuilder builder;

  private State currentState;

  NewCharacterMenu() {
    builder = CharacterBuilder.pointPool();
    currentState = CHOOSE_NAME;
  }

  private void setCurrentState(State state) {
    this.currentState = state;
  }

  @Override
  public String getMessage() {
    return currentState.getMessage();
  }

  @Override
  public void handleCommand(Session session, String command) {
    currentState.handleCommand(session, command);
  }

  // All the internal states defined below
//  private final State CHOOSE_CHARACTER = new State() {
//    public String getMessage() {
//      return "Select a character, or type 'new' to create a new character: ";
//    }
//
//    public void handleCommand(String command) {
//      setCurrentState(CHOOSE_NAME);
//    }
//  };

  private final State CHOOSE_NAME = new State() {
    public String getMessage() {
      return "Choose your name: ";
    }

    public void handleCommand(Session session, String command) {
      builder.name(command);
      setCurrentState(CHOOSE_RACE);
    }
  };

  private final State CHOOSE_RACE = new State() {
    public String getMessage() {
      StringBuilder msg = new StringBuilder();
      msg.append("1) Dwarf\n");
      msg.append("2) Elf\n");
      msg.append("3) Gnome\n");
      msg.append("4) Half-Elf\n");
      msg.append("5) Halfling\n");
      msg.append("6) Half-Orc\n");
      msg.append("7) Human\n");
      msg.append("\nChoose your race: ");
      return msg.toString();
    }

    public void handleCommand(Session session, String raceSelection) {
      final State nextState;
      int selection = Integer.parseInt(raceSelection);
      switch (selection) {
      case 1:
        builder.dwarf();
        nextState = CHOOSE_CLASS;
        break;
      case 2:
        builder.elf();
        nextState = CHOOSE_CLASS;
        break;
      case 3:
        builder.gnome();
        nextState = CHOOSE_CLASS;
        break;
      case 4:
        builder.halfelf(Ability.STR);
        nextState = CHOOSE_BONUS;
        break;
      case 5:
        builder.halfling();
        nextState = CHOOSE_CLASS;
        break;
      case 6:
        builder.halforc(Ability.STR);
        nextState = CHOOSE_BONUS;
        break;
      case 7:
        builder.human(Ability.STR);
        nextState = CHOOSE_BONUS;
        break;
      default:
        // send a "Huh?" message and return to this state.
        nextState = CHOOSE_RACE;
        break;
      }
      setCurrentState(nextState);
    }
  };

  private final State CHOOSE_CLASS = new State() {
    public String getMessage() {
      return "Choose your class: ";
    }

    public void handleCommand(Session session, String command) {
    }
  };

  private final State CHOOSE_BONUS = new State() {
    public String getMessage() {
      StringBuilder msg = new StringBuilder();
      msg.append("1) Strength(str)\n");
      msg.append("2) Dexterity(dex)\n");
      msg.append("3) Constitution(con)\n");
      msg.append("4) Intelligence(int)\n");
      msg.append("5) Wisdom(wis)\n");
      msg.append("6) Charisma(cha)\n");
      msg.append("\nChoose an ability for a +2 bonus: ");
      return msg.toString();
    }

    public void handleCommand(Session session, String command) {

    }
  };
}