package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class Gyroscope implements IGyroscope
{
	private AnalogGyro gyro;
	private boolean isEnabled;
	
	public Gyroscope(int channel)
	{
		gyro = new AnalogGyro(channel);
		
		gyro.initGyro();
		
		isEnabled = true;
	}
	
	@Override
	public double getCount() {
		if(isEnabled)
		{
			return gyro.getAngle();
		} else
		{
			return 0;
		}
	}

	@Override
	public void enable() {
		isEnabled = true;
	}

	@Override
	public void disable() {
		isEnabled = false;
	}

	@Override
	public void resetCount() {
		gyro.reset();
	}

}
