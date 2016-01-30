package org.fpsrobotics.actuators;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PWM;

public class BoschHallEffectMotor implements IMotor
{
	CANTalon motor;
	private int invert;
	private boolean invertDirection;
	private PWM hallEffect;
	
	public BoschHallEffectMotor(CANTalon motor, PWM hallEffect, boolean invertDirection)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
	}
	
	@Override
	public double getSpeed()
	{
		return motor.get();
	}

	@Override
	public void setSpeed(double speed)
	{
		invert = (invertDirection) ? -1 : 1;
		
		motor.set(speed*invert);
	}

	@Override
	public void stop()
	{
		motor.set(0.0);
	}
	
	public int getPosition()
	{
		return hallEffect.getRaw();
	}

}
