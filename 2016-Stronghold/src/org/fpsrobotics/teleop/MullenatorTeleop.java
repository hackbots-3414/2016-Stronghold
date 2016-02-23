package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.ILauncher;
import org.fpsrobotics.sensors.ButtonGamepad;
import org.fpsrobotics.sensors.ButtonJoystick;
import org.fpsrobotics.sensors.IGamepad;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MullenatorTeleop implements ITeleopControl
{
	private ExecutorService executor;

	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(2);
		
		SmartDashboard.putNumber("Preset", 477);
	}

	@Override
	public void doTeleop()
	{
		executor.submit(() ->
		{
			ActuatorConfig.getInstance().getDriveTrain().enablePID();
			ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);
			double correctedYOne, correctedYTwo, yOne, yTwo;
			boolean pidOn = true;

			while (RobotStatus.isRunning())
			{
				if (SensorConfig.getInstance().getRightJoystick().getButtonValue(ButtonJoystick.FIVE))
				{
					pidOn = !pidOn;

					if (pidOn)
					{
						ActuatorConfig.getInstance().getDriveTrain().enablePID();
						ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);

					} else
					{
						ActuatorConfig.getInstance().getDriveTrain().disablePID();

					}
					
					while(SensorConfig.getInstance().getRightJoystick().getButtonValue(ButtonJoystick.FIVE));
				}
				
				SmartDashboard.putBoolean("PID", pidOn);

				if (pidOn)
				{
					yOne = SensorConfig.getInstance().getRightJoystick().getY();
					yTwo = SensorConfig.getInstance().getLeftJoystick().getY();

					/* Linear Drive Control
					correctedYOne = yOne * 400;
					correctedYTwo = yTwo * 400;
					*/

					  /* Inverse Tangent Drive Control  */
					 correctedYOne = Math.atan(yOne)*(4/Math.PI)*400; 
					 correctedYTwo = Math.atan(yTwo)*(4/Math.PI)*400;
					 
					if (correctedYOne > 25 || correctedYOne < -25 || correctedYTwo > 25 || correctedYTwo < -25)
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYOne, correctedYTwo);
					} else
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(0, 0);
					}

				} else
				{
					correctedYOne = SensorConfig.getInstance().getRightJoystick().getY();
					correctedYTwo = SensorConfig.getInstance().getLeftJoystick().getY();

					if (correctedYOne > 0.1 || correctedYTwo > 0.1 || correctedYOne < -0.1 || correctedYTwo < -0.1)
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYOne, correctedYTwo);
					} else
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(0, 0);
					}

				}

				/*
				System.out.println("rate: " + ActuatorConfig.getInstance().getRightEncoder().getRate() + " " + ActuatorConfig.getInstance().getLeftEncoder().getRate());
				System.out.println("error: " + ActuatorConfig.getInstance().getRightEncoder().getError() + " " + ActuatorConfig.getInstance().getLeftEncoder().getError());
				*/
				
				System.out.println("Potentiometer " + SensorConfig.getInstance().getShooterPot().getCount());
				
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
			}
		});

		executor.submit(() ->
		{
			ILauncher launcher;
			IGamepad gamepad;

			launcher = ActuatorConfig.getInstance().getLauncher();
			gamepad = SensorConfig.getInstance().getGamepad();

			while (RobotStatus.isRunning())
			{
				// Shooter movement commands
				while (gamepad.getButtonValue(ButtonGamepad.TWO))
				{
					launcher.moveShooterDown();
				}

				while (gamepad.getButtonValue(ButtonGamepad.FOUR))
				{
					launcher.moveShooterUp();
				}

				launcher.stopShooterLifter();

				// Auger movement commands
				while (gamepad.getButtonValue(ButtonGamepad.TWELVE))
				{
					launcher.lowerAuger();
				}
				
				while (gamepad.getButtonValue(ButtonGamepad.ELEVEN))
				{
					launcher.raiseAuger();
				}
				
				launcher.stopAuger();
				
				// Launching commands
				while (gamepad.getButtonValue(ButtonGamepad.ONE))
				{
					launcher.spinShooterUp();
					
					if(gamepad.getButtonValue(ButtonGamepad.SIX))
					{
						launcher.launchBoulder(); 
						
						while (gamepad.getButtonValue(ButtonGamepad.SIX) || gamepad.getButtonValue(ButtonGamepad.ONE));
					}
				}

				if (gamepad.getButtonValue(ButtonGamepad.SEVEN))
				{
					launcher.shootSequence();
					
					while (gamepad.getButtonValue(ButtonGamepad.SEVEN));
				}
				
				while (gamepad.getButtonValue(ButtonGamepad.THREE))
				{
					launcher.intakeBoulder();
				}

				launcher.stopShooterWheels();
				
				while (gamepad.getButtonValue(ButtonGamepad.FIVE))
				{
					launcher.moveShooterToPosition(SmartDashboard.getNumber("Preset", 477));
				}
				
				launcher.stopShooterLifter();
				
				/*
				if (gamepad.getButtonValue(ButtonGamepad.EIGHT))
				{
					SensorConfig.getInstance().getGyro().resetCount();
					ActuatorConfig.getInstance().getDriveTrainAssist().centerDriveTrain(0.1);
					
					//ActuatorConfig.getInstance().getDriveTrain().enablePID();
					//ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);
					
					while(gamepad.getButtonValue(ButtonGamepad.EIGHT));
				}
				*/
			}
		});

	}
}
