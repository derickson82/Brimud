package com.brimud.model;

import java.util.HashSet;
import java.util.Set;

public class Zone {

  private String id;
  private String name;
  private String description;
  private String shortDescription;
  private String startingRoom;
  
  private Set<Player> players = new HashSet<Player>();
  
  private Set<Room> rooms = new HashSet<Room>();

  public Zone(String id) {
    this.id = id;
  }
  
  public Zone(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
  
  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }
  
  public String getShortDescription() {
    return shortDescription;
  }
  
  public void setStartingRoom(String startingRoom) {
    this.startingRoom = startingRoom;
  }

  public String getStartingRoom() {
    return startingRoom;
  }

  public Set<Player> getPlayers() {
    return players;
  }
  
  public void setPlayers(Set<Player> players) {
    this.players = players;
  }
  
  public void addPlayer(Player player) {
    this.players.add(player);
  }
  
  public void removePlayer(Player player) {
    this.players.remove(player);
  }
  
  public void setRooms(Set<Room> rooms) {
    this.rooms = rooms;
  }

  public Set<Room> getRooms() {
    return rooms;
  }
  
  public void addRoom(Room room) {
    this.rooms.add(room);
  }
  
  public Room getRoom(String roomId) {
    for (Room room : rooms) {
      if (room
          .getId()
          .getRoomId()
          .equals(roomId)) {
        return room;
      }
    }
    return null;
  }
  
  public void removeRoom(RoomId roomId) {
	  removeRoom(getRoom(roomId.getRoomId()));
  }
  
  public void removeRoom(Room room) {
    this.rooms.remove(room);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Zone other = (Zone) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
