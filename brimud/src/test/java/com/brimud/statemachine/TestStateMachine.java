/**
 * 
 */
package com.brimud.statemachine;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author dan
 *
 */
public class TestStateMachine {

	@Test
	public void test() {
	  SimpleStateMachine simple = new SimpleStateMachine();
	  StateMachine sm = new StateMachine(simple);
	  
	  Assert.assertNull(simple.value);
	  Assert.assertFalse(simple.executedFirst);
	  Assert.assertFalse(simple.executedSecond);
	  
	  String value = "blah";
	  sm.execute(value);
	  
	  Assert.assertEquals(simple.value, value);
	  Assert.assertTrue(simple.executedFirst);
	  Assert.assertFalse(simple.executedSecond);
	  
	  value = "hello";
	  sm.execute(value);
	  
	  Assert.assertEquals(simple.value, value);
	  Assert.assertTrue(simple.executedFirst);
	  Assert.assertTrue(simple.executedSecond);
	  
	  sm.execute("blah", "blah", "blah"); // no-op
	  
	  Assert.assertEquals(simple.value, value);
    Assert.assertTrue(simple.executedFirst);
    Assert.assertTrue(simple.executedSecond);
	}
	
	@Test
	public void dummyTest() {
	  Assert.assertTrue(true);
	}
}
