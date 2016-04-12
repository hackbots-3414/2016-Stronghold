package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;

public class FourtyKai implements IAutonomousControl
{
	public FourtyKai()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		// ActuatorConfig.getInstance().getLifter().retract();
		ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
		System.out.println("Fourty Kai'd");
	}

}
