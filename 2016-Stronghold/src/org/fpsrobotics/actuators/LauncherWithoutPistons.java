package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a Flywheel Launcher for the 2016 Season FIRST Stronghold: A Servo pushes the ball from a holding bay into two
 * motors to fire the ball. It has a linear actuator which controls the vertical angle of the shooter, along with two
 * limit switches and a potentiometer that define it's limits of travel. It also has an auger that utilizes a motor to
 * suck in the ball and a motor to move itself vertically. The auger also has limit switches for it's outer reaches of
 * travel.
 *
 * Update 4/4/16: The auger should move itself automatically if the shooter will hit [the auger] when the shooter is
 * being raised manually. The auger will detect if it is nearing the shooter or the ground, and slow down to a halt
 * before it collides.
 */
public class LauncherWithoutPistons implements ILauncher
{
	// Lifter Functions
	private double LINEAR_ACTUATOR_SPEED = 0.5; // intermediate speed to move
												// the shooter
	private double SLOW_FACTOR = 0.6; // teleop driver button 1 - move the
										// shooter slower

	private double TOP_LIMIT_POT_VALUE_SHOOTER; // top limit the shooter can
												// move to
	private double BOTTOM_LIMIT_POT_VALUE_SHOOTER; // bottom limit the shooter
													// can move to

	// Shooter Functions
	private final double INTAKE_SPEED = -0.7; // intake the boulder
	private double SHOOT_SPEED_HIGH = 1.0; // shoot high speed
	private double SHOOT_SPEED_LOW = 0.6; // shoot low speed

	// Auger Functions
	private final double INTAKE_AUGER_SPEED = 0.8; // intake the boulder
	private double AUGER_LIFTER_SPEED_RAISE; // intermediate speed to raise the auger
	private double AUGER_LIFTER_SPEED_LOWER; // intermediate speed to lower the auger
	private double HIGH_VALUE_AUGER_SPEED = 0.8; // speed to move the auger at higher values - towards top of path
	private double LOW_VALUE_AUGER_SPEED = -0.3; //TODO this is negative
	// speed to move the auger at lower values - towards bottom of path
	private int RAMP_RATE = 3; // to ramp up the auger

	private int TOP_POT_LIMIT_AUGER; // top limit the auger can move to
	private int BOTTOM_POT_LIMIT_AUGER;// bottom limit the auger can move to
	private int HIGH_VALUE_AUGER; // the pot position to define whether the auger is at higher values
	private int LOW_VALUE_AUGER; // the pot position to define whether the auger is at lower values
	private int AUGER_SHOOT_HIGH_POT_VALUE; // the pot position to define where the auger should move to in autonomous
	private int AUGER_SHOOT_LOW_POT_VALUE;// the pot position to define where the auger should move to in autonomous

	private double augerPrevValue; // for moveAugerToPosition();

	private int LOW_BAR_SHOOTER_VALUE; // the highest point for the shooter at which the shooter and auger will not
										// collide
	private int AUGER_WITHIN_RANGE_OF_HITTING_SOMETHING; // used for moving the auger if it will hit the shooter
															// or ground

	private boolean isAugerAtIntake = false; // used to stop intake boulder

	private ICANMotor leftShooterMotor, rightShooterMotor;
	private ICANMotor augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter;
	// private ILimitSwitch bottomLimitAuger, topLimitAuger;

	private IPIDFeedbackDevice shooterPot, augerPot;

	private ISolenoid shooterActuator;

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
	public LauncherWithoutPistons(ICANMotor leftShooterMotor, ICANMotor rightShooterMotor, ICANMotor shooterLifterMotor,
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
		// this.bottomLimitAuger = bottomLimitAuger;
		// this.topLimitAuger = topLimitAuger;
		this.augerPot = augerPot;

		if (RobotStatus.isAlpha())
		{
			// TODO: TUNE ALPHA
			// Shooter
			TOP_LIMIT_POT_VALUE_SHOOTER = 588;
			BOTTOM_LIMIT_POT_VALUE_SHOOTER = 2100;
			// Auger
			TOP_POT_LIMIT_AUGER = 1920;
			BOTTOM_POT_LIMIT_AUGER = 450;
			// Shooter-Auger Conflict Stuff
			LOW_BAR_SHOOTER_VALUE = 1700;

			AUGER_LIFTER_SPEED_RAISE = 0.5;
			AUGER_LIFTER_SPEED_LOWER = 0.6;

			HIGH_VALUE_AUGER = 1400;
			LOW_VALUE_AUGER = 794;

			AUGER_SHOOT_HIGH_POT_VALUE = 1416;
			AUGER_SHOOT_LOW_POT_VALUE = 716;
		} else
		{
			// Shooter
			TOP_LIMIT_POT_VALUE_SHOOTER = 190;
			BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1300;
			// Auger
			TOP_POT_LIMIT_AUGER = 2200;
			BOTTOM_POT_LIMIT_AUGER = 900; // was 950
			// Shooter-Auger Conflict Stuff
			LOW_BAR_SHOOTER_VALUE = 1130; // used to be 1000 before pistons were
											// added

			AUGER_LIFTER_SPEED_RAISE = 0.5; 
			AUGER_LIFTER_SPEED_LOWER = 1.0;//was 0.5, 1.0 for lifter

			HIGH_VALUE_AUGER = 1200;
			LOW_VALUE_AUGER = 900;

			AUGER_SHOOT_HIGH_POT_VALUE = 1650;
			AUGER_SHOOT_LOW_POT_VALUE = 1276;
		}

		AUGER_WITHIN_RANGE_OF_HITTING_SOMETHING = BOTTOM_POT_LIMIT_AUGER + 120; // TODO: Confirm this value with Presets

		SmartDashboard.putNumber("Top Pot Limit Auger", TOP_POT_LIMIT_AUGER);
		SmartDashboard.putNumber("Bottom Pot Limit Auger", BOTTOM_POT_LIMIT_AUGER);

		SmartDashboard.putNumber("Top Pot Limit Shooter", TOP_LIMIT_POT_VALUE_SHOOTER);
		SmartDashboard.putNumber("Bottom Pot Limit Shooter", BOTTOM_LIMIT_POT_VALUE_SHOOTER);
	}

	// Lifter Functions
	private void lowerShooterToBottomLimit()
	{
		while (!isShooterAtBottomLimit() && RobotStatus.isRunning())
		{
			lowerShooter(false);
		}
		stopShooterLifter();
	}

	private void raiseShooterToTopLimit()
	{
		while (!isShooterAtTopLimit() && RobotStatus.isRunning())
		{
			raiseShooter(false);
		}
		stopShooterLifter();
	}

	@Override
	/**
	 * 
	 * @param slow
	 *            - Used for Driver Control - Gamepad Button 1
	 */

	public void raiseShooter(boolean slow)
	{
		if (!isShooterAtTopLimit())
		{
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
	/**
	 * 
	 * @param slow
	 *            - Used for Driver Control - Gamepad Button 1
	 */
	public void lowerShooter(boolean slow)
	{
		if (!isShooterAtBottomLimit())
		{
			if (slow)
			{
				shooterLifterMotor.setSpeed(-Math.abs(LINEAR_ACTUATOR_SPEED) * 0.4);
			} else
			{
				shooterLifterMotor.setSpeed(-Math.abs(LINEAR_ACTUATOR_SPEED));
			}
		} else
		{
			stopShooterLifter();
		}
	}

	@Override
	public void stopShooterLifter()
	{
		shooterLifterMotor.stop();
	}

	private boolean isShooterAtTopLimit()
	{
		// if shooter is above top limit pot value, or top limit is hit
		if ((shooterPot.getCount() <= TOP_LIMIT_POT_VALUE_SHOOTER) || topLimitShooter.isHit())
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
		if ((shooterPot.getCount() >= BOTTOM_LIMIT_POT_VALUE_SHOOTER) || bottomLimitShooter.isHit())
		{
			return true;
		} else
		{
			return false;
		}
	}

	@Override
	public void moveShooterToPosition(double desiredPosition)
	{
		if ((shooterPot.getCount() < (desiredPosition - 50)) || (shooterPot.getCount() > (desiredPosition + 50)))
		{
			// If the shooter is less than the desired position
			if (shooterPot.getCount() > desiredPosition)
			{
				// while shooter is less than the desired position or we reach the top limit of travel
				while (!isShooterAtTopLimit() && (shooterPot.getCount() > desiredPosition) && (RobotStatus.isRunning()))
				{
					raiseShooter(false);
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
				}
				// If the shooter is greater than the desired position
			} else
			{
				// while shooter is greater than the desired position or we reach the bottom limit of our travel
				while (!isShooterAtBottomLimit() && (shooterPot.getCount() < desiredPosition)
						&& (RobotStatus.isRunning()))
				{
					lowerShooter(false);
					SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
				}
			}
		}
		stopShooterLifter();
	}

	// Shooter Functions
	@Override
	public void intakeBoulder()
	{

		rightShooterMotor.setSpeed(INTAKE_SPEED);
		leftShooterMotor.setSpeed(INTAKE_SPEED);

		augerIntakeMotor.setSpeed(INTAKE_AUGER_SPEED);
		
		if (!isAugerAtIntake)
		{
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
		spinShooterWheels(SHOOT_SPEED_LOW - 0.1);
	}

	private void spinShooterWheels(double speed)
	{
		leftShooterMotor.setSpeed(speed);
		rightShooterMotor.setSpeed(speed);
	}

	// private void jostle()
	// {
	// stopShooterWheels();
	//
	// leftShooterMotor.setSpeed(0.4);
	// rightShooterMotor.setSpeed(0.4);
	//
	// SensorConfig.getInstance().getTimer().waitTimeInMillis(400);
	// stopShooterWheels();
	// }

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
		raiseAuger(AUGER_LIFTER_SPEED_RAISE);
	}

	// Pot increases as Auger goes up
	private void raiseAuger(double speed)
	{
		if (!isAugerAtTopLimit())
		{
			if (augerLifterMotor.getSpeed() > 0)
			{
				// If auger is nearing the VERY Top, slow down
				if (augerPot.getCount() > (TOP_POT_LIMIT_AUGER - 100))
				{
					augerLifterMotor.setSpeed(Math.abs(AUGER_LIFTER_SPEED_RAISE - 0.1));
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
	public void lowerAuger()
	{
		System.out.println("Lower Auger");
		lowerAuger(AUGER_LIFTER_SPEED_LOWER);
	}

	private void lowerAuger(double speed)
	{
//		if (!isAugerAtBottomLimit())
		if (augerPot.getCount() < (500)) //TODO: this is alpha
		{
			// slow down and stop
			stopAugerLifter(false); //was true
			System.out.println("Stopped at lowerauger()");
		} else
		{
			if (augerPot.getCount() < (600))
			{
				augerLifterMotor.setSpeed(-Math.abs(-0.3));
			} else if (augerLifterMotor.getSpeed() < 0)
			// else move regularly
			{
				augerLifterMotor.setSpeed(-Math.abs(speed));
			} else
			// ramp up initially
			{
				rampUpMotor(-Math.abs(speed));
			}
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
	}

	@Override
	public void spinAugerWheels()
	{
		augerIntakeMotor.setSpeed(INTAKE_AUGER_SPEED);
	}

	@Override
	public void stopAugerWheels()
	{
		augerIntakeMotor.stop();
	}

	private void raiseAugerToTopLimit()
	{
		moveAugerToPosition(TOP_POT_LIMIT_AUGER);
	}

	private void lowerAugerToBottomLimit()
	{
		moveAugerToPosition(BOTTOM_POT_LIMIT_AUGER);
	}

	private boolean isAugerAtBottomLimit()
	{
		if (augerPot.getCount() < BOTTOM_POT_LIMIT_AUGER)
		{
			return true;
		} else
		{
			return false;
		}
	}

	private boolean isAugerAtTopLimit()
	{
		if (augerPot.getCount() >= TOP_POT_LIMIT_AUGER)
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
		augerPrevValue = augerPot.getCount();

		if ((augerPot.getCount() < (desiredPosition - 10)) || (augerPot.getCount() > (desiredPosition + 10)))
		{
			// if the auger is higher than the position
			if (augerPot.getCount() > desiredPosition)
			{
				while (!isAugerAtBottomLimit() && (augerPot.getCount() > desiredPosition) && (RobotStatus.isRunning()))
				{
					// lowerAuger() uses conditions
					lowerAuger();

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
			} else
			{
				while (!isAugerAtTopLimit() && (augerPot.getCount() < desiredPosition) && (RobotStatus.isRunning()))
				{
					// raiseAuger() uses conditions
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
		if (RobotStatus.isAlpha())
		{
			// ALPHA //TODO: Tune Alpha for Shooter
			// Top Alpha: 588
			// Bottom Alpha: 2100
			switch (preset)
			{
			case INTAKE: // USED FOR DRIVER PRESET
				lowerShooterToBottomLimit();
				break;
			case LOW_BAR: // USED FOR AUTONOMOUS: ANY LOW BAR PRESET
				moveShooterToPosition(LOW_BAR_SHOOTER_VALUE);
				break;
			case TOP_LIMIT: // USED FOR AUTONOMOUS
				raiseShooterToTopLimit();
				break;
			default:
				// Do Nothing
			}
		} else
		{
			// BETA
			// Top Beta: 190
			// Bottom Beta: 1300
			switch (preset)
			{
			case INTAKE: // USED FOR DRIVER PRESET
				moveShooterToPosition(2863);
				break;
			case LOW_BAR: // USED FOR AUTONOMOUS: ANY LOW BAR PRESET
				moveShooterToPosition(LOW_BAR_SHOOTER_VALUE);
				break;
			case TOP_LIMIT: // USED FOR AUTONOMOUS
				raiseShooterToTopLimit();
				break;
			default:
				// Do Nothing
			}
		}
	}

	public void moveAugerToPreset(EAugerPresets preset)
	{
		if (RobotStatus.isAlpha())
		{
			// ALPHA //TODO: Tune Alpha for Auger
			// Top Alpha 1622
			// Bottom Alpha 290
			switch (preset)
			{
			case BOTTOM_LIMIT: // USED FOR AUTONOMOUS: ANY LOW BAR PRESET
				lowerAugerToBottomLimit();
				break;
			case TOP_LIMIT: // USED FOR AUTONOMOUS: TO LIFT ROBOT
				raiseAugerToTopLimit();
				break;
			case FOURTY_KAI: // USED AT START OF MATCH
				moveAugerToPosition(1338);
				break;
			case SHOOT_HIGH: // USED FOR AUTONOMOUS AND DRIVER PRESET
				moveAugerToPosition(1338);
				break;
			case INTAKE: // BUTTON DRIVER PRESET
				moveAugerToPosition(550); // Used to be 465
				break;
			case SHOOT_LOW: // USED FOR AUTONOMOUS AND DRIVER PRESET
				moveAugerToPosition(716);
				break;
			case STANDARD_DEFENSE: // USED FOR DRIVER PRESET
				moveAugerToPosition(711);
				break;
			default:
				// Do Nothing
			}
		} else
		{
			// BETA
			// Top Beta 2100
			// Bottom Beta 850
			switch (preset)
			{
			case BOTTOM_LIMIT: // USED FOR AUTONOMOUS: ANY LOW BAR PRESET
				lowerAugerToBottomLimit();
				break;
			case TOP_LIMIT: // USED FOR AUTONOMOUS: TO LIFT ROBOT
				raiseAugerToTopLimit();
				break;
			case FOURTY_KAI: // USED AT START OF MATCH
				moveAugerToPosition(1650);
				break;
			case SHOOT_HIGH: // USED FOR AUTONOMOUS AND DRIVER PRESET
				moveAugerToPosition(1650);
				break;
			case INTAKE: // BUTTON DRIVER PRESET
				moveAugerToPosition(1050); // 858 without cylinders
				break;
			case SHOOT_LOW: // USED FOR AUTONOMOUS AND DRIVER PRESET
				moveAugerToPosition(1276);
				break;
			case STANDARD_DEFENSE: // USED FOR DRIVER PRESET
				moveAugerToPosition(983);
				break;
			default:
				// Do Nothing
			}
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
		if (augerPot.getCount() < (AUGER_SHOOT_HIGH_POT_VALUE))
		{
			moveAugerToPreset(EAugerPresets.SHOOT_HIGH);
		}

		spinShooterWheels(SHOOT_SPEED_HIGH);

		SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

		launchBoulder();
	}

	/**
	 * Used for Auton
	 */
	@Override
	public void shootSequenceLowAuto()
	{
		if (augerPot.getCount() < AUGER_SHOOT_LOW_POT_VALUE)
		{
			moveAugerToPreset(EAugerPresets.SHOOT_LOW);
		}

		spinShooterWheels(SHOOT_SPEED_LOW + 0.1); // 0.7

		SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

		launchBoulder();
	}
}