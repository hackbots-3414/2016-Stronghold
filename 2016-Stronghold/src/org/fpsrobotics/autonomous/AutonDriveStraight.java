package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
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
			
			// Go over rock wall
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 80000);
			
			
//			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5);
//						
//			if (!RobotStatus.isAuto())
//				break;
//
//			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);
//
//			if (!RobotStatus.isAuto())
//				break;
//			
//			ActuatorConfig.getInstance().getDriveTrain().stop();
			
			break;
		}

	}

}

