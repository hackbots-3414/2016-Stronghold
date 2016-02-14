package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.ILauncher;
import org.fpsrobotics.sensors.ButtonGamepad;
import org.fpsrobotics.sensors.GamepadLogger;
import org.fpsrobotics.sensors.IGamepad;
import org.fpsrobotics.sensors.ILogger;
import org.fpsrobotics.sensors.JoystickLogger;
import org.fpsrobotics.sensors.OutputDevice;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;
import org.fpsrobotics.sensors.ButtonJoystick;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MullenatorTeleop implements ITeleopControl
{
	private ExecutorService executor;

	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(2);
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
				}
				SmartDashboard.putBoolean("PID", pidOn);

				if (pidOn)
				{
					yOne = SensorConfig.getInstance().getRightJoystick().getY();
					yTwo = SensorConfig.getInstance().getLeftJoystick().getY();
					
					correctedYOne = yOne*400;
					correctedYTwo = yTwo*400;
					/*
					if(yOne > 0)
					{
						correctedYOne = Math.log10(yOne+.12) * 400 + 370;
					} else if (yOne < 0)
					{
						correctedYOne = -(Math.log10(Math.abs(yOne)+.12) * 400 + 370);
					} else
					{
						correctedYOne = 0;
					}
					
					if(yOne > 0)
					{
						correctedYTwo = Math.log10(yTwo+.12) * 400 + 370;
					} else if (yOne < 0)
					{
						correctedYTwo = -(Math.log10(Math.abs(yTwo)+.12) * 400 + 370);
					} else
					{
						correctedYTwo = 0;
					}
					*/
					
					if (correctedYOne > 20 || correctedYTwo > 20 || correctedYOne < -20 || correctedYTwo < -20)
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

				System.out.println("Potentiometer " + SensorConfig.getInstance().getShooterPot().getCount());
				/*
				 * System.out.println(correctedYOne + " " + correctedYTwo);
				 * 
				 * SmartDashboard.putNumber("PID Error Left",
				 * ActuatorConfig.getInstance().getLeftEncoder().getError());
				 * SmartDashboard.putNumber("PID Error Right",
				 * ActuatorConfig.getInstance().getRightEncoder().getError());
				 * 
				 * SmartDashboard.putNumber("Left Encoder Value",
				 * ActuatorConfig.getInstance().getLeftEncoder().getCount());
				 * SmartDashboard.putNumber("Right Encoder Value",
				 * ActuatorConfig.getInstance().getRightEncoder().getCount());
				 */

				SmartDashboard.putNumber("Angle", SensorConfig.getInstance().getGyro().getCount());
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
				while (gamepad.getButtonValue(ButtonGamepad.TWO))
				{
					launcher.moveShooterDown();
				}

				while (gamepad.getButtonValue(ButtonGamepad.FOUR))
				{
					launcher.moveShooterUp();
				}

				launcher.stopShooterLifter();

				while (gamepad.getButtonValue(ButtonGamepad.ONE))
				{
					launcher.spinShooterUp();
				}

				while (gamepad.getButtonValue(ButtonGamepad.THREE))
				{
					launcher.intakeBoulder();
				}

				if (gamepad.getButtonValue(ButtonGamepad.SIX))
				{
					launcher.launchBoulder();
				}

				launcher.stopShooterWheels();

				// System.out.println(SensorConfig.getInstance().getShooterPot().getCount()
				// + " Potentiometer");
			}
		});

		/*
		 * executor.submit(() -> { ILogger joyLogger, gamepadLogger;
		 * 
		 * joyLogger = new
		 * JoystickLogger(SensorConfig.getInstance().getLeftJoystick(),
		 * SensorConfig.getInstance().getRightJoystick()); gamepadLogger = new
		 * GamepadLogger(SensorConfig.getInstance().getGamepad());
		 * 
		 * while(RobotStatus.isRunning()) {
		 * joyLogger.reportInformation(OutputDevice.SMARTDASHBOARD);
		 * gamepadLogger.reportInformation(OutputDevice.SMARTDASHBOARD); } });
		 */

	}
}
