package com.brimud.filter;

import com.brimud.account.Account;
import com.brimud.db.AccountDao;
import com.brimud.db.ZoneDao;
import com.brimud.model.Room;
import com.brimud.model.Zone;
import com.brimud.session.Session;
import com.brimud.session.SessionManager;
import com.brimud.statemachine.EndState;
import com.brimud.statemachine.InitialState;
import com.brimud.statemachine.State;
import com.google.inject.Inject;

@InitialState("init")
@EndState("done")
public class LoginAndRegistration {
  
  private final AccountDao accountDao;
  private final ZoneDao zoneDao;
  private final SessionManager sessionManager;

  @Inject
  public LoginAndRegistration(AccountDao accountDao, ZoneDao zoneDao, SessionManager sessionManager) {
    this.accountDao = accountDao;
    this.zoneDao = zoneDao;
    this.sessionManager = sessionManager;
  }

  @State("init")
  public String init(Session session, String command) {
    session.addMessage("Type 'register' to create a new account.\n");
    session.addMessage("What is your login id? ");
    
    return "loginId";
  }
  
  @State("loginId")
  public String loginId(Session session, String command) {
    if ("register".equalsIgnoreCase(command)) {
      session.addMessage("What will be your login id? ");
      return "newLoginId";
    } else {
      return handleAccountName(session, command);
    }
  }
  
  private String handleAccountName(Session session, String accountName) {
    Account existingAccount = accountDao.getById(accountName);
    if (existingAccount == null) {
      session.addMessage("Invalid user name. Type 'register' to create a new account.\n");
      session.addMessage("What is your login id? ");
      return "loginId";
    } else {
      session.setAccount(existingAccount);
      session.addMessage("Password? ");
      return "password";
    }
  }
  
  @State("newLoginId")
  public String newLoginId(Session session, String accountName) {
    Account existingAccount = accountDao.getById(accountName);
    
    if (existingAccount == null) {
      session.setAccount(new Account(accountName));
      session.addMessage("Password? ");
      return "newPassword";
    } else {
      session.addMessage("That name is already in use. Try again.\n");
      session.addMessage("What will be your login id?");
      return "newLoginId";
    }
  }
  
  @State("newPassword")
  public String newPassword(Session session, String password) {
    
    // TODO confirm the new password before continuing
    session.setPassword(password);
//    accountDao.saveOrUpdate(session.getAccount());
    
    session.authenticate(password);
    
    setStartingRoom(session.getAccount());
    
    session.addMessage("You are registered\n");
    
    sessionManager.init(session);
    return "done";
  }
  
  private void setStartingRoom(Account account) {
    Zone zone = zoneDao.getStartingZone();
    String roomId = zone.getStartingRoom();
    
    Room room = zone.getRoom(roomId);
    
    account.getPlayer().setRoom(room);
  }

  @State("password")
  public String password(Session session, String password) {
    if (!session.authenticate(password)) {
      session.addMessage("Invalid password. Try again.\n");
      session.addMessage("Password? ");
      return "password";
    } else {
      // TODO hack in the player name as the character for now. move on to character creation later
      setStartingRoom(session.getAccount());

      session.addMessage("Logged in\n");
      
      sessionManager.init(session);
      return "done";
    }
  }
}