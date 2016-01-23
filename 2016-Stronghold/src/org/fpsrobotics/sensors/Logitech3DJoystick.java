package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.Joystick;

public class Logitech3DJoystick implements IJoystick
{
	Joystick joy;
	
	public Logitech3DJoystick(int channel)
	{
		joy = new Joystick(channel);
	}

	@Override
	public double getX() 
	{
		return joy.getRawAxis(0);
	}

	@Override
	public double getY() 
	{
		return joy.getRawAxis(1);
	}

	@Override
	public double getTwist() 
	{
		return joy.getTwist();
	}

	@Override
	public boolean getButtonValue(ButtonJoystick value) 
	{
		switch (value)
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
		}
		
		return false;
	}

}
