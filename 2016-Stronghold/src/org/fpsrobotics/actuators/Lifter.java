package org.fpsrobotics.actuators;

public class Lifter implements ILifter
{
	private DoubleSolenoid lifterOne;

	public Lifter(DoubleSolenoid lifterOne)
	{
		this.lifterOne = lifterOne;
	}

	public void lift()
	{
		lifterOne.engage();
	}

	public void retract()
	{
		lifterOne.disengage();
	}
}
