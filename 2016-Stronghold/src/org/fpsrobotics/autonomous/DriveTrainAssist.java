package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.sensors.IGyroscope;

public class DriveTrainAssist
{
	private IDriveTrain driveTrain;
	private IGyroscope gyro;
	
	public DriveTrainAssist(IDriveTrain driveTrain, IGyroscope gyro)
	{
		this.driveTrain = driveTrain;
		this.gyro = gyro;
	}
	
	public void centerDriveTrain(double speed)
	{
		driveTrain.disablePID();
		
		if(0 < gyro.getCount())
		{
			while(0 < gyro.getCount())
			{
				driveTrain.setSpeed(speed, -speed);
			}
		} else if(0 > gyro.getCount())
		{
			while(0 > gyro.getCount())
			{
				driveTrain.setSpeed(-speed, speed);
			}
		} 
		
		driveTrain.setSpeed(0, 0);
		driveTrain.enablePID();
	}
}
