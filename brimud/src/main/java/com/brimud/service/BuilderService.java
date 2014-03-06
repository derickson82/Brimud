package com.brimud.service;

import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.brimud.command.Direction;
import com.brimud.db.RoomDao;
import com.brimud.db.ZoneDao;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.google.inject.Inject;

public class BuilderService {

	private final ZoneDao zoneDao;
	private final RoomDao roomDao;

	@Inject
	public BuilderService(ZoneDao zoneDao, RoomDao roomDao) {
		this.zoneDao = zoneDao;
		this.roomDao = roomDao;
	}

	public void deleteRoom(Room toDelete) throws BuilderException {
		if (!toDelete.getPlayers().isEmpty()) {
			throw new BuilderException("The room must be empty to be deleted!");
		}

		Criteria criteria = roomDao.createCriteria();

		@SuppressWarnings("unchecked")
		List<Room> rooms = criteria.createCriteria("exits")
		    .add(Restrictions.idEq(toDelete.getId())).list();

		for (Room r : rooms) {
			for (Entry<Direction, Room> entry : r.getExits().entrySet()) {
				if (entry.getValue().getId().equals(toDelete.getId())) {
					r.removeExit(entry.getKey());
					break;
				}
			}
		}
		roomDao.delete(toDelete);
	}
	
	public void deleteRoom(RoomId roomId) throws BuilderException {
		Room toDelete = roomDao.getById(roomId);
		if (toDelete == null) {
			throw new BuilderException("Room " + roomId.toString()
			    + " does not exist!");
		}
		deleteRoom(toDelete);
	}

}
