package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBarAndShootHigh implements IAutonomousControl
{
	private int SHOOT_ANGLE = 46;

	private double DRIVE_SPEED = 0.80; // Used to be 0.5
	private int DRIVE_DISTANCE = 145_000; //130_000 alpha

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
			ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, DRIVE_DISTANCE);

			if (!RobotStatus.isAuto())
				break;

			// Angle drive train toward goal

			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.3);
			
			if (!RobotStatus.isAuto())
				break;
			
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(850); //alpha
			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(300); //beta

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
			
			break;
		}
	}
}
