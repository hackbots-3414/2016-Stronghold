package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBarAndShootLow implements IAutonomousControl
{
	private int SHOOT_ANGLE = 46;

	private double DRIVE_SPEED = 0.80; // Used to be 0.5
	private int DRIVE_DISTANCE = 130_000; // Used to be 146_000
	//used to be 122,000

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

			// Go under low bar
			ActuatorConfig.getInstance().getDriveTrain().goForward(DRIVE_SPEED, DRIVE_DISTANCE);

			if (!RobotStatus.isAuto())
				break;

			// Angle drive train toward goal

			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.3);

			if (!RobotStatus.isAuto())
				break;
			
			if (RobotStatus.isAlpha())
			{
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(850); // alpha //TODO: Tune Autonomous mode for Alpha (DO BEFORE COMPETITION)
			} else
			{
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(300); // beta
			}
			
			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getLauncher().shootSequenceLowAuto();

			break;
		}

	}

}
