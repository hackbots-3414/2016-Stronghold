package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class Motor implements IMotor
{
	private double p, i, d, speed;
	private IPIDFeedbackDevice device;
	private boolean invertDirection;
	
	private SpeedController motor;
	private CANTalon canMotor;
	
	private CANTalon.TalonControlMode controlMode;
	
	public Motor(SpeedController motor, boolean invertDirection)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
	}
	
	public Motor(SpeedController motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
		this.device = device;
		canMotor.setFeedbackDevice(device.whatPIDDevice());
	}
	
	public Motor(CANTalon motor, boolean invertDirection)
	{
		this.motor = canMotor;
		this.invertDirection = invertDirection;
	}
	
	public Motor(CANTalon motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		this.motor = canMotor;
		this.invertDirection = invertDirection;
		this.device = device;
		canMotor.setFeedbackDevice(device.whatPIDDevice());
	}
	
	public Motor(Talon motor, boolean invertDirection)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
	}
	
	public Motor(Talon motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		this.motor = motor;
		this.invertDirection = invertDirection;
		this.device = device;
		canMotor.setFeedbackDevice(device.whatPIDDevice());
	}
	
	@Override
	public double getSpeed() 
	{
		int invert = 1;
		
		if(invertDirection)
		{
			invert = -1;
		}
		
		if(device == null)
		{
			if(canMotor != null)
			{
				return canMotor.get() * invert;
			} else if(motor != null)
			{
				return motor.get() * invert;
			}
		} else if(canMotor != null && device != null)
		{
			return canMotor.getSetpoint() * invert;
		} else if(device != null)
		{
			// implement later
		}
		
		return 0.0;
	}

	@Override
	public void setSpeed(double speed) 
	{
		int invert = 1;
		
		if(invertDirection)
		{
			invert = -1;
		}
		
		if(device == null)
		{
			if(canMotor != null)
			{
				canMotor.set(speed * invert);
			} else if(motor != null)
			{
				motor.set(speed * invert);
			}
		} else if(canMotor != null && device != null)
		{
			canMotor.setSetpoint(speed * invert);
		} else if(device != null)
		{
			// implement later
		}
	}

	@Override
	public void stop() 
	{
		if(device == null)
		{
			if(canMotor != null)
			{
				canMotor.set(0.0);
			} else if(motor != null)
			{
				motor.set(0.0);
			}
		} else if(canMotor != null && device != null)
		{
			canMotor.setSetpoint(0.0);
		} else if(device != null)
		{
			// implement later
		}
	}

	@Override
	public void setP(double p) 
	{
		this.p = p;
		
		if(canMotor != null)
		{
			canMotor.setP(p);
		}
	}

	@Override
	public void setI(double i) 
	{
		this.i = i;
		
		if(canMotor != null)
		{
			canMotor.setI(i);
		}
	}

	@Override
	public void setD(double d) 
	{
		this.d = d;
		
		if(canMotor != null)
		{
			canMotor.setD(d);
		}
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device) 
	{
		this.device = device;
	}

	@Override
	public void enablePID() 
	{
		if(canMotor != null && device != null)
		{
			canMotor.enable();
		} else if(device != null)
		{
			// implement later
		}
	}

	@Override
	public void disablePID() 
	{
		if(canMotor != null && device != null)
		{
			canMotor.disable();
		} else if(device != null)
		{
			// implement later
		}
	}

	@Override
	public CANTalon.TalonControlMode getControlMode()
	{
		return controlMode;
	}
	
	@Override
	public void setControlMode(CANTalon.TalonControlMode mode)
	{
		controlMode = mode;
		canMotor.changeControlMode(controlMode);
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice() 
	{
		return device;
	}
}
