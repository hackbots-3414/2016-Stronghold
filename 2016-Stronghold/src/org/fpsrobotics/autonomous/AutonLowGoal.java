package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowGoal implements IAutonomousControl
{

	@Override
	public void doAuto()
	{
		System.out.println("Low Goal");
		
		// Move shooter to top limit
		if (RobotStatus.isAuto())
		{
			//ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(1100);
			
			SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);
		}
		
		// Go over rock wall
		if (RobotStatus.isAuto())
		{
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 100000);
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
			ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(400);
			ActuatorConfig.getInstance().getLauncher().shootSequence();
		}
	}

}
