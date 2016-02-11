package org.fpsrobotics.sensors;

public interface IGyroscope
{
	double getCount();
	void enable();
	void disable();
	void resetCount();
}
