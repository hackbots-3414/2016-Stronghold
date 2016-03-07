package org.fpsrobotics.actuators;

/**
 * Describes a motor that has a linear travel with bottom and top limits defined
 * through sensors.
 *
 */
public interface ILinearActuator
{
	public void goToTopLimit();

	public void goToBottomLimit();

	public void setSpeed(double speed);

	public void stop();
}
