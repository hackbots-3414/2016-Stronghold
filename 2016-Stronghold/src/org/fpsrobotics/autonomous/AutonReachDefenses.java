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
	public void doAuto(EAutoPositions position)
	{
		while (RobotStatus.isAuto())
		{
			// Shooter to rock wall position
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);

			if (!RobotStatus.isAuto())
				break;

			// Go to defenses
			if (timeBased)
			{
				// TIME BASED
				ActuatorConfig.getInstance().getDriveTrain().disablePID();

				ActuatorConfig.getInstance().getDriveTrain().setSpeed(0.5);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(2700);

				ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			} else
			{
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 40_000); //TODO: Use inches rather than encoder counts
			}

			break;
		}

	}

}
