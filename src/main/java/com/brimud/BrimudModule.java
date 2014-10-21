package com.brimud;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.model.World;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * @author dan
 * 
 */
public class BrimudModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(BrimudModule.class);

	@Override
	protected void configure() {

		Properties brimudProps = new Properties();
		try {
			brimudProps.load(BrimudModule.class.getResourceAsStream("/brimud.properties"));
		} catch (IOException e) {
			logger.warn("Properties could not be loaded. Using defaults", e);
		}
		Names.bindProperties(binder(), brimudProps);
		bind(World.class);
	}
}
