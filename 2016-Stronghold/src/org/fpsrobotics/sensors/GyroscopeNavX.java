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

	public GyroscopeNavX(AHRS ahrs)
	{
		this.ahrs = ahrs;

		ahrs.reset();
	}

	@Override
	/**
	 * Returns values in between 0 and 0
	 */
	public double getCount()
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
	public void resetCount()
	{
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
