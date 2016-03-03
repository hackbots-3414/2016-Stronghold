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
		SmartDashboard.putNumber("Shoot Speed", 0.8);
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
			boolean deadZoned = false;

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

					while (SensorConfig.getInstance().getRightJoystick().getButtonValue(ButtonJoystick.FIVE));
				}

				SmartDashboard.putBoolean("PID", pidOn);

				if (pidOn)
				{
					yOne = SensorConfig.getInstance().getRightJoystick().getY();
					yTwo = SensorConfig.getInstance().getLeftJoystick().getY();

					/*
					 * Linear Drive Control correctedYOne = yOne * 400;
					 * correctedYTwo = yTwo * 400;
					 */

					/* Inverse Tangent Drive Control */
					correctedYOne = Math.atan(yOne) * (4 / Math.PI) * 400;
					correctedYTwo = Math.atan(yTwo) * (4 / Math.PI) * 400;

					if (correctedYOne > 25 || correctedYOne < -25 || correctedYTwo > 25 || correctedYTwo < -25)
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYOne, correctedYTwo);
						deadZoned = false;
					} else
					{
						if(!deadZoned)
						{
							ActuatorConfig.getInstance().getDriveTrain().setSpeed(0, 0);
							deadZoned = true;
						} else
						{
							// don't do anything
						}
					}

				} else
				{
					correctedYOne = SensorConfig.getInstance().getRightJoystick().getY();
					correctedYTwo = SensorConfig.getInstance().getLeftJoystick().getY();

					if (correctedYOne > 0.1 || correctedYTwo > 0.1 || correctedYOne < -0.1 || correctedYTwo < -0.1)
					{
						try
						{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYOne, correctedYTwo);
						} catch(Exception e)
						{
							System.out.println("No encoder?");
							pidOn = false;
						}
					} else
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(0, 0);
					}

				}

				/*
				 * System.out.println("rate: " +
				 * ActuatorConfig.getInstance().getRightEncoder().getRate() +
				 * " " +
				 * ActuatorConfig.getInstance().getLeftEncoder().getRate());
				 * System.out.println("error: " +
				 * ActuatorConfig.getInstance().getRightEncoder().getError() +
				 * " " +
				 * ActuatorConfig.getInstance().getLeftEncoder().getError());
				 */

				//System.out.println("Potentiometer " + SensorConfig.getInstance().getShooterPot().getCount());

				//System.out.println("Auger Encoder " + ActuatorConfig.getInstance().getAugerEncoder().getCount());
				//System.out.println("Auger Encoder Rate " + ActuatorConfig.getInstance().getAugerEncoder().getRate());
				//System.out.println("Auger Encoder Error " + ActuatorConfig.getInstance().getAugerEncoder().getError());
				//System.out.println("Auger Limits " + SensorConfig.getInstance().getAugerBottomLimitSwitch().getValue() + SensorConfig.getInstance().getAugerTopLimitSwitch().getValue());
				//System.out.println("Shooter Limits " + SensorConfig.getInstance().getBottomLimitSwitch().getValue() + " " + SensorConfig.getInstance().getTopLimitSwitch().getValue());
				
				
				SmartDashboard.putNumber("Shooter Pot", SensorConfig.getInstance().getShooterPot().getCount());
				
				SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
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
				while (gamepad.getButtonValue(ButtonGamepad.ONE))
				{
					launcher.lowerAuger();
				}

				while (gamepad.getButtonValue(ButtonGamepad.SIX))
				{
					launcher.raiseAuger();
				}

				launcher.stopAugerLifter();

				/* Manual Launching
				while (gamepad.getButtonValue(ButtonGamepad.ONE))
				{
					launcher.spinShooterUp();

					if (gamepad.getButtonValue(ButtonGamepad.SIX))
					{
						launcher.launchBoulder();

						while (gamepad.getButtonValue(ButtonGamepad.SIX) || gamepad.getButtonValue(ButtonGamepad.ONE));
					}
				}
				*/
				
				if (gamepad.getButtonValue(ButtonGamepad.SEVEN))
				{
					launcher.shootSequence();

					while (gamepad.getButtonValue(ButtonGamepad.SEVEN));
				}
				
				if (gamepad.getButtonValue(ButtonGamepad.EIGHT))
				{
					launcher.shootSequence(SmartDashboard.getNumber("Shoot Speed", 0.85));

					while (gamepad.getButtonValue(ButtonGamepad.EIGHT));
				}

				while (gamepad.getButtonValue(ButtonGamepad.THREE))
				{
					launcher.intakeBoulder();
				}

				launcher.stopShooterWheels();

				while (gamepad.getButtonValue(ButtonGamepad.FIVE))
				{
					//launcher.moveShooterToPosition(SmartDashboard.getNumber("Preset", 477));
					
					launcher.moveShooterToBottomLimit();
					launcher.augerGoToPosition(1000);
				}

				launcher.stopShooterLifter();

				while (gamepad.getButtonValue(ButtonGamepad.NINE))
				{
					launcher.spinAugerUp();
				}
				
				launcher.stopAuger();
				
				if (SensorConfig.getInstance().getPressureSwitch().getValue())
				{
					SmartDashboard.putBoolean("Pressure", true);
				} else
				{
					SmartDashboard.putBoolean("Pressure", false);
				}
				
				SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
			}
		});
	}
}
