package org.fpsrobotics.actuators;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * This controls a single pneumatic solenoid, one that can either let air through a single tube or restrict it.
 *
 */
public class SingleSolenoid implements ISolenoid
{
	private Solenoid solenoid;
	
	public SingleSolenoid(int channel)
	{
		solenoid = new Solenoid(channel);
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
