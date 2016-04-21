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
	// private boolean timeBased = false;
	private boolean shoot = true;

	public AutonBreachDefensesAndShoot()
	{
	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		while (RobotStatus.isAuto())
		{

			// Move shooter to rock wall
			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER, EAugerPresets.STANDARD_DEFENSE_AUGER);
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
//			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);

			// Go over rock wall
			// if (timeBased)
			// {
			// //Time Based
			// ActuatorConfig.getInstance().getDriveTrain().disablePID();
			// ActuatorConfig.getInstance().getDriveTrain().setSpeed(-1.0);
			//
			// SensorConfig.getInstance().getTimer().waitTimeInMillis(8000);
			//
			// ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			//
			// } else
			// {
			// Encoder Based
			ActuatorConfig.getInstance().getDriveTrain().goForward(1.0, 130); //130
			// }

			ActuatorConfig.getInstance().getDriveTrain().stopDrive();

			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.3);
			// if(shoot)
			// {
			ActuatorConfig.getInstance().getAutoShot().shoot(position);
			// }

			// Angle drive train toward goal
			// ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.2);

			// if (!RobotStatus.isAuto())
			// break;

			// Shooter to Shoot Position
			// ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);

			// if (!RobotStatus.isAuto())
			// break;

			// Shoot high
			// ActuatorConfig.getInstance().getLauncher().shootSequenceLow();

			break;
		}

	}
}
