package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDEnabledDevice;

public interface IMotor extends IPIDEnabledDevice
{
	double getSpeed();
	void setSpeed(double speed);
	void stop();
}
