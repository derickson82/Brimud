/**
 * 
 */
package com.brimud.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.easymock.EasyMockSupport;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author dan
 *
 */
public class TestCommandAliasFilter extends EasyMockSupport {

  
  @DataProvider(name="commands")
  public Iterator<Object[]> commands() {
    List<Object[]> commands = new ArrayList<Object[]>();
    
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
    
    return commands.iterator();
  }
  
  private Object[] wrap(Object...objects) {
    return objects;
  }
  
  @Test(dataProvider="commands")
  public void test(String command, String expected) {
    CommandAliasFilter aliasFilter = new CommandAliasFilter();
    String actual = aliasFilter.replaceAlias(command);
    Assert.assertEquals(actual, expected); 
  }
}
