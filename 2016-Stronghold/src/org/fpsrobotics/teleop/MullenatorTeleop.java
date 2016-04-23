package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.IDriveTrain;
import org.fpsrobotics.actuators.ILauncher;
import org.fpsrobotics.autonomous.IDriveTrainAssist;
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

	// Driver Functions
	private double correctedYLeft, correctedYRight;
	private boolean pidOn = false;
	private boolean deadZoned = false;
	private double speedMultiplier = 1.0;
	private boolean toggleLockL5A = false;
	private boolean toggleLockL5B = true;
	private boolean autoGyroDriveActivated = false;

	// Manual Shooter, Lifter, and Auger
	private boolean shootingLockOut = false;
	private boolean driverLockOut = false;
	private boolean movedShooter = false;
	private boolean movedAuger = false;
	private boolean movedIntakeWheels = false;

	// Instances
	private ILauncher launcher;
	private IGamepad gamepad;
	private IGamepad easyButton;
	private IDriveTrain driveTrain;
	private IDriveTrainAssist driveTrainAssist;
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;

	// Auto Teleop
	private boolean doneWithPortcullisAutoAction = false;

	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(3);
		// executor = Executors.newFixedThreadPool(7); //For Button Eleven

		// Instances
		launcher = ActuatorConfig.getInstance().getLauncher();
		gamepad = SensorConfig.getInstance().getGamepad();
		easyButton = SensorConfig.getInstance().getEasyButton();
		driveTrain = ActuatorConfig.getInstance().getDriveTrain();
		driveTrainAssist = ActuatorConfig.getInstance().getDriveTrainAssist();
		leftJoystick = SensorConfig.getInstance().getLeftJoystick();
		rightJoystick = SensorConfig.getInstance().getRightJoystick();
	}

	@Override
	public void doTeleop()
	{

		// DRIVE THREAD
		executor.submit(() ->
		{
			// Default PID To Off
			driveTrain.enablePID();
			driveTrain.setControlMode(TalonControlMode.Speed);
			driveTrain.disablePID();
			PIDOverride.getInstance().setTeleopDisablePID(true);

			while (RobotStatus.isRunning())
			{
				if (RobotStatus.isTeleop())
				{
					if (!driverLockOut)
					{
						// Drive Train Loop
						doDriverFunctions();
					}
				}
				if (rightJoystick.getButtonValue(EJoystickButtons.SEVEN) && rightJoystick.getButtonValue(EJoystickButtons.EIGHT))
				{
					launcher.setLauncherAndShooterOverride(true);
					driveTrain.setDriveForwardBreak(true);
					driveTrainAssist.setDriveForwardBreak(true);
				}

				// PRINT SMARTDASHBAORD VALUES
				printToSmartDashboard();

				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
			}
		});

		// MANUAL COMMANDS
		executor.submit(() ->
		{
			while (RobotStatus.isRunning())
			{
				if (RobotStatus.isTeleop())
				{
					manualCommands();
				}
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
			}

		});

		// AUTOMATIC COMMANDS
		executor.submit(() ->
		{

			while (RobotStatus.isRunning())
			{
				if (RobotStatus.isTeleop())
				{
					// Intake
					if (gamepad.getButtonValue(EJoystickButtons.THREE) && !movedIntakeWheels && !shootingLockOut)
					{
						System.out.println("Intake Start");
						shootingLockOut = true;
						movedIntakeWheels = true;
						launcher.intakeBoulder();
					}
					if (!gamepad.getButtonValue(EJoystickButtons.THREE) && movedIntakeWheels)
					{
						System.out.println("Intake Stop");
						launcher.stopIntakeBoulder();
						movedIntakeWheels = false;
					}

					// Shoot Low
					if ((gamepad.getButtonValue(EJoystickButtons.ONE) && gamepad.getButtonValue(EJoystickButtons.SEVEN))
							|| ((gamepad.getButtonValue(EJoystickButtons.ONE) && gamepad.getButtonValue(EJoystickButtons.EIGHT)) && !shootingLockOut))
					{
						System.out.println("Shoot Sequence Low Start");
						shootingLockOut = true;
						launcher.shootSequenceLow();
						System.out.println("Shoot Sequence Low Stop");
					}

					// Low bar
					if ((rightJoystick.getButtonValue(EJoystickButtons.THREE) || leftJoystick.getButtonValue(EJoystickButtons.SEVEN)) && !shootingLockOut)
					{
						shootingLockOut = true;
						System.out.println("Low Bar Start");
						launcher.moveShooterAndAugerToPreset(EShooterPresets.LOW_BAR, EAugerPresets.LOW_BAR);
						// launcher.moveShooterToPreset(EShooterPresets.LOW_BAR);
						// launcher.moveAugerToPreset(EAugerPresets.LOW_BAR);
						System.out.println("Low Bar Stop");
					}

					// Any Normal Defense
					if ((rightJoystick.getButtonValue(EJoystickButtons.FOUR) || leftJoystick.getButtonValue(EJoystickButtons.EIGHT)) && !shootingLockOut)
					{
						shootingLockOut = true;
						System.out.println("Standard Defense Start");
						launcher.moveShooterAndAugerToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER, EAugerPresets.STANDARD_DEFENSE_AUGER);
						// launcher.moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
						// launcher.moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);
						System.out.println("Standard Defense Stop");
					}

					// Auto Cheval De Frise
					// This is not really a while statement -> used to be able
					// to "break" and not continue the command
					while (leftJoystick.getButtonValue(EJoystickButtons.NINE) && !shootingLockOut)
					{
						shootingLockOut = true;
						driverLockOut = true;

						System.out.println("Cheval De Fris Action Start");

						if (rightJoystick.getButtonValue(EJoystickButtons.SEVEN) && rightJoystick.getButtonValue(EJoystickButtons.EIGHT))
						{
							System.out.println("Cheval De Fris Action Stop");
							break;
						}

						driveTrain.goBackward(0.25, 6);

						if (rightJoystick.getButtonValue(EJoystickButtons.SEVEN) && rightJoystick.getButtonValue(EJoystickButtons.EIGHT))
						{
							System.out.println("Cheval De Fris Action Stop");
							break;
						}

						driveTrain.stopDrive();
						SensorConfig.getInstance().getTimer().waitTimeInMillis(250);

						if (rightJoystick.getButtonValue(EJoystickButtons.SEVEN) && rightJoystick.getButtonValue(EJoystickButtons.EIGHT))
						{
							System.out.println("Cheval De Fris Action Stop");
							break;
						}

						launcher.moveShooterAndAugerToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER, EAugerPresets.LOW_BAR);
						// launcher.moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
						// launcher.moveAugerToPreset(EAugerPresets.LOW_BAR);

						if (rightJoystick.getButtonValue(EJoystickButtons.SEVEN) && rightJoystick.getButtonValue(EJoystickButtons.EIGHT))
						{
							System.out.println("Cheval De Fris Action Stop");
							break;
						}

						driveTrain.goForward(1.0, 80);
						System.out.println("Cheval De Fris Action Stop");
						break; // DO NOT DELETE
					}

					// // TODO: PORTCULLIS-ACTION
					// while (leftJoystick.getButtonValue(EJoystickButtons.TEN) && !shootingLockOut)
					// {
					//
					// doneWithPortcullisAutoAction = false;
					//
					// // go backwards
					// driveTrain.goBackward(0.25, 4);
					//
					// if (SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.SEVEN)
					// && SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.EIGHT))
					// {
					// break;
					// }
					//
					// // stop
					// driveTrain.stopDrive();
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
					//
					// if (SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.SEVEN)
					// && SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.EIGHT))
					// {
					// break;
					// }
					// // drive forward
					// executor.submit(() ->
					// {
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
					// driveTrain.goForward(0.25, 40, false); // 0.3
					// driveTrain.goForward(0.7, 40, false);
					// doneWithPortcullisAutoAction = true;
					// });
					//
					// // auger raise
					// launcher.moveAugerToPreset(EAugerPresets.PORTCULLIS);
					//
					// while (!doneWithPortcullisAutoAction && !(SensorConfig.getInstance().getRightJoystick()
					// .getButtonValue(EJoystickButtons.SEVEN)
					// && SensorConfig.getInstance().getRightJoystick()
					// .getButtonValue(EJoystickButtons.EIGHT)))
					// {
					// System.out.println("Going through Portcullis");
					// }
					// break; // DO NOT DELETE
					// }

					// Center Shot preset
					if (gamepad.getButtonValue(EJoystickButtons.SEVEN) && !shootingLockOut)
					{
						shootingLockOut = true;
						System.out.println("Center Shot Start");
						if (RobotStatus.isAlpha())
						{
							// To raise shooter, lower values
							launcher.moveShooterToPosition(600); // was 640
							// top is 588
						} else
						{
							// Beta
							launcher.moveShooterToPosition(290);
						}
						launcher.shootSequenceHigh();
						System.out.println("Center Shot Stop");
					}

					if (gamepad.getPOV() == 90 && gamepad.getButtonValue(EJoystickButtons.ONE) && !shootingLockOut)
					{
						shootingLockOut = true;
						System.out.println("Shoot High Manual Start");
						launcher.shootSequenceHigh();
						System.out.println("Shoot High Manual Stop");
					}

					// Side/ corner shot preset
					if (gamepad.getButtonValue(EJoystickButtons.EIGHT) && !shootingLockOut)
					{
						shootingLockOut = true;
						System.out.println("Corner Shot Start");
						if (RobotStatus.isAlpha())
						{
							launcher.moveShooterToPosition(635);
						} else
						{
							// Beta
							launcher.moveShooterToPosition(285);
						}
						launcher.shootSequenceHigh();
						System.out.println("Corner Shot Stop");
					}

					// Center the drive train
					if (leftJoystick.getButtonValue(EJoystickButtons.ELEVEN) || rightJoystick.getButtonValue(EJoystickButtons.FIVE))
					{
						driverLockOut = true;
						System.out.println("Centering Drive Start");
						driveTrainAssist.centerDriveTrain(0.4);
						System.out.println("Centering Drive Stop");
					}

					if (RobotStatus.isAlpha())
					{
						if (gamepad.getButtonValue(EJoystickButtons.NINE) && !shootingLockOut)
						{
							shootingLockOut = true;
							System.out.println("Lifter Extending");
							// launcher.moveAugerToPreset(EAugerPresets.TOP_LIMIT);
							ActuatorConfig.getInstance().getLifter().lift();
						}

						// TODO: Easy Button
						if ((gamepad.getButtonValue(EJoystickButtons.TEN) || easyButton.getButtonValue(EJoystickButtons.ONE)) && !shootingLockOut)
						// if (gamepad.getButtonValue(EJoystickButtons.TEN) &&
						// isAugerReadyToLift && !shootingLockOut)
						{
							shootingLockOut = true;
							System.out.println("Lifter Retracting");

							ActuatorConfig.getInstance().getLifter().retract();
							SensorConfig.getInstance().getTimer().waitTimeInMillis(1750); // 1750

							launcher.moveAugerToPosition(1587, 1.0);
							launcher.moveAugerToPosition(1175, 1.0);
							launcher.moveAugerToPosition(762, 1.0);
							launcher.moveAugerToPosition(350, 1.0);

							System.out.println("Lifted");

							// launcher.moveAugerToPreset(EAugerPresets.END_GAME);
						}
					}

					// TODO: BUTTON ELEVEN
					// if (rightJoystick.getButtonValue(EJoystickButtons.ELEVEN)
					// && leftJoystick.getButtonValue(EJoystickButtons.ELEVEN)
					// && gamepad.getButtonValue(EJoystickButtons.ELEVEN))
					// {
					// executor.submit(() ->
					// {
					// while (true)
					// {
					// System.out.println("BUTTON ELEVEN");
					// launcher.moveAugerToPreset(EAugerPresets.LOW_BAR);
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(3000);
					// launcher.moveAugerToPreset(EAugerPresets.TOP_LIMIT);
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(3000);
					// }
					// });
					// executor.submit(() ->
					// {
					// while (true)
					// {
					// launcher.moveShooterToPreset(EShooterPresets.INTAKE);
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(3000);
					// launcher
					// .moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(3000);
					// }
					// });
					// executor.submit(() ->
					// {
					// while (true)
					// {
					// launcher.shootSequenceHigh();
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(3000);
					// launcher.intakeBoulder();
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(5000);
					// launcher.stopIntakeBoulder();
					// SensorConfig.getInstance().getTimer().waitTimeInMillis(2000);
					// }
					// });
					// while (true)
					// {
					// driveTrain.turnRight(0.8);
					// SensorConfig.getInstance().getTimer().waitTimeInSeconds(10);
					// driveTrain.turnLeft(0.8);
					// SensorConfig.getInstance().getTimer().waitTimeInSeconds(10);
					// }
					// }

					// Once commands finish, if all buttons are not pressed,
					// then we may continue
					if (!gamepad.getButtonValue(EJoystickButtons.THREE) && !gamepad.getButtonValue(EJoystickButtons.SEVEN)
							&& !gamepad.getButtonValue(EJoystickButtons.EIGHT) && !gamepad.getButtonValue(EJoystickButtons.NINE)
							&& !gamepad.getButtonValue(EJoystickButtons.TEN) && !rightJoystick.getButtonValue(EJoystickButtons.THREE)
							&& !rightJoystick.getButtonValue(EJoystickButtons.FOUR) && !rightJoystick.getButtonValue(EJoystickButtons.FIVE)
							&& !rightJoystick.getButtonValue(EJoystickButtons.SEVEN) && !rightJoystick.getButtonValue(EJoystickButtons.EIGHT)
							&& !rightJoystick.getButtonValue(EJoystickButtons.NINE) && !rightJoystick.getButtonValue(EJoystickButtons.TEN)
							&& !leftJoystick.getButtonValue(EJoystickButtons.SEVEN) && !leftJoystick.getButtonValue(EJoystickButtons.EIGHT)
							&& !leftJoystick.getButtonValue(EJoystickButtons.NINE) && !leftJoystick.getButtonValue(EJoystickButtons.TEN)
							&& !leftJoystick.getButtonValue(EJoystickButtons.ELEVEN) && !leftJoystick.getButtonValue(EJoystickButtons.TWELVE))
					{
						if (shootingLockOut)
						{
							System.out.println("System Reset");
						}
						launcher.setLauncherAndShooterOverride(false);
						driveTrain.setDriveForwardBreak(false);
						driveTrainAssist.setDriveForwardBreak(false);
						shootingLockOut = false;
						driverLockOut = false;
					}
				}

				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
			}
		});

	}

	private void doDriverFunctions()
	{
		// TOGGLE PID
		if ((SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && !toggleLockL5A && !toggleLockL5B)
		{
			toggleLockL5A = true;
			// DO 1 - On Click
			driveTrain.disablePID();
			PIDOverride.getInstance().setTeleopDisablePID(true);
			pidOn = false;
		}
		if ((!SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && toggleLockL5A && !toggleLockL5B)
		{
			toggleLockL5A = false;
			toggleLockL5B = true;
			// DO 2 - On Release
		}
		if ((SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && !toggleLockL5A && toggleLockL5B)
		{
			toggleLockL5A = true;
			// DO 3 - On Click
			driveTrain.enablePID();
			driveTrain.setControlMode(TalonControlMode.Speed);
			PIDOverride.getInstance().setTeleopDisablePID(false);
			pidOn = true;
		}
		if ((!SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && toggleLockL5A && toggleLockL5B)
		{
			toggleLockL5A = false;
			toggleLockL5B = false;
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

				if (rightJoystick.getButtonValue(EJoystickButtons.ONE))
				{
					if (rightJoystick.getButtonValue(EJoystickButtons.TWO))
					{
						if (rightJoystick.getY() > 0)
						{
							driveTrain.driveStraight((4 / Math.PI) * 400); // Go
																			// Forwards
						} else
						{
							driveTrain.driveStraight(-(4 / Math.PI) * 400); // Go
																			// Backwards
						}
					} else
					{
						driveTrain.setSpeed(correctedYRight * speedMultiplier);
					}
					SmartDashboard.putBoolean("DRIVE TOGETHER", true);
				} else
				{
					driveTrain.setSpeed(correctedYLeft * speedMultiplier, correctedYRight * speedMultiplier);
					SmartDashboard.putBoolean("DRIVE TOGETHER", false);
				}

				deadZoned = false;
			} else
			{
				if (!deadZoned)
				{
					driveTrain.stopDrive();
					deadZoned = true;
				} else
				{
					// don't do anything
				}
			}

			// WITHOUT PID
		} else
		{

			if (rightJoystick.getButtonValue(EJoystickButtons.ONE))
			{
				if (!autoGyroDriveActivated)
				{
					driveTrain.disablePID();

					SensorConfig.getInstance().getGyro().softResetCount();

					autoGyroDriveActivated = true;
				}

				if (rightJoystick.getButtonValue(EJoystickButtons.TWO))
				{
					if (SensorConfig.getInstance().getRightJoystick().getY() > 0)
					{
						driveTrain.driveStraight(1.0); // Go
														// Forwards
					} else
					{
						driveTrain.driveStraight(-1.0); // Go
														// Backwards
					}
				} else
				{
					driveTrain.driveStraight(SensorConfig.getInstance().getRightJoystick().getY());
				}
				SmartDashboard.putBoolean("DRIVE TOGETHER", true);
			} else
			{
				autoGyroDriveActivated = false;

				driveTrain.setSpeed(SensorConfig.getInstance().getLeftJoystick().getY() * speedMultiplier,
						SensorConfig.getInstance().getRightJoystick().getY() * speedMultiplier);
				SmartDashboard.putBoolean("DRIVE TOGETHER", false);
			}
		}

		if (leftJoystick.getButtonValue(EJoystickButtons.ONE))
		{
			speedMultiplier = 0.5;
			SmartDashboard.putBoolean("DRIVE BY HALF", true);
		} else
		{
			speedMultiplier = 1.0;
			SmartDashboard.putBoolean("DRIVE BY HALF", false);
		}
	}

	public void manualCommands()
	{
		if (!shootingLockOut)
		{
			// Manual Shooter
			if (gamepad.getButtonValue(EJoystickButtons.ONE))
			{
				// Shooter move half
				if (gamepad.getButtonValue(EJoystickButtons.TWO))
				{
					launcher.lowerShooter(true);
					movedShooter = true;
				} else if (gamepad.getButtonValue(EJoystickButtons.FOUR))
				{
					launcher.raiseShooter(true);
					movedShooter = true;
				} else if (movedShooter)
				{
					launcher.stopShooterLifter();
					movedShooter = false;
				}
			} else
			{
				// Shooter move Regular
				if (gamepad.getButtonValue(EJoystickButtons.TWO))
				{
					launcher.lowerShooter(false);
					movedShooter = true;
				} else if (gamepad.getButtonValue(EJoystickButtons.FOUR))
				{
					launcher.raiseShooter(false);
					movedShooter = true;
				} else if (movedShooter)
				{
					launcher.stopShooterLifter();
					movedShooter = false;
				}
			}
			if (gamepad.getButtonValue(EJoystickButtons.ONE) || gamepad.getButtonValue(EJoystickButtons.TWELVE))
			{
				// Auger move fast
				if (gamepad.getButtonValue(EJoystickButtons.FIVE))
				{
					launcher.lowerAugerForEndGame();
					movedAuger = true;
				} else if (gamepad.getButtonValue(EJoystickButtons.SIX))
				{
					launcher.raiseAugerForEndGame();
					movedAuger = true;
				} else if (movedAuger)
				{
					launcher.stopAugerLifter(true);
					movedAuger = false;
				}
			} else
			{

				// Auger move regular
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
			}

			// Reset Gyro
			if ((rightJoystick.getButtonValue(EJoystickButtons.ELEVEN) && rightJoystick.getButtonValue(EJoystickButtons.TWELVE)))
			{
				SensorConfig.getInstance().getGyro().hardResetCount();
			}
		}
	}

	@Override
	public void printToSmartDashboard()
	{
		SmartDashboard.putNumber("Shooter Pot", SensorConfig.getInstance().getShooterPot().getCount());

		SmartDashboard.putBoolean("Bottom Limit Shooter", SensorConfig.getInstance().getShooterBottomLimitSwitch().isHit());
		SmartDashboard.putBoolean("Top Limit Shooter", SensorConfig.getInstance().getShooterTopLimitSwitch().isHit());

		// Pressure sensor feedback
		SmartDashboard.putBoolean("Pressure", SensorConfig.getInstance().getPressureSwitch().isHit());

		// Auger Pot Value
		SmartDashboard.putNumber("Auger Pot", ActuatorConfig.getInstance().getAugerPotentiometer().getCount());

		// Compass
		SmartDashboard.putNumber("Soft Count", SensorConfig.getInstance().getGyro().getSoftCount());
		SmartDashboard.putNumber("Hard Count", SensorConfig.getInstance().getGyro().getHardCount());

		// Manual Lock Out
		SmartDashboard.putBoolean("Shooter Lock out", shootingLockOut);
		SmartDashboard.putBoolean("Driver Lock out", driverLockOut);

		if ((-10 < SensorConfig.getInstance().getGyro().getSoftCount()) && ((SensorConfig.getInstance().getGyro().getSoftCount() < 10)))
		{
			SmartDashboard.putBoolean("Are we Straight?", true);
		} else
		{
			SmartDashboard.putBoolean("Are we Straight?", false);
		}
	}

}
