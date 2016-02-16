package org.fpsrobotics.sensors;

import com.kauailabs.navx.frc.AHRS;

public class AccelerometerNavX implements IAccelerometer
{
	AHRS ahrs;
	
	public AccelerometerNavX(AHRS ahrs)
	{
		this.ahrs = ahrs;
	}

	@Override
	public double getX() {
		return ahrs.getWorldLinearAccelX()*9.8;
	}

	@Override
	public double getY() {
		return ahrs.getWorldLinearAccelY()*9.8;
	}

	@Override
	public double getZ() {
		return ahrs.getWorldLinearAccelZ()*9.8;
	}

	@Override
	public void reset() {
		ahrs.resetDisplacement();
	}

}
