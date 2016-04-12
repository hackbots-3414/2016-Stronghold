package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonBreachDefenses implements IAutonomousControl
{
	private boolean timeBased = false;

	public AutonBreachDefenses()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		while (RobotStatus.isAuto())
		{

			// Move shooter to rock wall
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);

			if (!RobotStatus.isAuto())
				break;

			// Go over standard defense
			if (timeBased)
			{
				// Time Based
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-0.8);

				if (!RobotStatus.isAuto())
					break;

				SensorConfig.getInstance().getTimer().waitTimeInMillis(4000);

				ActuatorConfig.getInstance().getDriveTrain().stopDrive();

			} else
			{

				// Encoder based
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 39); // TODO: tune value (in inches)
			}
			
			break;
		}

	}
}
