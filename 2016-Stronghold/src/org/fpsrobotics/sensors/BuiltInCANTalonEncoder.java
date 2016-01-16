package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class BuiltInCANTalonEncoder implements IPIDFeedbackDevice 
{
	CANTalon canMotor;
	
	public BuiltInCANTalonEncoder(CANTalon canMotor)
	{
		this.canMotor = canMotor;
	}
	
	@Override
	public double getCount() 
	{
		return canMotor.get();
	}

	@Override
	public void enable() 
	{
		canMotor.enableControl();
	}

	@Override
	public void disable() 
	{
		canMotor.disableControl();
	}

	@Override
	public void resetCount() 
	{
		canMotor.reset();
	}

	@Override
	public FeedbackDevice whatPIDDevice() 
	{
		return CANTalon.FeedbackDevice.QuadEncoder;
	}

}
