package org.fpsrobotics.sensors;

import com.kauailabs.navx.frc.AHRS;

/**
 * Implements the gyroscope included on the NavX MXP board.
 *
 */
public class GyroscopeNavX implements IGyroscope
{
	AHRS ahrs;
	
	boolean enabled = true;
	
	public GyroscopeNavX(AHRS ahrs)
	{
		this.ahrs = ahrs;
	}

	@Override
	public double getCount()
	{
		if(enabled)
		{
			return ahrs.getYaw();
		} else
		{
			return 0;
		}
	}

	@Override
	public void enable()
	{
		enabled = true;
	}

	@Override
	public void disable()
	{
		enabled= false;
	}

	@Override
	public void resetCount()
	{
		ahrs.reset();
	}
}
