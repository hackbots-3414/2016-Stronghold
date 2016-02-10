package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

/**
 * 
 * A class that implements the encoder connected to directly to the CAN Talon motor controller.
 *
 */
public class BuiltInCANTalonEncoder implements IPIDFeedbackDevice 
{
	private CANTalon canMotor;
	
	public BuiltInCANTalonEncoder(CANTalon canMotor)
	{
		this.canMotor = canMotor;
		canMotor.configEncoderCodesPerRev(2048);
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

	@Override
	public double getError() {
		return canMotor.getClosedLoopError();
	}

}
