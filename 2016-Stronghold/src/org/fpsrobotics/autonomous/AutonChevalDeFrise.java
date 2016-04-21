package org.fpsrobotics.autonomous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

/**
 * UNTESTED
 */
public class AutonChevalDeFrise implements IAutonomousControl
{
	// private boolean shooting = true;

	public AutonChevalDeFrise()
	{
	}

	public void doAuto(EAutoPositions position)
	{
		while (RobotStatus.isAuto())
		{
			System.out.println("Doin' it");

//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
//			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI_AUTO);
			ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(EShooterPresets.LOW_BAR, EAugerPresets.FOURTY_KAI_AUTO);
			
			ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 60); // Used to be 60, 85 used to be 0.8 speed

			ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);

			ActuatorConfig.getInstance().getDriveTrain().goBackward(0.5, 6); // used to be 0.25

			ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);

			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

			ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 90);

			// For shooting
			// if(shooting)
			// {
			ActuatorConfig.getInstance().getAutoShot().shoot(position);
			// }

			System.out.println("Finish it");

			break;
		}
	}
}
