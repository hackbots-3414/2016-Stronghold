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
		turnToAngle(0, speed);

	}

	public void turnToAngle(int desiredPosition, double speed)
	{
		if ((-180 <= desiredPosition) && (desiredPosition <= 180))
		{
			driveTrain.disablePID();

			if (gyro.getCount() > desiredPosition)
			{
				while (gyro.getCount() > desiredPosition)
				{

					driveTrain.setSpeed(speed, -speed);
				}
			} else if (gyro.getCount() < desiredPosition)
			{
				while (gyro.getCount() < desiredPosition)
				{
					driveTrain.setSpeed(-speed, speed);
				}
			}

			driveTrain.setSpeed(0, 0);
		}
	}
	
	public boolean shouldShooterBeRaised()
	{
		System.out.println("Attitude " + gyro.getAttitude());
		
		if(gyro.getAttitude() <= 0.2)
		{
			return true;
		} else
		{
			return false;
		}
	}
}
