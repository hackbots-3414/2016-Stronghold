package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class DoubleMotor implements IMotor
{
	Motor motorOne, motorTwo;
	boolean isCANTalon = true;
	
	public DoubleMotor(Motor motorOne, Motor motorTwo)
	{
		this.motorOne = motorOne;
		this.motorTwo = motorTwo;
		
		if(motorTwo.getCANTalon() != null)
		{
			motorTwo.setControlMode(TalonControlMode.Follower);
			motorTwo.getCANTalon().set(motorOne.getCANTalon().getDeviceID());
		} else
		{
			isCANTalon = false;
		}
	}
	
	@Override
	public double getSpeed() 
	{
		return ((motorOne.getSpeed() + motorTwo.getSpeed())/(2));
	}

	@Override
	public void setSpeed(double speed) 
	{
		motorOne.setSpeed(speed);
		
		if(!isCANTalon)
		{
			motorTwo.setSpeed(speed);
		}
	}

	@Override
	public void stop() 
	{
		motorOne.stop();
		if(!isCANTalon)
		{
			motorTwo.stop();
		}
	}

	@Override
	public void setP(double p) 
	{
		motorOne.setP(p);
		if(!isCANTalon)
		{
			motorTwo.setP(p);
		}
	}

	@Override
	public void setI(double i) {
		motorOne.setI(i);
		if(!isCANTalon)
		{
			motorTwo.setI(i);
		}
	}

	@Override
	public void setD(double d) 
	{
		motorOne.setD(d);
		if(!isCANTalon)
		{
			motorTwo.setD(d);
		}
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device) {
		if(isCANTalon)
		{
			motorOne.setPIDFeedbackDevice(device);
		} else
		{
			// implement later
		}
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice() 
	{
		if(isCANTalon)
		{
			return motorOne.getPIDFeedbackDevice();
		} else
		{
			// implement later
			return null;
		}
	}

	@Override
	public void enablePID() 
	{
		motorOne.enablePID();
		
		if(!isCANTalon)
		{
			motorTwo.enablePID();
		}
	}

	@Override
	public void disablePID() 
	{
		motorOne.disablePID();
		
		if(!isCANTalon)
		{
			motorTwo.disablePID();
		}
	}

	@Override
	public TalonControlMode getControlMode() 
	{
		if(isCANTalon)
		{
			return motorOne.getControlMode();
		} else
		{
			// implement later
			
			return null;
		}
	}

	@Override
	public void setControlMode(TalonControlMode mode) 
	{
		if(isCANTalon)
		{
			motorOne.setControlMode(mode);
		} else
		{
			// implement later
		}
	}

}
