package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;

public class AutonRockWall implements IAutonomousControl
{

	@Override
	public void doAuto()
	
	{
		ActuatorConfig.getInstance().getDriveTrain().goStraight(0.8, 100000);
		ActuatorConfig.getInstance().getDriveTrain().goStraight(0.2, 10000);
	}

}
