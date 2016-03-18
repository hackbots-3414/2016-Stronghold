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

	public boolean shouldShooterBeRaised()
	{
		SmartDashboard.putNumber("Pitch", gyro.getPitch());
		SmartDashboard.putNumber("Yaw", gyro.getRate());

		if (gyro.getRate() < 10)
		{
			if (gyro.getPitch() > 2)
			{
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}

	public void turnToAngle(double desiredDegrees, double speed)
	{
		if ((-180 <= desiredDegrees) && (desiredDegrees <= 180))
		{
			driveTrain.disablePID();

			if (gyro.getCount() > desiredDegrees)
			{
				while (gyro.getCount() > desiredDegrees)
				{

					driveTrain.setSpeed(speed, -speed);
				}
			} else if (gyro.getCount() < desiredDegrees)
			{
				while (gyro.getCount() < desiredDegrees)
				{
					driveTrain.setSpeed(-speed, speed);
				}
			}

			driveTrain.stop();
		}
	}
}
