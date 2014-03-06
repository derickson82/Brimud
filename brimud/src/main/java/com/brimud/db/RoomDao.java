/**
 * 
 */
package com.brimud.db;

import org.hibernate.SessionFactory;

import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.google.inject.Inject;

/**
 * @author dan
 *
 */
public class RoomDao extends HibernateDao<Room, RoomId> {

  @Inject
  RoomDao(SessionFactory sessionFactory) {
    super(Room.class, RoomId.class, sessionFactory);
  }

}
