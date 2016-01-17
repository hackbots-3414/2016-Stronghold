package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.Joystick;

public class DualShockTwoController implements IGamepad
{
	Joystick joy;
	
	public DualShockTwoController(int channel)
	{
		joy = new Joystick(channel);
	}
	
	@Override
	public double getAnalogStickValue(AnalogStick stick) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getButtonValue(ButtonGamepad button) 
	{
		// TODO Auto-generated method stub
		return false;
	}

}
