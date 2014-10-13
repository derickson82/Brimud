/**
 * 
 */
package com.brimud.character;

/**
 * @author dan
 *
 */
public class Validation {
  private final String msg;
  
  Validation(String msg) {
    this.msg = msg;
  }
  
  public String getMessage() {
    return msg;
  }
  
  @Override
  public String toString() {
    return getMessage();
  }
}
