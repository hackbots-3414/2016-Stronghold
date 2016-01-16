package org.fpsrobotics.actuators;

import edu.wpi.first.wpilibj.Solenoid;

public class SingleSolenoid implements ISolenoid
{
//	int channel;
	private Solenoid solenoid;
	
	public SingleSolenoid(int channel)
	{
		solenoid = new Solenoid(channel);
//		this.channel = channel;
	}

	@Override
	public void turnOn() 
	{
		solenoid.set(true);
	}

	@Override
	public void turnOff() 
	{
		solenoid.set(false);
	}

	@Override
	public void set(SolenoidValues value) 
	{
		switch (value)
		{
		case ON:
			solenoid.set(true);
			break;
		default:
			solenoid.set(false);
			
		}
	}

}
