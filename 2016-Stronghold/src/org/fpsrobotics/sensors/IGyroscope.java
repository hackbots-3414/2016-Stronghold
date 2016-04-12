package org.fpsrobotics.sensors;

/**
 * Defines a gyroscope in the yaw direction.
 *
 */
public interface IGyroscope
{
	public double getHardCount();
	
	public double getSoftCount();

	public void enable();

	public void disable();

	public void softResetCount();
	
	public void hardResetCount();
	
	public double getPitch();
	
	public double getRate();
	
	public double getPitchRate();
}

