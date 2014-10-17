package com.brimud.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;
import javax.inject.Named;

import com.brimud.account.Account;
import com.brimud.filter.CommandFilterChain;
import com.brimud.filter.FilterChain;
import com.brimud.filter.MessageFilterChain;
import com.brimud.statemachine.StateMachine;

public class Session {

  private final List<SessionListener> listeners = new ArrayList<SessionListener>();

  private final Queue<String> messageQueue = new ConcurrentLinkedQueue<String>();
  
  private final FilterChain commandFilter;
  
  private final FilterChain messageFilter;
  
  private final StateMachine loginStateMachine;
  
  private boolean quit = false;
  
  private Account account = null;
  
  @Inject
  public Session(@CommandFilterChain FilterChain commandFilter, @MessageFilterChain FilterChain messageFilter,
      @Named("loginSm") StateMachine loginStateMachine) {
    this.commandFilter = commandFilter;
    this.messageFilter = messageFilter;
    this.loginStateMachine = loginStateMachine;
  }
  
  public void sendMessage(String msg) {
    messageFilter.filter(this, msg);
  }
  
  public void addMessage(String msg) {
    messageQueue.add(msg);
  }

  public boolean checkLoginState(String command) {
    if (loginStateMachine.isDone()) {
      return true;
    }
    loginStateMachine.execute(this, command);
    return false;
  }
  
  public String popQueuedMessage() {
    return messageQueue.poll();
  }

  public void quit(String msg) {
    messageQueue.add(msg);
    quit = true;
  }

  public boolean isAuthenticated() {
    return account != null && account.isAuthenticated();
  }
  
  public boolean authenticate(String password) {
    return account != null && account.authenticate(password);
  }
  
  public void setPassword(String password) {
    if (account != null) {
      account.setNewPassword(password);
    }
  }
  
  public boolean hasCharacter() {
    return account != null && account.getPlayer() != null;
  }

  public Account getAccount() {
    return account;
  }
    
  public void setAccount(Account account) {
    this.account = account;
  }

  public void addSessionListener(SessionListener sessionListener) {
    this.listeners.add(sessionListener);
  }
  
  public void removeSessionListener(SessionListener sessionListener) {
    this.listeners.remove(sessionListener);
  }

  public void fireCommand(String command) {
    commandFilter.filter(this, command);
  }

  public void fireConnect() {
    for (SessionListener sl : listeners) {
      sl.init(this);
    }
  }

  public void fireDisconnect() {
    for (SessionListener sl : listeners) {
      sl.closing(this);
    }
  }
  
  public boolean isQuit() {
    return quit;
  }
}
