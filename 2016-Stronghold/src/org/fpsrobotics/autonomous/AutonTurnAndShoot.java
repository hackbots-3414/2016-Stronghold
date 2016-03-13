package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonTurnAndShoot implements IAutonomousControl
{

	public AutonTurnAndShoot()
	{
	}

	@Override
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{

			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(1400);

			if (!RobotStatus.isAuto())
				break;

			SensorConfig.getInstance().getTimer().waitTimeInMillis(10);

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 100000);

			if (!RobotStatus.isAuto())
				break;

			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 500_000);

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);

			if (!RobotStatus.isAuto())
				break;

			//ActuatorConfig.getInstance().getDriveTrainAssist().goToAngle(0.5, 90);

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(400);

			if (!RobotStatus.isAuto())
				break;

			SensorConfig.getInstance().getTimer().waitTimeInMillis(10);

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();

			break;
		}
	}

}
