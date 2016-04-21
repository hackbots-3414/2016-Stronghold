package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;

public class FourtyKai implements IAutonomousControl
{
	public FourtyKai()
	{

	}

	@Override
	public void doAuto(EAutoPositions position)
	{
		// ActuatorConfig.getInstance().getLifter().retract();
		ActuatorConfig.getInstance().getLauncher().moveShooterAndAugerToPreset(EShooterPresets.LOW_BAR, EAugerPresets.FOURTY_KAI);
		
		System.out.println("Fourty Kai'd");
	}

}
