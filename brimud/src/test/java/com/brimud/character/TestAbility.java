/**
 * 
 */
package com.brimud.character;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author dan
 *
 */
public class TestAbility {

  @DataProvider(name="abilityModifiers")
  public Iterator<Object[]> abilityModifiers() {
    List<Object[]> list = new ArrayList<Object[]>();
    
    // samples from Table 1-3 in core pathfinder rulebook
    list.add(wrap(1, -5));
    list.add(wrap(2, -4));
    list.add(wrap(3, -4));
    list.add(wrap(4, -3));
    list.add(wrap(5, -3));
    list.add(wrap(14, 2));
    list.add(wrap(15, 2));
    list.add(wrap(26, 8));
    list.add(wrap(27, 8));
    list.add(wrap(44, 17));
    list.add(wrap(45, 17));
    
    return list.iterator();
  }
  
  @Test(dataProvider="abilityModifiers")
  public void testAbilityModifiers(int abilityScore, int expectedModifier) {
    Assert.assertEquals(new Score(abilityScore).getModifier(), expectedModifier);
  }
  
  Object[] wrap(Object... objects) {
    return objects;
  }
}
