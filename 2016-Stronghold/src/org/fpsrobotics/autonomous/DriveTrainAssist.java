package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGyroscope;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainAssist implements IDriveTrainAssist
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

			if (gyro.getHardCount() > desiredDegrees)
			{
				driveTrain.setSpeed(speed, -speed);
				while ((gyro.getHardCount() > desiredDegrees) && RobotStatus.isRunning())
					;
			} else if (gyro.getHardCount() < desiredDegrees)
			{

				driveTrain.setSpeed(-speed, speed);
				while ((gyro.getHardCount() < desiredDegrees) && RobotStatus.isRunning())
					;
			}

			driveTrain.stopDrive();
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
	
	public void doChevalAutoActivate()
	{
		ActuatorConfig.getInstance().getDriveTrain().goBackward(0.25, 6);
		
		ActuatorConfig.getInstance().getDriveTrain().stopDrive();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
		
		ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
		ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

		ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 90);
		
	}
	
	public void driveTrainCoast(boolean coast)
	{
		ActuatorConfig.getInstance().getFrontLeftDriveMotor().enableBrakeMode(!coast);
		ActuatorConfig.getInstance().getFrontRightDriveMotor().enableBrakeMode(!coast);
		ActuatorConfig.getInstance().getBackLeftDriveMotor().enableBrakeMode(!coast);
		ActuatorConfig.getInstance().getBackRightDriveMotor().enableBrakeMode(!coast);
	}

}
