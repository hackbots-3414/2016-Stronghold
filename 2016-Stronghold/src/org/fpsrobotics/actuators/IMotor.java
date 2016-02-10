package org.fpsrobotics.actuators;

/**
 * Describes a generic motor
 */
public interface IMotor
{
	double getSpeed();
	void setSpeed(double speed);
	void stop();
}
