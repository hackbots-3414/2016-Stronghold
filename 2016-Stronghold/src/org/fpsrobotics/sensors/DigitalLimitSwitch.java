package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * A class that implements a simple digital two position limit switch.
 */
public class DigitalLimitSwitch implements ILimitSwitch
{
	private DigitalInput limitSwitch;
	private boolean reversed;

	public DigitalLimitSwitch(int channel, boolean reversed)
	{
		limitSwitch = new DigitalInput(channel);
		this.reversed = reversed;
	}

	public DigitalLimitSwitch(int channel)
	{
		limitSwitch = new DigitalInput(channel);
	}

	@Override
	public boolean isHit()
	{
		if (!reversed)
		{
			return limitSwitch.get();
		} else
		{
			return !limitSwitch.get();
		}
	}

}
