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
//	private boolean timeBased = false;
	private boolean shooting = false; // TODO: Make shooting work for cheval
//	private int shootPosition = 2;

	ExecutorService executor;
	
//	private int SHOOT_ANGLE;

	public AutonChevalDeFrise()
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
		while (RobotStatus.isAuto())
		{
			System.out.println("Doin' it");

			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
			
			ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 60);
			
			ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
			
			ActuatorConfig.getInstance().getDriveTrain().goBackward(0.25, 6);
			
			ActuatorConfig.getInstance().getDriveTrain().stopDrive();
			SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
			
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);

			ActuatorConfig.getInstance().getDriveTrain().goForward(0.8, 90); // TODO: Adjust where this ends up
			
			// might want to get rid of this...
			executor.submit(() ->
			{
//				SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);
			});
			
			// For shooting
			if(shooting)
			{
				ActuatorConfig.getInstance().getAutoShot().shoot(position);
			}
			
			System.out.println("Finish it");
			
			// Move shooter to normal defense
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
//			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);
//
//			if (!RobotStatus.isAuto())
//				break;
//
//			if (timeBased)
//			{
//				// TIME BASED
//
//				ActuatorConfig.getInstance().getDriveTrain().disablePID();
//
//				// Drive to Cheval
//				ActuatorConfig.getInstance().getDriveTrain().setSpeed(0.5);
//
//				SensorConfig.getInstance().getTimer().waitTimeInMillis(5000);
//
//				ActuatorConfig.getInstance().getDriveTrain().stopDrive();
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Move Auger to Bottom Limit
//				ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
//				// If the shooter is too low and we cannot do cheval like this, we cannot do this autonomous mode
//				// TODO: Check AutonChevalDeFris if Shooter collides with Auger
//				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Go over half of Cheval
//				ActuatorConfig.getInstance().getDriveTrain().setSpeed(0.5);
//
//				SensorConfig.getInstance().getTimer().waitTimeInMillis(1500);
//
//				ActuatorConfig.getInstance().getDriveTrain().stopDrive();
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Move Auger Up
//				ActuatorConfig.getInstance().getLauncher()
//						.moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
//				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Keep Driving
//				ActuatorConfig.getInstance().getDriveTrain().setSpeed(0.5);
//
//				SensorConfig.getInstance().getTimer().waitTimeInMillis(1500);
//
//				ActuatorConfig.getInstance().getDriveTrain().stopDrive();
//
//				if (!RobotStatus.isAuto())
//					break;
//
//			} else
//			{
//				// ENCODER BASED
//
//				// Drive to Cheval
//				ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 60); // TODO: Use inches rather than
//																					// encoder counts
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Move Auger to Bottom Limit
//				ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
//				// TODO: Check AutonChevalDeFris if Shooter collides with Auger
//				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.LOW_BAR);
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Go over half of chevel
//				ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 4000);
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Move Auger up
//				ActuatorConfig.getInstance().getLauncher()
//						.moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
//				ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Keep Driving
//				ActuatorConfig.getInstance().getDriveTrain().goForward(0.5, 4000);
//			}
//
//			ActuatorConfig.getInstance().getDriveTrain().stopDrive();
//
//			if (shooting)
//			{
//				// TO SHOOT
//				if (!RobotStatus.isAuto())
//					break;
//
//				// Straighten drive train
//				ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);
//
//				ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.3);
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				if (RobotStatus.isAlpha())
//				{
//					ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(765); // alpha
//					// TODO: Tune Auton Cheval - shoot angle and shoot height
//				} else
//				{
//					ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(300); // beta
//				}
//
//				if (!RobotStatus.isAuto())
//					break;
//
//				ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
//			}
			break;
		}
	}
}
