package com.brimud.character;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCharacterMenu {

  // CHAR_SELECT -> ENTER_GAME
  // CHAR_SELECT -> NEW_CHAR
  // NEW_CHAR -> CHAR_NAME
  // CHAR_NAME -> CHAR_NAME (failed validation, not unique)
  // CHAR_NAME -> RACE
  // RACE -> SELECT_BONUS (if allowed for the race)
  // SELECT_BONUS -> CLASS
  // RACE -> CLASS
  // CLASS -> ABILITY_SELECT
  // ABILITY_SELECT -> ABILITY_SCORE
  // ABILITY_SCORE -> ABILITY_SELECT
  // ABILTY_SELECT -> ABILITY_SELECT (print stats)
  // ABILITY_SELECT -> CONFIRM
  // CONFIRM -> COMPLETE
  
  @Test
  public void test() {
    
    NewCharacterMenu menu = new NewCharacterMenu();
//    Assert.assertEquals(menu.getMessage(), "Select a character, or type 'new' to create a new character: ");
    
//    menu.handleCommand("new");
    Assert.assertEquals(menu.getMessage(), "Choose your name: ");
    
    menu.handleCommand(null, "Jerik");
    Assert.assertTrue(menu.getMessage().contains("Choose your race: "));
    
    menu.handleCommand(null, "1");
    Assert.assertTrue(menu.getMessage().contains("Choose your class: "));
  }
}
