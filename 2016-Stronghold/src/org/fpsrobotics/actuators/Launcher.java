package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
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
	private final double LINEAR_ACTUATOR_SPEED = 0.7; // Used to be 0.5

	// Shooter Functions
	private final double INTAKE_SPEED = -0.7;
	private double SHOOT_SPEED_HIGH = 0.95;
	private double SHOOT_SPEED_LOW = 0.6;
	private int RAMP_RATE = 3;

	// Auger Functions
	private final double INTAKE_AUGER_SPEED = 0.8;

	private final double AUGER_LIFTER_SPEED_RAISE = 0.5;
	private final double AUGER_LIFTER_SPEED_LOWER = 0.4;
	private double HIGH_VALUE_AUGER_SPEED = 0.5;

	private double TOP_LIMIT_POT_VALUE_SHOOTER;
	private double BOTTOM_LIMIT_POT_VALUE_SHOOTER;

	private int TOP_POT_LIMIT_AUGER;
	private int BOTTOM_POT_LIMIT_AUGER;
	private double HIGH_VALUE_AUGER;

	// Calibrate Functions

//	private final double CALIBRATION_SPEED = 0.1;

	private boolean isAugerCalibrated;
	private boolean isAugerAtIntake = false;

	private ICANMotor leftShooterMotor, rightShooterMotor;
	private ICANMotor augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter;
	private ILimitSwitch bottomLimitAuger, topLimitAuger;	//TODO: Do we need??
	private IPIDFeedbackDevice shooterPot, augerPot;

	private ISolenoid shooterActuator;

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
			ILimitSwitch bottomLimitAuger, ILimitSwitch topLimitAuger, IPIDFeedbackDevice augerPot, boolean isAlpha)

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

		// calibrate();

		if (isAlpha)
		{
			//TODO
			TOP_LIMIT_POT_VALUE_SHOOTER = 291;
			BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1863;
			TOP_POT_LIMIT_AUGER = 0;
			BOTTOM_POT_LIMIT_AUGER = 1500;

			HIGH_VALUE_AUGER = 0;
		} else
		{
			TOP_LIMIT_POT_VALUE_SHOOTER = 190;
			BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1300;
			TOP_POT_LIMIT_AUGER = 2400;
			BOTTOM_POT_LIMIT_AUGER = 900;

			HIGH_VALUE_AUGER = 2300;
		}

		SmartDashboard.putNumber("Top Pot Limit Auger", TOP_POT_LIMIT_AUGER);
		SmartDashboard.putNumber("Bottom Pot Limit Auger", BOTTOM_POT_LIMIT_AUGER);
	}

//	/**
//	 * Auger and shooter calibration sequence, requires limit switches be
//	 * mounted or else things will break.
//	 */
//	private void calibrate()
//	{
//		// calibrates shooter
//		shooterLifterMotor.setSpeed(-CALIBRATION_SPEED);
//
//		while (!bottomLimitShooter.isHit() && RobotStatus.isRunning())
//		{
//
//		}
//
//		shooterLifterMotor.stop();
//
//		BOTTOM_LIMIT_POT_VALUE_SHOOTER = shooterPot.getCount() - 25;
//
//		shooterLifterMotor.setSpeed(CALIBRATION_SPEED);
//
//		while (!topLimitShooter.isHit() && RobotStatus.isRunning())
//		{
//
//		}
//
//		shooterLifterMotor.stop();
//
//		TOP_LIMIT_POT_VALUE_SHOOTER = shooterPot.getCount() + 25;
//
//		// calibrates auger
//		augerLifterMotor.setSpeed(-CALIBRATION_SPEED);
//
//		while (!bottomLimitAuger.isHit() && RobotStatus.isRunning())
//		{
//
//		}
//
//		augerLifterMotor.stop();
//
//		augerPot.resetCount();
//
//		BOTTOM_POT_LIMIT_AUGER = (int) augerPot.getCount();
//
//		augerLifterMotor.setSpeed(CALIBRATION_SPEED);
//
//		while (!topLimitAuger.isHit() && RobotStatus.isRunning())
//		{
//
//		}
//		stopShooterLifter();
//
//		TOP_POT_LIMIT_AUGER = (int) augerPot.getCount();
//
//		isAugerCalibrated = true;
//	}

	@Override
	public boolean isAugerCalibrated()
	{
		return isAugerCalibrated;
	}

	// Lifter Functions
	private void lowerShooterToBottomLimit()
	{
		while (!isShooterAtBottomLimit() && RobotStatus.isRunning())
		{
			lowerShooter();
		}

		stopShooterLifter();
	}

	private void raiseShooterToTopLimit()
	{
		while (!isShooterAtTopLimit() && RobotStatus.isRunning())
		{
			raiseShooter();
		}

		stopShooterLifter();
	}

	@Override
	public void raiseShooter()
	{
		if (!isShooterAtTopLimit())
		{
			shooterLifterMotor.setSpeed(Math.abs(LINEAR_ACTUATOR_SPEED));
		} else
		{
			stopShooterLifter();
		}
	}

	@Override
	public void lowerShooter()
	{
		if (!isShooterAtBottomLimit())
		{
			shooterLifterMotor.setSpeed(-Math.abs(LINEAR_ACTUATOR_SPEED));
		} else
		{
			stopShooterLifter();
		}
	}
	
	@Override
	public void raiseShooterSlow()
	{
		if (!isShooterAtTopLimit())
		{
			shooterLifterMotor.setSpeed(Math.abs(LINEAR_ACTUATOR_SPEED * 0.5));
		} else
		{
			stopShooterLifter();
		}
	}

	@Override
	public void lowerShooterSlow()
	{
		if (!isShooterAtBottomLimit())
		{
			shooterLifterMotor.setSpeed(-Math.abs(LINEAR_ACTUATOR_SPEED * 0.5));
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
		if ((shooterPot.getCount() < (desiredPosition - 50)) || (shooterPot.getCount() > (desiredPosition + 50)))
		{
			// If the shooter is less than the desired position
			if (shooterPot.getCount() > desiredPosition)
			{
				raiseShooter();

				// while this is so or we reach the top limit of travel
				while (!isShooterAtTopLimit() && (shooterPot.getCount() > desiredPosition) && (RobotStatus.isRunning()))
					;
				// If the shooter is greater than the desired position
			} else
			{
				lowerShooter();

				// while this is so or we reach the bottom limit of our travel
				while (!isShooterAtBottomLimit() && (shooterPot.getCount() < desiredPosition)
						&& (RobotStatus.isRunning()))
					;
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
			moveAugerToPreset(EAugerPresets.PICK_UP);
			moveShooterToPreset(EShooterPresets.PICK_UP);
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
		SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
		stopShooterWheels();
		shooterActuator.disengage();
	}

	@Override
	public void spinShooterWheelsHigh()
	{
		spinShooterWheels(SHOOT_SPEED_HIGH);
	}

	@Override
	public void spinShooterWheelsLow()
	{
		spinShooterWheels(SHOOT_SPEED_LOW);
	}

	private void spinShooterWheels(double speed)
	{
		leftShooterMotor.setSpeed(speed);
		rightShooterMotor.setSpeed(speed);
	}

	private void jostle()
	{
		stopShooterWheels();

		leftShooterMotor.setSpeed(0.4);
		rightShooterMotor.setSpeed(0.4);

		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);
		stopShooterWheels();
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
		raiseAuger(AUGER_LIFTER_SPEED_RAISE);
	}

	private void raiseAuger(double speed)
	{
		if (!isAugerAtTopLimit())
		{
			if (augerLifterMotor.getSpeed() > 0)
			{
				if (augerPot.getCount() > (HIGH_VALUE_AUGER))
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
			} else
			{
				rampUpMotor(-Math.abs(speed));
			}
		} else
		{
			stopAugerLifter(true);
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
		if (augerPot.getCount() > TOP_POT_LIMIT_AUGER)
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

		if ((augerPot.getCount() < (desiredPosition + 50)) || (augerPot.getCount() > (desiredPosition - 50)))
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
	public void moveShooterToPreset(EShooterPresets preset)
	{
		// Bottom Beta: 1300
		// Top Beta: 190
		switch (preset)
		{
		case PICK_UP:
			lowerShooterToBottomLimit();
			break;
		case SHOOT_LOW:
			moveShooterToPosition(BOTTOM_LIMIT_POT_VALUE_SHOOTER - 200); // to
																			// go
																			// higher,
																			// subtract
																			// more
			break;
		case SHOOT_HIGH:
			moveShooterToPosition(TOP_LIMIT_POT_VALUE_SHOOTER + 200);
			break;
		case LOW_BAR: // SIMILAR TO SHOOT_LOW
			moveShooterToPosition(BOTTOM_LIMIT_POT_VALUE_SHOOTER - 300);
			break;
		case TOP_LIMIT:
			raiseShooterToTopLimit();
			break;
		default:
			// Do Nothing
		}
	}

	public void moveAugerToPreset(EAugerPresets preset)
	{
		// Top Beta 2400
		// Bottom Beta 900
		switch (preset)
		{
		case BOTTOM_LIMIT:
			lowerAugerToBottomLimit();
			break;
		case TOP_LIMIT:
			raiseAugerToTopLimit();
			break;
		case FOURTY_KAI: // USED AT START OF MATCH
			moveAugerToPosition(BOTTOM_POT_LIMIT_AUGER + 500); 
			// used to be +400
			break;
		case SHOOT:
			moveAugerToPosition(BOTTOM_POT_LIMIT_AUGER + 750);
			break;
		case PICK_UP:
			moveAugerToPosition(BOTTOM_POT_LIMIT_AUGER + 175);
			break;
		default:
			// Do Nothing
		}
	}

	@Override
	public void shootSequenceHigh()
	{
		shootSequence(SHOOT_SPEED_HIGH);
	}

	@Override
	public void shootSequenceLow()
	{
		shootSequence(SHOOT_SPEED_LOW);
	}

	@Override
	public void shootSequence(double speed)
	{
		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);

		moveAugerToPreset(EAugerPresets.SHOOT);

		jostle();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);
		spinShooterWheels(speed);
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1500);
		launchBoulder();
	}

	@Override
	/**
	 * Used for auto when Tilt
	 */
	public void lowerAugerToBottomLimit(double speed)
	{
		while (!isAugerAtBottomLimit() && RobotStatus.isRunning())
		{
			lowerAuger(speed);
		}
		stopAugerLifter(false);
	}
}
