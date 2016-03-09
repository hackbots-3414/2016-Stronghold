package org.fpsrobotics.actuators;

import java.util.concurrent.ExecutorService;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import java.io.BufferedReader;
import java.io.FileReader;

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
	private final double INTAKE_SPEED = -0.4;
	private double SHOOT_SPEED = 0.90;
	//TODO: GOT RID OF SMARTDASHBOARD INPUT SHOT - redo that

	// Auger Functions
	private final double INTAKE_AUGER_SPEED = -0.8;

	private final double AUGER_LIFTER_MIDDLE_TRAVEL_SPEED = 0.8;
	private final double AUGER_LIFTER_SPEED = 0.5;

	private double TOP_LIMIT_POT_VALUE_SHOOTER;
	private double BOTTOM_LIMIT_POT_VALUE_SHOOTER;

	private double TOP_POT_LIMIT_AUGER;
	private double BOTTOM_POT_LIMIT_AUGER;

	// Calibrate Functions

	private final double CALIBRATION_SPEED = 0.1;

	private boolean isAugerCalibrated;

	private ICANMotor leftShooterMotor, rightShooterMotor, augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter, bottomLimitAuger, topLimitAuger;
	private IPIDFeedbackDevice shooterPot, augerPot;

	private ISolenoid shooterActuator;

	private boolean isAlpha;

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
	public Launcher(ICANMotor leftShooterMotor, ICANMotor rightShooterMotor, ICANMotor shooterLifterMotor, ISolenoid shooterActuator,
			ILimitSwitch shooterBottomLimit, ILimitSwitch shooterTopLimit, IPIDFeedbackDevice shooterPot,
			ICANMotor augerIntakeMotor, ICANMotor augerLifterMotor, ILimitSwitch bottomLimitAuger,
			ILimitSwitch topLimitAuger, IPIDFeedbackDevice augerPot, boolean isAlpha)
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
		this.isAlpha = isAlpha;

		// calibrate();

		if (isAlpha)
		{
			TOP_LIMIT_POT_VALUE_SHOOTER = 291;
			BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1863;
			TOP_POT_LIMIT_AUGER = 0; // fix when auger is added
			BOTTOM_POT_LIMIT_AUGER = 1500; // fix when auger is added
		} else
		{
			TOP_LIMIT_POT_VALUE_SHOOTER = 130;
			BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1500;
			TOP_POT_LIMIT_AUGER = 0; // fix when auger is added
			BOTTOM_POT_LIMIT_AUGER = 1500; // fix when auger is added
		}
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

		augerPot.resetCount();

		BOTTOM_POT_LIMIT_AUGER = augerPot.getCount();

		augerLifterMotor.setSpeed(CALIBRATION_SPEED);

		while (!topLimitAuger.isHit())
		{

		}
		stopShooterLifter();

		TOP_POT_LIMIT_AUGER = augerPot.getCount();

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

		rightShooterMotor.setSpeed(INTAKE_SPEED);
		leftShooterMotor.setSpeed(INTAKE_SPEED);
	}

	@Override
	public void stopIntakeBoulder()
	{
		augerIntakeMotor.stop();
		rightShooterMotor.stop();
		leftShooterMotor.stop();
	}

	@Override
	public void launchBoulder()
	{
		// if(shooterPot.getCount() < SHOOTER_POT_LOWER_SHOOTING_LIMIT ||
		// shooterPot.getCount() > SHOOTER_POT_BOWLING_LIMIT)
		// {
		shooterActuator.engage();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
		stopShooterWheels();
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
		raiseAuger(AUGER_LIFTER_SPEED);
	}

	private void raiseAuger(double speed)
	{
		if (!isAugerAtTopLimit())
		{

			if (augerPot.getCount() > (TOP_POT_LIMIT_AUGER + 600))
			{
				augerLifterMotor.setSpeed(Math.abs(speed));
			} else
			{
				augerLifterMotor.setSpeed(AUGER_LIFTER_MIDDLE_TRAVEL_SPEED);
			}
		}
	}

	@Override
	public void lowerAuger()
	{
		lowerAuger(AUGER_LIFTER_SPEED);
	}

	private void lowerAuger(double speed)
	{
		if (!isAugerAtBottomLimit())
		{
			if (augerPot.getCount() < (BOTTOM_POT_LIMIT_AUGER - 600))
			{
				augerLifterMotor.setSpeed(-Math.abs(speed));

			} else
			{
				augerLifterMotor.setSpeed(-AUGER_LIFTER_MIDDLE_TRAVEL_SPEED);
			}
		} else
		{
			stopAugerLifter();
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

	// TODO: What's up with this up-and-down reverse stuff?
	private boolean isAugerAtBottomLimit()
	{
		if (bottomLimitAuger.isHit() || (isAugerCalibrated && (augerPot.getCount() > BOTTOM_POT_LIMIT_AUGER)))
		{
			if (!isAugerCalibrated)
			{
				isAugerCalibrated = true;
				augerPot.resetCount();
			}
			return true;
		} else
		{
			return false;
		}
	}

	private boolean isAugerAtTopLimit()
	{

		if (topLimitAuger.isHit() || (isAugerCalibrated && (augerPot.getCount() < TOP_POT_LIMIT_AUGER)))
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

		if (isAugerCalibrated
				&& ((augerPot.getCount() < (desiredPosition - 50)) || (augerPot.getCount() > (desiredPosition + 50))))
		{
			// if the auger is higher than the position
			if (desiredPosition > augerPot.getCount())
			{
				lowerAuger();

				while (!isAugerAtBottomLimit() && (desiredPosition > augerPot.getCount() && (RobotStatus.isRunning())))
				{

				}
			} else
			{
				raiseAuger();

				while (!isAugerAtTopLimit() && (augerPot.getCount() > desiredPosition) && (RobotStatus.isRunning()))
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
//		augerLifterMotor.disablePID();

		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);

		// lowerAugerToBottomLimit();

		jostle(); // TODO: WHY DO WE NEED JOSTLE TO SHOOT?
		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);
		spinShooterWheels(speed);
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1300);
		launchBoulder();

//		augerLifterMotor.enablePID();
//		augerLifterMotor.setControlMode(TalonControlMode.Speed);
	}

}
