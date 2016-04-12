package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBarAndShootHigh implements IAutonomousControl
{
	private int SHOOT_ANGLE = 46;

	private double DRIVE_SPEED = 0.80;
	private double DRIVE_DISTANCE;

	private boolean centerDriveAndGoBack = true;

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
			// Move shooter to low bar
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

			if (!RobotStatus.isAuto())
				break;

			// Go under low bar
			ActuatorConfig.getInstance().getDriveTrain().goForward(DRIVE_SPEED, DRIVE_DISTANCE);

			if (!RobotStatus.isAuto())
				break;

			// Angle drive train toward goal

			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.2);

			if (!RobotStatus.isAuto())
				break;

			if (RobotStatus.isAlpha())
			{
				// lower numbers to raise shooter
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(765);
			} else
			{
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(300);
			}

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();

			if (!RobotStatus.isAuto())
				break;

			if (centerDriveAndGoBack)
			{

				 ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

				if (!RobotStatus.isAuto())
					break;

				ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.2);

				if (!RobotStatus.isAuto())
					break;

				ActuatorConfig.getInstance().getDriveTrain().goBackward(DRIVE_SPEED, 20, false); //TODO: TUNE THIS DISTANCE

				ActuatorConfig.getInstance().getFrontLeftDriveMotor().enableBrakeMode(false);
				ActuatorConfig.getInstance().getFrontRightDriveMotor().enableBrakeMode(false);
				ActuatorConfig.getInstance().getBackLeftDriveMotor().enableBrakeMode(false);
				ActuatorConfig.getInstance().getBackRightDriveMotor().enableBrakeMode(false);

				ActuatorConfig.getInstance().getDriveTrain().stopDrive();

				SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

				ActuatorConfig.getInstance().getDriveTrain().goBackward((DRIVE_SPEED / 5), 20, false);
				// TODO: TUNE THIS DISTANCE
				
				ActuatorConfig.getInstance().getDriveTrain().goBackward(DRIVE_SPEED, 20, true); //TODO: TUNE THIS DISTANCE

			}
			break;
		}
	}
}
