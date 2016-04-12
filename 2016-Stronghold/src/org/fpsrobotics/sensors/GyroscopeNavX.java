package org.fpsrobotics.sensors;

import com.kauailabs.navx.frc.AHRS;

/**
 * Implements the gyroscope included on the NavX MXP board.
 *
 */
public class GyroscopeNavX implements IGyroscope
{
	private AHRS ahrs;
	private boolean isEnabled = true;
	
	private double pastYaw = 0;
	private double currentYaw = 0;

	public GyroscopeNavX(AHRS ahrs)
	{
		this.ahrs = ahrs;

		ahrs.reset();
	}

	@Override
	/**
	 * Returns values in between -180 and 180
	 */
	public double getHardCount()
	{
		if (isEnabled)
		{
			return ahrs.getYaw();
		} else
		{
			return 0;
		}
	}
	
	@Override
	public double getSoftCount()
	{
		currentYaw = pastYaw + ahrs.getYaw();
		if (currentYaw > 180)
		{
			currentYaw -= 360;
		}
		if (currentYaw < -180)
		{
			currentYaw += 360;
		}
		return currentYaw;
	}

	@Override
	public void enable()
	{
		isEnabled = true;
	}

	@Override
	public void disable()
	{
		isEnabled = false;
	}

	@Override
	public void softResetCount()
	{
		pastYaw += ahrs.getYaw();
		ahrs.reset();
	}
	
	@Override
	public void hardResetCount()
	{
		pastYaw = ahrs.getYaw();
		ahrs.reset();
	}

	@Override
	public double getPitch()
	{
		return ahrs.getRoll();
	}

	@Override
	public double getRate()
	{
		return ahrs.getRate();
	}
	
	public double getPitchRate()
	{
		return ahrs.getRawGyroY();
	}
}
