/**
 * 
 */
package com.brimud.account;

import com.brimud.model.Player;


/**
 * @author dan
 * 
 */
public class Account {
  private String name;
  private Password password;
  
  private Player player;

  private boolean authenticated = false;

  Account() {
    // for hibernate
  }

  public Account(String name) {
    this.name = name;
    this.player = new Player(name);
  }

  public Account(String name, String password) {
    this(name);
    this.password = new Password(password);
  }

  public boolean authenticate(String password) {
    if (this.password != null) {
      authenticated = this.password.matches(password);
    }
    return authenticated;
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public static Account register(String userName) {
    return new Account(userName);
  }

  public String getName() {
    return name;
  }

  void setName(String name) {
    this.name = name;
  }

  public Password getPassword() {
    return password;
  }

  void setPassword(Password password) {
    this.password = password;
  }

  public void setNewPassword(String password) {
    this.password = new Password(password);
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public void setPlayer(Player player) {
    this.player = player;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    Account other = (Account) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
}
