package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Encoder;

public class QuadEncoder implements IPIDFeedbackDevice
{
	private Encoder encoder;
	private boolean isEnabled;

	public QuadEncoder(int channelA, int channelB)
	{
		encoder = new Encoder(channelA, channelB);
		resetCount();

		isEnabled = true;
	}

	@Override
	public double getCount()
	{
		if (isEnabled)
		{
			return encoder.get();
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
		encoder.reset();
	}

	@Override
	public FeedbackDevice whatPIDDevice()
	{
		return FeedbackDevice.QuadEncoder;
	}

	@Override
	public double getError()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRate()
	{
		return encoder.getRate();
	}

}
