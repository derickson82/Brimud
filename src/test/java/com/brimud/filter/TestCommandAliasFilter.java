/**
 * 
 */
package com.brimud.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author dan
 *
 */
public class TestCommandAliasFilter extends EasyMockSupport {

  
  private String[] wrap(String...objects) {
    return objects;
  }
  
  @Test
  public void test() {
	    List<String[]> commands = new ArrayList<String[]>();
	    
	    commands.add(wrap(null, null));
	    commands.add(wrap("hello", "hello"));
	    commands.add(wrap("'hello", "say hello"));
	    commands.add(wrap("  n  ", "north"));
	    commands.add(wrap("N", "north"));
	    commands.add(wrap("s", "south"));
	    commands.add(wrap("e", "east"));
	    commands.add(wrap("w", "west"));
	    commands.add(wrap("u", "up"));
	    commands.add(wrap("d", "down"));
	    commands.add(wrap("l", "look"));
	    commands.add(wrap("ew", "ew"));
	    
	    for (String[] val : commands) {
	    	String command = val[0];
	    	String expected = val[1];
	    	CommandAliasFilter aliasFilter = new CommandAliasFilter();
	        String actual = aliasFilter.replaceAlias(command);
	        Assert.assertEquals(actual, expected); 
	    }

  }
}
