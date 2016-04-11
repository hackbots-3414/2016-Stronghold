package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

/**
 * A class that implements the encoder connected to directly to the CAN Talon
 * motor controller.
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
		try
		{
			return canMotor.getEncPosition();
		} catch (Exception e)
		{
			System.out.println("Encoder not mounted somewhere, proceed with caution");
			return 0;
		}
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
		canMotor.setPosition(0);
	}

	@Override
	public FeedbackDevice whatPIDDevice()
	{
		return CANTalon.FeedbackDevice.QuadEncoder;
	}

	@Override
	public double getError()
	{
		try
		{
			return canMotor.getClosedLoopError();
		} catch (Exception e)
		{
			System.out.println("Encoder not mounted somewhere, proceed with caution");
			return 0;
		}
	}

	@Override
	public double getRate()
	{
		try
		{
			return canMotor.getEncVelocity();
		} catch (Exception e)
		{
			System.out.println("Encoder not mounted somewhere, proceed with caution");
			return 0;
		}
	}
	
	@Override
	public double getDistance() //TODO: Measure the field before Competition
	{
		return (getCount() / 785.79);
	}

}
