package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBarAndShoot implements IAutonomousControl
{
	private int SHOOT_ANGLE = 50;
	private boolean timeBased = false;
	private boolean shootHigh = false;
	
	private double DRIVE_SPEED = 0.5;
	private int DRIVE_TIME = 5000;
	private int DRIVE_DISTANCE = 146_000;

	@Override
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{
			// Move shooter to low bar
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);

			if (!RobotStatus.isAuto())
				break;
			
			if (timeBased)
			{
				//TIME BASED
				
				//Go under low bar
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-DRIVE_SPEED);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(DRIVE_TIME);

				ActuatorConfig.getInstance().getDriveTrain().stop();

				if (!RobotStatus.isAuto())
					break;

				// Straighten drive train
//				ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.2);

				// Angle drive train toward goal
				if (!RobotStatus.isAuto())
					break;

				ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.3);
				
			} else
			{
				// ENCODER BASED
				
				// Go under low bar
				ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, DRIVE_DISTANCE);

				if (!RobotStatus.isAuto())
					break;

				// Straighten drive train
//				ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.2);

				// Angle drive train toward goal
				if (!RobotStatus.isAuto())
					break;

				ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.3);
			}
			
			if (!RobotStatus.isAuto())
				break;
			
			// Shooter to Shoot Position
			if (shootHigh)
			{
				//Shoot High
				ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT_HIGH);

				if (!RobotStatus.isAuto())
					break;
				
				ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();
			} else
			{
				//Shoot Low
				ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT_LOW);

				if (!RobotStatus.isAuto())
					break;
				
				ActuatorConfig.getInstance().getLauncher().shootSequenceLow();
			}


			break;
		}

	}

}
