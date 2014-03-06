/**
 * 
 */
package com.brimud.telnet;

import java.io.IOException;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.filter.LoginAndRegistration;
import com.brimud.session.Session;
import com.brimud.session.SessionListener;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author dan
 * 
 */
public class MudShell implements Shell {

	private static final Logger logger = LoggerFactory
			.getLogger(MudShell.class);

	@Inject
	private static Injector injector;

	private final Session session;

	private Connection connection;
	private BasicTerminalIO terminalIo;

	@Inject
	MudShell(Session session, final LoginAndRegistration lar) {
		this.session = session;
		this.session.addSessionListener(new SessionListener() {
			@Override
			public void init(Session session) {
				session.checkLoginState(null);
			}

			@Override
			public void closing(Session session) {
			}
		});
	}

	public static Shell createShell() {
		return injector.getInstance(MudShell.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.wimpi.telnetd.shell.Shell#run(net.wimpi.telnetd.net.Connection)
	 */
	@Override
	public void run(Connection con) {
		try {
			initSession(con);
			while (!session.isQuit()) {
				transmitQueuedMessages();
				pollCommands();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					logger.warn("Thread interrupted", e);
				}
			}

		} catch (IOException e) {
			logger.error("IO error", e);
		}

		session.fireDisconnect();
	}

	private void initSession(Connection con) throws IOException {
		this.connection = con;
		this.terminalIo = con.getTerminalIO();
		this.terminalIo.eraseScreen();
		this.terminalIo.homeCursor();
		this.terminalIo.flush();
		session.fireConnect();
	}

	private void pollCommands() throws IOException {
		StringBuilder commandBuilder = new StringBuilder();
		int read;
		while ((read = terminalIo.read()) != 10) {
			commandBuilder.append((char) read);
		}
		String command = commandBuilder.toString();

		session.fireCommand(command);
	}

	private void transmitQueuedMessages() {
		boolean messagesToSend = false;
		StringBuilder b = new StringBuilder();
		String msg;
		while ((msg = session.popQueuedMessage()) != null) {
			messagesToSend = true;
			b.append(msg);
		}
		if (messagesToSend) {
			try {
				terminalIo.write(b.toString());
				terminalIo.flush();
			} catch (IOException e) {
				logger.error("Error writing message to queue", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.wimpi.telnetd.net.ConnectionListener#connectionIdle(net.wimpi.telnetd
	 * .net.ConnectionEvent)
	 */
	@Override
	public void connectionIdle(ConnectionEvent ce) {
		logger.info("Connection is idle: "
				+ ce.getSource().getConnectionData().getInetAddress());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.wimpi.telnetd.net.ConnectionListener#connectionLogoutRequest(net.
	 * wimpi.telnetd.net.ConnectionEvent)
	 */
	@Override
	public void connectionLogoutRequest(ConnectionEvent ce) {
		logger.info("Connection logout requested");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.wimpi.telnetd.net.ConnectionListener#connectionSentBreak(net.wimpi
	 * .telnetd.net.ConnectionEvent)
	 */
	@Override
	public void connectionSentBreak(ConnectionEvent ce) {
		logger.info("Connection sent break");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.wimpi.telnetd.net.ConnectionListener#connectionTimedOut(net.wimpi
	 * .telnetd.net.ConnectionEvent)
	 */
	@Override
	public void connectionTimedOut(ConnectionEvent ce) {
		logger.info("Connection timed out");
		try {
			terminalIo.write("Timout");
			terminalIo.flush();

			connection.close();
		} catch (IOException e) {
			logger.error("Error on timeout", e);
		}
	}
}
