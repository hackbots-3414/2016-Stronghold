package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.Timer;

public class ClockTimer implements ITimer {

	@Override
	public void waitTimeInMillis(double milliseconds) {
		Timer.delay(milliseconds*(1/1000));
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
