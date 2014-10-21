/**
 * 
 */
package com.brimud;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.command.CommandModule;
import com.brimud.command.builder.BuilderCommandModule;
import com.brimud.filter.FilterModule;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.model.World;
import com.brimud.model.Zone;
import com.brimud.netty.BrimudServer;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author dan
 * 
 */
public class Brimud {

	private static final Logger logger = LoggerFactory.getLogger(Brimud.class);

	private final BrimudServer server;
	private final World world;
	
	@Inject
	public Brimud(BrimudServer server, World world) {
		this.server = server;
		this.world = world;
	}

	private void run() {
		logger.info("Starting up Brimud Engine...");
		
		try {
			registerShutdownHook();
			logger.info("Starting server ...");
			server.run();
		} catch (Exception e) {
			logger.error("Error telnet properties file", e);
			System.exit(1);
		}
	}
	
	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("Shutting down Brimud...");
				server.shutdown();
			}
		});
	}

	void init() {

		logger.debug("calling init to set up the starting zone, if necessary");

		Zone zone1 = world.getStartingZone();
		if (zone1 == null) {
			zone1 = new Zone("talrissar", "Talrissar", "Main city");
			world.setStartingZone(zone1);
		}

		String startingRoomId = zone1.getStartingRoom();
		if (startingRoomId == null) {
			startingRoomId = "room1";
			RoomId roomId = new RoomId(zone1, startingRoomId);

			Room startingRoom = world.getRoomById(roomId);
			if (startingRoom == null) {
				startingRoom = new Room();
				startingRoom.setId(roomId);
				zone1.addRoom(startingRoom);
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
