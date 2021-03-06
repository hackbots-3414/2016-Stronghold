package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

public class AutonomousSwitches implements ICounterSwitch
{
	DigitalInput ones;
	DigitalInput twos;
	DigitalInput fours;
	
	public AutonomousSwitches(int channelOnes, int channelTwos, int channelFours)
	{
		ones = new DigitalInput(channelOnes);
		twos = new DigitalInput(channelTwos);
		fours = new DigitalInput(channelFours);
	}

	@Override
	public int getValue() 
	{
		if(ones.get() == false && twos.get() == false && fours.get() == false)
		{
			return 0;
		}
		
		if(ones.get() == true && twos.get() == false && fours.get() == false)
		{
			return 1;
		}
		
		if(ones.get() == false && twos.get() == true && fours.get() == false)
		{
			return 2;
		}
		
		if(ones.get() == true && twos.get() == true && fours.get() == false)
		{
			return 3;
		}
		
		if(ones.get() == false && twos.get() == false && fours.get() == true)
		{
			return 4;
		}
		
		if(ones.get() == true && twos.get() == false && fours.get() == true)
		{
			return 5;
		}
		
		if(ones.get() == false && twos.get() == true && fours.get() == true)
		{
			return 6;
		}
		
		if(ones.get() == true && twos.get() == true && fours.get() == true)
		{
			return 7;
		}
		
		return 0;
	}
}
