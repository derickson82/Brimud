package com.brimud.world;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brimud.event.Event;
import com.brimud.event.Events;

public class Ticker {
	
	private static final Logger logger = LoggerFactory.getLogger(Ticker.class);
	
	private static final int DEFAULT_PERIOD_MILLIS = 3000;
	
	private final Events events = new Events();
	
	private Timer timer = null;
	
	private final int periodInMilliseconds;
	
	public Ticker() {
		this(DEFAULT_PERIOD_MILLIS);
	}
	
	public Ticker(int periodInMilliseconds) {
		this.periodInMilliseconds = periodInMilliseconds;
	}
	
	public void addListener(Ticker.Listener listener) {
		events.listen(Ticker.TickEvent.class, listener);
	}
	
	public void start() {
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					events.fire(new TickEvent());
				}
			}, periodInMilliseconds, periodInMilliseconds);
		} else {
			logger.warn("Timer is already started! Ignoring request to start again");
		}
	}
	
	public void stop() {
		if (timer != null) {
			timer.cancel();
		} else {
			logger.warn("Timer is already stopped! Ignoring request to stop again");
		}
	}

	@SuppressWarnings("serial")
	public static class TickEvent implements Event<Ticker.Listener> {
		@Override
		public void notify(Ticker.Listener listener) {
			listener.tick(this);
		}
		
	}
	
	public static interface Listener {
		void tick(Ticker.TickEvent tickEvent);
	}
}