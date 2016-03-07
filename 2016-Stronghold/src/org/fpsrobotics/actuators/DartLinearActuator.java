package org.fpsrobotics.actuators;

import org.fpsrobotics.sensors.IHallEffectSensor;

/**
 * Controls the Dart Linear Actuator available in the 2016 season. This class is
 * used when a Dart Linear Actuator has hall effect sensors attached to it in
 * order to monitor it's upper and lower limits.
 * 
 * NOT USED FOR 2016 SEASON
 */
public class DartLinearActuator implements ILinearActuator
{
	private IMotor screwMotor;
	private IHallEffectSensor topLimitSensor;
	private IHallEffectSensor bottomLimitSensor;

	private final double LINEAR_ACTUATOR_SPEED = 0.8;

	public DartLinearActuator(IMotor screwMotor, IHallEffectSensor topLimitSensor, IHallEffectSensor bottomLimitSensor)
	{
		this.screwMotor = screwMotor;
		this.topLimitSensor = topLimitSensor;
		this.bottomLimitSensor = bottomLimitSensor;
	}

	@Override
	public void goToTopLimit()
	{
		screwMotor.setSpeed(LINEAR_ACTUATOR_SPEED);

		while (!topLimitSensor.isTriggered())
		{

		}

		screwMotor.stop();
	}

	@Override
	public void goToBottomLimit()
	{
		screwMotor.setSpeed(-LINEAR_ACTUATOR_SPEED);

		while (!bottomLimitSensor.isTriggered())
		{

		}

		screwMotor.stop();
	}

	@Override
	public void setSpeed(double speed)
	{
		if (speed > 0)
		{
			if (topLimitSensor.isTriggered())
			{
				screwMotor.stop();
				return;
			} else
			{
				screwMotor.setSpeed(speed);
			}
		} else if (speed < 0)
		{
			if (bottomLimitSensor.isTriggered())
			{
				screwMotor.stop();
				return;
			} else
			{
				screwMotor.setSpeed(speed);
			}
		} else
		{
			screwMotor.stop();
		}
	}

	@Override
	public void stop()
	{
		setSpeed(0.0);
	}

}
