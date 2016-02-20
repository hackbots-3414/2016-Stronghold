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
		executor = Executors.newFixedThreadPool(3);
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

					/* Linear Drive Control */
					correctedYOne = yOne * 400;
					correctedYTwo = yTwo * 400;

					/*
					 * Inverse Tangent Drive Control correctedYOne =
					 * Math.atan(yOne)*(4/Math.PI)*400; correctedYTwo =
					 * Math.atan(yTwo)*(4/Math.PI)*400;
					 */

					System.out.println(correctedYOne + " " + correctedYTwo);

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

				System.out.println("Potentiometer " + SensorConfig.getInstance().getShooterPot().getCount());

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

				while (gamepad.getButtonValue(ButtonGamepad.THREE))
				{
					launcher.intakeBoulder();
				}

				while (gamepad.getButtonValue(ButtonGamepad.ONE))
				{
					launcher.spinShooterUp();
				}

				if (gamepad.getButtonValue(ButtonGamepad.SEVEN))
				{
					launcher.shootSequence();
					
					while (gamepad.getButtonValue(ButtonGamepad.SEVEN));
				}

				launcher.stopShooterWheels();
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
				if (gamepad.getButtonValue(ButtonGamepad.SIX))
				{
					launcher.launchBoulder(); 
					
					while (gamepad.getButtonValue(ButtonGamepad.SIX));
				}
			}
			// System.out.println(SensorConfig.getInstance().getShooterPot().getCount()
			// + " Potentiometer");

		});

	}
}
