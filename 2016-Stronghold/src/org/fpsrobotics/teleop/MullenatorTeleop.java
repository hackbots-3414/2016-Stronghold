package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.IDriveTrain;
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

	// Driver Functions
	private double correctedYLeft, correctedYRight;
	private boolean pidOn = false;
	private boolean deadZoned = false;
	private double speedMultiplier = 1.0;
	private boolean toggleLockA = false;
	private boolean toggleLockB = true;
	private boolean autoGyroDriveActivated = false;

	// Manual Shooter, Lifter, and Auger
	private boolean shootingLockOut = false;
	private boolean movedShooter = false;
	private boolean movedAuger = false;
	private boolean movedIntakeWheels = false;

	// Lifter
	private boolean isAugerReadyToLift = false; // do not delete

	// Instances
	private ILauncher launcher;
	private IGamepad gamepad;
	private IDriveTrain driveTrain;
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;

	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(3);

		// Instances
		launcher = ActuatorConfig.getInstance().getLauncher();
		gamepad = SensorConfig.getInstance().getGamepad();
		driveTrain = ActuatorConfig.getInstance().getDriveTrain();
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
					// Drive Train Loop
					doDriverFunctions();
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
						shootingLockOut = true;
						movedIntakeWheels = true;
						launcher.intakeBoulder();
					}
					if (!gamepad.getButtonValue(EJoystickButtons.THREE) && movedIntakeWheels)
					{
						launcher.stopIntakeBoulder();
						movedIntakeWheels = false;
					}

					// Shoot Low
					if ((gamepad.getButtonValue(EJoystickButtons.ONE) && gamepad.getButtonValue(EJoystickButtons.SEVEN))
							|| ((gamepad.getButtonValue(EJoystickButtons.ONE)
									&& gamepad.getButtonValue(EJoystickButtons.EIGHT)) && !shootingLockOut))
					{
						shootingLockOut = true;
						launcher.shootSequenceLow();
					}

					// Low bar
					if ((rightJoystick.getButtonValue(EJoystickButtons.THREE)
							|| leftJoystick.getButtonValue(EJoystickButtons.SEVEN)) && !shootingLockOut)
					{
						shootingLockOut = true;

						launcher.moveShooterToPreset(EShooterPresets.LOW_BAR);
						launcher.moveAugerToPreset(EAugerPresets.LOW_BAR);
					}

					// Any Normal Defense
					if ((rightJoystick.getButtonValue(EJoystickButtons.FOUR)
							|| leftJoystick.getButtonValue(EJoystickButtons.EIGHT)) && !shootingLockOut)
					{
						shootingLockOut = true;

						launcher.moveShooterToPreset(EShooterPresets.STANDARD_DEFENSE_SHOOTER);
						launcher.moveAugerToPreset(EAugerPresets.STANDARD_DEFENSE_AUGER);
					}

					// TODO: Cheval Auto Activate
					// if (leftJoystick.getButtonValue(EJoystickButtons.NINE) && !shootingLockOut)
					// {
					// shootingLockOut = true;
					// doChevalAutoActivate(EJoystickButtons.NINE);
					// }

					// Center Shot preset
					if (gamepad.getButtonValue(EJoystickButtons.SEVEN) && !shootingLockOut)
					{
						shootingLockOut = true;
						if (RobotStatus.isAlpha())
						{
							// To raise shooter, lower values
							ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(680);
						} else
						{
							// Beta
							ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(290);
						}
						ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();
					}

					// Side/ corner shot preset
					if (gamepad.getButtonValue(EJoystickButtons.EIGHT) && !shootingLockOut)
					{
						shootingLockOut = true;
						if (RobotStatus.isAlpha())
						{
							ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(675);
						} else
						{
							// Beta
							ActuatorConfig.getInstance().getLauncher().moveShooterToPosition(285);
						}
						ActuatorConfig.getInstance().getLauncher().shootSequenceHigh();
					}

					if (RobotStatus.isAlpha())
					{
						if (gamepad.getButtonValue(EJoystickButtons.NINE) && !shootingLockOut)
						{
							shootingLockOut = true;
							// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.TOP_LIMIT);
							ActuatorConfig.getInstance().getLifter().lift();
							isAugerReadyToLift = true;
						}

						if (gamepad.getButtonValue(EJoystickButtons.TEN) && !shootingLockOut)
						// if (gamepad.getButtonValue(EJoystickButtons.TEN) &&
						// isAugerReadyToLift && !shootingLockOut)
						{
							shootingLockOut = true;
							ActuatorConfig.getInstance().getLifter().retract();
							SensorConfig.getInstance().getTimer().waitTimeInMillis(1750);
							ActuatorConfig.getInstance().getLauncher()
									.moveAugerToPreset(EAugerPresets.FOURTY_KAI_END_GAME);

							isAugerReadyToLift = false;
						}
					}

					if (!gamepad.getButtonValue(EJoystickButtons.THREE)
							&& !gamepad.getButtonValue(EJoystickButtons.SEVEN)
							&& !gamepad.getButtonValue(EJoystickButtons.EIGHT)
							&& !gamepad.getButtonValue(EJoystickButtons.NINE)
							&& !gamepad.getButtonValue(EJoystickButtons.TEN)
							&& !rightJoystick.getButtonValue(EJoystickButtons.NINE)
							&& !rightJoystick.getButtonValue(EJoystickButtons.TEN)
							&& !rightJoystick.getButtonValue(EJoystickButtons.FOUR)
							&& !rightJoystick.getButtonValue(EJoystickButtons.THREE)
							&& !leftJoystick.getButtonValue(EJoystickButtons.SEVEN)
							&& !leftJoystick.getButtonValue(EJoystickButtons.EIGHT)
							&& !leftJoystick.getButtonValue(EJoystickButtons.NINE)
							&& !leftJoystick.getButtonValue(EJoystickButtons.TEN)
							&& !leftJoystick.getButtonValue(EJoystickButtons.ELEVEN) && shootingLockOut)
					{
						shootingLockOut = false;
					}
				}
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
			}
		});

	}

	private void doDriverFunctions()
	{
		// TOGGLE PID
		if ((SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && !toggleLockA
				&& !toggleLockB)
		{
			toggleLockA = true;
			// DO 1 - On Click
			ActuatorConfig.getInstance().getDriveTrain().disablePID();
			PIDOverride.getInstance().setTeleopDisablePID(true);
			pidOn = false;
		}
		if ((!SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && toggleLockA
				&& !toggleLockB)
		{
			toggleLockA = false;
			toggleLockB = true;
			// DO 2 - On Release
		}
		if ((SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && !toggleLockA
				&& toggleLockB)
		{
			toggleLockA = true;
			// DO 3 - On Click
			ActuatorConfig.getInstance().getDriveTrain().enablePID();
			ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);
			PIDOverride.getInstance().setTeleopDisablePID(false);
			pidOn = true;
		}
		if ((!SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && toggleLockA
				&& toggleLockB)
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
					if (rightJoystick.getButtonValue(EJoystickButtons.TWO))
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(-1.0);
					} else
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYRight * speedMultiplier);
					}
					SmartDashboard.putBoolean("DRIVE TOGETHER", true);
				} else
				{
					ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYLeft * speedMultiplier,
							correctedYRight * speedMultiplier);
					SmartDashboard.putBoolean("DRIVE TOGETHER", false);
				}

				deadZoned = false;
			} else
			{
				if (!deadZoned)
				{
					ActuatorConfig.getInstance().getDriveTrain().stopDrive();
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
				if (!autoGyroDriveActivated)
				{
					ActuatorConfig.getInstance().getDriveTrain().disablePID();

					SensorConfig.getInstance().getGyro().resetCount();

					autoGyroDriveActivated = true;
				}

				if (rightJoystick.getButtonValue(EJoystickButtons.TWO))
				{
					ActuatorConfig.getInstance().getDriveTrain().driveStraight(-1.0);
				} else
				{
					ActuatorConfig.getInstance().getDriveTrain()
							.driveStraight(SensorConfig.getInstance().getRightJoystick().getY());

				}
				SmartDashboard.putBoolean("DRIVE TOGETHER", true);
			} else
			{
				autoGyroDriveActivated = false;

				ActuatorConfig.getInstance().getDriveTrain().setSpeed(
						SensorConfig.getInstance().getLeftJoystick().getY() * speedMultiplier,
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

			// Manual Auger
			if (gamepad.getButtonValue(EJoystickButtons.FIVE))
			{
				if (gamepad.getButtonValue(EJoystickButtons.TWELVE))
				{
					launcher.lowerAugerForEndGame();
				} else
				{
					launcher.lowerAuger();
				}
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

			// Reset Gyro
			if ((rightJoystick.getButtonValue(EJoystickButtons.ELEVEN)
					&& rightJoystick.getButtonValue(EJoystickButtons.TWELVE)))
			{
				SensorConfig.getInstance().getGyro().resetCount();
			}

		}
	}

	@Override
	public void printToSmartDashboard()
	{
		SmartDashboard.putNumber("Shooter Pot", SensorConfig.getInstance().getShooterPot().getCount());

		SmartDashboard.putBoolean("Bottom Limit Shooter",
				SensorConfig.getInstance().getShooterBottomLimitSwitch().isHit());
		SmartDashboard.putBoolean("Top Limit Shooter", SensorConfig.getInstance().getShooterTopLimitSwitch().isHit());

		// Pressure sensor feedback
		SmartDashboard.putBoolean("Pressure", SensorConfig.getInstance().getPressureSwitch().isHit());

		// Auger Pot Value
		SmartDashboard.putNumber("Auger Pot", ActuatorConfig.getInstance().getAugerPotentiometer().getCount());

		// Should we raise value
		SmartDashboard.putBoolean("Should we raise",
				ActuatorConfig.getInstance().getDriveTrainAssist().shouldShooterBeRaised());

		// Compass
		SmartDashboard.putNumber("Yaw", SensorConfig.getInstance().getGyro().getCount());

		// Manual Shooter Lock Out
		SmartDashboard.putBoolean("Shooter Lock out", shootingLockOut);

		if ((-10 < SensorConfig.getInstance().getGyro().getCount())
				&& ((SensorConfig.getInstance().getGyro().getCount() < 10)))
		{
			SmartDashboard.putBoolean("Are we Straight?", true);
		} else
		{
			SmartDashboard.putBoolean("Are we Straight?", false);
		}
	}

	
}
