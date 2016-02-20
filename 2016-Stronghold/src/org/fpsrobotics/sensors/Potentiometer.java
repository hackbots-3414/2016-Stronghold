package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

/**
 * 
 * Defines an analog potentiometer.
 *
 */
public class Potentiometer implements IPIDFeedbackDevice
{
	private AnalogInput analog;
	private boolean isEnabled;
	
	public Potentiometer(int channel)
	{
		analog = new AnalogInput(channel);
		isEnabled = true;
	}

	@Override
	public double getCount()
	{
		if(isEnabled)
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
		isEnabled = true;
	}

	@Override
	public void disable()
	{
		isEnabled = false;
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

	@Override
	public double getError() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRate() {
		// TODO: make it return rate of rotation
		return 0;
	}

}
