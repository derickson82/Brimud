/**
 * 
 */
package com.brimud.character;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brimud.character.race.Dwarf;
import com.brimud.character.race.Elf;
import com.brimud.character.race.Gnome;
import com.brimud.character.race.Halfelf;
import com.brimud.character.race.Halfling;
import com.brimud.character.race.Halforc;
import com.brimud.character.race.Human;
import com.brimud.character.race.Race;
import com.brimud.util.Preconditions;
import com.brimud.util.StringUtil;

/**
 * 
 * @author Dan
 */
public class CharacterBuilder {

  private String name;
  private Race race;
  private String characterClass;
  private final Map<Ability, Integer> abilityMap = new EnumMap<Ability, Integer>(Ability.class);
  private final Map<Ability, Integer> racialAbilityBonuses = new EnumMap<Ability, Integer>(Ability.class);

  private int pointPool;

  private Map<Integer, Integer> pointCosts = new HashMap<Integer, Integer>();

  public static CharacterBuilder pointPool() {
    return pointPool(20);
  }
  
  public static CharacterBuilder pointPool(int totalPoints) {
    return new CharacterBuilder(totalPoints);
  }

  private CharacterBuilder(int totalPoints) {
    for (Ability a : Ability.values()) {
      abilityMap.put(a, 10);
    }
    pointPool = totalPoints;

    // Table 1-1 in Pathfinder core player's guide
    pointCosts.put(7, -4);
    pointCosts.put(8, -2);
    pointCosts.put(9, -1);
    pointCosts.put(10, 0);
    pointCosts.put(11, 1);
    pointCosts.put(12, 2);
    pointCosts.put(13, 3);
    pointCosts.put(14, 5);
    pointCosts.put(15, 7);
    pointCosts.put(16, 10);
    pointCosts.put(17, 13);
    pointCosts.put(18, 17);
  }

  public CharacterBuilder name(String name) {
    this.name = StringUtil.trimToNull(name);
    return this;
  }
  
  public String getName() {
    return name;
  }
  
  public Race getRace() {
    return race;
  }
  
  public CharacterBuilder characterClass(String classKey) {
    this.characterClass = StringUtil.trimToNull(classKey);
    return this;
  }
  
  public String getCharacterClass() {
    return characterClass;
  }
  
  public CharacterBuilder dwarf() {
    race = new Dwarf();
    
    racialAbilityBonuses.clear();
    racialAbilityBonuses.put(Ability.CON, 2);
    racialAbilityBonuses.put(Ability.WIS, 2);
    racialAbilityBonuses.put(Ability.CHA, -2);
    
    return this;
  }
  
  public CharacterBuilder elf() {
    race = new Elf();
    
    racialAbilityBonuses.clear();
    racialAbilityBonuses.put(Ability.DEX, 2);
    racialAbilityBonuses.put(Ability.INT, 2);
    racialAbilityBonuses.put(Ability.CON, -2);
    
    return this;
  }
  
  public CharacterBuilder gnome() {
    race = new Gnome();
    
    racialAbilityBonuses.clear();
    racialAbilityBonuses.put(Ability.CON, 2);
    racialAbilityBonuses.put(Ability.CHA, 2);
    racialAbilityBonuses.put(Ability.STR, -2);
    
    return this;
  }
  
  /**
   * A halfelf gets to choose one ability to add a bonus to.
   * @param ability
   * @return
   */
  public CharacterBuilder halfelf(Ability ability) {
    Preconditions.checkNotNull(ability, "ability");
    race = new Halfelf();
    
    racialAbilityBonuses.clear();
    racialAbilityBonuses.put(ability, 2);
    
    return this;
  }
  
  public CharacterBuilder halforc(Ability ability) {
    Preconditions.checkNotNull(ability, "ability");
    race = new Halforc();
    
    racialAbilityBonuses.clear();
    racialAbilityBonuses.put(ability, 2);
    
    return this;
  }
  
  public CharacterBuilder halfling() {
    race = new Halfling();
    
    racialAbilityBonuses.clear();
    racialAbilityBonuses.put(Ability.DEX, 2);
    racialAbilityBonuses.put(Ability.CHA, 2);
    racialAbilityBonuses.put(Ability.STR, -2);
    
    return this;
  }
  
  public CharacterBuilder human(Ability ability) {
    race = new Human();
    
    racialAbilityBonuses.clear();
    racialAbilityBonuses.put(ability, 2);

    return this;
  }
  
  public Map<Ability, Integer> getAbilityBonuses() {
    return Collections.unmodifiableMap(racialAbilityBonuses);
  }
  
  private CharacterBuilder put(Ability ability, int score) throws CharacterBuilderException {
    throwIfInvalidScore(score, ability.name());
    modifyPool(abilityMap.get(ability), score);
    abilityMap.put(ability, score);
    return this;
  }

  public CharacterBuilder strength(int score) throws CharacterBuilderException {
    return put(Ability.STR, score);
  }

  public CharacterBuilder dexterity(int score) throws CharacterBuilderException {
    return put(Ability.DEX, score);
  }

  public CharacterBuilder constitution(int score) throws CharacterBuilderException {
    return put(Ability.CON, score);
  }

  public CharacterBuilder intelligence(int score) throws CharacterBuilderException {
    return put(Ability.INT, score);
  }

  public CharacterBuilder wisdom(int score) throws CharacterBuilderException {
    return put(Ability.WIS, score);
  }

  public CharacterBuilder charisma(int score) throws CharacterBuilderException {
    return put(Ability.CHA, score);
  }

  public List<Validation> validate() {
    List<Validation> messages = new ArrayList<Validation>();
    if (pointPool != 0) {
      messages.add(new Validation("Point pool must be zero. Points left: " + pointPool));
    }
    if (name == null) {
      messages.add(new Validation("Provide a name for your character."));
    }
    if (race == null) {
      messages.add(new Validation("Select a race for your character"));
    }
    if (characterClass == null) {
      messages.add(new Validation("Select a class for your character"));
    }
    return messages;
  }

  public Character build() throws CharacterBuilderException {
    List<Validation> validations = validate();
    if (!validations.isEmpty()) {
      throw new CharacterBuilderException(validations);
    }
    
    return new Character(name, race, characterClass, combineAbilityScores());
  }
  
  private Map<Ability, Integer> combineAbilityScores() {
    Map<Ability, Integer> combined = new EnumMap<Ability, Integer>(abilityMap);
    
    for (Ability ability : Ability.values()) {
      Integer bonus = racialAbilityBonuses.get(ability);
      if (bonus != null) {
        Integer baseScore = combined.get(ability);
        combined.put(ability, baseScore + bonus);
      }
    }
    return combined;
  }

  private void throwIfInvalidScore(int score, String ability) throws CharacterBuilderException {
    if (!isValidScore(score)) {
      throw new CharacterBuilderException("Invalid score for " + ability + ": " + score);
    }
  }

  public static boolean isValidScore(int score) {
    return score >= 7 && score <= 18;
  }

  private void modifyPool(int oldScore, int newScore) {
    pointPool += pointCosts.get(oldScore);
    pointPool -= pointCosts.get(newScore);
  }

  public Map<Ability, Integer> getBaseScores() {
    return abilityMap;
  }
  
  public int getStrength() {
    return abilityMap.get(Ability.STR);
  }

  public int getDexterity() {
    return abilityMap.get(Ability.DEX);
  }

  public int getConstitution() {
    return abilityMap.get(Ability.CON);
  }

  public int getIntelligence() {
    return abilityMap.get(Ability.INT);
  }

  public int getWisdom() {
    return abilityMap.get(Ability.WIS);
  }

  public int getCharisma() {
    return abilityMap.get(Ability.CHA);
  }

  public int getPointPool() {
    return pointPool;
  }

  @SuppressWarnings("serial")
  public static class CharacterBuilderException extends RuntimeException {

    private List<Validation> validations;

    public CharacterBuilderException(List<Validation> validations) {
      this.validations = validations;
    }

    public List<Validation> getValidationMessages() {
      if (validations == null) {
        validations = new ArrayList<Validation>();
      }
      return validations;
    }

    public CharacterBuilderException() {
      super();
    }

    public CharacterBuilderException(String message, Throwable cause) {
      super(message, cause);
    }

    public CharacterBuilderException(String message) {
      super(message);
    }

    public CharacterBuilderException(Throwable cause) {
      super(cause);
    }
    
    @Override
    public String toString() {
      StringBuilder b = new StringBuilder();
      for (Validation m : getValidationMessages()) {
        b.append("ERROR: " + m.toString() + "\n");
      }
      b.append(super.toString());
      return b.toString();
    }
  }

}
