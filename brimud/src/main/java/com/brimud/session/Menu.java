/**
 * 
 */
package com.brimud.session;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dan
 * 
 */
public class Menu implements MenuItem {
  
  private Map<String, MenuItem> options = new HashMap<String, MenuItem>();
  
  void addOption(String key, MenuItem option) {
    options.put(key, option);
  }
  
  void select(String key) {
    options.get(key).execute();
  }

  @Override
  public void execute() {
    System.out.println("Another menu has been selected");
  }
}
