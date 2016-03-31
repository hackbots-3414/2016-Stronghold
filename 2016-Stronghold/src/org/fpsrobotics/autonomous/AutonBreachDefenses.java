package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonBreachDefenses implements IAutonomousControl
{
	private boolean timeBased = true;

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
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE);

			if (!RobotStatus.isAuto())
				break;

			// Go over rock wall
			if (timeBased)
			{
				//Time Based
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-0.8);

				if (!RobotStatus.isAuto())
					break;

				SensorConfig.getInstance().getTimer().waitTimeInMillis(4000);

			} else
			{
				//Encoder based
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 90_000);
			}

			ActuatorConfig.getInstance().getDriveTrain().stop();

			break;
		}

	}
}
