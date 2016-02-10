package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * A class that allows other parts of the code call it in order to wait for a certain amount of time.
 *
 */
public class ClockTimer implements ITimer {

	public ClockTimer()
	{
		
	}
	
	@Override
	public void waitTimeInMillis(double milliseconds) {
		Timer.delay(milliseconds/1000);
	}

	@Override
	public void waitTimeInSeconds(double seconds) {
		Timer.delay(seconds);
		
	}

	@Override
	public void waitTimeInMinutes(double minutes) {
		Timer.delay(minutes*60);
	}


}
