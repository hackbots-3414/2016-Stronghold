package org.fpsrobotics.sensors;

public interface ITimer {
	public void waitTimeInMillis(double milliseconds);
	public void waitTimeInSeconds(double seconds);
	public void waitTimeInMinutes(double minutes);
}
