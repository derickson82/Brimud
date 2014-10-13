/**
 * 
 */
package com.brimud.character;

import java.util.EnumMap;
import java.util.Map;

import com.brimud.util.Preconditions;

/**
 * @author Dan
 *
 */
public class Character {
  private String name;
  private String race;
  private String characterClass;
  private Map<Ability, Score> abilityScores = new EnumMap<Ability, Score>(Ability.class);

  public Character() {
  }
  
  Character(String name, String race, String characterClass, Map<Ability, Integer> abilityScores) {
    Preconditions.checkNotNull(name, "name");
    Preconditions.checkNotNull(race, "race");
    Preconditions.checkNotNull(characterClass, "characterClass");
    Preconditions.checkNotNull(abilityScores, "abilityScores");
    
    this.name = name;
    this.race = race;
    this.characterClass = characterClass;
    for (Ability ability : Ability.values()) {
      Integer score = abilityScores.get(ability);
      if (score == null) {
        throw new IllegalArgumentException("Must provide a score for every ability. Missing: " + ability);
      }
      this.abilityScores.put(ability, new Score(score));
    }
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getRace() {
    return race;
  }
  
  public String getCharacterClass() {
    return characterClass;
  }
  
  public Score getStrength() {
    return abilityScores.get(Ability.STR);
  }
  
  public Score getDexterity() {
    return abilityScores.get(Ability.DEX);
  }
  
  public Score getConstitution() {
    return abilityScores.get(Ability.CON);
  }
  
  public Score getIntelligence() {
    return abilityScores.get(Ability.INT);
  }
  
  public Score getWisdom() {
    return abilityScores.get(Ability.WIS);
  }
  
  public Score getCharisma() {
    return abilityScores.get(Ability.CHA);
  }
  
  public SavingThrow getFortitude() {
    return null;
  }
  
  public SavingThrow getWill() {
    return null;
  }
  
  public SavingThrow getReflex() {
    return null;
  }
}
