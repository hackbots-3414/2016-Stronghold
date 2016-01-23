package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class Potentiometer implements IPIDFeedbackDevice
{
	AnalogInput analog;
	boolean enabled;
	
	public Potentiometer(int channel)
	{
		analog = new AnalogInput(channel);
		enabled = true;
	}

	@Override
	public double getCount()
	{
		if(enabled)
		{
			return analog.getValue();
		} else
		{
			return 0;
		}
	}

	@Override
	public void enable()
	{
		enabled = true;
	}

	@Override
	public void disable()
	{
		enabled = false;
	}

	@Override
	public void resetCount()
	{
		analog.resetAccumulator();
	}

	@Override
	public FeedbackDevice whatPIDDevice()
	{
		return FeedbackDevice.AnalogPot;
	}

}
