/**
 * 
 */
package com.brimud.character;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.brimud.character.CharacterBuilder.CharacterBuilderException;

/**
 * @author dan
 * 
 */
public class TestCharacterBuilder {

  // these defaults will spend 20 points from the point pool
  private static final int DEFAULT_STR = 9;
  private static final int DEFAULT_DEX = 11;
  private static final int DEFAULT_CON = 12;
  private static final int DEFAULT_INT = 13;
  private static final int DEFAULT_WIS = 14;
  private static final int DEFAULT_CHA = 16;

  @Test
  public void testPointPool() {
    CharacterBuilder builder = CharacterBuilder.pointPool();

    Assert.assertEquals(20, builder.getPointPool());

    // required elements
    builder.name("Bob").characterClass("barbarian");
    Assert.assertEquals(builder.getName(), "Bob");

    builder.dwarf();
    Assert.assertEquals(builder.getRace(), "Dwarf");

    // put more points in the pool by lowering ability score
    builder = builder.strength(7);
    Assert.assertEquals(builder.getStrength(), 7);
    Assert.assertEquals(builder.getPointPool(), 24);

    List<Validation> messages = builder.validate();
    Assert.assertEquals(messages.size(), 1);

    // set the strength back to the default, affecting the remaining points in
    // the pool
    builder.strength(10);
    Assert.assertEquals(builder.getStrength(), 10);
    Assert.assertEquals(builder.getPointPool(), 20);

    // use up all of the points in the pool
    builder.strength(18);
    builder.dexterity(13);
    Assert.assertEquals(builder.getPointPool(), 0);

    // check that the character has the expected points, which may have race
    // modifiers
    Character character = builder.build();
    Assert.assertEquals(character.getName(), "Bob");
    Assert.assertEquals(character.getStrength().getScore(), 18);
    Assert.assertEquals(character.getDexterity().getScore(), 13);
    Assert.assertEquals(character.getConstitution().getScore(), 12); // dwarf
    Assert.assertEquals(character.getWisdom().getScore(), 12); // dwarf
    Assert.assertEquals(character.getIntelligence().getScore(), 10);
    Assert.assertEquals(character.getCharisma().getScore(), 8); // dwarf

    // tests unique values for each score, to make sure they are getting
    // assigned to the right places
    builder.strength(DEFAULT_STR).dexterity(DEFAULT_DEX).constitution(DEFAULT_CON).wisdom(DEFAULT_WIS).intelligence(
        DEFAULT_INT).charisma(DEFAULT_CHA);

    character = builder.build();
    Assert.assertEquals(character.getStrength().getScore(), DEFAULT_STR);
    Assert.assertEquals(character.getDexterity().getScore(), DEFAULT_DEX);
    Assert.assertEquals(character.getConstitution().getScore(), DEFAULT_CON + 2); // dwarf
    Assert.assertEquals(character.getWisdom().getScore(), DEFAULT_WIS + 2); // dwarf
    Assert.assertEquals(character.getIntelligence().getScore(), DEFAULT_INT);
    Assert.assertEquals(character.getCharisma().getScore(), DEFAULT_CHA - 2); // dwarf

    // test some of the validations here
    messages = builder.validate();
    Assert.assertTrue(messages.isEmpty());

    Assert.assertFalse(CharacterBuilder.isValidScore(6));
    Assert.assertFalse(CharacterBuilder.isValidScore(19));
    Assert.assertTrue(CharacterBuilder.isValidScore(12));
    Assert.assertTrue(CharacterBuilder.isValidScore(15));

    builder.name("");
    messages = builder.validate();
    Assert.assertEquals(messages.size(), 1);

    try {
      character = builder.build();
      Assert.fail("expected character builder exception");
    } catch (CharacterBuilderException e) {
      messages = e.getValidationMessages();
      Assert.assertEquals(messages.size(), 1);
    }
  }

  @Test
  public void testClassSelection() {
    CharacterBuilder builder = CharacterBuilder.pointPool();
    builder.name("Gorlock").human(Ability.STR).strength(DEFAULT_STR).dexterity(DEFAULT_DEX).constitution(DEFAULT_CON)
        .intelligence(DEFAULT_INT).wisdom(DEFAULT_WIS).charisma(DEFAULT_CHA);
    
    // KEY for a class factory?
    String characterClass = "barbarian";    
    builder = builder.characterClass(characterClass);    
    Assert.assertEquals(builder.getCharacterClass(), characterClass);
    
    Character character = builder.build();
    
    Assert.assertEquals(character.getCharacterClass(), characterClass);
    
  }

  @Test
  public void testRaceModifiers() {

    CharacterBuilder builder = CharacterBuilder.pointPool();

    builder.name("Gorlock").characterClass("barbarian");
    builder.strength(DEFAULT_STR).dexterity(DEFAULT_DEX).constitution(DEFAULT_CON).wisdom(DEFAULT_WIS).intelligence(
        DEFAULT_INT).charisma(DEFAULT_CHA);
    Assert.assertEquals(builder.getName(), "Gorlock");

    // dwarf
    builder.dwarf();
    verifyRacialTraits(builder, new int[] { 0, 0, 2, 0, 2, -2 });

    // elf
    builder.elf();
    verifyRacialTraits(builder, new int[] { 0, 2, -2, 2, 0, 0 });

    // gnome
    builder.gnome();
    verifyRacialTraits(builder, new int[] { -2, 0, 2, 0, 0, 2 });

    // halfelf
    builder.halfelf(Ability.STR);
    verifyRacialTraits(builder, new int[] { 2, 0, 0, 0, 0, 0 });

    builder.halfelf(Ability.DEX);
    verifyRacialTraits(builder, new int[] { 0, 2, 0, 0, 0, 0 });

    builder.halfelf(Ability.CON);
    verifyRacialTraits(builder, new int[] { 0, 0, 2, 0, 0, 0 });

    builder.halfelf(Ability.INT);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 2, 0, 0 });

    builder.halfelf(Ability.WIS);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 0, 2, 0 });

    builder.halfelf(Ability.CHA);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 0, 0, 2 });

    // halforc
    builder.halforc(Ability.STR);
    verifyRacialTraits(builder, new int[] { 2, 0, 0, 0, 0, 0 });

    builder.halforc(Ability.DEX);
    verifyRacialTraits(builder, new int[] { 0, 2, 0, 0, 0, 0 });

    builder.halforc(Ability.CON);
    verifyRacialTraits(builder, new int[] { 0, 0, 2, 0, 0, 0 });

    builder.halforc(Ability.INT);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 2, 0, 0 });

    builder.halforc(Ability.WIS);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 0, 2, 0 });

    builder.halforc(Ability.CHA);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 0, 0, 2 });

    // halfling
    builder.halfling();
    verifyRacialTraits(builder, new int[] { -2, 2, 0, 0, 0, 2 });

    // human
    builder.human(Ability.STR);
    verifyRacialTraits(builder, new int[] { 2, 0, 0, 0, 0, 0 });

    builder.human(Ability.DEX);
    verifyRacialTraits(builder, new int[] { 0, 2, 0, 0, 0, 0 });

    builder.human(Ability.CON);
    verifyRacialTraits(builder, new int[] { 0, 0, 2, 0, 0, 0 });

    builder.human(Ability.INT);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 2, 0, 0 });

    builder.human(Ability.WIS);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 0, 2, 0 });

    builder.human(Ability.CHA);
    verifyRacialTraits(builder, new int[] { 0, 0, 0, 0, 0, 2 });
  }

  /**
   * @param builder
   * @param expectedBonuses
   *          must be length 6 and order is str, dex, con, int, wis, cha
   */
  private void verifyRacialTraits(CharacterBuilder builder, int[] expectedBonuses) {
    Map<Ability, Integer> racialAbilityBonuses = builder.getAbilityBonuses();

    for (Ability ability : Ability.values()) {
      if (expectedBonuses[ability.ordinal()] == 0) {
        Assert.assertNull(racialAbilityBonuses.get(ability));
      } else {
        Assert.assertEquals(racialAbilityBonuses.get(ability).intValue(), expectedBonuses[ability.ordinal()]);
      }
    }

    Character character = builder.build();
    String race = character.getRace();
    Assert.assertNotNull(race);
    Assert.assertEquals(race, builder.getRace());

    // see that the racial ability bonuses are applied to the base scores when
    // creating the character
    Assert.assertEquals(character.getStrength().getScore(), DEFAULT_STR + expectedBonuses[Ability.STR.ordinal()]);
    Assert.assertEquals(character.getDexterity().getScore(), DEFAULT_DEX + expectedBonuses[Ability.DEX.ordinal()]);
    Assert.assertEquals(character.getConstitution().getScore(), DEFAULT_CON + expectedBonuses[Ability.CON.ordinal()]);
    Assert.assertEquals(character.getWisdom().getScore(), DEFAULT_WIS + expectedBonuses[Ability.WIS.ordinal()]);
    Assert.assertEquals(character.getCharisma().getScore(), DEFAULT_CHA + expectedBonuses[Ability.CHA.ordinal()]);
    Assert.assertEquals(character.getIntelligence().getScore(), DEFAULT_INT + expectedBonuses[Ability.INT.ordinal()]);
  }
}
