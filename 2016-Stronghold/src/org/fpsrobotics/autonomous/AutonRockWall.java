package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.sensors.SensorConfig;

public class AutonRockWall implements IAutonomousControl
{

	@Override
	public void doAuto()
	
	{
		// Go over rock wall
		ActuatorConfig.getInstance().getDriveTrain().goStraight(0.8, 100000);
		
		// Straighten drive train
		ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);
		
		// Wait
		SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
		
		// Shoot ball
		ActuatorConfig.getInstance().getLauncher().shootSequence();
	}

}
