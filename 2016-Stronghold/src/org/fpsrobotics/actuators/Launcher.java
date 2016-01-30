package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class Launcher implements ILauncher
{
	// private double p, i, d;

	private final double INTAKE_SPEED = -0.5;
	private final double SHOOT_SPEED = 1.0;
	private final double INTAKE_AUGER_SPEED = 0.2;
	private final double LINEAR_ACTUATOR_SPEED = 0.8;
	private final double AUGER_MOVE_SPEED = 0.5;

	private final int TOP_LIMIT_POT_VALUE = 300;
	private final int BOTTOM_LIMIT_POT_VALUE = 0;
	private final int SHOOTER_ENCODER_VALUE = 200;

	private ICANMotor shooterMotorLeft, shooterMotorRight, augerIntakeMotor, augerLifterMotor, shooterLifterMotor;
	private ILimitSwitch bottomLimitShooter, bottomLimitAuger, topLimitAuger;
	private IPIDFeedbackDevice shooterPot;

	private ISolenoid shooterPiston;

	public Launcher(ICANMotor shooterMotorLeft, ICANMotor shooterMotorRight, ICANMotor shooterLifterMotor,
			ICANMotor augerIntakeMotor, ICANMotor augerLifterMotor, ILimitSwitch shooterBottomLimit,
			ILimitSwitch bottomLimitAuger, ILimitSwitch topLimitAuger, IPIDFeedbackDevice shooterPot,
			ISolenoid shooterPiston)
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
		this.shooterPiston = shooterPiston;
	}

	@Override
	public void intake()
	{
		augerIntakeMotor.setSpeed(INTAKE_AUGER_SPEED);

		shooterMotorLeft.setSpeed(INTAKE_SPEED);
		shooterMotorRight.setSpeed(INTAKE_SPEED);
	}

	@Override
	public void shootSequence()
	{
		spinUp();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);
		launch();

	}

	@Override
	public void stop()
	{
		shooterMotorLeft.stop();
		shooterMotorRight.stop();
	}

	@Override
	public void setP(double p)
	{
		shooterMotorLeft.setP(p);
		shooterMotorRight.setP(p);
	}

	@Override
	public void setI(double i)
	{
		shooterMotorLeft.setI(i);
		shooterMotorRight.setI(i);
	}

	@Override
	public void setD(double d)
	{
		shooterMotorLeft.setD(d);
		shooterMotorRight.setD(d);
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device)
	{
		shooterMotorLeft.setPIDFeedbackDevice(device);
		shooterMotorRight.setPIDFeedbackDevice(device);
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice()
	{
		return shooterMotorLeft.getPIDFeedbackDevice();
	}

	@Override
	public void enablePID()
	{
		shooterMotorLeft.enablePID();
		shooterMotorRight.enablePID();
	}

	@Override
	public void disablePID()
	{
		shooterMotorLeft.disablePID();
		shooterMotorRight.disablePID();
	}

	@Override
	public TalonControlMode getControlMode()
	{
		return shooterMotorLeft.getControlMode();
	}

	@Override
	public void setControlMode(TalonControlMode mode)
	{
		shooterMotorLeft.setControlMode(mode);
		shooterMotorRight.setControlMode(mode);
	}

	@Override
	public void goToBottom()
	{
		while (!atBottomLimit())
		{
			goDown();
		}

		shooterLifterMotor.stop();

	}

	@Override
	public void goToShootingPosition()
	{
		goToPosition(SHOOTER_ENCODER_VALUE);
	}

	@Override
	public void goToPosition(int position)
	{
		if (shooterPot.getCount() < position)
		{
			while ((shooterPot.getCount() < position) && !atTopLimit())
			{
				goUp();
			}
		} else
		{
			while ((shooterPot.getCount() > position) && !atBottomLimit())
			{
				goDown();
			}
		}
	}

	@Override
	public void goUp()
	{
		if (!atTopLimit())
		{
			if (shooterPot.getCount() > (TOP_LIMIT_POT_VALUE - 100))
			{
				shooterLifterMotor.setSpeed(LINEAR_ACTUATOR_SPEED / 5);
			} else
			{
				shooterLifterMotor.setSpeed(LINEAR_ACTUATOR_SPEED);
			}
		} else
		{
			shooterLifterMotor.stop();
		}
	}

	@Override
	public void goDown()
	{
		if (!atBottomLimit())
		{
			if (shooterPot.getCount() < (TOP_LIMIT_POT_VALUE + 100))
			{
				shooterLifterMotor.setSpeed(-LINEAR_ACTUATOR_SPEED / 5);
			} else
			{
				shooterLifterMotor.setSpeed(-LINEAR_ACTUATOR_SPEED);
			}
		} else
		{
			shooterLifterMotor.stop();
		}

	}

	private boolean atTopLimit()
	{
		if (shooterPot.getCount() >= TOP_LIMIT_POT_VALUE)
		{
			return true;
		}

		return false;
	}

	private boolean atBottomLimit()
	{
		if ((shooterPot.getCount() <= BOTTOM_LIMIT_POT_VALUE) || bottomLimitShooter.getValue())
		{
			return true;
		}

		return false;
	}

	@Override
	public void goToTopLimit()
	{
		if (!atTopLimit())
		{
			goUp();
		}
	}

	@Override
	public void raiseArm()
	{
		if (!topLimitAuger.getValue())
		{
			augerLifterMotor.setSpeed(AUGER_MOVE_SPEED);
		}
	}

	@Override
	public void lowerArm()
	{
		if (!bottomLimitAuger.getValue())
		{
			augerLifterMotor.setSpeed(-AUGER_MOVE_SPEED);
		}
	}

	@Override
	public void spinUp()
	{
		shooterMotorLeft.setSpeed(SHOOT_SPEED);
		shooterMotorRight.setSpeed(SHOOT_SPEED);
	}

	@Override
	public void launch()
	{
		shooterPiston.turnOn();
		SensorConfig.getInstance().getTimer().waitTimeInMillis(250);
		stop();
		shooterPiston.turnOff();
	}

}
