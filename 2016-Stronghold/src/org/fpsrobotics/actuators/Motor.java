package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

/**
 * A generic motor class that does not use the CAN interface. 
 * This class would be suitable to control the Jaguar, Victor, Talon and Talon SR motor controllers.
 *
 */
public class Motor implements IMotor
{
	private double p, i, d, invert;
	private IPIDFeedbackDevice device;
	private boolean invertDirection;
	
	private SpeedController motor;
	
	public Motor(SpeedController motor, boolean invertDirection)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
	}
	
	public Motor(SpeedController motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
		this.device = device;
	}
	
	public Motor(Talon motor, boolean invertDirection)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
	}
	
	public Motor(Talon motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
		this.device = device;
	}
	
	@Override
	public double getSpeed() 
	{	
		if(device == null && motor != null)
		{ 
			return motor.get();
		} else if(device != null)
		{
			// implement later
		}
		
		return 0.0;
	}

	@Override
	public void setSpeed(double speed) 
	{
		invert = (invertDirection) ? -1 : 1;
		
		if(device == null && motor != null)
		{
			motor.set(speed * invert);
		} else if(device != null)
		{
			// implement later
		}
	}

	@Override
	public void stop() 
	{
		if(device == null && motor != null)
		{
			motor.set(0.0);
		} else if(device != null)
		{
			// implement later
		}
	}
}
