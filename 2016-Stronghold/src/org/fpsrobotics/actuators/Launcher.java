package org.fpsrobotics.actuators;

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

	private final double AUGER_DOWN_SPEED_VBUS = -0.2;
	private final double AUGER_UP_SPEED_VBUS = 0.5;

	private final double AUGER_DOWN_SPEED_SPEED = -75;
	private final double AUGER_UP_SPEED_SPEED = 100;

	private final double LINEAR_ACTUATOR_SPEED = 0.5;

	//private double TOP_LIMIT_POT_VALUE_SHOOTER = 291; // alpha
	private double TOP_LIMIT_POT_VALUE_SHOOTER = 300; // beta
	private double BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1500; // beta
	//private double BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1863; // alpha

	private final int SHOOTER_POT_VALUE = 477;
	private final int SHOOTER_POT_LOWER_SHOOTING_LIMIT = 0; // define later
	private final int SHOOTER_POT_BOWLING_LIMIT = 0; // define later

	private double TOP_ENCODER_LIMIT_AUGER = 2600;
	private double BOTTOM_ENCODER_LIMIT_AUGER = 0;

	private final double CALIBRATION_SPEED = 0.1;

	private ICANMotor shooterMotorLeft, shooterMotorRight, augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter, bottomLimitAuger, topLimitAuger;
	private IPIDFeedbackDevice shooterPot, augerEncoder;

	private ISolenoid shooterActuator;

	public Launcher(ICANMotor shooterMotorLeft, ICANMotor shooterMotorRight, ICANMotor shooterLifterMotor,
			ICANMotor augerIntakeMotor, ICANMotor augerLifterMotor, ILimitSwitch shooterBottomLimit,
			ILimitSwitch topLimitShooter, ILimitSwitch bottomLimitAuger, ILimitSwitch topLimitAuger,
			IPIDFeedbackDevice shooterPot, IPIDFeedbackDevice augerEncoder, ISolenoid shooterActuator)
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
		this.augerEncoder = augerEncoder;

		// calibrate();
		
		augerEncoder.resetCount();
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

		augerEncoder.resetCount();

		BOTTOM_ENCODER_LIMIT_AUGER = augerEncoder.getCount();

		augerLifterMotor.setSpeed(CALIBRATION_SPEED);

		while (!topLimitAuger.getValue())
		{

		}

		augerLifterMotor.stop();

		TOP_ENCODER_LIMIT_AUGER = augerEncoder.getCount();
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
	public void goToPresetPosition()
	{
		moveShooterToPosition(SHOOTER_POT_VALUE);
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
			augerLifterMotor.stop();
		}
	}

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

			augerLifterMotor.stop();

			TOP_ENCODER_LIMIT_AUGER = (augerEncoder.getCount() - 500);

			SmartDashboard.putNumber("Top Limit", TOP_ENCODER_LIMIT_AUGER);

		}
	}

	private void lowerAuger(double speed)
	{
		if (!isAugerAtBottomLimit())
		{
			augerLifterMotor.setSpeed(-speed);
		} else
		{
			augerLifterMotor.stop();

			augerEncoder.resetCount();

			SmartDashboard.putNumber("Bottom Limit", BOTTOM_ENCODER_LIMIT_AUGER);
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
			augerLifterMotor.stop();
		}
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
		if (bottomLimitAuger.getValue())
		{
			augerEncoder.resetCount();

			SmartDashboard.putNumber("Bottom Limit", BOTTOM_ENCODER_LIMIT_AUGER);
		}

		if (bottomLimitAuger.getValue() || augerEncoder.getCount() < BOTTOM_ENCODER_LIMIT_AUGER)
		{
			return true;
		} else
		{
			return false;
		}
	}

	private boolean isAugerAtTopLimit()
	{
		if (topLimitAuger.getValue())
		{
			TOP_ENCODER_LIMIT_AUGER = (augerEncoder.getCount() - 300);

			SmartDashboard.putNumber("Top Limit", TOP_ENCODER_LIMIT_AUGER);
		}

		if (topLimitAuger.getValue() || augerEncoder.getCount() > TOP_ENCODER_LIMIT_AUGER)
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
		if (augerEncoder.getCount() > position + 30 || augerEncoder.getCount() < position - 30)
		{
			if (augerEncoder.getCount() < position)
			{
				while (!isAugerAtTopLimit() && augerEncoder.getCount() < position)
				{
					if (augerEncoder.getCount() < (position - 50))
					{
						raiseAuger(50);
					} else
					{
						raiseAuger(25);
					}
				}
			} else
			{
				while (!isAugerAtBottomLimit() && augerEncoder.getCount() > position)
				{
					if (augerEncoder.getCount() > (position + 50))
					{
						lowerAuger(50);
					} else
					{
						lowerAuger(25);
					}
				}
			}
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
