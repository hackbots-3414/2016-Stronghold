package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Encoder;

public class QuadEncoder implements IPIDFeedbackDevice
{
	Encoder encoder;
	boolean enabled;
	
	public QuadEncoder(int channelA, int channelB)
	{
		encoder = new Encoder(channelA, channelB);
		encoder.reset();
		
		enabled = true;
	}

	@Override
	public double getCount() {
		if(enabled)
		{
			return encoder.get();
		} else
		{
			return 0;
		}
	}

	@Override
	public void enable() {
		enabled = true;
	}

	@Override
	public void disable() {
		enabled = false;
	}

	@Override
	public void resetCount() {
		encoder.reset();
	}

	@Override
	public FeedbackDevice whatPIDDevice() {
		return FeedbackDevice.QuadEncoder;
	}

}
