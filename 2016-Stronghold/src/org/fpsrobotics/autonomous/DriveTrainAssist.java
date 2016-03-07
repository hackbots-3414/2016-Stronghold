package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.sensors.IGyroscope;
import org.usfirst.frc.team3414.robot.RobotStatus;

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

		if (gyro.getCount() > 0)
		{
			while ((gyro.getCount() > 0) && RobotStatus.isRunning())
			{
				driveTrain.setSpeed(speed, -speed);
			}
		} else if (gyro.getCount() < 0)
		{
			while ((gyro.getCount() < 0) && RobotStatus.isRunning())
			{
				driveTrain.setSpeed(-speed, speed);
			}
		}

		driveTrain.stop();
	}
	
	//TODO
	public void turnToDegrees(double desiredDgrees, double speed)
	{
		/*
		driveTrain.disablePID();

		if (gyro.getCount() > desiredDgrees)
		{
			while ((gyro.getCount() > desiredDgrees) && RobotStatus.isRunning())
			{
				driveTrain.setSpeed(speed, -speed);
			}
		} else if (gyro.getCount() < desiredDgrees)
		{
			while ((gyro.getCount() < desiredDgrees) && RobotStatus.isRunning())
			{
				driveTrain.setSpeed(-speed, speed);
			}
		}
		 */
		driveTrain.stop();
	}
}
