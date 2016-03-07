package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.AnalogGyro;

/**
 * Implements a simple analog gyroscope.
 */
public class Gyroscope implements IGyroscope
{
	private AnalogGyro gyro;
	private boolean isEnabled;

	public Gyroscope(AnalogGyro gyro)
	{
		this.gyro = gyro;

		gyro.initGyro();

		isEnabled = true;
	}

	@Override
	/**
	 * Returns values in between 0 and 0
	 */
	public double getCount()
	{
		if (isEnabled)
		{
			return gyro.getAngle();
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
		gyro.reset();
	}

}
