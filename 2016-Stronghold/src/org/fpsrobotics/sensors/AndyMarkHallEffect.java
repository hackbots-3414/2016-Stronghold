package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * 
 * A class that implements the andy mark hall effect sensor, a sensor that is triggered when a magnetic field is near.
 *
 */
public class AndyMarkHallEffect implements IHallEffectSensor
{
	private DigitalInput input;
	
	public AndyMarkHallEffect(int channel)
	{
		input = new DigitalInput(channel);
	}
	
	@Override
	public boolean isTriggered()
	{
		if(input.get())
		{
			return false;
		} else
		{
			return true;
		}
	}

}
