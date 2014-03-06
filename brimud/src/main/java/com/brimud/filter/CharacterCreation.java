package com.brimud.filter;

import com.brimud.character.Character;
import com.brimud.character.CharacterBuilder;
import com.brimud.db.PlayerDao;
import com.brimud.db.ZoneDao;
import com.brimud.model.Player;
import com.brimud.model.Room;
import com.brimud.model.Zone;
import com.brimud.session.Session;
import com.brimud.session.SessionManager;
import com.brimud.statemachine.EndState;
import com.brimud.statemachine.InitialState;
//import com.brimud.statemachine.State;
import com.google.inject.Inject;

// CHAR_SELECT -> ENTER_GAME
// CHAR_SELECT -> NEW_CHAR
// NEW_CHAR -> CHAR_NAME
// CHAR_NAME -> CHAR_NAME (failed validation, not unique)
// CHAR_NAME -> RACE
// RACE -> SELECT_BONUS (if allowed for the race)
// SELECT_BONUS -> CLASS
// RACE -> CLASS
// CLASS -> ABILITY_SELECT
// ABILITY_SELECT -> ABILITY_SCORE
// ABILITY_SCORE -> ABILITY_SELECT
// ABILTY_SELECT -> ABILITY_SELECT (print stats)
// ABILITY_SELECT -> CONFIRM
// CONFIRM -> COMPLETE

@InitialState("selectCharacter")
@EndState("complete")
public class CharacterCreation {

  private final SessionManager sessionManager;
  private final PlayerDao playerDao;
  private final ZoneDao zoneDao;
  
  @Inject
  public CharacterCreation(SessionManager sessionManager, PlayerDao playerDao, ZoneDao zoneDao) {
    this.sessionManager = sessionManager;
    this.playerDao = playerDao;
    this.zoneDao = zoneDao;
  }
  
//  @State("selectCharacter")
  public String selectCharacter(Session session, String command) {
    session.addMessage("This is where character selection should occur");
    doLogin(session);
    return "complete";
  }
  
  private void doLogin(Session session) {
//    Player player = playerDao.getById(session.getAccount().getName());
//    if (player == null) {
////      player = new Player(session.getAccount().getName());
//      playerDao.saveOrUpdate(player);
//    }
    
    Character pchar = new Character();
    pchar.setName(session.getAccount().getName());
    Player player = new Player(pchar);
    player.setName(session.getAccount().getName());
    
    playerDao.saveOrUpdate(player);
    
//    session.setPlayerName(player.getName());
    
    sessionManager.init(session);
    
    // TODO move this out! there are more steps after login (like creating/choosing a character)
    if (player.getRoom() == null) {
      Zone zone = zoneDao.getStartingZone();
      String startingRoomId = zone.getStartingRoom();
      Room startingRoom = zone.getRoom(startingRoomId);
      if (startingRoom != null) {
        startingRoom.enter(player);
      } else {
        throw new IllegalStateException("The room couldn't be saved");
      }
    }
    session.sendMessage(player.getRoom().look(player));
  }

}
