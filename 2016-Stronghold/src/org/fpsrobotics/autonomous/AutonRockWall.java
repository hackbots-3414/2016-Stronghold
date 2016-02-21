package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonRockWall implements IAutonomousControl
{

	@Override
	public void doAuto()

	{
		// Move shooter to top limit
		if (RobotStatus.isAuto())
		{
			for(int i = 0; i < 100; i++)
			{
				ActuatorConfig.getInstance().getLauncher().moveShooterToTopLimit();
				SensorConfig.getInstance().getTimer().waitTimeInMillis(10);
			}
			
			ActuatorConfig.getInstance().getLauncher().stopShooterLifter();
		}
		
		// Go over rock wall
		if (RobotStatus.isAuto())
		{
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.7, 100000);
		}

		if (RobotStatus.isAuto())
		{
			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);
		}

		// Straighten drive train
		if (RobotStatus.isAuto())
		{
			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);
		}

		if (RobotStatus.isAuto())
		{
			for(int i = 0; i < 100; i++)
			{
				ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(400);
				SensorConfig.getInstance().getTimer().waitTimeInMillis(10);
			}
			
			ActuatorConfig.getInstance().getLauncher().stopShooterLifter();
			
			ActuatorConfig.getInstance().getLauncher().shootSequence();
		}

		/*
		 * // Wait SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
		 * 
		 * // Shoot ball
		 * ActuatorConfig.getInstance().getLauncher().shootSequence();
		 */
	}

}
