package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class BuiltInAccelerometer implements IAccelerometer
{
	Accelerometer accel;
	
	public BuiltInAccelerometer()
	{
		
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
	public double getZ() {
		// TODO Auto-generated method stub
		return accel.getZ();
	}

	@Override
	public void reset() {
		// implement later
	}

}
