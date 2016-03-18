package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;
import org.fpsrobotics.actuators.ILauncher;
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
		executor = Executors.newFixedThreadPool(3);
	}

	@Override
	public void doTeleop()
	{
		ILauncher launcher = ActuatorConfig.getInstance().getLauncher();
		IGamepad gamepad = SensorConfig.getInstance().getGamepad();

		executor.submit(() ->
		{

			ActuatorConfig.getInstance().getDriveTrain().enablePID();
			ActuatorConfig.getInstance().getDriveTrain().setControlMode(TalonControlMode.Speed);

			double correctedYLeft, correctedYRight, yLeft, yRight;
			boolean pidOn = true;
			boolean deadZoned = false;

			double speedMultiplier = 1.0;
			boolean lockA = false;
			boolean lockB = false;

			// Drive Train Loop
			while (RobotStatus.isRunning())
			{
				// TOGGLE PID
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
					yLeft = SensorConfig.getInstance().getLeftJoystick().getY();
					yRight = SensorConfig.getInstance().getRightJoystick().getY();

					/*
					 * Linear Drive Control correctedYOne = yOne * 400;
					 * correctedYTwo = yTwo * 400;
					 */

					/* Inverse Tangent Drive Control */
					correctedYLeft = Math.atan(yLeft) * (4 / Math.PI) * 400;
					correctedYRight = Math.atan(yRight) * (4 / Math.PI) * 400;

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

						// SmartDashboard.putNumber("Left Drive",
						// correctedYLeft);
						// SmartDashboard.putNumber("Right Drive",
						// correctedYRight);

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
					// SmartDashboard.putNumber("Left Drive",
					// SensorConfig.getInstance().getLeftJoystick().getY() *
					// speedMultiplier);
					// SmartDashboard.putNumber("Right Drive",
					// SensorConfig.getInstance().getRightJoystick().getY() *
					// speedMultiplier);

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

				SmartDashboard.putNumber("Shooter Pot", SensorConfig.getInstance().getShooterPot().getCount());
				SmartDashboard.putNumber("Auger Pot", ActuatorConfig.getInstance().getAugerPotentiometer().getCount());
				SmartDashboard.putBoolean("Bottom Limit Shooter",
						SensorConfig.getInstance().getShooterBottomLimitSwitch().isHit());
				SmartDashboard.putBoolean("Top Limit Shooter",
						SensorConfig.getInstance().getShooterTopLimitSwitch().isHit());

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
			boolean movedIntakeWheels = true;

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

				if (movedShooter)
				{
					launcher.stopShooterLifter();
					movedShooter = false;
				}

				// Shooter launching controls
				if (gamepad.getButtonValue(EJoystickButtons.SEVEN))
				{

					SmartDashboard.putBoolean("Is Shooting High", true);
					launcher.shootSequenceHigh();

					while (gamepad.getButtonValue(EJoystickButtons.SEVEN));

					movedIntakeWheels = true;
					SmartDashboard.putBoolean("Is Shooting High", false);
				}

				if (gamepad.getButtonValue(EJoystickButtons.EIGHT))
				{
					SmartDashboard.putBoolean("Is Shooting Low", true);
					launcher.shootSequenceLow();
					
					while (gamepad.getButtonValue(EJoystickButtons.EIGHT));

					movedIntakeWheels = true;
					SmartDashboard.putBoolean("Is Shooting Low", false);
				}

				if (gamepad.getButtonValue(EJoystickButtons.THREE))
				{
					launcher.intakeBoulder();
					while (gamepad.getButtonValue(EJoystickButtons.THREE));
					movedIntakeWheels = true;
				}

				if (movedIntakeWheels)
				{
					launcher.stopAugerWheels();
					launcher.stopShooterWheels();

					movedIntakeWheels = false;
				}

				// Pressure sensor feedback
				if (SensorConfig.getInstance().getPressureSwitch().isHit())
				{
					SmartDashboard.putBoolean("Pressure", true);
				} else
				{
					SmartDashboard.putBoolean("Pressure", false);
				}

				SmartDashboard.putBoolean("Should shooter be raised?", ActuatorConfig.getInstance().getDriveTrainAssist().shouldShooterBeRaised());

				// Low bar
				if (SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.SEVEN))
				{
					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.BOTTOM_LIMIT);
					ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.LOW_BAR);
				}

				// Any normal defense
				if (SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.SIX))
				{
					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.TOP_LIMIT);
					ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.TOP_LIMIT);
				}
				
				// Shoot position
				if(SensorConfig.getInstance().getLeftJoystick().getButtonValue(EJoystickButtons.NINE))
				{
					ActuatorConfig.getInstance().getLauncher().moveAugerToPreset(EAugerPresets.TOP_LIMIT);
					ActuatorConfig.getInstance().getLauncher().moveShooterToPreset(EShooterPresets.SHOOT);
				}

		
//				  // TODO
//				  - Auger to shoot position (When gamepad-button 7 or 8) AUTO
//				  - Auger to load ball/ suck in position (Gamepad-Button 3) AUTO
//				  - Auger and shooter to normal defense: Left-Button 8 AUTO
//				  - Auger and Shooter to lift other non-normal defenses. AUTO
//				  - Raise auger and shooter at same time MANUAL
//				  - Fix Camera Image
//				  - Shooter doesn't go so low
//				  - autoRaise boolean: don't need unless in separate threads
//				  - Use autoRaise OR Defense Buttons
//				  - Gamepad button 9 and 10 for lifter
//				  - Should manual raise/lower shooter/auger override AUTO functions?

				try
				{
					Thread.sleep(100);
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});

		// Auger movement controls
		executor.submit(() ->
		{
			boolean movedAuger = true;
			
			while (RobotStatus.isRunning())
			{	
				while (gamepad.getButtonValue(EJoystickButtons.FIVE))
				{
					launcher.lowerAuger();
					movedAuger = true;
				}

				while (gamepad.getButtonValue(EJoystickButtons.SIX))
				{
					launcher.raiseAuger();
					movedAuger = true;
				}

				if (movedAuger)
				{
					launcher.stopAugerLifter();
					movedAuger = false;
				}
				
				try
				{
					Thread.sleep(100);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
