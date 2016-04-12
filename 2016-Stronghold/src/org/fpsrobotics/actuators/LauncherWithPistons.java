package org.fpsrobotics.actuators;

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
	private double augerPrevValue;
	private final int SLOW_AUGER_DISTANCE = 100;
	private final double AUGER_SPEED_RAISE = 0.5;
	private final double AUGER_SPEED_LOWER = 0.5;
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
	private final int HIGH_VALUE_AUGER;
	private final int FOURTY_KAI;
	private final int INTAKE_AUGER;
	private final int STANDARD_DEFENSE_AUGER;
	private final int END_GAME;

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
	public LauncherWithPistons(ICANMotor leftShooterMotor, ICANMotor rightShooterMotor, ICANMotor shooterLifterMotor,
			ISolenoid shooterActuator, ILimitSwitch shooterBottomLimit, ILimitSwitch shooterTopLimit,
			IPIDFeedbackDevice shooterPot, ICANMotor augerIntakeMotor, ICANMotor augerLifterMotor,
			ILimitSwitch bottomLimitAuger, ILimitSwitch topLimitAuger, IPIDFeedbackDevice augerPot)
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
			TOP_LIMIT_AUGER = 2000; // 1840
			BOTTOM_LIMIT_AUGER = 350;
			LOW_BAR_AUGER_FOR_SHOOTER = 600; // must be just above highest collision point
			// Presets
			LOW_BAR_SHOOTER = 1800; // must be just below lowest collision point
			HIGH_VALUE_AUGER = 1400;
			FOURTY_KAI = 1300; // used to be "shoot high"
			STANDARD_DEFENSE_AUGER = 370; // should be above LOW_BAR_AUGER_FOR_SHOOTER // 601
			INTAKE_AUGER = 420;
			END_GAME = FOURTY_KAI;
		} else
		{
			// Shooter
			BOTTOM_LIMIT_SHOOTER = 1200;
			TOP_LIMIT_SHOOTER = 190;
			// AUGER
			TOP_LIMIT_AUGER = 2200;
			BOTTOM_LIMIT_AUGER = 825;
			LOW_BAR_AUGER_FOR_SHOOTER = 0; // must be just above highest collision point 
			//1176 with pistons
			// Presets
			LOW_BAR_SHOOTER = 1130; // must be just below lowest collision point
			HIGH_VALUE_AUGER = 1200;
			FOURTY_KAI = 1650; // used to be "shoot high"
			STANDARD_DEFENSE_AUGER = 1100; // used to be "shoot low"
			INTAKE_AUGER = 1050;
			END_GAME = FOURTY_KAI;
		}

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
			if ((augerPot.getCount() < (LOW_BAR_AUGER_FOR_SHOOTER)) && (shooterPot.getCount() < LOW_BAR_SHOOTER)
					&& (!manualLowerAuger))
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
		// if shooter is above top limit pot value, or top limit is hit
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
		// if shooter is below bottom limit pot value, or bottom limit is hit
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
			// while shooter is less than the desired position or we reach the top limit of travel
			while (!isShooterAtTopLimit() && (shooterPot.getCount() > desiredPosition) && (RobotStatus.isRunning()))
			{
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
				raiseShooter(false);
			}

			// while shooter is greater than the desired position or we reach the bottom limit of our travel
			while (!isShooterAtBottomLimit() && (shooterPot.getCount() < desiredPosition) && (RobotStatus.isRunning()))
			{
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
				lowerShooter(false);
			}
		}
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
			
			moveShooterToPreset(EShooterPresets.INTAKE);
			moveAugerToPreset(EAugerPresets.INTAKE);

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
		SensorConfig.getInstance().getTimer().waitTimeInMillis(150);
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
		lowerAuger(1.0);
	}

	private void lowerAugerTest(double speed)
	{
		if (augerPot.getCount() < (BOTTOM_LIMIT_AUGER))
		{
			// Slow to a halt
			stopAugerLifter(true);
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
	}

	// if there is no desired position
	private void lowerAuger(double speed)
	{
		if (!autoRaiseAuger)
		{
			manualLowerAuger = true;
			// If shooter is above "Low Bar Preset," within range of getting hit
			if ((shooterPot.getCount() > LOW_BAR_SHOOTER))
			{
				// if auger is nearing the bottom limit
//				if (augerPot.getCount() < (BOTTOM_LIMIT_AUGER + SLOW_AUGER_DISTANCE))
				if (augerPot.getCount() < (BOTTOM_LIMIT_AUGER))
				{
					// Slow to a halt
//					stopAugerLifter(true);
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
			{
				// if auger is nearing the collision point
//				if (augerPot.getCount() < (LOW_BAR_AUGER_FOR_SHOOTER + SLOW_AUGER_DISTANCE))
				if (augerPot.getCount() < (LOW_BAR_AUGER_FOR_SHOOTER))
				{
					// Slow to a halt
//					stopAugerLifter(true);
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

	/**
	 * Used for moveAugerToPosition()
	 * 
	 * @param speed
	 * @param setPoint
	 */
	private void lowerAuger(double speed, int desiredPosition)
	{
		// If you are still going to hit the shooter before you hit your destination
//		if (desiredPosition < LOW_BAR_AUGER_FOR_SHOOTER)
//		{
//			lowerAuger(speed);
//		} else
//		// If you are still going to hit your destination before you hit the shooter
//		{
			if (!autoRaiseAuger)
			{
				manualLowerAuger = true;

				// if auger is nearing the collision point
//				if (augerPot.getCount() < (desiredPosition + SLOW_AUGER_DISTANCE))
				if (augerPot.getCount() < (desiredPosition))
				{
					// Slow to a halt
//					stopAugerLifter(true);
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
//			}
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
		augerPrevValue = augerPot.getCount();
		
		while (!isAugerAtBottomLimit() && (augerPot.getCount() > BOTTOM_LIMIT_AUGER) && (RobotStatus.isRunning()))
		{
			lowerAuger(AUGER_SPEED_LOWER, BOTTOM_LIMIT_AUGER);

			// Wait for thread
			SensorConfig.getInstance().getTimer().waitTimeInMillis(50);

			// If it gets stalled then stop
			if (augerPot.getCount() >= (augerPrevValue - 5))
			{
				System.out.println("Auger Stalled");
				stopAugerLifter(false);
				return;
			}
			augerPrevValue = augerPot.getCount();
		}
		stopAugerLifter(false);
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
	 * ALWAYS MOVE SHOOTER TO POSITION BEFORE AUGER
	 */
	public void moveAugerToPosition(int desiredPosition)
	{
		moveAugerToPosition(desiredPosition, AUGER_SPEED_LOWER);
	}

	@Override
	//USed for endgame
	public void moveAugerToPosition(int desiredPosition, double lowerSpeed)
	{
		augerPrevValue = augerPot.getCount();

		if ((augerPot.getCount() < (desiredPosition - 10)) || (augerPot.getCount() > (desiredPosition + 10)))
		{
			// if the auger is higher than the position
			while (!isAugerAtBottomLimit() && (augerPot.getCount() > desiredPosition) && (RobotStatus.isRunning()))
			{
				lowerAuger(lowerSpeed, desiredPosition);

				// Wait for thread
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);

				// If it gets stalled then stop
				if (augerPot.getCount() >= (augerPrevValue - 5))
				{
					System.out.println("Auger Stalled");
					stopAugerLifter(false);
					return;
				}
				augerPrevValue = augerPot.getCount();
			}

			while (!isAugerAtTopLimit() && (augerPot.getCount() < desiredPosition) && (RobotStatus.isRunning()))
			{
				raiseAuger();

				// Wait for thread
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);

				// if it gets stalled then stop
				if (augerPot.getCount() <= (augerPrevValue + 5))
				{
					System.out.println("Auger Stalled");
					stopAugerLifter(false);
					return;
				}
				augerPrevValue = augerPot.getCount();
			}
		}
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
		case INTAKE:
			lowerShooterToBottomLimit();
			break;
		case LOW_BAR:
			moveShooterToPosition(LOW_BAR_SHOOTER);
			break;
		case STANDARD_DEFENSE_SHOOTER:
			moveShooterToPosition(1390);
			break;
		default:
			// Do Nothing
		}
	}

	public void moveAugerToPreset(EAugerPresets preset)
	{
		switch (preset)
		{
		case LOW_BAR:
			lowerAugerToBottomLimit();
			break;
		case TOP_LIMIT: // USED TO LIFT ROBOT
			raiseAugerToTopLimit();
			break;
		case FOURTY_KAI: // USED AT START OF MATCH
			moveAugerToPosition(FOURTY_KAI);
			break;
		case INTAKE:
			moveAugerToPosition(INTAKE_AUGER);
			break;
		case FOURTY_KAI_END_GAME:
			moveAugerToPosition(END_GAME, 1.0);
			break;
		case STANDARD_DEFENSE_AUGER:
			if (augerPot.getCount() < (STANDARD_DEFENSE_AUGER - 15))
			{
				moveAugerToPosition(STANDARD_DEFENSE_AUGER); //TODO: Is this auger condition Okay???
			}
			break;
		default:
			// Do Nothing
		}
	}

	@Override
	/**
	 * Used for Teleop
	 */
	public void shootSequenceHigh()
	{
		spinShooterWheels(SHOOT_SPEED_HIGH);

		// // To avoid the wait if possible
		// if (augerPot.getCount() < (FOURTY_KAI - 50))
		// {
		// moveAugerToPosition(FOURTY_KAI);
		// } else if (augerPot.getCount() < FOURTY_KAI)
		// {
		// moveAugerToPosition(FOURTY_KAI);
		// SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
		// } else
		// {
		// SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
		// }

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

		// // To avoid the wait if possible
		// if (augerPot.getCount() < (STANDARD_DEFENSE_AUGER - 50))
		// {
		// moveAugerToPosition(STANDARD_DEFENSE_AUGER);
		// } else if (augerPot.getCount() < STANDARD_DEFENSE_AUGER)
		// {
		// moveAugerToPosition(STANDARD_DEFENSE_AUGER);
		// SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
		// } else
		// {
		// SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
		// }
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

		// To avoid the wait if possible
		if (augerPot.getCount() < (FOURTY_KAI - 50))
		{
			moveAugerToPosition(FOURTY_KAI);
		} else if (augerPot.getCount() < FOURTY_KAI)
		{
			moveAugerToPosition(FOURTY_KAI);
			SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
		} else
		{
			SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
		}

		launchBoulder();
	}

	/**
	 * Used for Auton
	 */
	@Override
	public void shootSequenceLowAuto()
	{
		spinShooterWheels(SHOOT_SPEED_LOW_AUTO);

		// To avoid the wait if possible
		if (augerPot.getCount() < (STANDARD_DEFENSE_AUGER - 50))
		{
			moveAugerToPosition(STANDARD_DEFENSE_AUGER);
		} else if (augerPot.getCount() < STANDARD_DEFENSE_AUGER)
		{
			moveAugerToPosition(STANDARD_DEFENSE_AUGER);
			SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
		} else
		{
			SensorConfig.getInstance().getTimer().waitTimeInMillis(500);
		}

		launchBoulder();
	}
}