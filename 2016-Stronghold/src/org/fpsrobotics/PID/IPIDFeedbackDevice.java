package org.fpsrobotics.PID;

import edu.wpi.first.wpilibj.CANTalon;

public interface IPIDFeedbackDevice
{
	double getCount();
	void enable();
	void disable();
	void resetCount();
	double getError();
	CANTalon.FeedbackDevice whatPIDDevice();
}
