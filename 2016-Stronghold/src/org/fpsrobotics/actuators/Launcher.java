package org.fpsrobotics.actuators;

import java.util.concurrent.ExecutorService;

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
	private final double LINEAR_ACTUATOR_SPEED = 0.5;

	// Shooter Functions
	private final double INTAKE_SPEED = -0.6;
	private double SHOOT_SPEED = 0.95;

	// TODO: WHAT IS THIS?
	private final int SHOOTER_POT_VALUE = 477;

	// Auger Functions
	private final double INTAKE_AUGER_SPEED = -0.8;

	private final double AUGER_DOWN_SPEED_VBUS = -0.2;
	private final double AUGER_UP_SPEED_VBUS = 0.5;

	private final double AUGER_DOWN_SPEED_SPEED = -75;
	private final double AUGER_UP_SPEED_SPEED = 100;

	private double TOP_ENCODER_LIMIT_AUGER = 2600;
	private double BOTTOM_ENCODER_LIMIT_AUGER = 0;

	// Calibrate Functions

	private final double CALIBRATION_SPEED = 0.1;

	// private double TOP_LIMIT_POT_VALUE_SHOOTER = 291; // alpha
	// private double BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1863; // alpha

	private double TOP_LIMIT_POT_VALUE_SHOOTER = 130; // beta
	private double BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1500; // beta

	private boolean isAugerCalibrated;

	private ICANMotor shooterMotors, augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter, bottomLimitAuger, topLimitAuger;
	private IPIDFeedbackDevice shooterPot, augerEncoder;

	private ISolenoid shooterActuator;

	/**
	 * 
	 * @param shooterMotors
	 * @param shooterLifterMotor
	 * @param shooterActuator
	 * @param shooterBottomLimit
	 * @param shooterTopLimit
	 * @param shooterPot
	 * @param augerIntakeMotor
	 * @param augerLifterMotor
	 * @param bottomLimitAuger
	 * @param topLimitAuger
	 * @param augerEncoder
	 */
	public Launcher(ICANMotor shooterMotors, ICANMotor shooterLifterMotor, ISolenoid shooterActuator,
			ILimitSwitch shooterBottomLimit, ILimitSwitch shooterTopLimit, IPIDFeedbackDevice shooterPot,
			ICANMotor augerIntakeMotor, ICANMotor augerLifterMotor, ILimitSwitch bottomLimitAuger,
			ILimitSwitch topLimitAuger, IPIDFeedbackDevice augerEncoder)
	{
		// Shooter
		this.shooterMotors = shooterMotors;
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
		this.augerEncoder = augerEncoder;

		// calibrate();

		// augerEncoder.resetCount(); //Does this already in QuadEncoder.java
	}

	/**
	 * Auger and shooter calibration sequence, requires limit switches be
	 * mounted or else things will break.
	 */
	private void calibrate()
	{
		// calibrates shooter
		shooterLifterMotor.setSpeed(-CALIBRATION_SPEED);

		while (!bottomLimitShooter.isHit())
		{

		}

		shooterLifterMotor.stop();

		BOTTOM_LIMIT_POT_VALUE_SHOOTER = shooterPot.getCount() - 25;

		shooterLifterMotor.setSpeed(CALIBRATION_SPEED);

		while (!topLimitShooter.isHit())
		{

		}

		shooterLifterMotor.stop();

		TOP_LIMIT_POT_VALUE_SHOOTER = shooterPot.getCount() + 25;

		// calibrates auger
		augerLifterMotor.setSpeed(-CALIBRATION_SPEED);

		while (!bottomLimitAuger.isHit())
		{

		}

		augerLifterMotor.stop();

		augerEncoder.resetCount();

		BOTTOM_ENCODER_LIMIT_AUGER = augerEncoder.getCount();

		augerLifterMotor.setSpeed(CALIBRATION_SPEED);

		while (!topLimitAuger.isHit())
		{

		}
		stopShooterLifter();

		TOP_ENCODER_LIMIT_AUGER = augerEncoder.getCount();

		isAugerCalibrated = true;
	}

	@Override
	public boolean isAugerCalibrated()
	{
		return isAugerCalibrated;
	}

	// Lifter Functions
	private void lowerShooterToBottomLimit()
	{
		while (!isShooterAtBottomLimit())
		{
			lowerShooter();
		}
		stopShooterLifter();
	}

	private void raiseShooterToTopLimit()
	{
		while (!isShooterAtTopLimit())
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
			shooterLifterMotor.setSpeed(LINEAR_ACTUATOR_SPEED);
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
			shooterLifterMotor.setSpeed(-LINEAR_ACTUATOR_SPEED);
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

	// TODO: Check +/- for Top and Bottom Pot Values
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
			// If the shooter is higher than the desired position
			if (shooterPot.getCount() < desiredPosition)
			{
				raiseShooter();

				// while this is so or we reach the top limit of travel
				while (!isShooterAtTopLimit() && (shooterPot.getCount() < desiredPosition) && (RobotStatus.isRunning()))
				{
				}
				// If the shooter is lower than the deisred position
			} else
			{
				lowerShooter();

				// while this is so or we reach the bottom limit of our travel
				while (!isShooterAtBottomLimit() && (desiredPosition < shooterPot.getCount())
						&& (RobotStatus.isRunning()))
				{
				}
			}
		}
		stopShooterLifter();
	}

	// Shooter Functions

	@Override
	public void intakeBoulder()
	{
		augerIntakeMotor.setSpeed(INTAKE_AUGER_SPEED);

		shooterMotors.setSpeed(INTAKE_SPEED);
	}

	@Override
	public void stopIntakeBoulder()
	{
		augerIntakeMotor.stop();
		shooterMotors.stop();
	}

	@Override
	public void launchBoulder()
	{
		// if(shooterPot.getCount() < SHOOTER_POT_LOWER_SHOOTING_LIMIT ||
		// shooterPot.getCount() > SHOOTER_POT_BOWLING_LIMIT)
		// {
		shooterActuator.engage();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
		stopShooterWheels(); // TODO: CHECK THIS
		shooterActuator.disengage();
		// }
	}

	@Override
	public void spinShooterWheels()
	{
		spinShooterWheels(SHOOT_SPEED);
	}

	private void spinShooterWheels(double speed)
	{
		shooterMotors.setSpeed(speed);
	}

	private void jostle()
	{
		stopShooterWheels();
		shooterMotors.setSpeed(0.4);
		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);
		stopShooterWheels();
	}

	@Override
	public void stopShooterWheels()
	{
		shooterMotors.stop();
	}

	// Auger Functions

	@Override
	public void raiseAuger()
	{
		if (!isAugerAtTopLimit())
		{
			if (augerLifterMotor.getControlMode() == TalonControlMode.PercentVbus)
			{
				if (augerEncoder.getCount() > (TOP_ENCODER_LIMIT_AUGER - 500))
				{
					augerLifterMotor.setSpeed(AUGER_UP_SPEED_VBUS / 3);
				} else
				{
					augerLifterMotor.setSpeed(AUGER_UP_SPEED_VBUS);
				}

			} else
			{
				if (augerEncoder.getCount() > (TOP_ENCODER_LIMIT_AUGER - 500))
				{
					augerLifterMotor.setSpeed(AUGER_UP_SPEED_SPEED / 5);
				} else
				{
					augerLifterMotor.setSpeed(AUGER_UP_SPEED_SPEED);
				}
			}

		} else
		{
			stopAugerLifter();
		}
	}

	// TODO: THIS DOES NOT MAKE SENSE.
	private void raiseAuger(double speed)
	{
		if (!isAugerAtTopLimit())
		{
			if (augerEncoder.getCount() > (TOP_ENCODER_LIMIT_AUGER - 500))
			{
				augerLifterMotor.setSpeed(speed / 4);
			} else
			{
				augerLifterMotor.setSpeed(speed);
			}
		} else
		{
			stopAugerLifter();
			TOP_ENCODER_LIMIT_AUGER = (augerEncoder.getCount() - 500);
			SmartDashboard.putNumber("Top Limit", TOP_ENCODER_LIMIT_AUGER);
		}
	}

	@Override
	public void lowerAuger()
	{
		if (!isAugerAtBottomLimit())
		{
			if (augerLifterMotor.getControlMode() == TalonControlMode.PercentVbus)
			{
				augerLifterMotor.setSpeed(AUGER_DOWN_SPEED_VBUS);
			} else
			{
				augerLifterMotor.setSpeed(AUGER_DOWN_SPEED_SPEED);
			}
		} else
		{
			stopAugerLifter();
		}
	}

	private void lowerAuger(double speed)
	{
		if (!isAugerAtBottomLimit())
		{
			augerLifterMotor.setSpeed(-speed);
		} else
		{
			stopAugerLifter();

			augerEncoder.resetCount();

			SmartDashboard.putNumber("Bottom Limit", BOTTOM_ENCODER_LIMIT_AUGER);
		}
	}

	public void stopAugerLifter()
	{
		augerLifterMotor.stop();
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
		while (!isAugerAtTopLimit())
		{
			raiseAuger();
		}
	}

	private void lowerAugerToBottomLimit()
	{
		while (!isAugerAtBottomLimit())
		{
			lowerAuger();
		}
	}

	private boolean isAugerAtBottomLimit()
	{
		if (bottomLimitAuger.isHit() || (isAugerCalibrated && (augerEncoder.getCount() < BOTTOM_ENCODER_LIMIT_AUGER)))
		{
			if (!isAugerCalibrated)
			{
				isAugerCalibrated = true;
				augerEncoder.resetCount();
				SmartDashboard.putNumber("Bottom Limit", BOTTOM_ENCODER_LIMIT_AUGER);
			}
			return true;
		} else
		{
			return false;
		}
	}

	private boolean isAugerAtTopLimit()
	{

		if (topLimitAuger.isHit() || (isAugerCalibrated && (augerEncoder.getCount() > TOP_ENCODER_LIMIT_AUGER)))
		{
			if (topLimitAuger.isHit())
			{
				// TODO: THIS DOES NOT MAKE SENSE
				TOP_ENCODER_LIMIT_AUGER = (augerEncoder.getCount() - 300);

				SmartDashboard.putNumber("Top Limit", TOP_ENCODER_LIMIT_AUGER);
			}
			return true;
		} else
		{
			return false;
		}

	}

	@Override
	public void moveAugerToPosition(int desiredPosition)
	{

		if (isAugerCalibrated && ((augerEncoder.getCount() > (desiredPosition + 30))
				|| (augerEncoder.getCount() < (desiredPosition - 30))))
		{
			if (desiredPosition < augerEncoder.getCount())
			{
				lowerAuger();

				while (!isAugerAtBottomLimit()
						&& (desiredPosition < augerEncoder.getCount() && (RobotStatus.isRunning())))
				{

				}
			} else
			{
				raiseAuger();

				while (!isAugerAtTopLimit() && (augerEncoder.getCount() < desiredPosition) && (RobotStatus.isRunning()))
				{

				}
			}
		}
		stopAugerLifter();
	}

	// Sequences

	@Override
	public void moveShooterToPreset(EShooterPresets preset)
	{
		switch (preset)
		{
		case POSITION400:
			moveShooterToPosition(400);
			break;
		case LOW_BAR:
			moveShooterToPosition(1400);
			break;
		case LOAD_BOULDER:
			lowerShooterToBottomLimit();
			break;
		case BOWL_BOULDER:
			lowerShooterToBottomLimit();
			break;
		case SHOOT:
			raiseShooterToTopLimit();
			break;
		case BOTTOM_LIMIT:
			lowerShooterToBottomLimit();
			break;
		case TOP_LIMIT:
			raiseShooterToTopLimit();
			break;
		default:
			// Do Nothing
		}
		// moveShooterToPosition(SHOOTER_POT_VALUE);
	}

	public void moveAugerToPreset(EAugerPresets preset)
	{
		switch (preset)
		{

		case POSITION1150:
			moveAugerToPosition(1150);
			break;
		case LAUNCH:
			lowerAugerToBottomLimit();
			break;
		case BOTTOM_LIMIT:
			lowerAugerToBottomLimit();
			break;
		case TOP_LIMIT:
			raiseAugerToTopLimit();
			break;
		default:
			// Do Nothing
		}
	}

	@Override
	public void shootSequence()
	{
		shootSequence(SHOOT_SPEED);
	}

	@Override
	public void shootSequence(double speed)
	{
		augerLifterMotor.disablePID();

		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);

		// lowerAugerToBottomLimit();

		jostle(); // TODO: WHY DO WE NEED JOSTLE TO SHOOT?
		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);
		spinShooterWheels(speed);
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1300);
		launchBoulder();

		augerLifterMotor.enablePID();
		augerLifterMotor.setControlMode(TalonControlMode.Speed);
	}

}
