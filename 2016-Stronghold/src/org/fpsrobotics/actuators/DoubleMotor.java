package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class DoubleMotor implements ICANMotor
{
	Motor motorOne, motorTwo;
	CANMotor CANMotorOne, CANMotorTwo;
	
	public DoubleMotor(Motor motorOne, Motor motorTwo)
	{
		this.motorOne = motorOne;
		this.motorTwo = motorTwo;
	}
	
	public DoubleMotor(CANMotor motorOne, CANMotor motorTwo)
	{
		this.CANMotorOne = motorOne;
		this.CANMotorTwo = motorTwo;

		motorTwo.setControlMode(TalonControlMode.Follower);
		motorTwo.setFollowerDeviceID(motorOne.getDeviceID());
	}
	
	@Override
	public double getSpeed() 
	{
		return ((motorOne.getSpeed() + motorTwo.getSpeed())/(2));
	}

	@Override
	public void setSpeed(double speed) 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.setSpeed(speed);
		} else
		{
			motorOne.setSpeed(speed);
			motorTwo.setSpeed(speed);
		}
	}

	@Override
	public void stop() 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.stop();
		} else
		{
			motorOne.stop();
			motorTwo.stop();
		}
	}

	public void setP(double p) 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.setP(p);
		}
	}

	public void setI(double i) 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.setI(i);
		}
	}

	public void setD(double d) 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.setD(d);
		}
	}

	public void setPIDFeedbackDevice(IPIDFeedbackDevice device) 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.setPIDFeedbackDevice(device);
		}
	}

	public IPIDFeedbackDevice getPIDFeedbackDevice() 
	{
		if(CANMotorOne != null)
		{
			return CANMotorOne.getPIDFeedbackDevice();
		}
		
		return null;
	}

	public void enablePID() 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.enablePID();
		}
	}

	public void disablePID() 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.disablePID();
		}
	}

	public TalonControlMode getControlMode() 
	{
		if(CANMotorOne != null)
		{
			return CANMotorOne.getControlMode();
		}
		
		return null;
	}

	public void setControlMode(TalonControlMode mode) 
	{
		if(CANMotorOne != null)
		{
			CANMotorOne.setControlMode(mode);
		}
	}

	@Override
	public void PIDOutput() {
		// TODO Auto-generated method stub
		
	}

}
