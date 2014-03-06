/**
 * 
 */
package com.brimud.command.builder;

import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.brimud.command.Command;
import com.brimud.command.Direction;
import com.brimud.db.PlayerDao;
import com.brimud.db.RoomDao;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.service.MessageService;
import com.google.inject.Inject;

/**
 * @author Dan
 *
 */
class RoomIdCommand implements Command {

 static final String ROOM_ID = "rid";
  
  private final int ID_MAX_LENGTH = Integer.MAX_VALUE;
  private final MessageService messageService;
  private final RoomDao roomDao;
  private final PlayerDao playerDao;

  @Inject
  RoomIdCommand(MessageService messageService, RoomDao roomDao, PlayerDao playerDao) {
    this.messageService = messageService;
    this.roomDao = roomDao;
    this.playerDao = playerDao;
  }
  
  /* (non-Javadoc)
   * @see com.brimud.command.Command#doCommand(com.brimud.model.Player, java.lang.String, java.lang.String)
   */
  @Override
  public void doCommand(Player player, String command, String newRoomId) {
    if (newRoomId.length() > ID_MAX_LENGTH) {
      messageService.sendMessage(player, "Room ids cannot exceed " + ID_MAX_LENGTH + " characters.");
    } else {
      Room currentRoom = player.getRoom();
      RoomId roomId = new RoomId(currentRoom.getId().getZone(), newRoomId);
      
      Room newRoom = roomDao.getById(roomId);
      if (newRoom != null) {
        messageService.sendMessage(player, "A room with the id: " + newRoomId + " already exists!");
        return;
      }
      newRoom = new Room();
      newRoom.setId(roomId);
      
      newRoom.setName(currentRoom.getName());
      newRoom.setDescription(currentRoom.getDescription());
      newRoom.setPlayers(currentRoom.getPlayers());
      newRoom.setExits(currentRoom.getExits());
      
      Criteria criteria = roomDao.createCriteria();

      @SuppressWarnings("unchecked")
      List<Room> rooms = criteria.createCriteria("exits").add(Restrictions.idEq(currentRoom.getId())).list();
      
      for (Room r : rooms) {
        for (Entry<Direction, Room> entry : r.getExits().entrySet()) {
          if (entry.getValue().getId().equals(currentRoom.getId())) {
            r.addExit(entry.getKey(), newRoom);
            break;
          }
        }
      }
      
      @SuppressWarnings("unchecked")
      List<Player> playersInRoom = playerDao.createCriteria().add(Restrictions.eq("room", currentRoom)).list();
      for (Player p : playersInRoom) {
        p.setRoom(newRoom);
      }
      
      roomDao.saveOrUpdate(newRoom);
      roomDao.delete(currentRoom);
      messageService.sendMessage(player, newRoom.look(player));
    }
  }
}
