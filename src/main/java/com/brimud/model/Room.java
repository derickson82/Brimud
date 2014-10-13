package com.brimud.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.brimud.command.Direction;

public class Room {

  private RoomId id;

  private String name;

  private String shortDescription;

  private String description;

  private Map<Direction, Room> exits = new HashMap<Direction, Room>();
  
  private Map<Direction, String> scanDescriptions = new HashMap<Direction, String>();
  
  private Map<String, String> extraDescriptions = new HashMap<String, String>();

  private Set<Player> players = new HashSet<Player>();

  public Room() {
    this.name = "Room needs a name";
    this.shortDescription = "You see nothing of interest.";
    this.description = "Room needs a description";
  }

  public RoomId getId() {
    return id;
  }

  public void setId(RoomId roomId) {
    this.id = roomId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<Player> getPlayers() {
    return players;
  }

  public void setPlayers(Set<Player> players) {
    this.players = players;
  }

  public void enter(Player player) {
    this.players.add(player);
    id.getZone().addPlayer(player);
    player.setRoom(this);
  }

  public void exit(Player player) {
    player.setRoom(null);
    this.players.remove(player);
    this.id.getZone().removePlayer(player);
  }

  public String look(Player player) {
    StringBuilder description = new StringBuilder();
    description.append(getName()).append("\n");
    description.append(getDescription()).append("\n");
    description.append(buildPlayerDescriptions(player));
    return description.toString();
  }
  
  public String scan(Player player) {
    StringBuilder scan = new StringBuilder();
    scan.append(getShortDescription()).append("\n");
    scan.append(buildPlayerDescriptions(player));
    return scan.toString();
  }
  
  public String buildPlayerDescriptions(Player player) {
    StringBuilder description = new StringBuilder();
    for (Player p : players) {
      if (!p.equals(player)) {
        description.append(p.getName()).append(" is standing here.\n");
      }
    }
    return description.toString();
  }

  public Map<Direction, Room> getExits() {
    if (exits == null) {
      exits = new HashMap<Direction, Room>();
    }
    return exits;
  }

  public void setExits(Map<Direction, Room> exits) {
    this.exits = exits;
  }

  public void addExit(Direction d, Room adjacent) {
    getExits().put(d, adjacent);
  }

  public void removeExit(Direction d) {
    getExits().remove(d);
  }

  public Room getExit(Direction direction) {
    return getExits().get(direction);
  }

  public void setScanDescriptions(Map<Direction, String> scanDescriptions) {
    this.scanDescriptions = scanDescriptions;
  }

  public Map<Direction, String> getScanDescriptions() {
    if (scanDescriptions == null) {
      scanDescriptions = new HashMap<Direction, String>();
    }
    return scanDescriptions;
  }
  
  public String getScanDescription(Direction d) {
    return getScanDescriptions().get(d);
  }
  
  public void removeScanDescription(Direction d) {
    getScanDescriptions().remove(d);
  }
  
  public void addScanDescription(Direction d, String description) {
    getScanDescriptions().put(d, description);
  }

  public void setExtraDescriptions(Map<String, String> extraDescriptions) {
    this.extraDescriptions = extraDescriptions;
  }

  public Map<String, String> getExtraDescriptions() {
    if (extraDescriptions == null) {
      extraDescriptions = new HashMap<String, String>();
    }
    return extraDescriptions;
  }
  
  public void addExtraDescription(String keyword, String description) {
    getExtraDescriptions().put(keyword, description);
  }
  
  public void removeExtraDescription(String keyword) {
    getExtraDescriptions().remove(keyword);
  }
  
  public String getExtraDescription(String keyword) {
    return getExtraDescriptions().get(keyword);
  }
}
