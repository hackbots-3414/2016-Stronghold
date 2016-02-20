package org.fpsrobotics.PID;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * 
 * Describes a generic device that can provide feedback to a PID loop, like an encoder on a wheel of a drive train.
 */
public interface IPIDFeedbackDevice
{
	double getCount();
	void enable();
	void disable();
	void resetCount();
	double getError();
	double getRate();
	CANTalon.FeedbackDevice whatPIDDevice();
}
