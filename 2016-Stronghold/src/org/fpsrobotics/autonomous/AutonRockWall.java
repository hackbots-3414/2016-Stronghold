package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonRockWall implements IAutonomousControl
{
	
	private int SHOOT_ANGLE = 0;

	@Override
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{

			// Move shooter to rock wall
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);

			if (!RobotStatus.isAuto())
				break;

			System.out.println("A");
			
			// Go over rock wall

			ActuatorConfig.getInstance().getDriveTrain().disablePID();
			ActuatorConfig.getInstance().getDriveTrain().setSpeed(-0.8);
			
			
			System.out.println("B");
			if (!RobotStatus.isAuto())
				break;
			
			SensorConfig.getInstance().getTimer().waitTimeInMillis(2700);
			
			System.out.println("C");
			if (!RobotStatus.isAuto())
				break;
			
			ActuatorConfig.getInstance().getDriveTrain().stop();
			System.out.println("D");
//			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.8, 90_000);

			if (!RobotStatus.isAuto())
				break;
			
			// Angle drive train toward goal
//			if (!RobotStatus.isAuto())
//				break;

//			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.1);

//			if (!RobotStatus.isAuto())
//				break;

//			// Shooter to Shoot Position
//			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT);
//
//			if (!RobotStatus.isAuto())
//				break;
//
//			// Shoot high
//			ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();

			break;
		}

	}
}
