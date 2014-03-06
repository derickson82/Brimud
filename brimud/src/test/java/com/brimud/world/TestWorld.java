package com.brimud.world;



import com.brimud.world.Ticker.TickEvent;

public class TestWorld {

	public static void main(String[] args) {
		final Ticker timer = new Ticker();
		class TestListener implements Ticker.Listener {
			private int ticks = 0;
			
			@Override
			public void tick(TickEvent tickEvent) {
				ticks++;
				System.out.println("Tick Event fired: " + ticks);
				if (ticks >= 3) {
					timer.stop();
				}
			}
		}

		TestListener listener = new TestListener();
		timer.addListener(listener);
		
		timer.start();
	}
	
	public static class World {
		
	}
	
	public static class Zone {
		
	}
	
	public static class Room {
		
	}
	
	public static class Item {
		
	}
	
	public static class Mob {
		
	}

}
