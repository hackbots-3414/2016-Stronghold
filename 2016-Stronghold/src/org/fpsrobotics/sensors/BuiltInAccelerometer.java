package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class BuiltInAccelerometer implements IAccelerometer
{
	private Accelerometer accel;

	public BuiltInAccelerometer(Accelerometer accel)
	{
		this.accel = accel;
	}

	@Override
	public double getX()
	{
		return accel.getX();
	}

	@Override
	public double getY()
	{
		return accel.getY();
	}

	@Override
	public double getZ()
	{
		return accel.getZ();
	}

	@Override
	public void reset()
	{
		// This does nothing
	}
}
