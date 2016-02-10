package org.fpsrobotics.actuators;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Used to control a double solenoid connected to the pneumatics breakout board in the 2016 season.
 *
 */
public class DoubleSolenoid implements ISolenoid
{
//	private int channelOne;
//	private int channelTwo;
	private edu.wpi.first.wpilibj.DoubleSolenoid solenoid;
	
	public DoubleSolenoid(int channelOne, int channelTwo)
	{
		solenoid = new edu.wpi.first.wpilibj.DoubleSolenoid(channelOne, channelTwo);
//		this.channelOne = channelOne;
//		this.channelTwo = channelTwo;
	}
	
	@Override
	public void turnOn() 
	{
		solenoid.set(Value.kForward);
	}

	@Override
	public void turnOff() 
	{
		solenoid.set(Value.kReverse);
	}

	@Override
	public void set(SolenoidValues value) 
	{
		switch (value)
		{
		case ON:
			solenoid.set(Value.kForward);
			break;
		case OFF:
			solenoid.set(Value.kReverse);
			break;
		case NEUTRALSTATE:
			solenoid.set(Value.kOff);
			break;
		}
	}

}
