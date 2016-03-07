package org.fpsrobotics.actuators;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PWM;

/**
 * Controls the Bosch motor donated to us. The motor uses hall effect sensors to
 * determine the position of the shaft. This class is incomplete and unused
 * because the motors sent to us have defective hall effect sensors and
 * therefore just use the normal CANMotor or Motor class.
 *
 * NOT USED FOR 2016 SEASON
 */
public class BoschHallEffectMotor implements IMotor
{
	private CANTalon motor;
	private int invert;
	private boolean invertDirection;
	private PWM hallEffect;

	public BoschHallEffectMotor(CANTalon motor, PWM hallEffect, boolean invertDirection)
	{
		this.motor = motor;
		this.motor.setInverted(invertDirection);
	}

	@Override
	/**
	 * Returns current applied throttle.
	 */
	public double getSpeed()
	{
		return motor.get();
	}

	@Override
	public void setSpeed(double speed)
	{
		motor.set(speed);
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
