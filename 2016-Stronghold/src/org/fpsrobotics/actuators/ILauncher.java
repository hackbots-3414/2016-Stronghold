package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDEnabledDevice;

public interface ILauncher extends IPIDEnabledDevice
{
	public void inTake();
	public void shoot();
	public void stop();

}
