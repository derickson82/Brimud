package com.brimud.session;

import org.junit.Assert;
import org.junit.Test;


public class TestLoginContext {

  @Test
  public void test() {
    /*
     * These are the State transitions for the Login Context
     */
    // ACCOUNTNAME -> ACCOUNTNAME (invalid account name or account does not exist!)
    // ACCOUNTNAME -> PASSWORD
    // PASSWORD -> PASSWORD (fewer than max retries)
    // PASSWORD -> DISCONNECT (max retries met)
    // PASSWORD -> LOGIN
    // ACCOUNT -> REGISTER
    // REGISTER -> NEW_ACCOUNTNAME
    // NEW_ACCOUNTNAME -> NEW_ACCOUNTNAME (invalid or used)
    // NEW_ACCOUNTNAME -> NEW_PASSWORD
    // NEW_PASSWORD -> VERIFY_PASSWORD
    // VERIFY_PASSWORD -> NEW_PASSWORD (passwords don't match)
    // VERIFY_PASSWORD -> LOGIN
    
    LoginContext ctx = new LoginContext();
    Assert.assertEquals(ctx.getMessage(), "Type 'register' to create a new account.\nWhat is your login id? ");

    ctx.handleCommand(null, "register");
    Assert.assertEquals(ctx.getMessage(), "What will be your login id? ");
    
    ctx.handleCommand(null, "Jerik");
    Assert.assertEquals(ctx.getMessage(), "Password? ");
    
    ctx.handleCommand(null, "password");
    Assert.assertEquals(ctx.getMessage(), "Logged in");
    
    
  }
  
}
