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
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{
			// Move shooter to low bar
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

			if (!RobotStatus.isAuto())
				break;

			if (timeBased)
			{
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-0.5);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(5000);

				ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			} else
			{
				// Go under low bar
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 130_000); //TODO: Use inches rather than encoder counts
			}

			ActuatorConfig.getInstance().getDriveTrain().stopDrive();

			break;
		}

	}

}
