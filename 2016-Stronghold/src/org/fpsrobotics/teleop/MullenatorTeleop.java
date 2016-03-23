package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.ILauncher;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.IGamepad;
import org.fpsrobotics.sensors.IJoystick;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MullenatorTeleop implements ITeleopControl
{
	private ExecutorService executor;
	private boolean shootingLockOut = false;

	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(2);
	}

	@Override
	public void doTeleop()
	{
		/*
		 * 
		 * Gamepad
		 * 
		 * 2 Shooter Down; 3 Suck Ball In; 4 Shooter Up; 5 Auger Down; 6 Auger
		 * Up 7 Shoot High; 8 Shoot Low; 9 Lifter; 10 Lifter Retract
		 * 
		 * RightJoystick 1 Drive Together; 11 & 12 Reset Gyro
		 * 
		 * Left Joystick 1 Half Speed; 5 Toggle PID; 7 Low bar; 8 Rock Wall: 9
		 * Normal Defense: 10 Cheval
		 */

		ILauncher launcher = ActuatorConfig.getInstance().getLauncher();
		IGamepad gamepad = SensorConfig.getInstance().getGamepad();

		IJoystick leftJoystick = SensorConfig.getInstance().getLeftJoystick();
		IJoystick rightJoystick = SensorConfig.getInstance().getRightJoystick();

		executor.submit(() ->
		{

			ActuatorConfig.getInstance().getDriveTrain().enablePID();
			ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);

			double correctedYLeft, correctedYRight, yLeft, yRight;
			boolean pidOn = true;
			boolean deadZoned = false;

			double speedMultiplier = 1.0;
			boolean toggleLockA = false;
			boolean toggleLockB = false;

			boolean movedShooter = false;
			boolean movedAuger = false;
			boolean movedIntakeWheels = false;

			// Drive Train Loop
			while (RobotStatus.isRunning())
			{
				// TOGGLE PID
				if ((SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && !toggleLockA && !toggleLockB)
				{
					toggleLockA = true;
					// DO 1 - On Click
					ActuatorConfig.getInstance().getDriveTrain().disablePID();
					PIDOverride.getInstance().setTeleopDisablePID(true);
					pidOn = false;
				}
				if ((!SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && toggleLockA && !toggleLockB)
				{
					toggleLockA = false;
					toggleLockB = true;
					// DO 2 - On Release
				}
				if ((SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && !toggleLockA && toggleLockB)
				{
					toggleLockA = true;
					// DO 3 - On Click
					ActuatorConfig.getInstance().getDriveTrain().enablePID();
					ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);
					PIDOverride.getInstance().setTeleopDisablePID(false);
					pidOn = true;
				}
				if ((!SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && toggleLockA && toggleLockB)
				{
					toggleLockA = false;
					toggleLockB = false;
					// DO 4 - On Release
				}

				SmartDashboard.putBoolean("PID", pidOn);

				// WITH PID
				if (pidOn)
				{
					correctedYLeft = Math.atan(SensorConfig.getInstance().getLeftJoystick().getY()) * (4 / Math.PI) * 400;
					correctedYRight = Math.atan(SensorConfig.getInstance().getRightJoystick().getY()) * (4 / Math.PI) * 400;

					if (correctedYLeft > 25 || correctedYLeft < -25 || correctedYRight > 25 || correctedYRight < -25)
					{

						if (SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.ONE))
						{
							ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYRight * speedMultiplier);
							SmartDashboard.putBoolean("DRIVE TOGETHER", true);
						} else
						{
							ActuatorConfig.getInstance().getDriveTrain()
									.setSpeed(correctedYLeft * speedMultiplier, correctedYRight * speedMultiplier);
							SmartDashboard.putBoolean("DRIVE TOGETHER", false);
						}

						deadZoned = false;
					} else
					{
						if (!deadZoned)
						{
							ActuatorConfig.getInstance().getDriveTrain().setSpeed(0, 0);
							deadZoned = true;
						} else
						{
							// don't do anything
						}
					}

					// WITHOUT PID
				} else
				{

					if (SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.ONE))
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(SensorConfig.getInstance().getRightJoystick().getY() * speedMultiplier);
						SmartDashboard.putBoolean("DRIVE TOGETHER", true);
					} else
					{
						ActuatorConfig
								.getInstance()
								.getDriveTrain()
								.setSpeed(SensorConfig.getInstance().getLeftJoystick().getY() * speedMultiplier,
										SensorConfig.getInstance().getRightJoystick().getY() * speedMultiplier);
						SmartDashboard.putBoolean("DRIVE TOGETHER", false);
					}
				}

				if (SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.ONE))
				{
					speedMultiplier = 0.5;
					SmartDashboard.putBoolean("DRIVE BY HALF", true);
				} else
				{
					speedMultiplier = 1.0;
					SmartDashboard.putBoolean("DRIVE BY HALF", false);
				}

				// MANUAL COMMANDS
				if (!shootingLockOut)
				{
					// Manual Shooter
					if (gamepad.getButtonValue(EJoystickButtons.TWO))
					{
						launcher.lowerShooter();
						movedShooter = true;
					} else if (gamepad.getButtonValue(EJoystickButtons.FOUR))
					{
						launcher.raiseShooter();
						movedShooter = true;
					} else if (movedShooter)
					{
						launcher.stopShooterLifter();
						movedShooter = false;
					}

					// Manual Auger
					if (gamepad.getButtonValue(EJoystickButtons.FIVE))
					{
						launcher.lowerAuger();
						movedAuger = true;
					} else if (gamepad.getButtonValue(EJoystickButtons.SIX))
					{
						launcher.raiseAuger();
						movedAuger = true;
					} else if (movedAuger)
					{
						launcher.stopAugerLifter(true);
						movedAuger = false;
					}

					// Manual Intake
					if (gamepad.getButtonValue(EJoystickButtons.THREE))
					{
						launcher.intakeBoulder();
						movedIntakeWheels = true;
					} else if (movedIntakeWheels)
					{
						launcher.stopIntakeBoulder();
						movedIntakeWheels = false;
					}
					
					//Reset Gyro
					if ((rightJoystick.getButtonValue(EJoystickButtons.ELEVEN) && rightJoystick.getButtonValue(EJoystickButtons.TWELVE)))
					{
						SensorConfig.getInstance().getGyro().resetCount();
					}

				}

				// PRINT SMARTDASHBAORD VALUES
				SmartDashboard.putNumber("Shooter Pot", SensorConfig.getInstance().getShooterPot().getCount());

				SmartDashboard.putBoolean("Bottom Limit Shooter", SensorConfig.getInstance().getShooterBottomLimitSwitch().isHit());
				SmartDashboard.putBoolean("Top Limit Shooter", SensorConfig.getInstance().getShooterTopLimitSwitch().isHit());

				// Auger Current
				SmartDashboard.putNumber("Auger Current", SensorConfig.getInstance().getPdp().getCurrent(3));

				// Pressure sensor feedback
				if (SensorConfig.getInstance().getPressureSwitch().isHit())
				{
					SmartDashboard.putBoolean("Pressure", true);
				} else
				{
					SmartDashboard.putBoolean("Pressure", false);
				}

				// Auger Pot Value
				SmartDashboard.putNumber("Auger Pot", ActuatorConfig.getInstance().getAugerPotentiometer().getCount());

				// Should we raise value
				SmartDashboard.putBoolean("Should we raise", ActuatorConfig.getInstance().getDriveTrainAssist().shouldShooterBeRaised());

//				// should we auto raise auger
//				SmartDashboard.putBoolean("Auto Raise Auger", ActuatorConfig.getInstance().getDriveTrainAssist().isTilt());

				// gyro yaw
				SmartDashboard.putNumber("Yaw", SensorConfig.getInstance().getGyro().getCount());
				// TODO: Compass stuff

				SmartDashboard.putBoolean("Shooter Lock out", shootingLockOut);

				// Wait for Thread
				try
				{
					Thread.sleep(50);
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});

		executor.submit(() ->
		{
			ActuatorConfig.getInstance().getLifter().retract();
			ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);

			while (RobotStatus.isRunning())
			{

				// Shooter high
				if (gamepad.getButtonValue(EJoystickButtons.SEVEN) && !shootingLockOut)
				{
					shootingLockOut = true;
					launcher.shootSequenceHigh();
				}

				// Shooter low
				if (gamepad.getButtonValue(EJoystickButtons.EIGHT) && !shootingLockOut)
				{
					shootingLockOut = true;
					launcher.shootSequenceLow();
				}

				// Low bar
				if (leftJoystick.getButtonValue(EJoystickButtons.SEVEN) && !shootingLockOut)
				{
					shootingLockOut = true;
					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);
					ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
				}

				// Rock Wall
				if (leftJoystick.getButtonValue(EJoystickButtons.EIGHT) && ActuatorConfig.getInstance().getDriveTrainAssist().isTilt()
						&& !shootingLockOut)
				{
					ActuatorConfig.getInstance().getLauncher().lowerAugerToBottomLimit(0.9);
				}

				// Any normal defense: Rouch Terrain, Moat, Ramparts,
				// Drawbridge, Sally port
				if (leftJoystick.getButtonValue(EJoystickButtons.NINE) && !shootingLockOut)
				{
					shootingLockOut = true;
					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);
					ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
				}

				// Cheval de friz
				if (leftJoystick.getButtonValue(EJoystickButtons.TEN) && ActuatorConfig.getInstance().getDriveTrainAssist().isTiltGyro()
						&& !shootingLockOut)
				{
					shootingLockOut = true;
					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);
				}

//				// Portcullis
//				if (leftJoystick.getButtonValue(EJoystickButtons.ELEVEN) && !shootingLockOut)
//				{
//					shootingLockOut = true;
//					ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
//					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);
//					ActuatorConfig.getInstance().getDriveTrain().setSpeed(0.2, 0.2);
//					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.TOP_LIMIT);
//				}

//				// Lift Robot
//				if (gamepad.getButtonValue(EJoystickButtons.NINE) && !shootingLockOut)
//				{
//					shootingLockOut = true;
//					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.TOP_LIMIT);
//					ActuatorConfig.getInstance().getLifter().lift();
//				}
//
//				if (gamepad.getButtonValue(EJoystickButtons.TEN) && !shootingLockOut)
//				{
//					shootingLockOut = true;
//					ActuatorConfig.getInstance().getLifter().retract();
//				}

				if (!gamepad.getButtonValue(EJoystickButtons.SEVEN) && !gamepad.getButtonValue(EJoystickButtons.EIGHT)
						&& !gamepad.getButtonValue(EJoystickButtons.NINE) && !gamepad.getButtonValue(EJoystickButtons.TEN)
						&& !leftJoystick.getButtonValue(EJoystickButtons.EIGHT) && !leftJoystick.getButtonValue(EJoystickButtons.SEVEN)
						&& !leftJoystick.getButtonValue(EJoystickButtons.NINE) && !leftJoystick.getButtonValue(EJoystickButtons.TEN)
						&& !leftJoystick.getButtonValue(EJoystickButtons.ELEVEN) && shootingLockOut)
				{
					shootingLockOut = false;
				}

				// Wait for Thread
				try
				{
					Thread.sleep(50);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		// // Shooter Loop
		// executor.submit(() ->
		// {
		// boolean movedShooter = true;
		// boolean movedIntakeWheels = true;
		//
		// ActuatorConfig.getInstance().getLifter().retract();
		// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
		//
		// while (RobotStatus.isRunning())
		// {
		// // Shooter movement controls
		// if (gamepad.getButtonValue(EJoystickButtons.TWO))
		// {
		// launcher.lowerShooter();
		// movedShooter = true;
		// }
		//
		// else if (gamepad.getButtonValue(EJoystickButtons.FOUR))
		// {
		// launcher.raiseShooter();
		// movedShooter = true;
		// }
		//
		// // if (movedShooter)
		// else
		// {
		// launcher.stopShooterLifter();
		// movedShooter = false;
		// }
		//
		// // Shooter launching controls
		// if (gamepad.getButtonValue(EJoystickButtons.SEVEN))
		// {
		//
		// // SmartDashboard.putBoolean("Is Shooting High", true);
		// launcher.shootSequenceHigh();
		//
		// while (gamepad.getButtonValue(EJoystickButtons.SEVEN))
		// ;
		//
		// movedIntakeWheels = true;
		// // SmartDashboard.putBoolean("Is Shooting High", false);
		// }
		//
		// if (gamepad.getButtonValue(EJoystickButtons.EIGHT))
		// {
		// // SmartDashboard.putBoolean("Is Shooting Low", true);
		// launcher.shootSequenceLow();
		//
		// while (gamepad.getButtonValue(EJoystickButtons.EIGHT))
		// ;
		//
		// movedIntakeWheels = true;
		// // SmartDashboard.putBoolean("Is Shooting Low", false);
		// }
		//
		// while (gamepad.getButtonValue(EJoystickButtons.THREE))
		// {
		// launcher.intakeBoulder();
		// movedIntakeWheels = true;
		// }
		//
		// if (movedIntakeWheels)
		// {
		// launcher.stopIntakeBoulder();
		//
		// movedIntakeWheels = false;
		// }
		//
		// if(gamepad.getButtonValue(EJoystickButtons.ONE))
		// {
		// launcher.moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);
		// launcher.moveShooterToPreset(EShooterPresets.LOAD_BOULDER);
		// }
		//
		// // Low bar
		// if
		// (SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.SEVEN))
		// {
		// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);
		// ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
		// }
		//
		// //Rock Wall
		// if
		// (SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.EIGHT)
		// && ActuatorConfig.getInstance().getDriveTrainAssist().isTilt())
		// {
		// ActuatorConfig.getInstance().getLauncher().lowerAugerToBottomLimit(0.9);
		// }
		//
		// // Any normal defense
		// if
		// (SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.NINE))
		// {
		// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);
		// ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
		// }
		//
		// // Lift Robot
		// if (gamepad.getButtonValue(EJoystickButtons.NINE))
		// {
		// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.TOP_LIMIT);
		// ActuatorConfig.getInstance().getLifter().lift();
		// }
		//
		// if (gamepad.getButtonValue(EJoystickButtons.TEN))
		// {
		// ActuatorConfig.getInstance().getLifter().retract();
		// }
		//
		// // Print SmartDashboard values
		// SmartDashboard.putNumber("Shooter Pot",
		// SensorConfig.getInstance().getShooterPot().getCount());
		//
		// SmartDashboard.putBoolean("Bottom Limit Shooter",
		// SensorConfig.getInstance().getShooterBottomLimitSwitch().isHit());
		// SmartDashboard.putBoolean("Top Limit Shooter",
		// SensorConfig.getInstance().getShooterTopLimitSwitch().isHit());
		//
		// // Auger Current
		// SmartDashboard.putNumber("Auger Current",
		// SensorConfig.getInstance().getPdp().getCurrent(3));
		//
		// try
		// {
		// Thread.sleep(100);
		// } catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		//
		// }
		// });
		//
		// // Auger movement controls
		// executor.submit(() ->
		// {
		// boolean movedAuger = true;
		//
		// while (RobotStatus.isRunning())
		// {
		// if (gamepad.getButtonValue(EJoystickButtons.FIVE))
		// {
		// launcher.lowerAuger();
		// movedAuger = true;
		// } else if (gamepad.getButtonValue(EJoystickButtons.SIX))
		// {
		// launcher.raiseAuger();
		// movedAuger = true;
		// }
		//
		// else
		// {
		// launcher.stopAugerLifter(true);
		// movedAuger = false;
		// }
		//
		// // Pressure sensor feedback
		// if (SensorConfig.getInstance().getPressureSwitch().isHit())
		// {
		// SmartDashboard.putBoolean("Pressure", true);
		// } else
		// {
		// SmartDashboard.putBoolean("Pressure", false);
		// }
		//
		// // Auger Pot Value
		// SmartDashboard.putNumber("Auger Pot",
		// ActuatorConfig.getInstance().getAugerPotentiometer().getCount());
		//
		// // Should we raise value
		// SmartDashboard.putBoolean("Should we raise",
		// ActuatorConfig.getInstance().getDriveTrainAssist().shouldShooterBeRaised());
		//
		// // should we auto raise auger
		// SmartDashboard.putBoolean("Auto Raise Auger",
		// ActuatorConfig.getInstance().getDriveTrainAssist().isTilt());
		//
		// // gyro yaw
		// SmartDashboard.putNumber("Yaw",
		// SensorConfig.getInstance().getGyro().getCount());
		// try
		// {
		// Thread.sleep(100);
		// } catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		// }
		// });

		// executor.submit(() ->
		// {
		// boolean movedShooter = false;
		// boolean movedAuger = false;
		// boolean movedIntakeWheels = false;
		//
		// while (RobotStatus.isRunning())
		// {
		// // Manual Shooter
		// if (!shootingLockOut)
		// {
		// if (gamepad.getButtonValue(EJoystickButtons.TWO))
		// {
		// launcher.lowerShooter();
		// movedShooter = true;
		// } else if (gamepad.getButtonValue(EJoystickButtons.FOUR))
		// {
		// launcher.raiseShooter();
		// movedShooter = true;
		// } else if (movedShooter)
		// {
		// launcher.stopShooterLifter();
		// movedShooter = false;
		// }
		//
		// // Manual Auger
		// if (gamepad.getButtonValue(EJoystickButtons.FIVE))
		// {
		// launcher.lowerAuger();
		// movedAuger = true;
		// } else if (gamepad.getButtonValue(EJoystickButtons.SIX))
		// {
		// launcher.raiseAuger();
		// movedAuger = true;
		// } else if (movedAuger)
		// {
		// launcher.stopAugerLifter(true);
		// movedAuger = false;
		// }
		//
		// // Manual Intake
		// if (gamepad.getButtonValue(EJoystickButtons.THREE))
		// {
		// launcher.intakeBoulder();
		// movedIntakeWheels = true;
		// } else if (movedIntakeWheels)
		// {
		// launcher.stopAugerWheels();
		// launcher.stopShooterWheels();
		// movedIntakeWheels = false;
		// }
		//
		// // Print SmartDashboard values
		// SmartDashboard.putNumber("Shooter Pot",
		// SensorConfig.getInstance().getShooterPot().getCount());
		//
		// SmartDashboard.putBoolean("Bottom Limit Shooter",
		// SensorConfig.getInstance().getShooterBottomLimitSwitch().isHit());
		// SmartDashboard.putBoolean("Top Limit Shooter",
		// SensorConfig.getInstance().getShooterTopLimitSwitch().isHit());
		//
		// // Auger Current
		// SmartDashboard.putNumber("Auger Current",
		// SensorConfig.getInstance().getPdp().getCurrent(3));
		//
		// // Pressure sensor feedback
		// if (SensorConfig.getInstance().getPressureSwitch().isHit())
		// {
		// SmartDashboard.putBoolean("Pressure", true);
		// } else
		// {
		// SmartDashboard.putBoolean("Pressure", false);
		// }
		//
		// // Auger Pot Value
		// SmartDashboard.putNumber("Auger Pot",
		// ActuatorConfig.getInstance().getAugerPotentiometer().getCount());
		//
		// // Should we raise value
		// SmartDashboard.putBoolean("Should we raise",
		// ActuatorConfig.getInstance().getDriveTrainAssist().shouldShooterBeRaised());
		//
		// // should we auto raise auger
		// SmartDashboard.putBoolean("Auto Raise Auger",
		// ActuatorConfig.getInstance().getDriveTrainAssist().isTilt());
		//
		// // gyro yaw
		// SmartDashboard.putNumber("Yaw",
		// SensorConfig.getInstance().getGyro().getCount());
		//
		// }
		//
		// // Wait for Thread
		// try
		// {
		// Thread.sleep(100);
		// } catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		// }
		// });

	}
}
