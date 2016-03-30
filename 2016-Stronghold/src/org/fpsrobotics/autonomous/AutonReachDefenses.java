package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonReachDefenses implements IAutonomousControl
{

	private boolean timeBased = false;

	private int DRIVE_TIME = 2700;
	private int DRIVE_DISTANCE = 40_000;
	private double DRIVE_SPEED = 0.5;

	public AutonReachDefenses()
	{

	}

	@Override
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{
			// Shooter to rock wall position
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);

			if (!RobotStatus.isAuto())
				break;
			
			// Go to defenses
			if (timeBased)
			{
				// TIME BASED
				ActuatorConfig.getInstance().getDriveTrain().disablePID();

				ActuatorConfig.getInstance().getDriveTrain().setSpeed(DRIVE_SPEED);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(DRIVE_TIME);

				ActuatorConfig.getInstance().getDriveTrain().stop();
			} else
			{
				ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, DRIVE_DISTANCE);
			}

			break;
		}

	}

}
