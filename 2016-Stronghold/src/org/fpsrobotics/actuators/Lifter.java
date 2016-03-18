package org.fpsrobotics.actuators;

public class Lifter implements ILifter
{
	private DoubleSolenoid lifterOne;
	private DoubleSolenoid lifterTwo;

	public Lifter(DoubleSolenoid lifterOne, DoubleSolenoid lifterTwo)
	{
		this.lifterOne = lifterOne;
		this.lifterTwo = lifterTwo;
	}

	public void lift()
	{
		lifterOne.engage();
		lifterTwo.engage();
	}

	public void retract()
	{
		lifterOne.disengage();
		lifterTwo.disengage();
	}
}
