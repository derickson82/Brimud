/**
 * 
 */
package com.brimud.character;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Dan
 */
public class TestCharacter {

  // TODO ability scores
  // TODO implement multiple methods of creating ability scores?
  @Test
  public void test() {
    CharacterBuilder builder = CharacterBuilder.pointPool();
    builder.charisma(18);
    builder.intelligence(13);
    builder.name("Bob");
    builder.human(Ability.STR);
    builder.characterClass("Rogue");
    Character character = builder.build();
    
    Assert.assertTrue(character.getStrength().getScore() > 0);
    Assert.assertTrue(character.getDexterity().getScore() > 0);
    Assert.assertTrue(character.getConstitution().getScore() > 0);
    Assert.assertTrue(character.getIntelligence().getScore() > 0);
    Assert.assertTrue(character.getWisdom().getScore() > 0);
    Assert.assertTrue(character.getCharisma().getScore() > 0);
    
  }
  
//  @Test
//  public void everything() {
//    
//    Character character = CharacterBuilder.pointPool(0).name("Bob").build();
//    SavingThrow fortitude = character.getFortitude();
//    SavingThrow will = character.getWill();
//    SavingThrow reflex = character.getReflex();
//    
//  }
}
