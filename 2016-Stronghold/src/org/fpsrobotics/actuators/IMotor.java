package org.fpsrobotics.actuators;

/**
 * Describes a generic motor
 */
public interface IMotor
{
	public double getSpeed();

	public void setSpeed(double speed);

	public void stop();
}
