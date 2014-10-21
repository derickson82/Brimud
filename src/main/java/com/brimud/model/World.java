package com.brimud.model;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import com.brimud.service.BuilderException;

@Singleton
public class World {
	
	private Zone startingZone;
	private Set<Zone> zones = new HashSet<Zone>();

	public Zone getZoneById(String zoneId) {
		for (Zone z : zones) {
			if (z.getId().equals(zoneId)) {
				return z;
			}
		}
		return null;
	}

	public Room getRoomById(RoomId roomId) {
		Zone zone = getZoneById(roomId.getZoneId());
		if (zone != null) {
			return zone.getRoom(roomId.getRoomId());
		}
		return null;
	}

	public void deleteRoom(RoomId toDelete) throws BuilderException {
		Zone zone = getZoneById(toDelete.getZoneId());
		if (zone != null) {
			zone.removeRoom(toDelete);
		}
	}

	public Zone getStartingZone() {
		return startingZone;
	}

	public void setStartingZone(Zone zone1) {
		zones.add(zone1);
		startingZone = zone1;
	}

  public void addZone(Zone zone) {
    zones.add(zone);
  }
	
}
