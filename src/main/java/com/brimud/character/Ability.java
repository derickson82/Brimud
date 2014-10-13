/**
 * 
 */
package com.brimud.character;

public enum Ability {
  STR("Strength(str)"), 
  DEX("Dexterity(dex)"), 
  CON("Constitution(con)"), 
  INT("Intelligence(int)"), 
  WIS("Wisdom(wis)"),
  CHA("Charisma(cha)");
  
  private final String longString;
  
  private Ability(String longString) {
    this.longString = longString;
  }
  
  public String getLongString() {
    return longString;
  }
}