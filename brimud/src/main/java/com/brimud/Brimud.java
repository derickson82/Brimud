/**
 * 
 */
package com.brimud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.command.CommandModule;
import com.brimud.command.builder.BuilderCommandModule;
import com.brimud.filter.FilterModule;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.model.World;
import com.brimud.model.Zone;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author dan
 * 
 */
public class Brimud {

	private static final Logger logger = LoggerFactory.getLogger(Brimud.class);

	private final World world;

	@Inject
	public Brimud(World world) {
		this.world = world;
	}

	private void run() {
		logger.info("Starting up Brimud Engine...");
//		try {

			registerShutdownHook();

//			Properties props = new Properties();
//			InputStream telnetConfig = Brimud.class
//			    .getResourceAsStream("/telnet/telnetd.properties");
//			props.load(telnetConfig);
//			telnetDaemon = TelnetD.createTelnetD(props);
//
//			logger.info("Starting telnet daemon...");
//			telnetDaemon.start();
//		} catch (IOException e) {
//			logger.error("Error telnet properties file", e);
//			System.exit(1);
//		} catch (BootException e) {
//			logger.error("Couldn't start telnet daemon", e);
//			System.exit(1);
//		}
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("Shutting down Brimud...");
			}
		});
	}

	void init() {

		logger.debug("calling init to set up the starting zone, if necessary");

		Zone zone1 = world.getStartingZone();
		if (zone1 == null) {
			zone1 = new Zone("talrissar", "Talrissar", "Main city");
			zone1.setStartingZone(true);
		}

		String startingRoomId = zone1.getStartingRoom();
		if (startingRoomId == null) {
			startingRoomId = "room1";
			RoomId roomId = new RoomId(zone1, startingRoomId);

			Room startingRoom = world.getRoomById(roomId);
			if (startingRoom == null) {
				startingRoom = new Room();
				startingRoom.setId(roomId);
			}
			zone1.setStartingRoom(startingRoomId);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Injector injector = Guice.createInjector(new BrimudModule(), new FilterModule(),
		    new CommandModule(), new BuilderCommandModule());

		Brimud brimud = injector.getInstance(Brimud.class);
		brimud.init();
		brimud.run();
	}
}
