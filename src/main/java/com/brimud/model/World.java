package com.brimud.model;

import java.util.Set;

import com.brimud.service.BuilderException;

public class World {
	Set<Zone> zones;

	public Zone getZoneById(String zoneId) {
		throw new RuntimeException("Loading zone by id not implemented");
	}

	public Room getRoomById(RoomId roomId) {
		return null;
	}

	public void deleteRoom(RoomId toDelete) throws BuilderException {
	}

	public Zone getStartingZone() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
