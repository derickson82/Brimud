/**
 * 
 */
package com.brimud.command;

/**
 * @author dan
 * 
 */
public enum Direction {
  NORTH, EAST, SOUTH, WEST, UP, DOWN;

  public String getName() {
    return name().toLowerCase();
  }

  public static Direction fromName(String name) {
    Direction val = null;
    for (Direction d : values()) {
      if (d.getName().equalsIgnoreCase(name)) {
        val = d;
        break;
      }
    }
    if (val == null) {
      if ("n".equalsIgnoreCase(name)) {
        val = Direction.NORTH;
      } else if ("s".equalsIgnoreCase(name)) {
        val = Direction.SOUTH;
      } else if ("e".equalsIgnoreCase(name)) {
        val = Direction.EAST;
      } else if ("w".equalsIgnoreCase(name)) {
        val = Direction.WEST;
      } else if ("u".equalsIgnoreCase(name)) {
        val = Direction.UP;
      } else if ("d".equalsIgnoreCase(name)) {
        val = Direction.DOWN;
      }
    }

    return val;
  }

  public Direction opposite() {
    Direction opposite;
    switch (this) {
    case NORTH:
      opposite = SOUTH;
      break;
    case SOUTH:
      opposite = NORTH;
      break;
    case EAST:
      opposite = WEST;
      break;
    case WEST:
      opposite = EAST;
      break;
    case UP:
      opposite = DOWN;
      break;
    case DOWN:
      opposite = UP;
      break;
    default:
      throw new IllegalArgumentException("Never will happen");
    }
    return opposite;
  }
}
