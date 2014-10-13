/**
 * 
 */
package com.brimud.character;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author dan
 *
 */
public class TestAbility {

  @Test
  public void testAbilityModifiers() {
	  
	    List<Integer[]> list = new ArrayList<Integer[]>();
	    
	    // samples from Table 1-3 in core pathfinder rulebook
	    list.add(new Integer[]{1, -5});
	    list.add(new Integer[]{2, -4});
	    list.add(new Integer[]{3, -4});
	    list.add(new Integer[]{4, -3});
	    list.add(new Integer[]{5, -3});
	    list.add(new Integer[]{14, 2});
	    list.add(new Integer[]{15, 2});
	    list.add(new Integer[]{26, 8});
	    list.add(new Integer[]{27, 8});
	    list.add(new Integer[]{44, 17});
	    list.add(new Integer[]{45, 17});

	    for (Integer[] val : list) {
	        Assert.assertEquals(new Score(val[0]).getModifier(), (int)val[1]);
	    }
  }
}
