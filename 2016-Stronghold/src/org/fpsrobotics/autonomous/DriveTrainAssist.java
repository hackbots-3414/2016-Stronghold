package org.fpsrobotics.autonomous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGyroscope;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.PIDOverride;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainAssist implements IDriveTrainAssist
{
	private IDriveTrain driveTrain;
	private IGyroscope gyro;
	private boolean driveBreaker = false;

	// For turnToAngle & moveShooterAndAugerToPreset
	private ExecutorService executor;
	private boolean isAugerAndShooterAtPreset = false;

	public DriveTrainAssist(IDriveTrain driveTrain, IGyroscope gyro)
	{
		this.driveTrain = driveTrain;
		this.gyro = gyro;

		executor = Executors.newFixedThreadPool(1);
	}

	@Override
	public void centerDriveTrain(double speed)
	{
		turnToAngle(0, speed);
	}

	@Override
	/**
	 * Left is Negative; Right is Positive
	 */
	public void turnNumberOfDegrees(double desiredDegrees, double speed)
	{
		if ((-180 <= desiredDegrees) && (desiredDegrees <= 180))
		{
			gyro.softResetCount();

			driveTrain.disablePID();

			if (gyro.getHardCount() > desiredDegrees) // If bigger (farther right) than we're supposed to be
			{
				driveTrain.turnLeft(speed);
				while ((gyro.getHardCount() > desiredDegrees) && RobotStatus.isRunning() && !driveBreaker)
					;
			} else if (gyro.getHardCount() < desiredDegrees) // If smaller (farther left) than we're supposed to be
			{
				driveTrain.turnRight(speed);
				while ((gyro.getHardCount() < desiredDegrees) && RobotStatus.isRunning() && !driveBreaker)
					;
			}

			driveTrain.stopDrive();

			if (!PIDOverride.getInstance().isTeleopDisablePID())
			{
				driveTrain.enablePID();
			}
		}
	}

	@Override
	/**
	 * Warning: If at -170 and want to go to 170, it will turn RIGHT the long way around
	 * WARNING: Overshoots by +/- 5 degrees (with coast on)
	 */
	public void turnToAngle(double desiredDegrees, double speed)
	{
		
		SmartDashboard.putNumber("Desired Position", desiredDegrees);

		if ((-180 <= desiredDegrees) && (desiredDegrees <= 180))
		{
			driveTrain.disablePID();

			if (gyro.getSoftCount() > desiredDegrees) // If bigger (farther right) than we're supposed to be
			{
				System.out.println("Turn to Angle - Left - Values going down");
				driveTrain.turnLeft(speed);
				if (RobotStatus.isAuto())
				{
					while ((gyro.getSoftCount() > desiredDegrees) && RobotStatus.isRunning() && RobotStatus.isAuto()
							&& !driveBreaker)
						;
				} else
				{
					while ((gyro.getSoftCount() > desiredDegrees) && RobotStatus.isRunning() && !driveBreaker)
						;
				}
			} else if (gyro.getSoftCount() < desiredDegrees) // If smaller (farther left) than we're supposed to be
			{
				System.out.println("Turn to Angle - Right - Values going up");
				driveTrain.turnRight(speed);
				if (RobotStatus.isAuto())
				{
					while ((gyro.getSoftCount() < desiredDegrees) && RobotStatus.isRunning() && RobotStatus.isAuto()
							&& !driveBreaker)
						;

				} else
				{
					while ((gyro.getSoftCount() < desiredDegrees) && RobotStatus.isRunning() && !driveBreaker)
						;
				}
			}

			driveTrain.stopDrive();
			driveBreaker = false;
			if (!PIDOverride.getInstance().isTeleopDisablePID())
			{
				driveTrain.enablePID();
			}

		}
	}

	@Override
	public void setDriveForwardBreak(boolean driveBreaker)
	{
		SmartDashboard.putBoolean("Drive Breaker", driveBreaker);
		this.driveBreaker = driveBreaker;
	}

	@Override
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
	@Override
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

	@Override
	public void driveTrainCoast(boolean coast)
	{
		ActuatorConfig.getInstance().getFrontLeftDriveMotor().enableBrakeMode(!coast);
		ActuatorConfig.getInstance().getFrontRightDriveMotor().enableBrakeMode(!coast);
		ActuatorConfig.getInstance().getBackLeftDriveMotor().enableBrakeMode(!coast);
		ActuatorConfig.getInstance().getBackRightDriveMotor().enableBrakeMode(!coast);
	}

	@Override
	public void turnToAngleAndMoveShooterAndAugerToPreset(double desiredDegrees, double speed,
			EShooterPresets desiredShooter, EAugerPresets desiredAuger)
	{
		isAugerAndShooterAtPreset = false;

		executor.submit(() ->
		{
			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(desiredShooter, desiredAuger);
			isAugerAndShooterAtPreset = true;
		});

		turnToAngle(desiredDegrees, speed);

		while (!isAugerAndShooterAtPreset)
			;

	}
	
	@Override
	public void turnToAngleAndMoveShooterAndAugerToPreset(double desiredDegrees, double speed,
			int desiredShooter, EAugerPresets desiredAuger)
	{
		isAugerAndShooterAtPreset = false;

		executor.submit(() ->
		{
			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(desiredShooter, desiredAuger);
			isAugerAndShooterAtPreset = true;
		});

		turnToAngle(desiredDegrees, speed);

		while (!isAugerAndShooterAtPreset)
			;

	}
	
	@Override
	public void turnToAngleAndMoveShooterAndAugerToPreset(double desiredDegrees, double speed,
			EShooterPresets desiredShooter, int desiredAuger)
	{
		isAugerAndShooterAtPreset = false;

		executor.submit(() ->
		{
			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(desiredShooter, desiredAuger);
			isAugerAndShooterAtPreset = true;
		});

		turnToAngle(desiredDegrees, speed);

		while (!isAugerAndShooterAtPreset)
			;

	}
	
	@Override
	public void turnToAngleAndMoveShooterAndAugerToPreset(double desiredDegrees, double speed,
			int desiredShooter, int desiredAuger)
	{
		isAugerAndShooterAtPreset = false;

		executor.submit(() ->
		{
			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(desiredShooter, desiredAuger);
			isAugerAndShooterAtPreset = true;
		});

		turnToAngle(desiredDegrees, speed);

		while (!isAugerAndShooterAtPreset)
			;

	}

}
