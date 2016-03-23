package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonDriveStraight implements IAutonomousControl
{

	@Override
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{
			
			//Shooter to rock wall position
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);
			
			// Go to defenses
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.8, 12_000);
			
			break;
		}

	}

}

