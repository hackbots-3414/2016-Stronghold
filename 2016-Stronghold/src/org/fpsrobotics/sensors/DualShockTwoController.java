package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A class that interfaces with the Logitech dual shock two controller.
 *
 */
public class DualShockTwoController implements IGamepad
{
	private Joystick joy;
	private static final double TOLERANCE = 0.2;

	public DualShockTwoController(Joystick joy)
	{
		this.joy = joy;
	}

	@Override
	/**
	 * left horizontal 1; left vertical 2; right horizontal 3; right vertical 4
	 */
	public double getAnalogStickValue(EAnalogStickAxis axis)
	{
		// switch (axis)
		// {
		// case LEFT_HORIZONTAL:
		// if (Math.abs(joy.getRawAxis(1)) < TOLERANCE)
		// {
		// return 0.0;
		// } else
		// {
		// return joy.getRawAxis(1);
		// }
		// case LEFT_VERTICAL:
		// if (Math.abs(joy.getRawAxis(2)) < TOLERANCE)
		// {
		// return 0.0;
		// } else
		// {
		// return joy.getRawAxis(2);
		// }
		// case RIGHT_HORIZONTAL:
		// if (Math.abs(joy.getRawAxis(3)) < TOLERANCE)
		// {
		// return 0.0;
		// } else
		// {
		// return joy.getRawAxis(3);
		// }
		// case RIGHT_VERTICAL:
		// if (Math.abs(joy.getRawAxis(4)) < TOLERANCE)
		// {
		// return 0.0;
		// } else
		// {
		// return joy.getRawAxis(4);
		// }
		//
		// default:
		// return 0.0;
		// }

		switch (axis)
		{
		case LEFT_HORIZONTAL:
			return joy.getRawAxis(1);

		case LEFT_VERTICAL:
			return joy.getRawAxis(2);

		case RIGHT_HORIZONTAL:

			return joy.getRawAxis(3);

		case RIGHT_VERTICAL:

			return joy.getRawAxis(4);

		default:
			return 0.0;
		}
	}

	@Override
	public boolean getButtonValue(EJoystickButtons button)
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
