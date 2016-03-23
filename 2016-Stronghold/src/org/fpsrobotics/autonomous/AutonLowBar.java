package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBar implements IAutonomousControl
{
	private int SHOOT_ANGLE = 50;

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
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 146_000);

			if (!RobotStatus.isAuto())
				break;

			// Straighten drive train
//			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.2);

			// Angle drive train toward goal
			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.3);

			if (!RobotStatus.isAuto())
				break;

			// Shooter to Shoot Position
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT);
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.AUTO_LOW);

			if (!RobotStatus.isAuto())
				break;

			// Shoot high
//			ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();
			ActuatorConfig.getInstance().getLauncher().shootSequenceLow();

			break;
		}

	}

}
