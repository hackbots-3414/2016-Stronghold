package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

/**
 * UNTESTED
 */
public class AutonChevelDeFriz implements IAutonomousControl
{
	private boolean timeBased = false;
	private double DRIVE_SPEED = 0.5;
	private int SHOOT_ANGLE = 90;

	public AutonChevelDeFriz()
	{

	}

	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{

			// Move shooter to normal defense
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT_HIGH);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);

			if (!RobotStatus.isAuto())
				break;

			if (timeBased)
			{
				// TIME BASED

				// Drive to Cheval
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(DRIVE_SPEED);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(5000);

				ActuatorConfig.getInstance().getDriveTrain().stop();

				if (!RobotStatus.isAuto())
					break;

				// Move Auger to Bottom Limit
				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);

				if (!RobotStatus.isAuto())
					break;

				// Go over half of Cheval
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(DRIVE_SPEED);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(1500);

				ActuatorConfig.getInstance().getDriveTrain().stop();

				if (!RobotStatus.isAuto())
					break;

				// Move Auger Up
				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);

				if (!RobotStatus.isAuto())
					break;

				// Keep Driving
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(DRIVE_SPEED);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(1500);

				ActuatorConfig.getInstance().getDriveTrain().stop();

				if (!RobotStatus.isAuto())
					break;
				
			} else
			{
				// ENCODER BASED

				// Drive to Cheval
				ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, 1000);

				if (!RobotStatus.isAuto())
					break;
				
				// Move Auger to Bottom Limit
				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);
				
				if (!RobotStatus.isAuto())
					break;

				// Go over half of chevel
				ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, 4000);
				
				if (!RobotStatus.isAuto())
					break;

				// Move Auger up
				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
				
				if (!RobotStatus.isAuto())
					break;

				// Keep Driving
				ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, 4000);
			}

			ActuatorConfig.getInstance().getDriveTrain().stop();

			// if (!RobotStatus.isAuto())
			// break;
			//
			// // Straighten drive train
			// ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);
			//
			// // Angle drive train toward goal
			// if (!RobotStatus.isAuto())
			// break;
			//
			// ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE,
			// 0.1);
			//
			// if (!RobotStatus.isAuto())
			// break;
			//
			// // Shooter to Shoot Position
			// ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT_LOW);
			//
			// if (!RobotStatus.isAuto())
			// break;
			//
			// // Shoot Low
			// ActuatorConfig.getInstance().getLauncher().shootSequenceLow();

			break;
		}
	}
}
