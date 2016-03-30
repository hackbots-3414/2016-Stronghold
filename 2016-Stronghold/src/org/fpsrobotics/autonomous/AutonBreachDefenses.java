package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonBreachDefenses implements IAutonomousControl
{
	private boolean timeBased = true;
	private int DRIVE_TIME = 4000;
	private int DRIVE_DISTANCE = 90_000;
	private double DRIVE_SPEED = 0.8;

	public AutonBreachDefenses()
	{

	}

	@Override
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{

			// Move shooter to rock wall
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT_LOW);

			if (!RobotStatus.isAuto())
				break;

			// Go over rock wall
			if (timeBased)
			{
				//Time Based
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-DRIVE_SPEED);

				if (!RobotStatus.isAuto())
					break;

				SensorConfig.getInstance().getTimer().waitTimeInMillis(DRIVE_TIME);

			} else
			{
				//Encoder based
				ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, DRIVE_DISTANCE);
			}

			ActuatorConfig.getInstance().getDriveTrain().stop();

			break;
		}

	}
}
