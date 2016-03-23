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
	private int DRIVE_TIME = 2700;
	private int DRIVE_DISTANCE = 90_000;
	private double DRIVE_SPEED = 0.8;
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
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);

			if (!RobotStatus.isAuto())
				break;

			// Go over rock wall
			if (timeBased)
			{
				//Time Based
				ActuatorConfig.getInstance().getDriveTrain().disablePID();
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(-DRIVE_SPEED);

				SensorConfig.getInstance().getTimer().waitTimeInMillis(DRIVE_TIME);

			} else
			{
				//Encoder Based
				ActuatorConfig.getInstance().getDriveTrain().goStraight(DRIVE_SPEED, DRIVE_DISTANCE);
			}

			ActuatorConfig.getInstance().getDriveTrain().stop();

			if (!RobotStatus.isAuto())
				break;

			// Angle drive train toward goal
			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.2);

			if (!RobotStatus.isAuto())
				break;

			// Shooter to Shoot Position
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT_LOW);

			if (!RobotStatus.isAuto())
				break;

			// Shoot high
			ActuatorConfig.getInstance().getLauncher().shootSequenceLow();

			
			break;
		}

	}
}
