package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBarAndShootHigh implements IAutonomousControl
{
	private int SHOOT_ANGLE = 46;

	private double DRIVE_SPEED = 1.00; // 0.80
	private double DRIVE_DISTANCE;

	private boolean centerDrive = false;
	// private boolean turnAndGrabBall = false;
	
	// Todo: Measure the field before Competition

	public AutonLowBarAndShootHigh()
	{
		if (RobotStatus.isAlpha())
		{
			DRIVE_DISTANCE = 178.165; // 140_000 // 178.165
		} else
		{
			DRIVE_DISTANCE = 39; // 130_000 //165.438
		}
	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		while (RobotStatus.isAuto())
		{
			// Move shooter and auger to low bar
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

			// Go under low bar
			ActuatorConfig.getInstance().getDriveTrain().goForward(DRIVE_SPEED, DRIVE_DISTANCE);

			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);

			// Angle drive train toward goal
			ActuatorConfig.getInstance().getDriveTrain().turnRight(0.3, SHOOT_ANGLE);

			//Move shooter
			if (RobotStatus.isAlpha())
			{
				// lower numbers to raise shooter
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(765);
			} else
			{
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(300);
			}

			//Raise Auger and Shoot
			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();

			if (centerDrive)
			{

				SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);

				ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

				ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.4);

				// 15 seconds

				// ActuatorConfig.getInstance().getDriveTrain().goBackward(DRIVE_SPEED, 53, false); //Todo: TUNE Auton
				// Low Bar and Shoot High
				//
				// ActuatorConfig.getInstance().getDriveTrainAssist().driveTrainCoast(true);
				//
				// ActuatorConfig.getInstance().getDriveTrain().stopDrive();
				// SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
				//
				// ActuatorConfig.getInstance().getDriveTrain().goBackward((DRIVE_SPEED / 5), 50, false); // Todo: TUNE
				// Auton Low Bar and Shoot High
				//
				// ActuatorConfig.getInstance().getDriveTrain().goBackward(DRIVE_SPEED, 70, false); //Todo: TUNE Auton
				// Low Bar and Shoot High
				//
				// if (turnAndGrabBall)
				// {
				// ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(130, 0.2);
				//
				// ActuatorConfig.getInstance().getLauncher().intakeBoulder();
				//
				// ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 40);
				//
				// ActuatorConfig.getInstance().getLauncher().stopIntakeBoulder();
				//
				// ActuatorConfig.getInstance().getDriveTrain().stopDrive();
				// SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
				//
				// ActuatorConfig.getInstance().getDriveTrain().goBackward(0.8, 40);
				//
				// ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.2);
				//
				// ActuatorConfig.getInstance().getDriveTrain().goForward(DRIVE_SPEED, DRIVE_DISTANCE);
				// }
			}
			break;
		}
	}
}
