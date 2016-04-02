package org.fpsrobotics.actuators;

import org.fpsrobotics.sensors.SensorConfig;

public class Lifter implements ILifter
{
	private SingleSolenoid lifterOne;
	private SingleSolenoid lifterTwo;

	public Lifter(SingleSolenoid lifterOne, SingleSolenoid lifterTwo)
	{
		this.lifterOne = lifterOne;
		this.lifterTwo = lifterTwo;
	}

	public void lift()
	{
		lifterOne.engage();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);
		lifterOne.disengage();
	}

	public void retract()
	{
		lifterTwo.engage();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);
		lifterTwo.disengage();
	}
}
