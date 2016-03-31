package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.sensors.IGyroscope;
import org.fpsrobotics.sensors.SensorConfig;
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
				driveTrain.setSpeed(speed, -speed);
				while ((gyro.getCount() > desiredDegrees) && RobotStatus.isRunning())
					;
			} else if (gyro.getCount() < desiredDegrees)
			{

				driveTrain.setSpeed(-speed, speed);
				while ((gyro.getCount() < desiredDegrees) && RobotStatus.isRunning())
					;
			}

			driveTrain.stop();
		}
	}

	public boolean isTilt()
	{
		SmartDashboard.putNumber("Y Rate", SensorConfig.getInstance().getAccelerometer().getY());

		if (SensorConfig.getInstance().getAccelerometer().getY() > 6)
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * Doesn't work too well
	 * 
	 * @return isTiltGyro
	 */
	public boolean isTiltGyro()
	{
		if (SensorConfig.getInstance().getGyro().getPitchRate() > 20) // From 25
		{
			return true;
		} else
		{
			return false;
		}
	}

}
