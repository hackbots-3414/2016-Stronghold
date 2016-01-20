package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

public class AndyMarkHallEffect implements IHallEffectSensor
{
	DigitalInput input;
	
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
