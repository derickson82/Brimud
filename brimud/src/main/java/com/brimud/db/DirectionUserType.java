/**
 * 
 */
package com.brimud.db;

import com.brimud.command.Direction;

/**
 * @author dan
 *
 */
public class DirectionUserType extends EnumUserType<Direction> {
  public DirectionUserType() {
    super(Direction.class);
  }
}
