package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonRockWall implements IAutonomousControl
{

	@Override
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{

			// Move shooter to rock wall
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);

			if (!RobotStatus.isAuto())
				break;

			// Go over rock wall
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 80000);

			if (!RobotStatus.isAuto())
				break;

			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);

			if (!RobotStatus.isAuto())
				break;

			// Straighten drive train
			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);

			// Angle drive train toward goal
			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(90, 0.1);

			if (!RobotStatus.isAuto())
				break;

			// Shooter to Shoot Position
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT);

			if (!RobotStatus.isAuto())
				break;

			// Shoot high
			ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();

			break;
		}

	}
}
