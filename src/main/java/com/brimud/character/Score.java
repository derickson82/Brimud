/**
 * 
 */
package com.brimud.character;

import com.brimud.util.Preconditions;

/**
 * @author dan
 * 
 */
public class Score implements Modifier {
  private final int baseScore;

  Score(int score) {
    Preconditions.checkPositive(score, "score");
    this.baseScore = score;
  }

  /**
   * The modifier is based on the ability score, and should match Table 1-3 in
   * the Pathfinder core rulebook.
   */
  @Override
  public int getModifier() {
    return -5 + baseScore / 2;
  }

  public int getScore() {
    return baseScore;
  }

  @Override
  public String toString() {
    return Integer.toString(baseScore);
  }
}
