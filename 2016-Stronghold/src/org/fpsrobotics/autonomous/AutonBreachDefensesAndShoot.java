package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

/**
 * UNTESTED
 */
public class AutonBreachDefensesAndShoot implements IAutonomousControl
{
	private boolean timeBased = true;
	private int SHOOT_ANGLE = 30;

	public AutonBreachDefensesAndShoot()
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

				SensorConfig.getInstance().getTimer().waitTimeInMillis(2700);

			} else
			{
				//Encoder Based
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 90_000);
			}

			ActuatorConfig.getInstance().getDriveTrain().stop();

			if (!RobotStatus.isAuto())
				break;

			// Angle drive train toward goal
			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.2);

			if (!RobotStatus.isAuto())
				break;

			// Shooter to Shoot Position
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);

			if (!RobotStatus.isAuto())
				break;

			// Shoot high
			ActuatorConfig.getInstance().getLauncher().shootSequenceLow();

			
			break;
		}

	}
}
