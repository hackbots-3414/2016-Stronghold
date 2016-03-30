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

		ILauncher launcher = ActuatorConfig.getInstance().getLauncher();
		IGamepad gamepad = SensorConfig.getInstance().getGamepad();

		IJoystick leftJoystick = SensorConfig.getInstance().getLeftJoystick();
		IJoystick rightJoystick = SensorConfig.getInstance().getRightJoystick();

		executor.submit(() ->
		{

			ActuatorConfig.getInstance().getDriveTrain().enablePID();
			ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);

			double correctedYLeft, correctedYRight;
			// double yLeft, yRight;
			boolean pidOn = false;
			ActuatorConfig.getInstance().getDriveTrain().disablePID();
			PIDOverride.getInstance().setTeleopDisablePID(true);
			
			boolean deadZoned = false;

			double speedMultiplier = 1.0;
			boolean toggleLockA = false;
			boolean toggleLockB = true;

			boolean movedShooter = false;
			boolean movedAuger = false;
			boolean movedIntakeWheels = false;

			// Drive Train Loop
			while (RobotStatus.isRunning())
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
					correctedYLeft = Math.atan(SensorConfig.getInstance().getLeftJoystick().getY()) * (4 / Math.PI)
							* 400;
					correctedYRight = Math.atan(SensorConfig.getInstance().getRightJoystick().getY()) * (4 / Math.PI)
							* 400;

					if (correctedYLeft > 25 || correctedYLeft < -25 || correctedYRight > 25 || correctedYRight < -25)
					{

						if (SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.ONE))
						{
							ActuatorConfig.getInstance().getDriveTrain().setSpeed(correctedYRight * speedMultiplier);
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
							ActuatorConfig.getInstance().getDriveTrain().stop();
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
						ActuatorConfig.getInstance().getDriveTrain()
								.setSpeed(SensorConfig.getInstance().getRightJoystick().getY() * speedMultiplier);
						SmartDashboard.putBoolean("DRIVE TOGETHER", true);
					} else
					{
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

				// MANUAL COMMANDS
				if (!shootingLockOut)
				{
					// Manual Shooter
					if (gamepad.getButtonValue(EJoystickButtons.ONE))
					{
						// Shooter move half
						if (gamepad.getButtonValue(EJoystickButtons.TWO))
						{
							launcher.lowerShooterSlow();
							movedShooter = true;
						} else if (gamepad.getButtonValue(EJoystickButtons.FOUR))
						{
							launcher.raiseShooterSlow();
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

					// Reset Gyro
					if ((rightJoystick.getButtonValue(EJoystickButtons.ELEVEN)
							&& rightJoystick.getButtonValue(EJoystickButtons.TWELVE)))
					{
						SensorConfig.getInstance().getGyro().resetCount();
					}

				}

				// PRINT SMARTDASHBAORD VALUES
				SmartDashboard.putNumber("Shooter Pot", SensorConfig.getInstance().getShooterPot().getCount());

				SmartDashboard.putBoolean("Bottom Limit Shooter",
						SensorConfig.getInstance().getShooterBottomLimitSwitch().isHit());
				SmartDashboard.putBoolean("Top Limit Shooter",
						SensorConfig.getInstance().getShooterTopLimitSwitch().isHit());

				// Auger Current
				//SmartDashboard.putNumber("Auger Current", SensorConfig.getInstance().getPdp().getCurrent(3));

				// Pressure sensor feedback
				SmartDashboard.putBoolean("Pressure", SensorConfig.getInstance().getPressureSwitch().isHit());
				// if (SensorConfig.getInstance().getPressureSwitch().isHit())
				// {
				// SmartDashboard.putBoolean("Pressure", true);
				// } else
				// {
				// SmartDashboard.putBoolean("Pressure", false);
				// }

				// Auger Pot Value
				//SmartDashboard.putNumber("Auger Pot", ActuatorConfig.getInstance().getAugerPotentiometer().getCount());
				System.out.println(ActuatorConfig.getInstance().getAugerPotentiometer().getCount());

				// Should we raise value
				SmartDashboard.putBoolean("Should we raise",
						ActuatorConfig.getInstance().getDriveTrainAssist().shouldShooterBeRaised());

				// // should we auto raise auger
				// SmartDashboard.putBoolean("Auto Raise Auger",
				// ActuatorConfig.getInstance().getDriveTrainAssist().isTilt());

				// Compass
				SmartDashboard.putNumber("Yaw", SensorConfig.getInstance().getGyro().getCount());

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
			// ActuatorConfig.getInstance().getLifter().retract(); //TODO
			//ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);	

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
				
				// Any normal defense: Rough Terrain, Moat, Ramparts,
				// Drawbridge, Sally port
				if (leftJoystick.getButtonValue(EJoystickButtons.EIGHT) && !shootingLockOut)
				{
					shootingLockOut = true;
					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.SHOOT);
					ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT_HIGH);
				}

				// Rock Wall
				if (leftJoystick.getButtonValue(EJoystickButtons.NINE)
						&& ActuatorConfig.getInstance().getDriveTrainAssist().isTilt() && !shootingLockOut)
				{
					shootingLockOut = true;
					ActuatorConfig.getInstance().getLauncher().lowerAugerToBottomLimit(0.9);
				}

				// Cheval de friz
				if (leftJoystick.getButtonValue(EJoystickButtons.TEN)
						&& ActuatorConfig.getInstance().getDriveTrainAssist().isTiltGyro() && !shootingLockOut)
				{
					shootingLockOut = true;
					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.FOURTY_KAI);
				}
				
				//if (leftJoystick.getButtonValue(EJoystickButton.ELEVEN)&& )
				//	&& !shootingLockOut && ActuatorConfig.getInstance().getLauncher()
						

				// // Portcullis TODO: ONLY IF WE HAVE HOOKS IN FRONT OF AUGER
				// if (leftJoystick.getButtonValue(EJoystickButtons.ELEVEN) &&
				// !shootingLockOut)
				// {
				// shootingLockOut = true;
				// ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
				// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);
				// ActuatorConfig.getInstance().getDriveTrain().setSpeed(0.2,
				// 0.2);
				// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.TOP_LIMIT);
				// }

				// // Lift Robot TODO: ONLY IF WE HAVE PISTONS ATTACHED
				// if (gamepad.getButtonValue(EJoystickButtons.NINE) &&
				// !shootingLockOut)
				// {
				// shootingLockOut = true;
				// ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.TOP_LIMIT);
				// ActuatorConfig.getInstance().getLifter().lift();
				// }
				//
				// if (gamepad.getButtonValue(EJoystickButtons.TEN) &&
				// !shootingLockOut)
				// {
				// shootingLockOut = true;
				// ActuatorConfig.getInstance().getLifter().retract();
				// }

				if (!gamepad.getButtonValue(EJoystickButtons.SEVEN) && !gamepad.getButtonValue(EJoystickButtons.EIGHT)
						&& !gamepad.getButtonValue(EJoystickButtons.NINE)
						&& !gamepad.getButtonValue(EJoystickButtons.TEN)
						&& !leftJoystick.getButtonValue(EJoystickButtons.EIGHT)
						&& !leftJoystick.getButtonValue(EJoystickButtons.SEVEN)
						&& !leftJoystick.getButtonValue(EJoystickButtons.NINE)
						&& !leftJoystick.getButtonValue(EJoystickButtons.TEN)
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

	}
}
