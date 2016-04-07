package org.fpsrobotics.actuators;

public class Lifter implements ILifter
{
	private DoubleSolenoid lifter;

	public Lifter(DoubleSolenoid lifter)
	{
		this.lifter = lifter;
	}

	public void lift()
	{
		lifter.engage();
	}

	public void retract()
	{
		lifter.disengage();
	}
}
