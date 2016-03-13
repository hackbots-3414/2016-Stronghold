package org.fpsrobotics.sensors;

/**
 * Defines a gyroscope in the yaw direction.
 *
 */
public interface IGyroscope
{
	double getCount();
	void enable();
	void disable();
	void resetCount();
	double getAttitude();
}
