package org.fpsrobotics.actuators;

public interface IMotor
{
	double getSpeed();
	void setSpeed(double speed);
	void stop();
}
