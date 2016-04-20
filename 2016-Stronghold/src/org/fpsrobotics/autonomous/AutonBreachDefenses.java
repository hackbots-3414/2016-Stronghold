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
		if (RobotStatus.isAuto())
		{

			// Move shooter to rock wall
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);

			// Go over standard defense
			if (timeBased)
			{
				// Time Based
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-0.8);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(4000);

				ActuatorConfig.getInstance().getDriveTrain().stopDrive();

			} else
			{
				// Encoder based
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 160); // Used to be 130
			}
			
		}

	}
}
