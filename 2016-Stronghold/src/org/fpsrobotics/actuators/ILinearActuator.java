package org.fpsrobotics.actuators;

public interface ILinearActuator
{
	public void goToTopLimit();
	public void goToBottomLimit();
	public void setSpeed(double speed);
	public void stop();
}
