package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.ILauncher;
import org.fpsrobotics.sensors.EAnalogStickAxis;
import org.fpsrobotics.sensors.EJoystickButtons;
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

		// SmartDashboard.putNumber("Preset", 477);
//		SmartDashboard.putNumber("Shoot Speed", 0.40);
	}

	private boolean launchReadyA = false;
	private boolean launchReadyB = false;
	private boolean augerLockOut = false;
	private final static long TIMEOUT = 500;

	@Override
	public void doTeleop()
	{
		ILauncher launcher = ActuatorConfig.getInstance().getLauncher();
		IGamepad gamepad = SensorConfig.getInstance().getGamepad();

		executor.submit(() ->
		{

			ActuatorConfig.getInstance().getDriveTrain().enablePID();
			ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);

			double correctedYOne, correctedYTwo, yOne, yTwo;
			boolean pidOn = true;
			boolean deadZoned = false;

			double notPIDspeedMultiplyer = 1.0;
			boolean lockA = false;
			boolean lockB = false;

			// Drive Train Loop
			while (RobotStatus.isRunning())
			{
				// Toggle PID
				if ((SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && !lockA
						&& !lockB)
				{
					lockA = true;
					// DO 1 - On Click
					ActuatorConfig.getInstance().getDriveTrain().disablePID();
					PIDOverride.getInstance().setTeleopDisablePID(true);
					pidOn = false;
				}
				if ((!SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && lockA
						&& !lockB)
				{
					lockA = false;
					lockB = true;
					// DO 2 - On Release
				}
				if ((SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && !lockA
						&& lockB)
				{
					lockA = true;
					// DO 3 - On Click
					ActuatorConfig.getInstance().getDriveTrain().enablePID();
					ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);
					PIDOverride.getInstance().setTeleopDisablePID(false);
					pidOn = true;
				}
				if ((!SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.FIVE)) && lockA
						&& lockB)
				{
					lockA = false;
					lockB = false;
					// DO 4 - On Release
				}

				SmartDashboard.putBoolean("PID", pidOn);

				// With PID
				if (pidOn)
				{
					yOne = SensorConfig.getInstance().getLeftJoystick().getY();
					yTwo = SensorConfig.getInstance().getRightJoystick().getY();

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
						if (!deadZoned)
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

					// Without PID
				} else
				{

					if (SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.ONE))
					{
						notPIDspeedMultiplyer = 0.5;
						SmartDashboard.putBoolean("DRIVE BY HALF", true);
					} else
					{
						notPIDspeedMultiplyer = 1.0;
						SmartDashboard.putBoolean("DRIVE BY HALF", false);
					}

					if (SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.ONE))
					{
						ActuatorConfig.getInstance().getDriveTrain()
								.setSpeed(SensorConfig.getInstance().getRightJoystick().getY() * notPIDspeedMultiplyer);
						SmartDashboard.putBoolean("DRIVE TOGETHER", true);
					} else
					{
						ActuatorConfig.getInstance().getDriveTrain().setSpeed(
								SensorConfig.getInstance().getLeftJoystick().getY() * notPIDspeedMultiplyer,
								SensorConfig.getInstance().getRightJoystick().getY() * notPIDspeedMultiplyer);
						SmartDashboard.putBoolean("DRIVE TOGETHER", false);
					}
					SmartDashboard.putNumber("Left R * SpeedMultiplyer",
							SensorConfig.getInstance().getRightJoystick().getY() * notPIDspeedMultiplyer);

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

				// System.out.println("Potentiometer " +
				// SensorConfig.getInstance().getShooterPot().getCount());

				// System.out.println("Auger Encoder " +
				// ActuatorConfig.getInstance().getAugerEncoder().getCount());
				// System.out.println("Auger Encoder Rate " +
				// ActuatorConfig.getInstance().getAugerEncoder().getRate());
				// System.out.println("Auger Encoder Error " +
				// ActuatorConfig.getInstance().getAugerEncoder().getError());
				// System.out.println("Auger Limits " +
				// SensorConfig.getInstance().getAugerBottomLimitSwitch().getValue()
				// +
				// SensorConfig.getInstance().getAugerTopLimitSwitch().getValue());
				// System.out.println("Shooter Limits " +
				// SensorConfig.getInstance().getBottomLimitSwitch().getValue()
				// + " " +
				// SensorConfig.getInstance().getTopLimitSwitch().getValue());
				// if
				// (SensorConfig.getInstance().getRightJoystick().getButtonValue(ButtonJoystick.TEN))
				// {
				// ActuatorConfig.getInstance().getDriveTrain().disablePID();
				// ActuatorConfig.getInstance().getDriveTrain().turnRight(0.1);
				// while
				// (SensorConfig.getInstance().getRightJoystick().getButtonValue(ButtonJoystick.TEN));
				// {}
				//
				// ActuatorConfig.getInstance().getDriveTrain().goStraight(0);
				// ActuatorConfig.getInstance().getDriveTrain().enablePID();
				// }
				//
				// if
				// (SensorConfig.getInstance().getRightJoystick().getButtonValue(ButtonJoystick.NINE))
				// {
				//
				// ActuatorConfig.getInstance().getDriveTrain().disablePID();
				// ActuatorConfig.getInstance().getDriveTrain().turnLeft(0.1);
				// while
				// (SensorConfig.getInstance().getRightJoystick().getButtonValue(ButtonJoystick.NINE));
				// {
				//
				// }
				//
				// ActuatorConfig.getInstance().getDriveTrain().goStraight(0);
				// ActuatorConfig.getInstance().getDriveTrain().enablePID();
				// }

				SmartDashboard.putNumber("Shooter Pot", SensorConfig.getInstance().getShooterPot().getCount());

				try
				{
					Thread.sleep(100);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		 // Shooter Loop
		 executor.submit(() ->
		 {
		 boolean movedShooter = true;
		 boolean movedAuger = true;
		 boolean movedShooterWheels = true;
		 boolean movedAugerWheels = true;
		
		 while (RobotStatus.isRunning())
		 {
		 // Shooter movement controls
		 while (gamepad.getButtonValue(EJoystickButtons.TWO))
		 {
		 launcher.lowerShooter();
		 movedShooter = true;
		 }
		
		 while (gamepad.getButtonValue(EJoystickButtons.FOUR))
		 {
		 launcher.raiseShooter();
		 movedShooter = true;
		 }
		
		 // while (gamepad.getButtonValue(EJoystickButtons.FIVE))
		 // {
		 //
		 //launcher.moveShooterToPosition(SmartDashboard.getNumber("Preset",
		 // 477));
		 //
		 // launcher.moveShooterToBottomLimit();
		 // launcher.augerGoToPosition(1000);
		 //
		 // movedShooter = true;
		 // }
		
		 if (movedShooter)
		 {
		 launcher.stopShooterLifter();
		 movedShooter = false;
		 }
		 /*
		 * // Auger movement controls while
		 * (gamepad.getButtonValue(EJoystickButtons.ONE)) {
		 * launcher.lowerAuger(); movedAuger = true; } while
		 * (gamepad.getButtonValue(EJoystickButtons.SIX)) {
		 * launcher.raiseAuger(); movedAuger = true; } if(movedAuger) {
		 * launcher.stopAugerLifter(); movedAuger = false; }
		 *
		 * // Auger wheel controls while
		 * (gamepad.getButtonValue(EJoystickButtons.NINE)) {
		 * launcher.spinAugerUp();
		 *
		 * movedAugerWheels = true; }
		 *
		 * if(movedAugerWheels) { launcher.stopAuger(); movedAugerWheels
		 * = false; }
		 */
		
		 // Shooter launching controls
		 if (gamepad.getButtonValue(EJoystickButtons.SEVEN))
		 {
		 launcher.shootSequence();
		
		 while (gamepad.getButtonValue(EJoystickButtons.SEVEN))
		 ;
		
		 movedShooterWheels = true;
		 }
		
		 if (gamepad.getButtonValue(EJoystickButtons.EIGHT))
		 {
		 launcher.shootSequence(SmartDashboard.getNumber("Shoot Speed",
		 0.85));
		
		 while (gamepad.getButtonValue(EJoystickButtons.EIGHT))
		 ;
		
		 movedShooterWheels = true;
		 }
		
		 while (gamepad.getButtonValue(EJoystickButtons.THREE))
		 {
		 launcher.intakeBoulder();
		
		 movedShooterWheels = true;
		 }
		
		 if (movedShooterWheels)
		 {
		 launcher.stopShooterWheels();
		 movedShooterWheels = false;
		 }
		
		 /*
		 * Manual Launching while
		 * (gamepad.getButtonValue(EJoystickButtons.ONE)) {
		 * launcher.spinShooterUp(); if
		 * (gamepad.getButtonValue(EJoystickButtons.SIX)) {
		 * launcher.launchBoulder(); while
		 * (gamepad.getButtonValue(EJoystickButtons.SIX) ||
		 * gamepad.getButtonValue(EJoystickButtons.ONE)); } }
		 */
		
		 // Pressure sensor feedback
		 if (SensorConfig.getInstance().getPressureSwitch().isHit())
		 {
		 SmartDashboard.putBoolean("Pressure", true);
		 } else
		 {
		 SmartDashboard.putBoolean("Pressure", false);
		 }
		
		 SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
		 }
		 });

		// // LIFTER
		// executor.submit(() ->
		// {
		// while (RobotStatus.isRunning())
		// {
		// // With internal while statements
		// if (gamepad.getButtonValue(EJoystickButtons.TEN))
		// {
		// launcher.moveShooterToPreset(EShooterPresets.TOP_LIMIT);
		// }
		// if (gamepad.getButtonValue(EJoystickButtons.NINE))
		// {
		// launcher.moveShooterToPreset(EShooterPresets.BOTTOM_LIMIT);
		// }
		//
		// if (gamepad.getButtonValue(EJoystickButtons.FOUR))
		// {
		// launcher.raiseShooter();
		// } else if (gamepad.getButtonValue(EJoystickButtons.TWO))
		// {
		// launcher.lowerShooter();
		// } else
		// {
		// launcher.stopShooterLifter();
		// }
		// try
		// {
		// Thread.sleep(100);
		// } catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		// }
		// });

		// // AUGER
		// executor.submit(() ->
		// {
		// while (RobotStatus.isRunning())
		// {
		// if (!augerLockOut)
		// {
		// if (gamepad.getAnalogStickValue(EAnalogStickAxis.LEFT_VERTICAL) >
		// 0.0)
		// {
		// launcher.raiseAuger();
		// } else if
		// (gamepad.getAnalogStickValue(EAnalogStickAxis.LEFT_VERTICAL) < 0.0)
		// {
		// launcher.lowerAuger();
		// } else
		// {
		// launcher.stopAugerLifter();
		// }
		// }
		// try
		// {
		// Thread.sleep(100);
		// } catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		// }
		// });
		// TODO: Include auger to max, auger to min, auger to pull up, auger
		// pull up, auger to pick up ball, auger to shoot,

//		// LAUNCHER
//		executor.submit(() ->
//		{
//			boolean launchSequenceLockOut = false;
//			boolean intakeLockout = false;
//
//			while (RobotStatus.isRunning())
//			{
//
//				// With internal while statements
//				if (gamepad.getButtonValue(EJoystickButtons.TEN))
//				{
//					launcher.moveShooterToPreset(EShooterPresets.TOP_LIMIT);
//				}
//				if (gamepad.getButtonValue(EJoystickButtons.NINE))
//				{
//					launcher.moveShooterToPreset(EShooterPresets.BOTTOM_LIMIT);
//				}
//				if (gamepad.getButtonValue(EJoystickButtons.FOUR))
//				{
//					launcher.raiseShooter();
//					while (gamepad.getButtonValue(EJoystickButtons.FOUR))
//						;
//					launcher.stopShooterLifter();
//				} else if (gamepad.getButtonValue(EJoystickButtons.TWO))
//				{
//					launcher.lowerShooter();
//					while (gamepad.getButtonValue(EJoystickButtons.TWO))
//						;
//					launcher.stopShooterLifter();
//				} 
//
//				// Launch Ball Sequence; locks out other options
//				if (!launchSequenceLockOut && !intakeLockout && !launchReadyA
//						&& gamepad.getButtonValue(EJoystickButtons.SEVEN))
//				{
//					augerLockOut = true;
//					SmartDashboard.putBoolean("Auger Lock Out", true);
//					SmartDashboard.putBoolean("Launch Sequence Lock Out", true);
//					launchSequenceLockOut = true;
//					launcher.shootSequence();
//					// TODO: move auger out of the way
//				}
//				if (launchSequenceLockOut && !gamepad.getButtonValue(EJoystickButtons.SEVEN))
//				{
//					SmartDashboard.putBoolean("Auger Lock Out", false);
//					SmartDashboard.putBoolean("Launch Sequence Lock Out", false);
//					try
//					{
//						Thread.sleep(TIMEOUT);
//					} catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//					launchSequenceLockOut = false;
//					augerLockOut = false;
//				}
//
//				// Launch Ball Sequence; locks out other options
//				if (!launchSequenceLockOut && !intakeLockout && !launchReadyA
//						&& gamepad.getButtonValue(EJoystickButtons.EIGHT))
//				{
//					augerLockOut = true;
//					SmartDashboard.putBoolean("Auger Lock Out", true);
//					SmartDashboard.putBoolean("Launch Sequence Lock Out", true);
//					launchSequenceLockOut = true;
//					launcher.shootSequence(0.60);
//					// TODO: move auger out of the way
//				}
//				if (launchSequenceLockOut && !gamepad.getButtonValue(EJoystickButtons.EIGHT))
//				{
//					SmartDashboard.putBoolean("Auger Lock Out", false);
//					SmartDashboard.putBoolean("Launch Sequence Lock Out", false);
//					try
//					{
//						Thread.sleep(TIMEOUT);
//					} catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//					launchSequenceLockOut = false;
//					augerLockOut = false;
//				}
//
//				// Intake Ball; locks out other options
//				if (!launchSequenceLockOut && !launchReadyA && gamepad.getButtonValue(EJoystickButtons.THREE))
//				{
//					intakeLockout = true;
//					launcher.intakeBoulder();
//				}
//				if (intakeLockout && !gamepad.getButtonValue(EJoystickButtons.THREE))
//				{
//					launcher.stopIntakeBoulder();
//					try
//					{
//						Thread.sleep(TIMEOUT);
//					} catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//					intakeLockout = false;
//				}
//
//				// ONLY WANT TO SHOOT WHEN WHEELS SPINNING
//				if (!launchSequenceLockOut && !intakeLockout && gamepad.getButtonValue(EJoystickButtons.FIVE))
//				{
//					launcher.spinShooterWheels();
//					launchReadyA = true;
//				}
//				if (launchReadyA && !gamepad.getButtonValue(EJoystickButtons.FIVE))
//				{
//					launcher.stopShooterWheels();
//					launchReadyA = false;
//					try
//					{
//						Thread.sleep(TIMEOUT);
//					} catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//				}
//
////				if (launchReadyB && gamepad.getButtonValue(EJoystickButtons.SIX))
//				if ( gamepad.getButtonValue(EJoystickButtons.SIX))
//				{
//					launcher.launchBoulder();
//					launchReadyB = false;
//					try
//					{
//						Thread.sleep(TIMEOUT);
//					} catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//				}
//				try
//				{
//					Thread.sleep(100);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//
//				SmartDashboard.putBoolean("Inkate Lock Out", intakeLockout);
//				SmartDashboard.putBoolean("Launch Ready A", launchReadyA);
//				SmartDashboard.putBoolean("Launch Ready B", launchReadyB);
//			}
//		});

//		executor.submit(() ->
//		{
//			while (RobotStatus.isRunning())
//			{
//				if (launchReadyA)
//				{
//					augerLockOut = true;
//					SmartDashboard.putBoolean("Auger Lock Out", true);
//					// launcher.moveAugerToPreset(EAugerPresets.LAUNCH);
//					try
//					{
//						Thread.sleep(TIMEOUT);
//					} catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//					if (launchReadyA)
//					{
//						launchReadyB = true;
//					}
//				} else
//				{
//					augerLockOut = false;
//					SmartDashboard.putBoolean("Auger Lock Out", false);
//					launchReadyB = false;
//				}
//				try
//				{
//					Thread.sleep(100);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		});

	}
}
