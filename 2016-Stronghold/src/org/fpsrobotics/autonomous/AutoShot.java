package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.actuators.ILauncher;

public class AutoShot
{
	private ILauncher launcher;
	private IDriveTrain driveTrain;
	private IDriveTrainAssist driveTrainAssist;

	private double TURN_SPEED = 0.3; // used to be 0.2
	private double DRIVE_SPEED = 0.7;

	public AutoShot(ILauncher launcher, IDriveTrain driveTrain, IDriveTrainAssist driveTrainAssist)
	{
		this.launcher = launcher;
		this.driveTrain = driveTrain;
		this.driveTrainAssist = driveTrainAssist;
	}

	public void shoot(EAutoPositions position)
	{
		// NEW
		switch (position)
		{
		case ZERO:
			System.out.println("Didn't know how to shoot it -Raul");
			break;
		case TWO:
			System.out.println("Position Two");
			driveTrain.goForward((DRIVE_SPEED / 5), 3);
			// driveTrainAssist.turnToAngleAndMoveShooterAndAugerToPreset(33, TURN_SPEED,
			// EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_TWO_AUTO,
			// EAugerPresets.FOURTY_KAI_AUTO);
			driveTrain.turnRight(TURN_SPEED, 33);
			launcher.moveShooterAndAugerToPreset(EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_TWO_AUTO, EAugerPresets.FOURTY_KAI_AUTO);
			launcher.shootSequenceHighAuto();
			break;
		case THREE:
			System.out.println("Position Three");
			driveTrain.goForward((DRIVE_SPEED / 5), 3);
			// driveTrainAssist.turnToAngleAndMoveShooterAndAugerToPreset(15, TURN_SPEED,
			// EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_THREE_AUTO,
			// EAugerPresets.FOURTY_KAI_AUTO);
			driveTrain.turnRight(TURN_SPEED, 15); // 15
			launcher.moveShooterAndAugerToPreset(EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_THREE_AUTO, EAugerPresets.FOURTY_KAI_AUTO);
			launcher.shootSequenceHighAuto();
			break;
		case FOUR:
			System.out.println("Position Four");
			driveTrain.goForward((DRIVE_SPEED / 5), 3);
			// driveTrainAssist.turnToAngleAndMoveShooterAndAugerToPreset(-5, TURN_SPEED,
			// EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_FOUR_AUTO,
			// EAugerPresets.FOURTY_KAI_AUTO);
			driveTrain.turnLeft(TURN_SPEED, 5); // 7
			launcher.moveShooterAndAugerToPreset(EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_FOUR_AUTO, EAugerPresets.FOURTY_KAI_AUTO);
			launcher.shootSequenceHighAuto();
			break;
		case FIVE:
			System.out.println("Position Five");
			driveTrain.goForward((DRIVE_SPEED / 5), 3);
			// driveTrainAssist.turnToAngleAndMoveShooterAndAugerToPreset(-22, TURN_SPEED,
			// EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_FIVE_AUTO,
			// EAugerPresets.FOURTY_KAI_AUTO);
			driveTrain.turnLeft(TURN_SPEED, 22);
			launcher.moveShooterAndAugerToPreset(EShooterPresets.SHOOTER_POSITION_AT_DEFENSE_FIVE_AUTO, EAugerPresets.FOURTY_KAI_AUTO);
			launcher.shootSequenceHighAuto();
			break;
		}

		// // OLD
		// switch (position)
		// {
		// case ZERO:
		// System.out.println("Didn't know how to shoot it -Raul");
		// break;
		// // Some other defense
		// case TWO:
		// System.out.println("Position Two (High Boy)");
		// // Might have to stop after defense in order to recalibrate gyro, test this.
		// // Shoot High
		// driveTrain.goForward((DRIVE_SPEED / 5), 3);
		// driveTrain.turnRight(TURN_SPEED, 33);
		// launcher.moveShooterToPosition(SHOOTER_POSITION);
		// // Auger already raises in shootSequenceHighAuto
		// launcher.shootSequenceHighAuto();
		// break;
		//
		// // Some other defense
		// case THREE:
		// System.out.println("Position Three (High Boy)");
		// // Might have to stop after defense in order to recalibrate gyro, test this.
		// driveTrain.goForward((DRIVE_SPEED / 5), 3);
		// driveTrain.turnRight(TURN_SPEED, 15); // 15
		// launcher.moveShooterToPosition(SHOOTER_POSITION);
		// // Auger already raises in shootSequenceHighAuto
		// launcher.shootSequenceHighAuto();
		// break;
		//
		// // Some other defense
		// case FOUR:
		// System.out.println("Position Four (High Boy)");
		// // Might have to stop after defense in order to recalibrate gyro, test this.
		// driveTrain.goForward((DRIVE_SPEED / 5), 3);
		// driveTrain.turnLeft(TURN_SPEED, 5); // 7
		// launcher.moveShooterToPosition(SHOOTER_POSITION);
		// // Auger already raises in shootSequenceHighAuto
		// launcher.shootSequenceHighAuto();
		// break;
		//
		// // Some other defense
		// case FIVE: // 22
		// System.out.println("Position Five (High Boy)");
		// // Might have to stop after defense in order to recalibrate gyro, test this.
		//
		// // Shoot Low
		// // driveTrain.goForward(DRIVE_SPEED, 115);
		// // driveTrain.turnLeft(TURN_SPEED, 73);
		// // launcher.moveShooterToPreset(EShooterPresets.LOW_BAR);
		// // launcher.shootSequenceLowAuto();
		//
		// // Shoot High
		// driveTrain.goForward((DRIVE_SPEED / 5), 3);
		// driveTrain.turnLeft(TURN_SPEED, 22);
		// launcher.moveShooterToPosition(SHOOTER_POSITION);
		// // // Auger already raises in shootSequenceHighAuto
		// launcher.shootSequenceHighAuto();
		// break;
		// }
	}
}
