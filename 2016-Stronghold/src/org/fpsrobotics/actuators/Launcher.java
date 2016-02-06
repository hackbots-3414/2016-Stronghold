package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class Launcher implements ILauncher
{
	// private double p, i, d;

	private final double INTAKE_SPEED = -0.2;
	private final double SHOOT_SPEED = 1.0;
	private final double INTAKE_AUGER_SPEED = 0.2;
	private final double LINEAR_ACTUATOR_SPEED = 0.7;
	private final double AUGER_MOVE_SPEED = 0.5;

	private final int TOP_LIMIT_POT_VALUE = 3932;
	private final int BOTTOM_LIMIT_POT_VALUE = 3972;
	private final int SHOOTER_ENCODER_VALUE = 200;

	private ICANMotor shooterMotorLeft, shooterMotorRight, augerIntakeMotor, shooterLifterMotor;
	private ICANMotor augerLifterMotor;
	private ILimitSwitch bottomLimitShooter, bottomLimitAuger, topLimitAuger;
	private IPIDFeedbackDevice shooterPot;

	private IServo shooterActuator;

	public Launcher(
			ICANMotor shooterMotorLeft, 
			ICANMotor shooterMotorRight, 
			ICANMotor shooterLifterMotor,
			ICANMotor augerIntakeMotor, 
			ICANMotor augerLifterMotor, 
			ILimitSwitch shooterBottomLimit,
			ILimitSwitch bottomLimitAuger, 
			ILimitSwitch topLimitAuger, 
			IPIDFeedbackDevice shooterPot,
			IServo shooterActuator)
	{
		this.shooterMotorLeft = shooterMotorLeft;
		this.shooterMotorRight = shooterMotorRight;
		this.shooterLifterMotor = shooterLifterMotor;
		this.bottomLimitShooter = shooterBottomLimit;
		this.shooterPot = shooterPot;
		this.augerIntakeMotor = augerIntakeMotor;
		this.augerLifterMotor = augerLifterMotor;
		this.bottomLimitAuger = bottomLimitAuger;
		this.topLimitAuger = topLimitAuger;
		this.shooterActuator = shooterActuator;
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
		spinShooterUp();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);
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
	public void moveShooterToPosition(int position)
	{
		if (shooterPot.getCount() < position)
		{
			while ((shooterPot.getCount() < position) && !shooterAtTopLimit())
			{
				moveShooterUp();
			}
		} else
		{
			while ((shooterPot.getCount() > position) && !shooterAtBottomLimit())
			{
				moveShooterDown();
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
		if (shooterPot.getCount() <= TOP_LIMIT_POT_VALUE)
		{
			return true;
		}

		return false;
	}

	private boolean shooterAtBottomLimit()
	{
		//if ((shooterPot.getCount() <= BOTTOM_LIMIT_POT_VALUE) || bottomLimitShooter.getValue())
		if (shooterPot.getCount() >= BOTTOM_LIMIT_POT_VALUE)
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
		return bottomLimitAuger.getValue();
	}
	
	private boolean isAugerAtTopLimit()
	{
		return topLimitAuger.getValue();
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
		shooterActuator.engage();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
		stopShooterWheels();
		shooterActuator.disengage();
	}

	@Override
	public void augerGoToPosition(int position)
	{
		/*
		if(!isAugerAtBottomLimit() && !isAugerAtTopLimit())
		{
			if(augerLifterMotor.getPosition() < position)
			{
				while(!isAugerAtBottomLimit() && !isAugerAtTopLimit() && augerLifterMotor.getPosition() < position)
				{
					augerLifterMotor.setSpeed(AUGER_MOVE_SPEED);
				}
			} else
			{
				while(!isAugerAtBottomLimit() && !isAugerAtTopLimit() && augerLifterMotor.getPosition() > position)
				{
					augerLifterMotor.setSpeed(-AUGER_MOVE_SPEED);
				}
			}
		}
		*/
	}

}
