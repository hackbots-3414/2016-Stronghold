package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

public interface IAccelerometer
{
	double getX();
	double getY();
	double getZ();
	void reset();
}
