package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.sensors.IGyroscope;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	
	public boolean shouldShooterBeRaised()
	{
		SmartDashboard.putNumber("pitch", gyro.getPitch());
		
		if(gyro.getPitch() > 2)
		{
			return true;
		} else
		{
			return false;
		}
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
