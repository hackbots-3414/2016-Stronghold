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

			// Drive Train Loop
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
						SmartDashboard.putNumber("Left Drive", correctedYOne);
						SmartDashboard.putNumber("Right Drive", correctedYTwo);
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYOne, correctedYTwo);
						deadZoned = false;
					} else
					{
						if(!deadZoned)
						{
							SmartDashboard.putNumber("Left Drive", 0.0);
							SmartDashboard.putNumber("Right Drive", 0.0);
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
							SmartDashboard.putNumber("Left Drive", correctedYOne);
							SmartDashboard.putNumber("Right Drive", correctedYTwo);
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
				
				//System.out.println(SensorConfig.getInstance().getShooterPot().getCount());
				
				SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
			}
		});

		
		// Shooter Loop
		executor.submit(() ->
		{
			ILauncher launcher;
			IGamepad gamepad;

			launcher = ActuatorConfig.getInstance().getLauncher();
			gamepad = SensorConfig.getInstance().getGamepad();
			
			boolean movedShooter = true;
			boolean movedAuger = true;
			boolean movedShooterWheels = true;
			boolean movedAugerWheels = true;

			while (RobotStatus.isRunning())
			{
				// Shooter movement controls
				while (gamepad.getButtonValue(ButtonGamepad.TWO))
				{
					launcher.moveShooterDown();
					movedShooter = true;
				}

				while (gamepad.getButtonValue(ButtonGamepad.FOUR))
				{
					launcher.moveShooterUp();
					movedShooter = true;
				}
				
//				while (gamepad.getButtonValue(ButtonGamepad.FIVE))
//				{
//					//launcher.moveShooterToPosition(SmartDashboard.getNumber("Preset", 477));
//					
//					launcher.moveShooterToBottomLimit();
//					launcher.augerGoToPosition(1000);
//					
//					movedShooter = true;
//				}

				if(movedShooter)
				launcher.stopShooterLifter();

				// Auger movement commands
				while (gamepad.getButtonValue(ButtonGamepad.NINE))
				{
					launcher.stopShooterLifter();
					movedShooter = false;
				}
/*
				// Auger movement controls
				
				while (gamepad.getButtonValue(ButtonGamepad.TEN))
				{
					launcher.raiseAuger();
				}
				
				launcher.stopAuger();
				
				// Launching commands
				while (gamepad.getButtonValue(ButtonGamepad.ONE))
				{
					launcher.lowerAuger();
					movedAuger = true;
				}

				while (gamepad.getButtonValue(ButtonGamepad.SIX))
				{
					launcher.raiseAuger();
					movedAuger = true;
				}

				if(movedAuger)
				{
					launcher.stopAugerLifter();
					movedAuger = false;
				}
				
				// Auger wheel controls
				while (gamepad.getButtonValue(ButtonGamepad.NINE))
				{
					launcher.spinAugerUp();
					
					movedAugerWheels = true;
				}
				
				if(movedAugerWheels)
				{
					launcher.stopAuger();
					movedAugerWheels = false;
				} 
				*/
				
				// Shooter launching controls
				if (gamepad.getButtonValue(ButtonGamepad.SEVEN))
				{
					launcher.shootSequence();

					while (gamepad.getButtonValue(ButtonGamepad.SEVEN));
					
					movedShooterWheels = true;
				}
				
				if (gamepad.getButtonValue(ButtonGamepad.EIGHT))
				{
					launcher.shootSequence(SmartDashboard.getNumber("Shoot Speed", 0.85));

					while (gamepad.getButtonValue(ButtonGamepad.EIGHT));
					
					movedShooterWheels = true;
				}

				while (gamepad.getButtonValue(ButtonGamepad.THREE))
				{
					launcher.intakeBoulder();
					
					movedShooterWheels = true;
				}

				if(movedShooterWheels)
				{
					launcher.stopShooterWheels();
					movedShooterWheels = false;
				}
				
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
				
				// Pressure sensor feedback
				if (SensorConfig.getInstance().getPressureSwitch().getValue())
				{
					SmartDashboard.putBoolean("Pressure", true);
				} else
				{
					SmartDashboard.putBoolean("Pressure", false);
				}
				
				/*
				if(ActuatorConfig.getInstance().getDriveTrainAssist().shouldShooterBeRaised())
				{
					ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(1100);
				}
				*/
				
				SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
			}
		});
	}
}
