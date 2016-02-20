package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * 
 * A class that implements the 8 position switch that is used to determine the autonomous mode at the beginning of a match.
 *
 */
public class AutonomousSwitches implements ICounterSwitch
{
	private DigitalLimitSwitch ones;
	private DigitalLimitSwitch twos;
	private DigitalLimitSwitch fours;
	
	public AutonomousSwitches(int channelOnes, int channelTwos, int channelFours, boolean invertSwitches)
	{
		ones = new DigitalLimitSwitch(channelOnes, invertSwitches);
		twos = new DigitalLimitSwitch(channelTwos, invertSwitches);
		fours = new DigitalLimitSwitch(channelFours, invertSwitches);
	}

	@Override
	public int getValue() 
	{
		System.out.println(ones.getValue() + " " + twos.getValue() + " " + fours.getValue());
		
		
		
		if(ones.getValue() == false && twos.getValue() == false && fours.getValue() == false)
		{
			return 0;
		}
		
		if(ones.getValue() == true && twos.getValue() == false && fours.getValue() == false)
		{
			return 1;
		}
		
		if(ones.getValue() == false && twos.getValue() == true && fours.getValue() == false)
		{
			return 2;
		}
		
		if(ones.getValue() == true && twos.getValue() == true && fours.getValue() == false)
		{
			return 3;
		}
		
		if(ones.getValue() == false && twos.getValue() == false && fours.getValue() == true)
		{
			return 4;
		}
		
		if(ones.getValue() == true && twos.getValue() == false && fours.getValue() == true)
		{
			return 5;
		}
		
		if(ones.getValue() == false && twos.getValue() == true && fours.getValue() == true)
		{
			return 6;
		}
		
		if(ones.getValue() == true && twos.getValue() == true && fours.getValue() == true)
		{
			return 7;
		}
		
		return 0;
	}
}
