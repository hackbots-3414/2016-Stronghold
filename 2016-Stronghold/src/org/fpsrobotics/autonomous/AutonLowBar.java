package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBar implements IAutonomousControl
{
	private boolean timeBased = false;
	private double DRIVE_SPEED = 0.5;
	private int DRIVE_TIME = 5000;
	private int DRIVE_DISTANCE = 130_000;//used to be 90,000

	public AutonLowBar()
	{

	}

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
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-DRIVE_SPEED);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(DRIVE_TIME);

				ActuatorConfig.getInstance().getDriveTrain().stop();
			} else
			{
				// Go under low bar
				ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, DRIVE_DISTANCE);
			}

			ActuatorConfig.getInstance().getDriveTrain().stop();

			break;
		}

	}

}
