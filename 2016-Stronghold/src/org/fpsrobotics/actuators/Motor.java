package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

/**
 * A generic motor class that does not use the CAN interface. This class would
 * be suitable to control the Jaguar, Victor, Talon and Talon SR motor
 * controllers.
 *
 */
public class Motor implements IMotor
{
	// private double p, i, d;
	private IPIDFeedbackDevice device;
	private SpeedController motor;
	private boolean isInverted;

	public Motor(SpeedController motor, boolean invertDirection)
	{
		this.motor = motor;
		// this.motor.setInverted(invertDirection);
		this.isInverted = invertDirection;
	}

	public Motor(SpeedController motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		this(motor, invertDirection);
		this.device = device;
	}

	@Override
	/**
	 * Returns the current applied throttle.
	 */
	public double getSpeed()
	{
		if (device == null)
		{
			return motor.get();
		} else
		{
			// implement later
		}

		return 0.0;
	}

	@Override
	public void setSpeed(double speed)
	{
		if (device == null)
		{
			if (isInverted)
			{
				motor.set(-speed);
			} else
			{
				motor.set(speed);
			}
		} else
		{
			// implement later
		}
	}

	@Override
	public void stop()
	{
		if (device == null)
		{
			motor.set(0.0);
		} else
		{
			// implement later
		}
	}
}
