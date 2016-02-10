package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * 
 * A class that implements a simple digital two position limit switch.
 *
 */
public class DigitalLimitSwitch implements ILimitSwitch
{
	private DigitalInput limitSwitch;
	
	public DigitalLimitSwitch(int channel)
	{
		limitSwitch = new DigitalInput(channel);
	}

	@Override
	public boolean getValue() 
	{
		return limitSwitch.get();
	}

}
