package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDEnabledDevice;

public interface ILauncher extends IPIDEnabledDevice
{
	public void goToBottom();
	public void goToShootingPosition();
	public void goToPosition(int position);
	public void goUp();
	public void goDown();
	public void intake();
	public void shootSequence();
	public void stop();
	public void goToTopLimit();
	public void raiseArm();
	public void lowerArm();
	public void spinUp();
	public void launch();

}
