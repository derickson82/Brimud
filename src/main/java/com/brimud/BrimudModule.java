package com.brimud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.model.World;
import com.google.inject.AbstractModule;

/**
 * @author dan
 * 
 */
public class BrimudModule extends AbstractModule {

  private static final Logger logger = LoggerFactory.getLogger(BrimudModule.class);

  @Override
  protected void configure() {
    bind(World.class);
  }
}
