/**
 * 
 */
package com.brimud.model;

import com.brimud.character.Character;

/**
 * @author dan
 * 
 */
public class Player {

  private String name;
  private Character character;
  private Room room;

  Player() {
    // for hibernate
  }
  
  public Player(String name) {
    this.name = name;
    this.character = new Character();
    this.character.setName(name);
  }

  public Player(Character character) {
    this.character = character;
  }

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }
  
  public void setCharacter(Character character) {
    this.character = character;
  }
  
  public Character getCharacter() {
    return character;
  }
  
  
}
