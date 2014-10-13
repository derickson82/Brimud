package com.brimud.account;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.brimud.util.Base64;

public class Password {

  private static final int ITERATIONS = 1027;
  
  private String passwordHash;
  private String salt;
  
  Password() {
    
  }
  
  public Password(String rawPassword) {
    this.salt = UUID.randomUUID().toString();

    this.passwordHash = digest(rawPassword, this.salt);
  }
  
  public String getPasswordHash() {
    return passwordHash;
  }
  
  void setPasswordHash(String hash) {
    this.passwordHash = hash;
  }

  String getSalt() {
    return salt;
  }
  
  void setSalt(String salt) {
    this.salt = salt;
  }
  
  private String digest(String rawPassword, String salt) {
    String toEncrypt = salt + rawPassword;
    MessageDigest md = null;
    
    try {
      md = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error getting SHA-512 instance!", e);
    }
    byte[] raw;
    try {
      raw = toEncrypt.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("No support for UTF-8 encoding!", e);
    }

    for (int i = 0; i < ITERATIONS; i++) {
      md.update(raw);
      raw = md.digest();
    }
    
    return Base64.encodeBytes(raw);
  }
  
  public boolean matches(String checkPassword) {
    String checkHash = digest(checkPassword, this.salt);
    return checkHash.equals(this.passwordHash);
  }  
}
