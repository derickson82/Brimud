/**
 * 
 */
package com.brimud.model;

import java.io.Serializable;

import com.brimud.util.StringUtil;

/**
 * @author dan
 * 
 */
public class RoomId implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private Zone zone;
  private String roomId;
  
  RoomId() {
    // for hibernate
  }
  
  public RoomId(String zoneId, String roomId) {
    setZone(zoneId);
    setRoomId(roomId);
  }
  
  public RoomId(Zone zone, String roomId) {
    setZone(zone);
    setRoomId(roomId);
  }
  
  public void setZone(Zone zone) {
    this.zone = zone;
  }
  
  public void setZone(String zoneId) {
    this.zone = new Zone(zoneId);
  }
  
  public Zone getZone() {
    return zone;
  }
  
  public String getZoneId() {
    return zone != null ? zone.getId() : null;
  }
  
  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }
  
  public String getRoomId() {
    return roomId;
  }
  
  @Override
  public String toString() {
    return zone.getId() + "." + roomId;
  }
  
  public static RoomId fromString(String roomId) {
    final RoomId parsedRoomId;
    String trimmedRoomId = StringUtil.trimToNull(roomId);
    if (trimmedRoomId != null) {
      String[] split = trimmedRoomId.split("\\.");
      if (split.length != 2) {
        parsedRoomId = null;
      } else {
        parsedRoomId = new RoomId(split[0], split[1]);
      }
    } else {
      parsedRoomId = null;
    }
    return parsedRoomId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((roomId == null) ? 0 : roomId.hashCode());
    result = prime * result + ((zone == null) ? 0 : zone.hashCode());
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
    RoomId other = (RoomId) obj;
    if (roomId == null) {
      if (other.roomId != null)
        return false;
    } else if (!roomId.equals(other.roomId))
      return false;
    if (zone == null) {
      if (other.zone != null)
        return false;
    } else if (!zone.equals(other.zone))
      return false;
    return true;
  }

}
