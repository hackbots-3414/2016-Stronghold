package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;

public class AutonDoNothing implements IAutonomousControl
{
	public AutonDoNothing()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		System.out.println("Didn't know how to do it -Raul");
		
		ActuatorConfig.getInstance().getAutoShot().shoot(position); // TODO: Get shooting to work, do nothing doesn't actually do nothing
	}

}
