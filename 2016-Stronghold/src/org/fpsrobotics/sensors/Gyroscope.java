package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class Gyroscope implements IPIDFeedbackDevice
{
	AnalogGyro gyro;
	boolean enabled;
	
	public Gyroscope(int channel)
	{
		gyro = new AnalogGyro(channel);
		
		gyro.initGyro();
		
		enabled = true;
	}
	
	@Override
	public double getCount() {
		if(enabled)
		{
			return gyro.getAngle();
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
		gyro.reset();
	}

	@Override
	public FeedbackDevice whatPIDDevice() {
		// isn't actually a PID device... 
		return null;
	}

}
