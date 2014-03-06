/**
 * 
 */
package com.brimud.character;

import org.testng.annotations.Test;

/**
 * @author dan
 *
 */
public class TestCharacterCreation {

  @Test
  public void test() {
    
    
//    Choose a name, race, class, and spend points to increase your character's ability scores.
//    Type 'help' for more instructions
    
//    (Help menu)
//    Examples: Type 'name Jerik' to set your name to Jerik 
//    Type 'human' to set your race as human.
//    Type 'barbarian' to set your class to barbarian
//    To spend points on your abilty scores, type 'str 16'
//    Also put ability score costs here
    
// Points: -2           Jerik       Dwarf, Barbarian
//                      Base Score  Racial Bonus    Total Scores
// Strength(str)        18                       |  18
// Dexterity(dex)       14                       |  14
// Constitution(con)    10          +2           |  12
// Intelligence(int)    10                       |  10
// Wisdom(wis)          10          +2           |  12
// Charisma(cha)        10          -2           |  8
    
//  You spent to many points! Decrease one of your ability scores before proceeding!
    
    CharacterCreation cc = new CharacterCreation();
    String display = cc.display();
    
    System.out.println(display);
  }
}
