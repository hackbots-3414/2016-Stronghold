package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.actuators.ILauncher;

public class AutoShot
{
	private ILauncher launcher;
	private IDriveTrain driveTrain;

	private double TURN_SPEED = 0.3; // used to be 0.2
	private int SHOOTER_POSITION = 600; // 765
	private double DRIVE_SPEED = 0.7;

	public AutoShot(ILauncher launcher, IDriveTrain driveTrain)
	{
		this.launcher = launcher;
		this.driveTrain = driveTrain;
	}

	public void shoot(EAutoPositions position)
	{
		switch (position)
		{
		case ZERO:
			System.out.println("Didn't know how to shoot it -Raul");

			break;
		// Always Low Bar; CASE IS NEVER BEING USED
		case ONE:
			System.out.println("Position One (Low Bar)");
			// No drive forward because it is handled elsewhere and is constant every time
			// ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(46, TURN_SPEED);
			// ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
			// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
			// ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();

			// Don't do it big boy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			System.out.println("Didn't know how to do it - Raul");
			break;
		// Some other defense
		case TWO:
			System.out.println("Position Two (High Boy)");
			// Might have to stop after defense in order to recalibrate gyro, test this.
			//Shoot High
			driveTrain.goForward((DRIVE_SPEED / 5), 3);
			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngleAndMoveShooterAndAugerToPreset(33, TURN_SPEED, EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_TWO, EAugerPresets.FOURTY_KAI);
//			ActuatorConfig.getInstance().getDriveTrain().turnRight(TURN_SPEED, 33);
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
			// Auger already raises in shootSequenceHighAuto
			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
			break;

		// Some other defense
		case THREE:
			System.out.println("Position Three (High Boy)");
			// Might have to stop after defense in order to recalibrate gyro, test this.
			driveTrain.goForward((DRIVE_SPEED / 5), 3);
			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngleAndMoveShooterAndAugerToPreset(15, TURN_SPEED, SHOOTER_POSITION, EAugerPresets.FOURTY_KAI_AUTO);
//			ActuatorConfig.getInstance().getDriveTrain().turnRight(TURN_SPEED, 15); // 15
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
			// Auger already raises in shootSequenceHighAuto
			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
			break;

		// Some other defense
		case FOUR:
			System.out.println("Position Four (High Boy)");
			// Might have to stop after defense in order to recalibrate gyro, test this.
			driveTrain.goForward((DRIVE_SPEED / 5), 3);
			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngleAndMoveShooterAndAugerToPreset(-5, TURN_SPEED, SHOOTER_POSITION, EAugerPresets.FOURTY_KAI_AUTO);
//			ActuatorConfig.getInstance().getDriveTrain().turnLeft(TURN_SPEED, 5); // 7
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
			// Auger already raises in shootSequenceHighAuto
			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
			break;

		// Some other defense
		case FIVE: // 22
			System.out.println("Position Five (High Boy)");
			// Might have to stop after defense in order to recalibrate gyro, test this.

			// Shoot Low
			// driveTrain.goForward(DRIVE_SPEED, 115);
			// ActuatorConfig.getInstance().getDriveTrain().turnLeft(TURN_SPEED, 73);
			// ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
			// ActuatorConfig.getInstance().getLauncher().shootSequenceLowAuto();

			// Shoot High
			driveTrain.goForward((DRIVE_SPEED / 5), 3);
			//A
			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngleAndMoveShooterAndAugerToPreset(-22, TURN_SPEED, EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_FIVE, EAugerPresets.FOURTY_KAI);
//			B
//			ActuatorConfig.getInstance().getDriveTrain().turnLeft(TURN_SPEED, 22);
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
//			// Auger already raises in shootSequenceHighAuto
			ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();

			break;
		}
	}
}
