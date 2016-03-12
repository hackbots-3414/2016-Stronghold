package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

public class AutonLowBar implements IAutonomousControl
{

	@Override
	public void doAuto()
	{
		while (RobotStatus.isAuto())
		{
			//TODO: What did this do?
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.POSITION1150);

			if (!RobotStatus.isAuto())
				break;

			// Move shooter to top limit
			// ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(1400);
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);

			if (!RobotStatus.isAuto())
				break;

			// Go over rock wall
			ActuatorConfig.getInstance().getDriveTrain().goStraight(0.5, 80000);

			if (!RobotStatus.isAuto())
				break;

			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);

			if (!RobotStatus.isAuto())
				break;

			// Straighten drive train
			ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);

			// Angle toward goal
			// TODO: Implement this Auto

			if (!RobotStatus.isAuto())
				break;

			// Shoot
			//TODO: What did this do?
			ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.POSITION400);
			
			if (!RobotStatus.isAuto())
				break;
			
			ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();

			break;
		}

	}

}
