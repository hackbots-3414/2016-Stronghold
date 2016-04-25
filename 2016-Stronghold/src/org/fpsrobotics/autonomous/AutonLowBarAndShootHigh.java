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
			DRIVE_DISTANCE = 180; // 130_000 //165.438
		}
	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		while (RobotStatus.isAuto())
		{
			// NEW

			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(EShooterPresets.LOW_BAR, EAugerPresets.LOW_BAR);
			ActuatorConfig.getInstance().getDriveTrain().goForward(DRIVE_SPEED, DRIVE_DISTANCE);
			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);

			// lower numbers to raise shooter
			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.3);
			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(EShooterPresets.LOW_BAR_SHOOT_HIGH_AUTO, EAugerPresets.FOURTY_KAI_AUTO);
			// ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngleAndMoveShooterAndAugerToPreset(SHOOT_ANGLE,
			// 0.3, 765, EAugerPresets.FOURTY_KAI_AUTO);

			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
			SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);
			if (centerDrive)
			{
				ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(EShooterPresets.LOW_BAR, EAugerPresets.LOW_BAR);
				ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.3); // Was 0.4
			}
//			// OLD
//			// Move shooter and auger to low bar
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
//			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);
//
//			// Go under low bar
//			ActuatorConfig.getInstance().getDriveTrain().goForward(DRIVE_SPEED, DRIVE_DISTANCE);
//
//			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
//
//			// Angle drive train toward goal
//			ActuatorConfig.getInstance().getDriveTrain().turnRight(0.3, SHOOT_ANGLE);
//
//			// lower numbers to raise shooter
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(765);
//
//			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
//
//			// Raise Auger and Shoot
//			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
//
//			if (centerDrive)
//			{
//				SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);
//
//				ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
//				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);
//
//				ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.3); // Was 0.4
//
//				// 15 seconds
//
//				// ActuatorConfig.getInstance().getDriveTrain().goBackward(DRIVE_SPEED, 53, false); //Todo: TUNE Auton
//				// Low Bar and Shoot High
//				//
//				// ActuatorConfig.getInstance().getDriveTrainAssist().driveTrainCoast(true);
//				//
//				// ActuatorConfig.getInstance().getDriveTrain().stopDrive();
//				// SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
//				//
//				// ActuatorConfig.getInstance().getDriveTrain().goBackward((DRIVE_SPEED / 5), 50, false); // Todo: TUNE
//				// Auton Low Bar and Shoot High
//				//
//				// ActuatorConfig.getInstance().getDriveTrain().goBackward(DRIVE_SPEED, 70, false); //Todo: TUNE Auton
//				// Low Bar and Shoot High
//				//
//				// if (turnAndGrabBall)
//				// {
//				// ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(130, 0.2);
//				//
//				// ActuatorConfig.getInstance().getLauncher().intakeBoulder();
//				//
//				// ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 40);
//				//
//				// ActuatorConfig.getInstance().getLauncher().stopIntakeBoulder();
//				//
//				// ActuatorConfig.getInstance().getDriveTrain().stopDrive();
//				// SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
//				//
//				// ActuatorConfig.getInstance().getDriveTrain().goBackward(0.8, 40);
//				//
//				// ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.2);
//				//
//				// ActuatorConfig.getInstance().getDriveTrain().goForward(DRIVE_SPEED, DRIVE_DISTANCE);
//				// }
//			}
			break;
		}
	}
}
