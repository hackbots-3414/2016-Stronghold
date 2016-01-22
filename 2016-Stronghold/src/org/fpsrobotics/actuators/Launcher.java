package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.ILimitSwitch;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class Launcher implements ILauncher
{
//	private double p, i, d;
	
	private final double INTAKE_SPEED = 0.5;
	private final double SHOOT_SPEED = 1.0;
	private final double LINEAR_ACTUATOR_SPEED = 0.8;
	
	private final int TOP_LIMIT_POT_VALUE = 300;
	private final int BOTTOM_LIMIT_POT_VALUE = 0;
	private final int SHOOTER_ENCODER_VALUE = 200;
	
	private ICANMotor motorLeft, motorRight;
	private ICANMotor linearActuator;
	private ILimitSwitch bottomLimit;
	private IPIDFeedbackDevice pot;
	
	public Launcher(ICANMotor motorLeft, ICANMotor motorRight, ICANMotor linearActuator, ILimitSwitch bottomLimit, IPIDFeedbackDevice pot)
	{
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
		this.linearActuator = linearActuator;
		this.bottomLimit = bottomLimit;
		this.pot = pot;
	}
	
	@Override
	public void intake()
	{
		motorLeft.setSpeed(INTAKE_SPEED);
		motorRight.setSpeed(INTAKE_SPEED);
	}
	
	@Override
	public void shoot()
	{
		motorLeft.setSpeed(SHOOT_SPEED);
		motorRight.setSpeed(SHOOT_SPEED);
	}

	@Override
	public void stop()
	{
		motorLeft.stop();
		motorRight.stop();
	}

	@Override
	public void setP(double p) 
	{
		motorLeft.setP(p);
		motorRight.setP(p);
	}

	@Override
	public void setI(double i) 
	{
		motorLeft.setI(i);
		motorRight.setI(i);
	}

	@Override
	public void setD(double d) 
	{
		motorLeft.setD(d);
		motorRight.setD(d);
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device) 
	{
		motorLeft.setPIDFeedbackDevice(device);
		motorRight.setPIDFeedbackDevice(device);
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice() 
	{
		return motorLeft.getPIDFeedbackDevice();
	}

	@Override
	public void enablePID() 
	{
		motorLeft.enablePID();
		motorRight.enablePID();
	}

	@Override
	public void disablePID() 
	{
		motorLeft.disablePID();
		motorRight.disablePID();
	}

	@Override
	public TalonControlMode getControlMode() 
	{
		return motorLeft.getControlMode();
	}

	@Override
	public void setControlMode(TalonControlMode mode) 
	{
		motorLeft.setControlMode(mode);
		motorRight.setControlMode(mode);
	}

	@Override
	public void goToBottom() 
	{
		while(!atBottomLimit())
		{
			goDown();
		}
		
		linearActuator.stop();
		
	}

	@Override
	public void goToShootingPosition() {
		goToPosition(SHOOTER_ENCODER_VALUE);
	}

	@Override
	public void goToPosition(int position) 
	{
		if(pot.getCount() < position)
		{
			while((pot.getCount() < position) && !atTopLimit())
			{
				goUp();
			}
		} else
		{
			while((pot.getCount() > position) && !atBottomLimit())
			{
				goDown();
			}
		}
	}

	@Override
	public void goUp() {
		if(!atTopLimit())
		{
			if(pot.getCount() > (TOP_LIMIT_POT_VALUE-100))
			{
				linearActuator.setSpeed(LINEAR_ACTUATOR_SPEED/5);
			} else
			{
				linearActuator.setSpeed(LINEAR_ACTUATOR_SPEED);
			}
		} else
		{
			linearActuator.stop();
		}
	}

	@Override
	public void goDown() {
		if(!atBottomLimit())
		{
			if(pot.getCount() < (TOP_LIMIT_POT_VALUE+100))
			{
				linearActuator.setSpeed(-LINEAR_ACTUATOR_SPEED/5);
			} else
			{
				linearActuator.setSpeed(-LINEAR_ACTUATOR_SPEED);
			}
		} else
		{
			linearActuator.stop();
		}
		
	}
	
	private boolean atTopLimit()
	{
		if(pot.getCount() >= TOP_LIMIT_POT_VALUE)
		{
			return true;
		}
		
		return false;
	}
	
	private boolean atBottomLimit()
	{
		if(pot.getCount() <= BOTTOM_LIMIT_POT_VALUE || bottomLimit.getValue())
		{
			return true;
		}
		
		return false;
	}

	@Override
	public void goToTopLimit() 
	{
		if(!atTopLimit())
		{
			goUp();
		}
	}

}
