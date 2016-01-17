package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDEnabledDevice;

public interface ILauncher extends IPIDEnabledDevice
{
	public void intake();
	public void shoot();
	public void stop();

}
