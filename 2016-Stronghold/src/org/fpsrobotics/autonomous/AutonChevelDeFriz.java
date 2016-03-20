package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonChevelDeFriz implements IAutonomousControl
{
	
	private int SHOOT_ANGLE = 90;

	public AutonChevelDeFriz()
	{

	}

	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{

			// Move shooter to normal defense
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.NORMAL_DEFENSES);

			if (!RobotStatus.isAuto())
				break;

			// drive to chevel
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 1000);

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);


			if (!RobotStatus.isAuto())
				break;
			
			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);

			if (!RobotStatus.isAuto())
					break;

			// Go over half of chevel
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 4000);

			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);

			if (!RobotStatus.isAuto())
				break;

			// Keep Driving
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 4000);

			if (!RobotStatus.isAuto())
				break;
			
			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);
			
			if (!RobotStatus.isAuto())
				break;

			// Straighten drive train
			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);

			// Angle drive train toward goal
			if (!RobotStatus.isAuto())
				break;

			ActuatorConfig.getInstance().getDriveTrainAssist().turnToAngle(SHOOT_ANGLE, 0.1);

			if (!RobotStatus.isAuto())
				break;

			// Shooter to Shoot Position
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT);

			if (!RobotStatus.isAuto())
				break;

			// Shoot high
			ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();

			break;
		}
	}
}
