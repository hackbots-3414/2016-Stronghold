package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;

public class AutonTestShooting implements IAutonomousControl
{
	public AutonTestShooting()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		System.out.println("Testing Shoot -Raul");
		
		ActuatorConfig.getInstance().getAutoShot().shoot(position); 
		// TODO: Auto: get shooting to work, do nothing doesn't actually do nothing
	}

}
