package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a launcher for the 2016 season Stronghold. It uses two independent motors to fire the ball 
 * as well as a servo to launch it into the firing position.
 * It has a linear actuator that controls the vertical position of the shooter, along with two limit switches 
 * and a potentiometer that define it's limits of travel.
 * It also has an auger that needs a motor to suck in the ball and a motor to move itself vertically.
 * The auger also has limit switches for it's outer reaches of travel.
 *
 */
public class Launcher implements ILauncher
{
	private final double INTAKE_SPEED = -0.6;
	private double SHOOT_SPEED = 0.95;
	
	private final double INTAKE_AUGER_SPEED = 0.2;
	
	private final double LINEAR_ACTUATOR_SPEED = 0.5;
	private final double AUGER_MOVE_SPEED = 0.5;

	private double TOP_LIMIT_POT_VALUE_SHOOTER = 291;
	private double BOTTOM_LIMIT_POT_VALUE_SHOOTER = 1863;
	private final int SHOOTER_ENCODER_VALUE = 200;
	
	private double TOP_ENCODER_LIMIT_AUGER = 1000;
	private double BOTTOM_ENCODER_LIMIT_AUGER = 0;
	
	private final double CALIBRATION_SPEED = 0.1;

	private ICANMotor shooterMotorLeft, shooterMotorRight, augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, topLimitShooter, bottomLimitAuger, topLimitAuger;
	private IPIDFeedbackDevice shooterPot, augerEncoder;

	private ISolenoid shooterActuator;

	public Launcher(
			ICANMotor shooterMotorLeft, 
			ICANMotor shooterMotorRight, 
			ICANMotor shooterLifterMotor,
			ICANMotor augerIntakeMotor, 
			ICANMotor augerLifterMotor, 
			ILimitSwitch shooterBottomLimit,
			ILimitSwitch topLimitShooter,
			ILimitSwitch bottomLimitAuger,
			ILimitSwitch topLimitAuger, 
			IPIDFeedbackDevice shooterPot,
			IPIDFeedbackDevice augerEncoder,
			ISolenoid shooterActuator)
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
		
		
		SmartDashboard.putNumber("Shooter Speed", 0.95);
		//calibrate();
	}


	/**
	 * Auger and shooter calibration sequence, requires limit switches be mounted or else things will break.
	 */
	private void calibrate()
	{
		// calibrates shooter
		shooterLifterMotor.setSpeed(-CALIBRATION_SPEED);
		
		while(!bottomLimitShooter.getValue())
		{
			
		}
		
		shooterLifterMotor.stop();
		
		BOTTOM_LIMIT_POT_VALUE_SHOOTER = shooterPot.getCount() - 25;
		
		shooterLifterMotor.setSpeed(CALIBRATION_SPEED);
		
		while(!topLimitShooter.getValue())
		{
			
		}
		
		shooterLifterMotor.stop();
		
		TOP_LIMIT_POT_VALUE_SHOOTER = shooterPot.getCount() + 25;
		
		// calibrates auger
		augerLifterMotor.setSpeed(-CALIBRATION_SPEED);
		
		while(!bottomLimitAuger.getValue())
		{
			
		}
		
		augerLifterMotor.stop();
		
		augerEncoder.resetCount();
		
		BOTTOM_ENCODER_LIMIT_AUGER = augerEncoder.getCount();
		
		augerLifterMotor.setSpeed(CALIBRATION_SPEED);
		
		while(!topLimitAuger.getValue())
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
		SHOOT_SPEED = SmartDashboard.getNumber("Shooter Speed", 0.95);
		
		lowerAuger();
		spinShooterUp();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(750);
		launchBoulder();
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
		moveShooterToPosition(SHOOTER_ENCODER_VALUE);
	}

	@Override
	public void moveShooterToPosition(double position)
	{
		// If the position is too high or too low for the shooter to go
		if(position > BOTTOM_LIMIT_POT_VALUE_SHOOTER || position < TOP_LIMIT_POT_VALUE_SHOOTER)
		{
			return;
		}
		
		// if the shooter is higher than the position
		if (shooterPot.getCount() < position)
		{
			// while this is so or we reach the top limit of our travel
			while ((shooterPot.getCount() < position) && !shooterAtTopLimit())
			{
				moveShooterDown();
			}
		} 
		// If the shooter is lower than the position
		else
		{
			// while this is so or we reach the bottom limit of travel
			while ((shooterPot.getCount() > position) && !shooterAtBottomLimit())
			{
				moveShooterUp();
			}
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
			augerLifterMotor.setSpeed(AUGER_MOVE_SPEED);
		}
	}

	@Override
	public void lowerAuger()
	{
		if (!isAugerAtBottomLimit())
		{
			augerLifterMotor.setSpeed(-AUGER_MOVE_SPEED);
		}
	}
	
	private boolean isAugerAtBottomLimit()
	{
		if(bottomLimitAuger.getValue() || augerEncoder.getCount() < BOTTOM_ENCODER_LIMIT_AUGER)
		{
			return true;
		} else
		{
			return false;
		}
	}
	
	private boolean isAugerAtTopLimit()
	{
		if(topLimitAuger.getValue() || augerEncoder.getCount() > TOP_ENCODER_LIMIT_AUGER)
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
		shooterMotorLeft.setSpeed(SHOOT_SPEED);
		shooterMotorRight.setSpeed(SHOOT_SPEED);
	}

	@Override
	public void launchBoulder()
	{
		shooterActuator.turnOn();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(100);
		stopShooterWheels();
		shooterActuator.turnOff();
	}
	
	public void stopAuger()
	{
		augerLifterMotor.stop();
	}

	@Override
	public void augerGoToPosition(int position)
	{
		if(!isAugerAtBottomLimit() && !isAugerAtTopLimit())
		{
			if(augerEncoder.getCount() < position)
			{
				while(!isAugerAtBottomLimit() && !isAugerAtTopLimit() && augerEncoder.getCount() < position)
				{
					augerLifterMotor.setSpeed(AUGER_MOVE_SPEED);
				}
			} else
			{
				while(!isAugerAtBottomLimit() && !isAugerAtTopLimit() && augerEncoder.getCount() > position)
				{
					augerLifterMotor.setSpeed(-AUGER_MOVE_SPEED);
				}
			}
		}
	}

}
