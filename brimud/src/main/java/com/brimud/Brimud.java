/**
 * 
 */
package com.brimud;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.wimpi.telnetd.BootException;
import net.wimpi.telnetd.TelnetD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.command.CommandModule;
import com.brimud.command.builder.BuilderCommandModule;
import com.brimud.db.DatabaseModule;
import com.brimud.db.RoomDao;
import com.brimud.db.Transacted;
import com.brimud.db.ZoneDao;
import com.brimud.filter.FilterModule;
import com.brimud.model.Room;
import com.brimud.model.RoomId;
import com.brimud.model.Zone;
import com.brimud.telnet.TelnetModule;
import com.brimud.tools.Exporter;
import com.brimud.tools.Importer;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author dan
 * 
 */
public class Brimud {

	private static final Logger logger = LoggerFactory.getLogger(Brimud.class);

	// private static Injector injector;

	private TelnetD telnetDaemon = null;

	private final ZoneDao zoneDao;

	private final RoomDao roomDao;

	@Inject
	public Brimud(ZoneDao zoneDao, RoomDao roomDao) {
		this.zoneDao = zoneDao;
		this.roomDao = roomDao;
	}

	private void run() {
		logger.info("Starting up Brimud Engine...");
		try {

			registerShutdownHook();

			Properties props = new Properties();
			InputStream telnetConfig = Brimud.class
			    .getResourceAsStream("/telnet/telnetd.properties");
			props.load(telnetConfig);
			telnetDaemon = TelnetD.createTelnetD(props);

			logger.info("Starting telnet daemon...");
			telnetDaemon.start();
		} catch (IOException e) {
			logger.error("Error telnet properties file", e);
			System.exit(1);
		} catch (BootException e) {
			logger.error("Couldn't start telnet daemon", e);
			System.exit(1);
		}
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("Shutting down Brimud...");
				if (telnetDaemon != null) {
					telnetDaemon.stop();
				}
			}
		});
	}

	@Transacted
	void init() {

		logger.debug("calling init to set up the starting zone, if necessary");

		Zone zone1 = zoneDao.getStartingZone();
		if (zone1 == null) {
			zone1 = new Zone("talrissar", "Talrissar", "Main city");
			zone1.setStartingZone(true);
			zoneDao.saveOrUpdate(zone1);
		}

		String startingRoomId = zone1.getStartingRoom();
		if (startingRoomId == null) {
			startingRoomId = "room1";
			RoomId roomId = new RoomId(zone1, startingRoomId);

			Room startingRoom = roomDao.getById(roomId);
			if (startingRoom == null) {
				startingRoom = new Room();
				startingRoom.setId(roomId);
				roomDao.saveOrUpdate(startingRoom);
			}
			zone1.setStartingRoom(startingRoomId);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length > 0) {
			Injector injector = Guice.createInjector(new BrimudModule(),
			    new DatabaseModule());
			if ("export".equals(args[0])) {
				Exporter exporter = injector.getInstance(Exporter.class);
				exporter.export();
			} else if ("import".equals(args[0])) {
				Importer importer = injector.getInstance(Importer.class);
				if (args.length > 2 && "-z".equals(args[1])) {
					importer.importZone(args[2]);
				} else {
					importer.importWorld(args[1]);
				}
			}
		} else {
			Injector injector = Guice.createInjector(new BrimudModule(),
			    new TelnetModule(), new FilterModule(), new DatabaseModule(),
			    new CommandModule(), new BuilderCommandModule());

			Brimud brimud = injector.getInstance(Brimud.class);
			brimud.init();
			brimud.run();
		}
	}
}
