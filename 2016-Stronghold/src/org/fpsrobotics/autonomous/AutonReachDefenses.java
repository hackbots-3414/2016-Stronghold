package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonReachDefenses implements IAutonomousControl
{

	private boolean timeBased = false;

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
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE);

			if (!RobotStatus.isAuto())
				break;

			// Go to defenses
			if (timeBased)
			{
				// TIME BASED
				ActuatorConfig.getInstance().getDriveTrain().disablePID();

				ActuatorConfig.getInstance().getDriveTrain().setSpeed(0.5);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(2700);

				ActuatorConfig.getInstance().getDriveTrain().stop();
			} else
			{
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 40_000);
			}

			break;
		}

	}

}
