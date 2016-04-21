package org.fpsrobotics.autonomous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

/**
 * UNTESTED
 */
public class AutonPortcullis implements IAutonomousControl
{
	// private boolean timeBased = false;
	private boolean shooting = true;
	private boolean doneWithGoThrough = false;

	private ExecutorService executor;

	public AutonPortcullis()
	{
		executor = Executors.newFixedThreadPool(1);
	}

	/**
	 * THIS JUST GOES
	 */
	public void doAuto(EAutoPositions position)
	{
		if (RobotStatus.isAuto())
		{
			System.out.println("Doin' it (This Just Goes)");

			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER, EAugerPresets.LOW_BAR);
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
//			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

			ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 130);
			
			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.3);

			// ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 90);
			//
			// ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			// SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
			//
			// ActuatorConfig.getInstance().getDriveTrain().goBackward(0.25, 2);
			//
			// ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			// SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
			//
			// // drive forward
			// executor.submit(() ->
			// {
			// SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
			// ActuatorConfig.getInstance().getDriveTrain().goForward(0.25, 40, false); //0.3
			// ActuatorConfig.getInstance().getDriveTrain().goForward(0.7, 40, false);
			// doneWithGoThrough = true;
			// });
			//
			// // auger raise
			// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.PORTCULLIS);
			//
			// while (!doneWithGoThrough)
			// {
			// System.out.println("Going through Portcullis");
			// }
			//
			// SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
			//
			// For shooting
//			if (shooting)
//			{
				ActuatorConfig.getInstance().getAutoShot().shoot(position);
//			}

			System.out.println("Finish it");
		}
	}
}
