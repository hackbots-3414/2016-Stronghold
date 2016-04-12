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
public class AutonPortcullis implements IAutonomousControl
{
//	private boolean timeBased = false;
	private boolean shooting = true;
//	private int shootPosition = 2;

	ExecutorService executor;
	
//	private int SHOOT_ANGLE;

	public AutonPortcullis()
	{
		executor = Executors.newFixedThreadPool(1);
		
//		switch (shootPosition)
//		{
//		case 1:
//			SHOOT_ANGLE = 30;
//			break;
//		case 2:
//			SHOOT_ANGLE = 30;
//			break;
//		case 3:
//			SHOOT_ANGLE = 30;
//			break;
//		case 4:
//			SHOOT_ANGLE = 30;
//			break;
//		case 5:
//			SHOOT_ANGLE = 30;
//			break;
//		}
	}

	public void doAuto(EAutoPositions position)
	{
		if (RobotStatus.isAuto())
		{
			System.out.println("Doin' it");

//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);
			
			ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 60);
			
			if (!RobotStatus.isAuto())
				return;
			
			ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
			
			if (!RobotStatus.isAuto())
				return;
			
			ActuatorConfig.getInstance().getDriveTrain().goBackward(0.25, 2);
			
			if (!RobotStatus.isAuto())
				return;
			
			ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);

			executor.submit(() ->
			{
				ActuatorConfig.getInstance().getDriveTrain().goForward(0.3, 40); // TODO: Adjust where this ends up
			});
			
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
			
			SensorConfig.getInstance().getTimer().waitTimeInMillis(3000); // TODO: Adjust time in order to delay the auto shoot sequence from taking over.
			
			if (!RobotStatus.isAuto())
				return;
			
			// For shooting
			if(shooting)
			{
				ActuatorConfig.getInstance().getAutoShot().shoot(position);
			}
			
			System.out.println("Finish it");
		}
	}
}
