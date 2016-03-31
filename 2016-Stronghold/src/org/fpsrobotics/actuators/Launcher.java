package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.EJoystickButtons;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a Launcher for the 2016 Season FIRST Stronghold: A Servo pushes the
 * ball from a holding bay into two motors to fire the ball. It has a linear
 * actuator which controls the vertical angle of the shooter, along with two
 * limit switches and a potentiometer that define it's limits of travel. It also
 * has an auger that utilizes a motor to suck in the ball and a motor to move
 * itself vertically. The auger also has limit switches for it's outer reaches
 * of travel.
 *
 */
public class Launcher implements ILauncher
{
	// Lifter Functions
	private double LINEAR_ACTUATOR_SPEED = 0.5;

	// Shooter Functions
	private final double INTAKE_SPEED = -0.7;
	private double SHOOT_SPEED_HIGH = 1.0;
	private double SHOOT_SPEED_LOW = 0.6;
	private int RAMP_RATE = 3;

	// Auger Functions
	private final double INTAKE_AUGER_SPEED = 0.8;

	private double AUGER_LIFTER_SPEED_RAISE;
	private double AUGER_LIFTER_SPEED_LOWER;
	private double HIGH_VALUE_AUGER_SPEED = 0.8;
	private double LOW_VALUE_AUGER_SPEED = 0.4;

	private double TOP_LIMIT_POT_VALUE_SHOOTER;
	private double BOTTOM_LIMIT_POT_VALUE_SHOOTER;

	private int TOP_POT_LIMIT_AUGER;
	private int BOTTOM_POT_LIMIT_AUGER;
	private int HIGH_VALUE_AUGER;
	private int LOW_VALUE_AUGER;

	private boolean isAugerAtIntake = false;

	private ICANMotor leftShooterMotor, rightShooterMotor;
	private ICANMotor augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter;
	// private ILimitSwitch bottomLimitAuger, topLimitAuger; // TODO: Do we
	// need??
	private IPIDFeedbackDevice shooterPot, augerPot;

	private ISolenoid shooterActuator;

	// private boolean isAlpha;

	/**
	 * 
	 * @param leftShooterMotor
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
	public Launcher(ICANMotor leftShooterMotor, ICANMotor rightShooterMotor, ICANMotor shooterLifterMotor,
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
			// Shooter
			TOP_LIMIT_POT_VALUE_SHOOTER = 588;
			BOTTOM_LIMIT_POT_VALUE_SHOOTER = 2100;
			// Auger
			TOP_POT_LIMIT_AUGER = 1622;
			BOTTOM_POT_LIMIT_AUGER = 290;

			AUGER_LIFTER_SPEED_RAISE = 0.5;
			AUGER_LIFTER_SPEED_LOWER = 0.6;

			HIGH_VALUE_AUGER = 1400;
			LOW_VALUE_AUGER = 0;
		} else
		{
			// Shooter
			TOP_LIMIT_POT_VALUE_SHOOTER = 190;
			BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1300;
			// Auger
			TOP_POT_LIMIT_AUGER = 2100;
			BOTTOM_POT_LIMIT_AUGER = 850; // was 800

			AUGER_LIFTER_SPEED_RAISE = 0.5;
			AUGER_LIFTER_SPEED_LOWER = 0.5;

			HIGH_VALUE_AUGER = 1200;
			LOW_VALUE_AUGER = 900;
		}

		SmartDashboard.putNumber("Top Pot Limit Auger", TOP_POT_LIMIT_AUGER);
		SmartDashboard.putNumber("Bottom Pot Limit Auger", BOTTOM_POT_LIMIT_AUGER);

		SmartDashboard.putNumber("Top Pot Limit Shooter", TOP_LIMIT_POT_VALUE_SHOOTER);
		SmartDashboard.putNumber("Bottom Pot Limit Shooter", BOTTOM_LIMIT_POT_VALUE_SHOOTER);
	}

	// Lifter Functions
	private void lowerShooterToBottomLimit()
	{

		lowerShooter(false);
		while (!isShooterAtBottomLimit() && RobotStatus.isRunning())
			;
		stopShooterLifter();
	}

	private void raiseShooterToTopLimit()
	{

		raiseShooter(false);
		while (!isShooterAtTopLimit() && RobotStatus.isRunning())
			;
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
				shooterLifterMotor.setSpeed(Math.abs(LINEAR_ACTUATOR_SPEED) * 0.4);
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
//		if ((shooterPot.getCount() < (desiredPosition - 10)) || (shooterPot.getCount() > (desiredPosition + 10)))
		if ((shooterPot.getCount() < (desiredPosition + 10)) || (shooterPot.getCount() > (desiredPosition - 10))) //TODO
		{
			// If the shooter is less than the desired position
			if (shooterPot.getCount() > desiredPosition)
			{
				raiseShooter(false);

				// while this is so or we reach the top limit of travel
				while (!isShooterAtTopLimit() && (shooterPot.getCount() > desiredPosition) && (RobotStatus.isRunning()))
				{
					try
					{
						Thread.sleep(50);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				// If the shooter is greater than the desired position
			} else
			{
				lowerShooter(false);

				// while this is so or we reach the bottom limit of our travel
				while (!isShooterAtBottomLimit() && (shooterPot.getCount() < desiredPosition)
						&& (RobotStatus.isRunning()))
				{
					try
					{
						Thread.sleep(50);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
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
		if (!isAugerAtIntake)
		{
			moveAugerToPreset(EAugerPresets.INTAKE);
			moveShooterToPreset(EShooterPresets.INTAKE);
			isAugerAtIntake = true;
		}
		augerIntakeMotor.setSpeed(INTAKE_AUGER_SPEED);
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
				if (augerPot.getCount() > (TOP_POT_LIMIT_AUGER - 100))
				{
					augerLifterMotor.setSpeed(Math.abs(AUGER_LIFTER_SPEED_RAISE - 0.1));
				} else if (augerPot.getCount() > (HIGH_VALUE_AUGER))
				{
					augerLifterMotor.setSpeed(Math.abs(HIGH_VALUE_AUGER_SPEED));
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
					try
					{
						Thread.sleep(RAMP_RATE);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
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
					try
					{
						Thread.sleep(RAMP_RATE);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
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
		lowerAuger(AUGER_LIFTER_SPEED_LOWER);
	}

	private void lowerAuger(double speed)
	{
		if (!isAugerAtBottomLimit())
		{
			if (augerLifterMotor.getSpeed() < 0)
			{
				augerLifterMotor.setSpeed(-Math.abs(speed));
			} else if (augerPot.getCount() < (LOW_VALUE_AUGER))
			{
				augerLifterMotor.setSpeed(Math.abs(LOW_VALUE_AUGER_SPEED));
			} else
			{
				rampUpMotor(-Math.abs(speed));
			}
		} else
		{
			stopAugerLifter(true);
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
					try
					{
						Thread.sleep(RAMP_RATE);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
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
					try
					{
						Thread.sleep(RAMP_RATE);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
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
	public void moveAugerToPosition(int desiredPosition)
	{
		double augerPrevValue = augerPot.getCount();

		// if ((augerPot.getCount() < (desiredPosition + 10)) ||
		// (augerPot.getCount() > (desiredPosition - 10)))
		if ((augerPot.getCount() < (desiredPosition - 10)) || (augerPot.getCount() > (desiredPosition + 10)))
		{
			// if the auger is higher than the position
			if (desiredPosition < augerPot.getCount())
			{
				lowerAuger();

				while (!isAugerAtBottomLimit() && (desiredPosition < augerPot.getCount()) && (RobotStatus.isRunning()))
				{
					try
					{
						Thread.sleep(50);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					if (augerPot.getCount() >= (augerPrevValue - 5))
					// if it gets stalled then stop
					{
						System.out.println("Auger Stalled");
						stopAugerLifter(false);
						return;
					}

					augerPrevValue = augerPot.getCount();

				}
			} else
			{
				raiseAuger();

				while (!isAugerAtTopLimit() && (augerPot.getCount() < desiredPosition) && (RobotStatus.isRunning()))
				{
					try
					{
						Thread.sleep(50);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					if (augerPot.getCount() <= (augerPrevValue + 5))
					// if it gets stalled then stop
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
			// ALPHA
			// Top Alpha: 588
			// Bottom Alpha: 2100
			switch (preset)
			{
			case INTAKE: // USED FOR DRIVER PRESET
				lowerShooterToBottomLimit();
				break;
			case LOW_BAR: // USED FOR AUTONOMOUS: ANY LOW BAR PRESET
				moveShooterToPosition(1800);
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
				moveShooterToPosition(1000);
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
			// ALPHA
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
				moveAugerToPosition(1416);
				break;
			case SHOOT_HIGH: // USED FOR AUTONOMOUS AND DRIVER PRESET
				moveAugerToPosition(1416);
				break;
			case INTAKE: // BUTTON DRIVER PRESET
				moveAugerToPosition(298); // Used to be 465
				break;
			case SHOOT_LOW: // USED FOR AUTONOMOUS AND DRIVER PRESET
				moveAugerToPosition(716);
				break;
			case STANDARD_DEFENSE: // USED FOR DRIVER PRESET
				moveAugerToPosition(983);
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
				moveAugerToPosition(858);
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

		if (RobotStatus.isAlpha())
		{
			if (augerPot.getCount() < (1416))
			{
				moveAugerToPreset(EAugerPresets.SHOOT_HIGH);
			}
		} else
		{
			if (augerPot.getCount() < (1650))
			{
				moveAugerToPreset(EAugerPresets.SHOOT_HIGH);
			}
		}

		moveAugerToPreset(EAugerPresets.SHOOT_HIGH);

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
		if (RobotStatus.isAlpha())
		{
			if (augerPot.getCount() < (716))
			{
				moveAugerToPreset(EAugerPresets.SHOOT_LOW);
			}
		} else
		{
			if (augerPot.getCount() < (1276))
			{
				moveAugerToPreset(EAugerPresets.SHOOT_LOW);
			}
		}

		spinShooterWheels(SHOOT_SPEED_LOW + 0.1); // 0.7

		SensorConfig.getInstance().getTimer().waitTimeInMillis(500);

		launchBoulder();
	}
	

//	// TODO
//	public void moveShooterAndAugerToPosition(double desiredShooter, double desiredAuger)
//	{
//
//		double augerPrevValue = augerPot.getCount();
//
//		boolean isShooterGood = false;
//		boolean isAugerGood = false;
//
//		if ((shooterPot.getCount() < (desiredShooter - 10)) || (shooterPot.getCount() > (desiredShooter + 10)))
//		{
//			// If the shooter is less than the desired position
//			if (shooterPot.getCount() > desiredShooter)
//			{
//				raiseShooter(false);
//			} else
//			{
//				lowerShooter(false);
//			}
//		} else
//		{
//			isShooterGood = true;
//		}
//
//		if ((augerPot.getCount() < (desiredAuger + 10)) || (augerPot.getCount() > (desiredAuger - 10)))
//		{
//			// if the auger is higher than the position
//			if (desiredAuger < augerPot.getCount())
//			{
//				lowerAuger();
//			} else
//			{
//				raiseAuger();
//			}
//		} else
//		{
//			isAugerGood = true;
//		}
//
//		while ((!isShooterGood || !isAugerGood) && RobotStatus.isRunning())
//		{
//			// Shooter
//			if ((!isShooterAtTopLimit() && (shooterPot.getCount() > desiredShooter))
//					|| (!isShooterAtBottomLimit() && (shooterPot.getCount() < desiredShooter)))
//			{
//				// Keep Going
//			} else
//			{
//				stopShooterLifter();
//				isShooterGood = true;
//			}
//
//			// Auger
//			if (!isAugerGood && (!isAugerAtBottomLimit() && (desiredAuger < augerPot.getCount())
//					|| (!isAugerAtBottomLimit() && (desiredAuger < augerPot.getCount()))))
//			{
//				// If stalled then stop
//				if ((augerPot.getCount() >= (augerPrevValue - 5)) || (augerPot.getCount() <= (augerPrevValue + 5)))
//				{
//					System.out.println("Auger Stalled");
//					stopAugerLifter(false);
//					isAugerGood = true;
//				}
//
//				augerPrevValue = augerPot.getCount();
//				// Otherwise keep going
//			} else
//			{
//				stopAugerLifter(false);
//				isAugerGood = true;
//			}
//
//			try
//			{
//				Thread.sleep(50);
//			} catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
//		}
//		stopShooterLifter();
//		stopAugerLifter(false);
//	}
}
