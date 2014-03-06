/**
 * 
 */
package com.brimud.character;


/**
 * @author dan
 * 
 */
public class CharacterCreation {

  private static final String NL = "\n";
  
  private CharacterBuilder charBuilder;

  CharacterCreation() {
    charBuilder = CharacterBuilder.pointPool();
    charBuilder.name("Golroc").human(Ability.STR).characterClass("Barbarian");
    charBuilder.strength(12);
    charBuilder.dexterity(14);
  }

  public String display() {
    StringBuilder display = new StringBuilder();
    
    String firstCol21 = "                     ";
    String secondCol12 = "            ";
    display.append("Points: ").append(charBuilder.getPointPool());
    
    int usedChars = display.length();
    display.append(firstCol21.substring(usedChars));
    
    String name = charBuilder.getName();
    if (name == null) {
      name = "<Name>";
    }
    display.append(name);
    display.append(secondCol12.substring(name.length()));
    
    String race = "<Race>";
    if (charBuilder.getRace() != null) {
      race = charBuilder.getRace().toString();
    }
    display.append(race);
    
    String charClass = charBuilder.getCharacterClass();
    if (charClass == null) {
      charClass = "<Class>";
    }
    display.append(", ").append(charClass).append(NL);
    
    
    display.append(firstCol21).append("Base Score  Racial Bonus     Total Scores").append(NL);
    for (Ability ability : Ability.values()) {
      display.append(ability.getLongString()).append(firstCol21.substring(ability.getLongString().length()));

      int baseScore = charBuilder.getBaseScores().get(ability);
      display.append(baseScore).append(secondCol12.substring(baseScore < 10 ? 1 : 2));
      
      Integer bonus = charBuilder.getAbilityBonuses().get(ability);
      if (bonus != null) {
        display.append(bonus > 0 ? "+" + bonus : bonus);
        display.append(secondCol12.substring(2)).append(" |").append("   ");
      } else {
        display.append(secondCol12).append(" |").append("   ");
      }
      
      display.append(bonus != null ? baseScore + bonus : baseScore);
      display.append(NL);
    }
    
    return display.toString();
  }
}
