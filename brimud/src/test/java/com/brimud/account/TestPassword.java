package com.brimud.account;


import org.junit.Assert;
import org.junit.Test;

public class TestPassword {

  @Test
  public void testPassword() {
    String rawPassword = "password";
    Password password = new Password(rawPassword);
    
    Assert.assertTrue(password.matches(rawPassword));
    
    Password password2 = new Password(rawPassword);
    
    Assert.assertNotSame(password.getPasswordHash(), password2.getPasswordHash());
    
    Assert.assertTrue(password.matches(rawPassword));
    Assert.assertTrue(password2.matches(rawPassword));    
  }
}
