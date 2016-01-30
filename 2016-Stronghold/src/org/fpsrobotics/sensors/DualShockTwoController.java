package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.Joystick;

public class DualShockTwoController implements IGamepad
{
	private Joystick joy;

	public DualShockTwoController(int channel)
	{
		joy = new Joystick(channel);
	}

	// horizontal left 1
	// vertical left 2

	// horizontal right 3
	// vertical right 4

	@Override
	public double getAnalogStickValue(AnalogStick stick, GamepadDirection direction)
	{
		if (stick == AnalogStick.LEFT)
		{
			if (direction == GamepadDirection.HORIZONTAL)
			{
				return joy.getRawAxis(0);
			} else
			{
				return joy.getRawAxis(1);
			}
		} else
		{
			if (direction == GamepadDirection.HORIZONTAL)
			{
				return joy.getRawAxis(2);
			} else
			{
				return -joy.getRawAxis(3);
			}
		}
	}

	@Override
	public boolean getButtonValue(ButtonGamepad button)
	{
		switch (button)
		{
		case ONE:
			return joy.getRawButton(1);
		case TWO:
			return joy.getRawButton(2);
		case THREE:
			return joy.getRawButton(3);
		case FOUR:
			return joy.getRawButton(4);
		case FIVE:
			return joy.getRawButton(5);
		case SIX:
			return joy.getRawButton(6);
		case SEVEN:
			return joy.getRawButton(7);
		case EIGHT:
			return joy.getRawButton(8);
		case NINE:
			return joy.getRawButton(9);
		case TEN:
			return joy.getRawButton(10);
		case ELEVEN:
			return joy.getRawButton(11);
		case TWELVE:
			return joy.getRawButton(12);
		default:
			return false;
		}

	}

}
