package org.fpsrobotics.sensors;

/**
 * Defines a gyroscope in the yaw direction.
 *
 */
public interface IGyroscope
{
	public double getCount();

	public void enable();

	public void disable();

	public void resetCount();
	
	public double getAttitude();
}
