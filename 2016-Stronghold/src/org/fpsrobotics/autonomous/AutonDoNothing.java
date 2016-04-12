package org.fpsrobotics.autonomous;

public class AutonDoNothing implements IAutonomousControl
{
	public AutonDoNothing()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		System.out.println("Didn't know how to do it -Raul");
	}

}
