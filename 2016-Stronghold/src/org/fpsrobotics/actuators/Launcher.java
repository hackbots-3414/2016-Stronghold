package org.fpsrobotics.actuators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a launcher for the 2016 season Stronghold. It uses two independent
 * motors to fire the ball as well as a servo to launch it into the firing
 * position. It has a linear actuator that controls the vertical position of the
 * shooter, along with two limit switches and a potentiometer that define it's
 * limits of travel. It also has an auger that needs a motor to suck in the ball
 * and a motor to move itself vertically. The auger also has limit switches for
 * it's outer reaches of travel.
 *
 */
public class Launcher implements ILauncher
{
	private final double INTAKE_SPEED = -0.6;
	private double SHOOT_SPEED = 0.95;

	private final double INTAKE_AUGER_SPEED = -0.8;

	private final double AUGER_LIFTER_MIDDLE_TRAVEL_SPEED = 0.8;
	private final double AUGER_LIFTER_SPEED = 0.5;

	private final double LINEAR_ACTUATOR_SPEED = 0.5;

	private double TOP_LIMIT_POT_VALUE_SHOOTER;
	private double BOTTOM_LIMIT_POT_VALUE_SHOOTER;

	private double TOP_POT_LIMIT_AUGER;
	private double BOTTOM_POT_LIMIT_AUGER;

	private final double CALIBRATION_SPEED = 0.1;

	private ICANMotor shooterMotorLeft, shooterMotorRight, augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter, bottomLimitAuger, topLimitAuger;
	private IPIDFeedbackDevice shooterPot, augerPot;

	private ISolenoid shooterActuator;

	public Launcher(ICANMotor shooterMotorLeft, ICANMotor shooterMotorRight, ICANMotor shooterLifterMotor,
			ICANMotor augerIntakeMotor, ICANMotor augerLifterMotor, ILimitSwitch shooterBottomLimit,
			ILimitSwitch topLimitShooter, ILimitSwitch bottomLimitAuger, ILimitSwitch topLimitAuger,
			IPIDFeedbackDevice shooterPot, IPIDFeedbackDevice augerPot, ISolenoid shooterActuator, boolean isAlpha)
	{
		this.shooterMotorLeft = shooterMotorLeft;
		this.shooterMotorRight = shooterMotorRight;
		this.shooterLifterMotor = shooterLifterMotor;
		this.bottomLimitShooter = shooterBottomLimit;
		this.shooterPot = shooterPot;
		this.augerIntakeMotor = augerIntakeMotor;
		this.augerLifterMotor = augerLifterMotor;
		this.bottomLimitAuger = bottomLimitAuger;
		this.topLimitShooter = topLimitShooter;
		this.topLimitAuger = topLimitAuger;
		this.shooterActuator = shooterActuator;
		this.augerPot = augerPot;

		// calibrate();

		if(isAlpha)
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

		while (!bottomLimitShooter.getValue())
		{

		}

		shooterLifterMotor.stop();

		BOTTOM_LIMIT_POT_VALUE_SHOOTER = shooterPot.getCount() - 25;

		shooterLifterMotor.setSpeed(CALIBRATION_SPEED);

		while (!topLimitShooter.getValue())
		{

		}

		shooterLifterMotor.stop();

		TOP_LIMIT_POT_VALUE_SHOOTER = shooterPot.getCount() + 25;

		// calibrates auger
		augerLifterMotor.setSpeed(-CALIBRATION_SPEED);

		while (!bottomLimitAuger.getValue())
		{

		}

		augerLifterMotor.stop();

		augerPot.resetCount();

		BOTTOM_POT_LIMIT_AUGER = augerPot.getCount();

		augerLifterMotor.setSpeed(CALIBRATION_SPEED);

		while (!topLimitAuger.getValue())
		{

		}

		augerLifterMotor.stop();

		TOP_POT_LIMIT_AUGER = augerPot.getCount();
	}

	@Override
	public void intakeBoulder()
	{
		augerIntakeMotor.setSpeed(INTAKE_AUGER_SPEED);

		shooterMotorLeft.setSpeed(INTAKE_SPEED);
		shooterMotorRight.setSpeed(INTAKE_SPEED);
	}

	@Override
	public void shootSequence()
	{
		shootSequence(SHOOT_SPEED);
	}

	@Override
	public void stopShooterWheels()
	{
		shooterMotorLeft.stop();
		shooterMotorRight.stop();
	}

	@Override
	public void moveShooterToBottomLimit()
	{
		while (!shooterAtBottomLimit())
		{
			moveShooterDown();
		}

		shooterLifterMotor.stop();

	}
	
	@Override
	public void moveShooterToPosition(double position)
	{
		if (shooterPot.getCount() < position - 50 || shooterPot.getCount() > position + 50)
		{
			// if the shooter is higher than the position
			if (shooterPot.getCount() < position)
			{
				// while this is so or we reach the top limit of our travel
				while ((shooterPot.getCount() < position))
				{
					moveShooterDown();
				}
			}
			// If the shooter is lower than the position
			else
			{
				// while this is so or we reach the bottom limit of travel
				while ((shooterPot.getCount() > position))
				{
					moveShooterUp();
				}
			}
		} else
		{
			stopShooterLifter();
		}
	}

	@Override
	public void moveShooterUp()
	{
		if (!shooterAtTopLimit())
		{
			shooterLifterMotor.setSpeed(LINEAR_ACTUATOR_SPEED);
		} else
		{
			shooterLifterMotor.stop();
		}
	}

	@Override
	public void moveShooterDown()
	{
		if (!shooterAtBottomLimit())
		{
			shooterLifterMotor.setSpeed(-LINEAR_ACTUATOR_SPEED);
		} else
		{
			shooterLifterMotor.stop();
		}

	}

	public void stopShooterLifter()
	{
		shooterLifterMotor.stop();
	}

	private boolean shooterAtTopLimit()
	{
		if (shooterPot.getCount() <= TOP_LIMIT_POT_VALUE_SHOOTER || topLimitShooter.getValue())
		{
			return true;
		}

		return false;
	}

	private boolean shooterAtBottomLimit()
	{
		if (shooterPot.getCount() >= BOTTOM_LIMIT_POT_VALUE_SHOOTER || bottomLimitShooter.getValue())
		{
			return true;
		}

		return false;
	}

	@Override
	public void moveShooterToTopLimit()
	{
		if (!shooterAtTopLimit())
		{
			moveShooterUp();
		}
	}

	@Override
	public void raiseAuger()
	{
		raiseAuger(AUGER_LIFTER_SPEED);
	}

	private void raiseAuger(double speed)
	{
		if (speed < 0)
		{
			speed = -speed;
		}

		if (!isAugerAtTopLimit())
		{
			if (augerPot.getCount() > TOP_POT_LIMIT_AUGER + 600) // getting
																	// close to
																	// top limit
			{
				augerLifterMotor.setSpeed(speed);
			} else
			{
				augerLifterMotor.setSpeed(AUGER_LIFTER_MIDDLE_TRAVEL_SPEED);
			}
		}
	}

	private void lowerAuger(double speed)
	{
		if (speed < 0)
		{
			speed = -speed;
		}

		if (!isAugerAtBottomLimit())
		{
			if (augerPot.getCount() < BOTTOM_POT_LIMIT_AUGER - 600) // getting
																	// close to
																	// bottom
																	// limit
			{
				augerLifterMotor.setSpeed(-speed);
			} else
			{
				augerLifterMotor.setSpeed(-AUGER_LIFTER_MIDDLE_TRAVEL_SPEED);
			}
		}
	}

	@Override
	public void lowerAuger()
	{
		lowerAuger(AUGER_LIFTER_SPEED);
	}

	public void lowerAugerToBottomLimit()
	{
		while (!isAugerAtBottomLimit())
		{
			lowerAuger();
		}
	}

	public void raiseAugerToTopLimit()
	{
		while (!isAugerAtTopLimit())
		{
			raiseAuger();
		}
	}

	private boolean isAugerAtBottomLimit()
	{
		if (bottomLimitAuger.getValue() || augerPot.getCount() > BOTTOM_POT_LIMIT_AUGER)
		{
			return true;
		} else
		{
			return false;
		}
	}

	private boolean isAugerAtTopLimit()
	{
		if (topLimitAuger.getValue() || augerPot.getCount() < TOP_POT_LIMIT_AUGER)
		{
			return true;
		} else
		{
			return false;
		}
	}

	@Override
	public void spinShooterUp()
	{
		spinShooterUp(SHOOT_SPEED);
	}

	private void jostle()
	{
		stopShooterWheels();
		shooterMotorLeft.setSpeed(0.4);
		shooterMotorRight.setSpeed(0.4);
		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);
		stopShooterWheels();
	}

	@Override
	public void launchBoulder()
	{
		// if(shooterPot.getCount() < SHOOTER_POT_LOWER_SHOOTING_LIMIT ||
		// shooterPot.getCount() > SHOOTER_POT_BOWLING_LIMIT)
		// {
		shooterActuator.turnOn();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
		stopShooterWheels();
		shooterActuator.turnOff();
		// }
	}

	public void stopAugerLifter()
	{
		augerLifterMotor.stop();
	}

	@Override
	public void augerGoToPosition(int position)
	{
		if (augerPot.getCount() < position - 50 || augerPot.getCount() > position + 50)
		{
			// if the auger is higher than the position
			if (augerPot.getCount() < position)
			{
				// while this is so or we reach the top limit of our travel
				while ((augerPot.getCount() < position))
				{
					lowerAuger();
				}
			}
			// If the shooter is lower than the position
			else
			{
				// while this is so or we reach the bottom limit of travel
				while ((augerPot.getCount() > position))
				{
					raiseAuger();
				}
			}
		} else
		{
			stopAugerLifter();
		}
	}

	private void spinShooterUp(double speed)
	{
		shooterMotorLeft.setSpeed(speed);
		shooterMotorRight.setSpeed(speed);
	}

	@Override
	public void shootSequence(double speed)
	{
		augerLifterMotor.disablePID();

		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);

		// lowerAugerToBottomLimit();
		jostle();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(400);
		spinShooterUp(speed);
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1300);
		launchBoulder();

		augerLifterMotor.enablePID();
		augerLifterMotor.setControlMode(TalonControlMode.Speed);
	}

	@Override
	public void spinAugerUp()
	{
		augerIntakeMotor.setSpeed(INTAKE_AUGER_SPEED);
	}

	@Override
	public void stopAuger()
	{
		augerIntakeMotor.stop();
	}

}
