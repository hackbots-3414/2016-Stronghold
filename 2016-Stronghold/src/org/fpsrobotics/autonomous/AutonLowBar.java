package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBar implements IAutonomousControl
{
	private boolean timeBased = false;

	public AutonLowBar()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		while (RobotStatus.isAuto())
		{
			// Move shooter to low bar
			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(EShooterPresets.LOW_BAR, EAugerPresets.LOW_BAR);
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
//			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);
			if (timeBased)
			{
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-0.5);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(5000);

				ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			} else
			{
				// Go under low bar
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 140_000);
			}

			ActuatorConfig.getInstance().getDriveTrain().stopDrive();

			break;
		}

	}

}
