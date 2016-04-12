package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.actuators.ILauncher;

public class AutoShot
{
	private ILauncher launcher;
	private IDriveTrain driveTrain;
	
	private double TURN_SPEED = 0.2;
	private int SHOOTER_POSITION = 765;
	private double DRIVE_SPEED = 0.8;
	
	public AutoShot(ILauncher launcher, IDriveTrain driveTrain)
	{
		this.launcher = launcher;
		this.driveTrain = driveTrain;
	}
	
	public void shoot(EAutoPositions position)
	{
		switch(position)
		{
			// Always Low Bar
			case ONE:
				System.out.println("Position One");
				// No drive forward because it is handled elsewhere and is constant every time
				ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(46, TURN_SPEED);
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
				ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
				break;
			// Some other defense
			case TWO:
				System.out.println("Position Two");
				// Might have to stop after defense in order to recalibrate gyro, test this.
				driveTrain.goForward(DRIVE_SPEED, 100); // TODO: Tune value (in inches)
				ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(46, TURN_SPEED); // TODO: Adjust angle
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
				ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
				break;
				
			// Some other defense
			case THREE:
				System.out.println("Position Three");
				// Might have to stop after defense in order to recalibrate gyro, test this.
				driveTrain.goForward(DRIVE_SPEED, 100); // TODO: Tune value (in inches)
				ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(46, TURN_SPEED); // TODO: Adjust angle
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
				ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
				break;
				
			// Some other defense
			case FOUR:
				System.out.println("Position Four");
				// Might have to stop after defense in order to recalibrate gyro, test this.
				driveTrain.goForward(DRIVE_SPEED, 100); // TODO: Tune value (in inches)
				ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(46, TURN_SPEED); // TODO: Adjust angle
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
				ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
				break;
				
			// Some other defense
			case FIVE:
				System.out.println("Position Five");
				// Might have to stop after defense in order to recalibrate gyro, test this.
				driveTrain.goForward(DRIVE_SPEED, 100); // TODO: Tune value (in inches)
				ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(46, TURN_SPEED); // TODO: Adjust angle
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(SHOOTER_POSITION);
				ActuatorConfig.getInstance().getLauncher().shootSequenceHighAuto();
				break;
		
		}
	}
}
