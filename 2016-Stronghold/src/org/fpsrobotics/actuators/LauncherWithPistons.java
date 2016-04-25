package org.fpsrobotics.actuators;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LauncherWithPistons implements ILauncher
{
	// Lifter Functions
	private final double LINEAR_ACTUATOR_SPEED = 0.5;
	private final double SLOW_FACTOR = 0.4;

	// Shooter Function
	private boolean isAugerAtIntake = false; // used to stop intake boulder
	private final double SHOOT_SPEED_HIGH = 1.0;
	private final double SHOOT_SPEED_LOW = 0.5;
	private final double SHOOT_SPEED_LOW_AUTO = 0.7;

	// Intake Functions
	private final double INTAKE_SHOOTER_SPEED = -0.7;
	private final double INTAKE_AUGER_SPEED = 0.8;

	// Auger Functions
	private boolean manualLowerAuger = false;
	private boolean autoRaiseAuger = false;
	private final int RAMP_RATE = 3;
	private final int STALL_RATE = 3;
	private double augerPrevValue;
	private final int SLOW_AUGER_DISTANCE = 100;
	private final double AUGER_SPEED_RAISE = 0.5;
	private final double AUGER_SPEED_LOWER = 0.5;
	private final double FULL_SPEED = 1.0;
	private final double HIGH_VALUE_AUGER_SPEED = 0.8;
	private final double AUTO_AUGER_SPEED = 0.35;

	// Fields
	private ICANMotor leftShooterMotor, rightShooterMotor;
	private ICANMotor augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter;
	private ILimitSwitch bottomLimitAuger, topLimitAuger;

	private IPIDFeedbackDevice shooterPot, augerPot;

	private ISolenoid shooterActuator;

	// Based on Alpha or Beta
	// Shooter
	private final int BOTTOM_LIMIT_SHOOTER; // Bottom limit the shooter can travel to
	private final int TOP_LIMIT_SHOOTER; // Top limit the shooter can travel to
	// AUGER
	private final int TOP_LIMIT_AUGER;
	private final int BOTTOM_LIMIT_AUGER;
	private final int LOW_BAR_AUGER_FOR_SHOOTER;
	// Presets
	private final int LOW_BAR_SHOOTER;
	private final int STANDARD_DEFENSE_SHOOTER;
	private final int HIGH_VALUE_AUGER;
	private final int FOURTY_KAI;
	private final int INTAKE_AUGER;
	private final int STANDARD_DEFENSE_AUGER;
	private final int LOW_SHOT_AUTO_AUGER;

	// For moveShooterAndAuger
	private ExecutorService executor;
	private boolean isAugerAtPreset = false;
	private final int SHOOTER_POSITION_AT_POSITION_TWO_AUTO;
	private final int SHOOTER_POSITION_AT_POSITION_THREE_AUTO;
	private final int SHOOTER_POSITION_AT_POSITION_FOUR_AUTO;
	private final int SHOOTER_POSITION_AT_POSITION_FIVE_AUTO;
	private final int SHOOTER_POSITION_CENTER_TELEOP;
	private final int SHOOTER_POSITION_CORNER_TELEOP;
	private final int SHOOTER_POSITION_LOW_BAR_SHOOT_HIGH_AUTO;

	// Override
	private boolean augerOverride = false;
	private boolean shooterOverride = false;

	/**
	 * 
	 * @param leftShooterMotor
	 * @param rightShooterMotor
	 * @param shooterLifterMotor
	 * @param shooterActuator
	 * @param shooterBottomLimit
	 * @param shooterTopLimit
	 * @param shooterPot
	 * @param augerIntakeMotor
	 * @param augerLifterMotor
	 * @param bottomLimitAuger
	 * @param topLimitAuger
	 * @param augerPot
	 */
	public LauncherWithPistons(ICANMotor leftShooterMotor, ICANMotor rightShooterMotor, ICANMotor shooterLifterMotor, ISolenoid shooterActuator,
			ILimitSwitch shooterBottomLimit, ILimitSwitch shooterTopLimit, IPIDFeedbackDevice shooterPot, ICANMotor augerIntakeMotor,
			ICANMotor augerLifterMotor, ILimitSwitch bottomLimitAuger, ILimitSwitch topLimitAuger, IPIDFeedbackDevice augerPot)
	{

		// Shooter
		this.leftShooterMotor = leftShooterMotor;
		this.rightShooterMotor = rightShooterMotor;
		this.shooterLifterMotor = shooterLifterMotor;
		this.shooterActuator = shooterActuator;
		this.bottomLimitShooter = shooterBottomLimit;
		this.topLimitShooter = shooterTopLimit;
		this.shooterPot = shooterPot;
		// Auger
		this.augerIntakeMotor = augerIntakeMotor;
		this.augerLifterMotor = augerLifterMotor;
		this.bottomLimitAuger = bottomLimitAuger;
		this.topLimitAuger = topLimitAuger;
		this.augerPot = augerPot;

		if (RobotStatus.isAlpha())
		{
			// Shooter
			BOTTOM_LIMIT_SHOOTER = 2100;
			TOP_LIMIT_SHOOTER = 588;
			// AUGER
			TOP_LIMIT_AUGER = 1800;
			BOTTOM_LIMIT_AUGER = 546;
			HIGH_VALUE_AUGER = 1300; // When Auger is high, increase speed
			// Collision Prevention
			LOW_BAR_AUGER_FOR_SHOOTER = 670; // position where auger must be to not collide with the shooter when up
												// must be just above highest collision point
			LOW_BAR_SHOOTER = 1800; // position where shooter must be to not collide with the auger when down
									// must be just below lowest collision point
			// Presets
			STANDARD_DEFENSE_SHOOTER = 1390; // Where shooter should be when going over standard defense
			FOURTY_KAI = 1400; // Used for High Shot Auto
			STANDARD_DEFENSE_AUGER = 740; // should be above LOW_BAR_AUGER_FOR_SHOOTER
			INTAKE_AUGER = 690;
			LOW_SHOT_AUTO_AUGER = 830; // Used so auger doesn't have to go up that much during auto

			// To Shoot
			SHOOTER_POSITION_AT_POSITION_TWO_AUTO = 800;
			SHOOTER_POSITION_AT_POSITION_THREE_AUTO = 600;
			SHOOTER_POSITION_AT_POSITION_FOUR_AUTO = 600;
			SHOOTER_POSITION_AT_POSITION_FIVE_AUTO = 758;
			SHOOTER_POSITION_CENTER_TELEOP = 600;
			SHOOTER_POSITION_CORNER_TELEOP = 635;
			SHOOTER_POSITION_LOW_BAR_SHOOT_HIGH_AUTO = 765;
		} else
		{
			// Shooter
			BOTTOM_LIMIT_SHOOTER = 1200;
			TOP_LIMIT_SHOOTER = 250;
			// AUGER
			TOP_LIMIT_AUGER = 2200;
			BOTTOM_LIMIT_AUGER = 825;
			HIGH_VALUE_AUGER = 1200;
			// Collision Prevention
			LOW_BAR_AUGER_FOR_SHOOTER = 1090; // must be just above highest collision point //1176 with pistons
			LOW_BAR_SHOOTER = 1130; // must be just below lowest collision point
			// Presets
			STANDARD_DEFENSE_SHOOTER = 800;
			FOURTY_KAI = 1650;
			STANDARD_DEFENSE_AUGER = 1000;
			INTAKE_AUGER = 1050;
			LOW_SHOT_AUTO_AUGER = 600;
			// To Shoot
			SHOOTER_POSITION_AT_POSITION_TWO_AUTO = 300;
			SHOOTER_POSITION_AT_POSITION_THREE_AUTO = 200;
			SHOOTER_POSITION_AT_POSITION_FOUR_AUTO = 300;
			SHOOTER_POSITION_AT_POSITION_FIVE_AUTO = 258;
			SHOOTER_POSITION_CENTER_TELEOP = 250;
			SHOOTER_POSITION_CORNER_TELEOP = 250;
			SHOOTER_POSITION_LOW_BAR_SHOOT_HIGH_AUTO = 300;
		}

		executor = Executors.newFixedThreadPool(1);

		SmartDashboard.putNumber("Top Pot Limit Auger", TOP_LIMIT_AUGER);
		SmartDashboard.putNumber("Bottom Pot Limit Auger", BOTTOM_LIMIT_AUGER);

		SmartDashboard.putNumber("Top Pot Limit Shooter", TOP_LIMIT_SHOOTER);
		SmartDashboard.putNumber("Bottom Pot Limit Shooter", BOTTOM_LIMIT_SHOOTER);
	}

	// Lifter Functions

	@Override
	public void raiseShooter(boolean slow)
	{
		if (!isShooterAtTopLimit())
		{
			if ((augerPot.getCount() < (LOW_BAR_AUGER_FOR_SHOOTER)) && (shooterPot.getCount() < LOW_BAR_SHOOTER) && (!manualLowerAuger))
			{
				raiseAuger(AUTO_AUGER_SPEED);
				autoRaiseAuger = true;
			} else
			{
				stopAugerLifter(false);
				autoRaiseAuger = false;
			}

			if (slow)
			{
				shooterLifterMotor.setSpeed(Math.abs(LINEAR_ACTUATOR_SPEED) * SLOW_FACTOR);
			} else
			{
				shooterLifterMotor.setSpeed(Math.abs(LINEAR_ACTUATOR_SPEED));
			}
		} else
		{
			stopShooterLifter();
		}
	}

	@Override
	public void lowerShooter(boolean slow)
	{
		if (!isShooterAtBottomLimit())
		{
			if (slow)
			{
				shooterLifterMotor.setSpeed(-Math.abs(LINEAR_ACTUATOR_SPEED) * SLOW_FACTOR);
			} else
			{
				shooterLifterMotor.setSpeed(-Math.abs(LINEAR_ACTUATOR_SPEED));
			}
		} else
		{
			stopShooterLifter();
		}
	}

	private void raiseShooterToTopLimit()
	{
		moveShooterToPosition(TOP_LIMIT_SHOOTER);
	}

	private void lowerShooterToBottomLimit()
	{
		moveShooterToPosition(BOTTOM_LIMIT_SHOOTER);
	}

	@Override
	public void stopShooterLifter()
	{
		shooterLifterMotor.stop();
		if (autoRaiseAuger)
		{
			stopAugerLifter(true);
		}
	}

	private boolean isShooterAtTopLimit()
	{
		// if shooter is ABOVE top limit pot value, or top limit is hit
		if ((shooterPot.getCount() <= TOP_LIMIT_SHOOTER) || topLimitShooter.isHit())
		{
			return true;
		} else
		{
			return false;
		}
	}

	private boolean isShooterAtBottomLimit()
	{
		// if shooter is BELOW bottom limit pot value, or bottom limit is hit
		if ((shooterPot.getCount() >= BOTTOM_LIMIT_SHOOTER) || bottomLimitShooter.isHit())
		{
			return true;
		} else
		{
			return false;
		}
	}

	@Override
	/**
	 * We want the shooter to UNDERSHOOT
	 */
	public void moveShooterToPosition(double desiredPosition)
	{
		if ((shooterPot.getCount() < (desiredPosition - 50)) || (shooterPot.getCount() > (desiredPosition + 50)))
		{
			if (SensorConfig.getInstance().getGamepad().getButtonValue(EJoystickButtons.THREE))
			// If Intake, then if hold button three
			{// while shooter is less than the desired position or we reach the top limit of travel
				while (!isShooterAtTopLimit() && (shooterPot.getCount() > desiredPosition) && (RobotStatus.isRunning()) && !shooterOverride
						&& SensorConfig.getInstance().getGamepad().getButtonValue(EJoystickButtons.THREE))
				{
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
					raiseShooter(false);
				}

				// while shooter is greater than the desired position or we reach the bottom limit of our travel
				while (!isShooterAtBottomLimit() && (shooterPot.getCount() < desiredPosition) && (RobotStatus.isRunning()) && !shooterOverride
						&& SensorConfig.getInstance().getGamepad().getButtonValue(EJoystickButtons.THREE))
				{
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
					lowerShooter(false);
				}
			} else
			{
				// while shooter is less than the desired position or we reach the top limit of travel
				while (!isShooterAtTopLimit() && (shooterPot.getCount() > desiredPosition) && (RobotStatus.isRunning()) && !shooterOverride)
				{
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
					raiseShooter(false);
				}

				// while shooter is greater than the desired position or we reach the bottom limit of our travel
				while (!isShooterAtBottomLimit() && (shooterPot.getCount() < desiredPosition) && (RobotStatus.isRunning()) && !shooterOverride)
				{
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
					lowerShooter(false);
				}
			}
		}
		setShooterOverride(false);
		stopShooterLifter();
	}

	// Shooter Functions
	@Override
	public void intakeBoulder()
	{
		if (!isAugerAtIntake)
		{
			rightShooterMotor.setSpeed(INTAKE_SHOOTER_SPEED);
			leftShooterMotor.setSpeed(INTAKE_SHOOTER_SPEED);

			augerIntakeMotor.setSpeed(INTAKE_AUGER_SPEED);

			moveShooterAndAugerToPreset(EShooterPresets.INTAKE, EAugerPresets.INTAKE);
			// moveShooterToPreset(EShooterPresets.INTAKE);
			// moveAugerToPreset(EAugerPresets.INTAKE);

			isAugerAtIntake = true;
		}
	}

	@Override
	public void stopIntakeBoulder()
	{
		isAugerAtIntake = false;
		augerIntakeMotor.stop();
		rightShooterMotor.stop();
		leftShooterMotor.stop();
	}

	@Override
	public void launchBoulder()
	{
		shooterActuator.engage();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1000); // 150
		stopShooterWheels();
		shooterActuator.disengage();
	}

	@Override
	/**
	 * Used for Teleop if Sami wants manual spinUp
	 */
	public void spinShooterWheelsHigh()
	{
		spinShooterWheels(SHOOT_SPEED_HIGH);
	}

	@Override
	/**
	 * Used for Teleop if Sami wants manual spinUp
	 */
	public void spinShooterWheelsLow()
	{
		spinShooterWheels(SHOOT_SPEED_LOW);
	}

	private void spinShooterWheels(double speed)
	{
		leftShooterMotor.setSpeed(speed);
		rightShooterMotor.setSpeed(speed);
	}

	@Override
	public void stopShooterWheels()
	{
		rightShooterMotor.stop();
		leftShooterMotor.stop();
	}

	// Auger Functions

	@Override
	public void raiseAuger()
	{
		raiseAuger(AUGER_SPEED_RAISE);
	}

	// Pot increases as Auger goes up
	private void raiseAuger(double speed)
	{
		if (!isAugerAtTopLimit())
		{
			if (augerLifterMotor.getSpeed() > 0)
			{
				// If auger is nearing the VERY Top, slow down
				if (augerPot.getCount() > (TOP_LIMIT_AUGER - 100))
				{
					augerLifterMotor.setSpeed(Math.abs(AUGER_SPEED_RAISE + 0.1));
					// If Auger is nearing the top, speed up
				} else if (augerPot.getCount() > (HIGH_VALUE_AUGER))
				{
					augerLifterMotor.setSpeed(Math.abs(HIGH_VALUE_AUGER_SPEED));
					// Else if auger is somewhere in between, go at assigned
					// speed
				} else
				{
					augerLifterMotor.setSpeed(Math.abs(speed));
				}
			} else
			{
				rampUpMotor(Math.abs(speed));
			}
		} else
		{
			stopAugerLifter(true);
		}
	}

	private void rampUpMotor(double speed)
	{
		if (speed > 0)
		{
			for (double i = 0; i <= speed; i += 0.01)
			{
				if (RobotStatus.isRunning())
				{
					augerLifterMotor.setSpeed(i);
					SensorConfig.getInstance().getTimer().waitTimeInMillis(RAMP_RATE);
				} else
				{
					return;
				}
			}
		} else
		{
			for (double i = 0; i >= speed; i -= 0.01)
			{
				if (RobotStatus.isRunning())
				{
					augerLifterMotor.setSpeed(i);
					SensorConfig.getInstance().getTimer().waitTimeInMillis(RAMP_RATE);
				} else
				{
					return;
				}
			}
		}
	}

	@Override
	/**
	 * Used in teleop
	 */
	public void lowerAuger()
	{
		lowerAuger(AUGER_SPEED_LOWER);
		// lowerAugerTest(AUGER_SPEED_LOWER);
	}

	@Override
	/**
	 * Used for endgame
	 */
	public void lowerAugerForEndGame()
	{
		lowerAuger(FULL_SPEED);
	}

	@Override
	public void raiseAugerForEndGame()
	{
		raiseAuger(FULL_SPEED);
	}

	private void lowerAugerTest(double speed)
	{
		// If auger is lower than the bottom limit
		if (augerPot.getCount() < (BOTTOM_LIMIT_AUGER))
		{
			// Slow to a halt
			stopAugerLifter(true);
		} else
		{
			// Travel normally
			if (augerLifterMotor.getSpeed() < 0)
			{
				augerLifterMotor.setSpeed(-Math.abs(speed));
			} else
			{
				rampUpMotor(-Math.abs(speed));
			}
		}
	}

	// if there is no desired position, accounting for shooter and bottom only
	private void lowerAuger(double speed)
	{
		if (!autoRaiseAuger)
		{
			manualLowerAuger = true;
			// If shooter is below "Low Bar Preset," safe from getting hit
			if ((shooterPot.getCount() > LOW_BAR_SHOOTER))
			{
				// if auger is nearing the bottom limit
				// if (augerPot.getCount() < (BOTTOM_LIMIT_AUGER + SLOW_AUGER_DISTANCE))
				if (augerPot.getCount() < (BOTTOM_LIMIT_AUGER))
				{
					// Slow to a halt
					// stopAugerLifter(true);
					stopAugerLifter(false);
				} else
				// If the shooter is safe from getting hit
				{
					// Travel normally
					if (augerLifterMotor.getSpeed() < 0)
					{
						augerLifterMotor.setSpeed(-Math.abs(speed));
					} else
					{
						rampUpMotor(-Math.abs(speed));
					}
				}
			} else
			// If shooter is above "Low Bar Preset," within range of getting hit
			{
				// if auger is nearing the collision point
				// if (augerPot.getCount() < (LOW_BAR_AUGER_FOR_SHOOTER + SLOW_AUGER_DISTANCE))
				if (augerPot.getCount() < (LOW_BAR_AUGER_FOR_SHOOTER))
				{
					// Slow to a halt
					// stopAugerLifter(true);
					stopAugerLifter(false);
				} else
				{
					// Travel normally
					if (augerLifterMotor.getSpeed() < 0)
					{
						augerLifterMotor.setSpeed(-Math.abs(speed));
					} else
					{
						rampUpMotor(-Math.abs(speed));
					}
				}
			}
		}
	}

	// Used if there IS a desired position
	// If desired position is below shooter, accounting for shooter and bottom only (see above)
	private void lowerAuger(double speed, int desiredPosition)
	{ // Todo: Why did we get rid of this??
		// If you are still going to hit the shooter before you hit your destination
		// if (desiredPosition < LOW_BAR_AUGER_FOR_SHOOTER)
		// {
		// lowerAuger(speed);
		// } else
		// // If you are still going to hit your destination before you hit the shooter
		// {
		if (!autoRaiseAuger)
		{
			manualLowerAuger = true;

			// if auger is nearing the collision point
			// if (augerPot.getCount() < (desiredPosition + SLOW_AUGER_DISTANCE))
			if (augerPot.getCount() < (desiredPosition))
			{
				// Slow to a halt
				// stopAugerLifter(true);
				stopAugerLifter(false);
			} else
			{
				// Else, travel normally
				if (augerLifterMotor.getSpeed() < 0)
				{
					augerLifterMotor.setSpeed(-Math.abs(speed));
				} else
				{
					rampUpMotor(-Math.abs(speed));
				}
			}
			// }
		}
	}

	private void rampDownMotor(double speed)
	{
		if (speed > 0)
		{
			for (double i = speed; i >= 0; i -= 0.01)
			{
				if (RobotStatus.isRunning())
				{
					augerLifterMotor.setSpeed(i);
					SensorConfig.getInstance().getTimer().waitTimeInMillis(RAMP_RATE);
				} else
				{
					return;
				}
			}
		} else
		{
			for (double i = speed; i <= 0; i += 0.01)
			{
				if (RobotStatus.isRunning())
				{
					augerLifterMotor.setSpeed(i);
					SensorConfig.getInstance().getTimer().waitTimeInMillis(RAMP_RATE);
				} else
				{
					return;
				}
			}
		}
	}

	public void stopAugerLifter(boolean rampDown)
	{
		if (rampDown)
		{
			rampDownMotor(augerLifterMotor.getSpeed());
		} else
		{
			augerLifterMotor.stop();
		}
		manualLowerAuger = false;
		autoRaiseAuger = false;
	}

	@Override
	public void stopAugerWheels()
	{
		augerIntakeMotor.stop();
	}

	private void raiseAugerToTopLimit()
	{
		moveAugerToPosition(TOP_LIMIT_AUGER);
	}

	private void lowerAugerToBottomLimit()
	{
		moveAugerToPosition(BOTTOM_LIMIT_AUGER);
	}

	private boolean isAugerAtBottomLimit()
	{
		if ((augerPot.getCount() < BOTTOM_LIMIT_AUGER) || bottomLimitAuger.isHit())
		{
			return true;
		} else
		{
			return false;
		}
	}

	private boolean isAugerAtTopLimit()
	{
		if ((augerPot.getCount() >= TOP_LIMIT_AUGER) || topLimitAuger.isHit())
		{
			return true;
		} else
		{
			return false;
		}

	}

	@Override
	/**
	 * - ALWAYS MOVE SHOOTER TO POSITION BEFORE AUGER - Auger designed to OVERSHOOT
	 */
	public void moveAugerToPosition(int desiredPosition)
	{
		moveAugerToPosition(desiredPosition, AUGER_SPEED_LOWER);
	}

	@Override
	public void moveAugerToPosition(int desiredPosition, double lowerSpeed)
	{
		augerPrevValue = augerPot.getCount();

		// if ((augerPot.getCount() < (desiredPosition - 50)) || (augerPot.getCount() > (desiredPosition + 50)))
		// {
		if (SensorConfig.getInstance().getGamepad().getButtonValue(EJoystickButtons.THREE))
		// If Intake, then if hold button three
		{
			if (augerPot.getCount() > desiredPosition)
			{
				// if the auger is higher than the position
				while (!isAugerAtBottomLimit() && (augerPot.getCount() > desiredPosition) && (RobotStatus.isRunning()) && !augerOverride
						&& SensorConfig.getInstance().getGamepad().getButtonValue(EJoystickButtons.THREE))
				{
					// Lower Auger
					lowerAuger(lowerSpeed, desiredPosition);

					// Wait for thread
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);

					// If it gets stalled then stop
					if (augerPot.getCount() >= (augerPrevValue - STALL_RATE))
					{
						System.out.println("Auger Stalled Going Down");
						stopAugerLifter(false);
						return;
					}
					augerPrevValue = augerPot.getCount();
				}
			} else if (augerPot.getCount() < desiredPosition)
			{
				while (!isAugerAtTopLimit() && (augerPot.getCount() < desiredPosition) && (RobotStatus.isRunning()) && !augerOverride
						&& SensorConfig.getInstance().getGamepad().getButtonValue(EJoystickButtons.THREE))
				{
					// Raise Auger
					raiseAuger();

					// Wait for thread
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);

					// if it gets stalled then stop
					if (augerPot.getCount() <= (augerPrevValue + STALL_RATE))
					{
						System.out.println("Auger Stalled Going Up");
						stopAugerLifter(false);
						return;
					}
					augerPrevValue = augerPot.getCount();
				}
			}
		} else
		{

			if (augerPot.getCount() > desiredPosition)
			{
				// if the auger is higher than the position
				while (!isAugerAtBottomLimit() && (augerPot.getCount() > desiredPosition) && (RobotStatus.isRunning()) && !augerOverride)
				{
					// Lower Auger
					lowerAuger(lowerSpeed, desiredPosition);

					// Wait for thread
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);

					// If it gets stalled then stop
					if (augerPot.getCount() >= (augerPrevValue - STALL_RATE))
					{
						System.out.println("Auger Stalled Going Down");
						stopAugerLifter(false);
						return;
					}
					augerPrevValue = augerPot.getCount();
				}
			} else if (augerPot.getCount() < desiredPosition)
			{
				while (!isAugerAtTopLimit() && (augerPot.getCount() < desiredPosition) && (RobotStatus.isRunning()) && !augerOverride)
				{
					// Raise Auger
					raiseAuger();

					// Wait for thread
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);

					// if it gets stalled then stop
					if (augerPot.getCount() <= (augerPrevValue + STALL_RATE))
					{
						System.out.println("Auger Stalled Going Up");
						stopAugerLifter(false);
						return;
					}
					augerPrevValue = augerPot.getCount();
				}
			}
			// }
		}
		setAugerOverride(false);
		// Stop Auger when at desired position
		stopAugerLifter(false);
	}

	// Sequences

	@Override
	/**
	 * To go higher, subtract more
	 */
	public void moveShooterToPreset(EShooterPresets preset)
	{
		switch (preset)
		{
		case INTAKE: // When Intaking the ball
			lowerShooterToBottomLimit();
			break;
		case LOW_BAR: // Going under the low bar
			moveShooterToPosition(LOW_BAR_SHOOTER);
			break;
		case STANDARD_DEFENSE_SHOOTER: // Going over a standard defense
			moveShooterToPosition(STANDARD_DEFENSE_SHOOTER);
			break;
		case SHOOTER_POSITION_AT_DEFENSE_TWO_AUTO: // AutoShot Auto
			moveShooterToPosition(SHOOTER_POSITION_AT_POSITION_TWO_AUTO);
			break;
		case SHOOTER_POSITION_AT_DEFENSE_THREE_AUTO: // AutoShot Auto
			moveShooterToPosition(SHOOTER_POSITION_AT_POSITION_THREE_AUTO);
			break;
		case SHOOTER_POSITION_AT_DEFENSE_FOUR_AUTO: // AutoShot Auto
			moveShooterToPosition(SHOOTER_POSITION_AT_POSITION_FOUR_AUTO);
			break;
		case SHOOTER_POSITION_AT_DEFENSE_FIVE_AUTO: // AutoShot Auto
			moveShooterToPosition(SHOOTER_POSITION_AT_POSITION_FIVE_AUTO);
			break;
		case CENTER_SHOT_TELEOP: // Center Shooting Teleop
			moveShooterToPosition(SHOOTER_POSITION_CENTER_TELEOP);
			break;
		case CORNER_SHOT_TELEOP: // Corner Shooting Teleop
			moveShooterToPosition(SHOOTER_POSITION_CORNER_TELEOP);
			break;
		case LOW_BAR_SHOOT_HIGH_AUTO: // Low bar and shoot high auto
			moveShooterToPosition(SHOOTER_POSITION_LOW_BAR_SHOOT_HIGH_AUTO);
			break;
		default:
			// Do Nothing
			System.err.println("Shooter Defaulted: Doing Nothing, Probably Going Haywire");
		}
	}

	public void moveAugerToPreset(EAugerPresets preset)
	{
		switch (preset)
		{
		case LOW_BAR: // Going under the low bar
			lowerAugerToBottomLimit();
			break;
		case TOP_LIMIT: // NOT USED
			raiseAugerToTopLimit();
			break;
		case FOURTY_KAI: // Used at start of match
			moveAugerToPosition(FOURTY_KAI);
			break;
		case INTAKE: // Intake a ball
			moveAugerToPosition(INTAKE_AUGER);
			break;
		case STANDARD_DEFENSE_AUGER: // Going over a standard defense
			moveAugerToPosition(STANDARD_DEFENSE_AUGER);
			break;
		case FOURTY_KAI_AUTO: // Auton chevalDeFrize, LowBarShootHigh, All autoShot Positions
			if (augerPot.getCount() < FOURTY_KAI)
			{
				moveAugerToPosition(FOURTY_KAI);
			}
			break;
		default:
			// Do Nothing
			System.err.println("Auger Defaulted: Doing Nothing, Probably Going Haywire");
		}
	}

	@Override
	/**
	 * Used for Teleop
	 */
	public void shootSequenceHigh()
	{
		spinShooterWheels(SHOOT_SPEED_HIGH);

		SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

		if (!SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.SEVEN)
				&& !SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.EIGHT))
		{
			launchBoulder();
		} else
		{
			stopShooterWheels();
		}

	}

	/**
	 * Used for Teleop
	 */
	@Override
	public void shootSequenceLow()
	{
		spinShooterWheels(SHOOT_SPEED_LOW);

		SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

		if (!SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.SEVEN)
				&& !SensorConfig.getInstance().getRightJoystick().getButtonValue(EJoystickButtons.EIGHT))
		{
			launchBoulder();
		} else
		{
			stopShooterWheels();
		}
	}

	/**
	 * Used for Auton
	 */
	@Override
	public void shootSequenceHighAuto()
	{
		spinShooterWheels(SHOOT_SPEED_HIGH);

		moveAugerToPreset(EAugerPresets.FOURTY_KAI_AUTO);
		// if (augerPot.getCount() < FOURTY_KAI)
		// {
		// moveAugerToPosition(FOURTY_KAI); // Don't trust me
		// }
		SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

		if (augerPot.getCount() >= (FOURTY_KAI - 100))
		{
			launchBoulder();
		}

	}

	/**
	 * Used for Auton
	 */
	@Override
	public void shootSequenceLowAuto()
	{
		spinShooterWheels(SHOOT_SPEED_LOW_AUTO);

		if (augerPot.getCount() < LOW_SHOT_AUTO_AUGER)
		{
			moveAugerToPosition(LOW_SHOT_AUTO_AUGER);
		}
		SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

		launchBoulder();
	}

	@Override
	public void setAugerOverride(boolean launcherAndShooterOverride)
	{
		SmartDashboard.putBoolean("Auger Override", launcherAndShooterOverride);
		this.augerOverride = launcherAndShooterOverride;
	}

	@Override
	public void setShooterOverride(boolean launcherAndShooterOverride)
	{
		SmartDashboard.putBoolean("Shooter Override", launcherAndShooterOverride);
		this.shooterOverride = launcherAndShooterOverride;
	}

	@Override
	public void moveShooterAndAugerToPreset(EShooterPresets desiredShooter, EAugerPresets desiredAuger)
	{
		isAugerAtPreset = false;

		executor.submit(() ->
		{
			moveAugerToPreset(desiredAuger);
			isAugerAtPreset = true;
		});

		moveShooterToPreset(desiredShooter);

		while (!isAugerAtPreset && RobotStatus.isRunning())
		{
			SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
		}
	}

	@Override
	public void moveShooterAndAugerToPreset(int desiredShooter, int desiredAuger)
	{
		isAugerAtPreset = false;

		executor.submit(() ->
		{
			moveAugerToPosition(desiredAuger);
			isAugerAtPreset = true;
		});

		moveShooterToPosition(desiredShooter);

		while (!isAugerAtPreset && RobotStatus.isRunning())
		{
			SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
		}
	}

	@Override
	public void moveShooterAndAugerToPreset(EShooterPresets desiredShooter, int desiredAuger)
	{
		isAugerAtPreset = false;

		executor.submit(() ->
		{
			moveAugerToPosition(desiredAuger);
			isAugerAtPreset = true;
		});

		moveShooterToPreset(desiredShooter);

		while (!isAugerAtPreset && RobotStatus.isRunning())
		{
			SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
		}

	}

	@Override
	public void moveShooterAndAugerToPreset(int desiredShooter, EAugerPresets desiredAuger)
	{
		isAugerAtPreset = false;

		executor.submit(() ->
		{
			moveAugerToPreset(desiredAuger);
			isAugerAtPreset = true;
		});

		moveShooterToPosition(desiredShooter);

		while (!isAugerAtPreset && RobotStatus.isRunning())
		{
			SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
		}
	}
}