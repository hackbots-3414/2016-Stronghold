package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.PID.*;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class CANMotor implements ICANMotor
{
	private boolean invertDirection;
	private CANTalon canMotor;
	private IPIDFeedbackDevice device;
	
	private int invert;
	
	public CANMotor(CANTalon motor, boolean invertDirection)
	{
		this.setControlMode(TalonControlMode.Speed);
		
		this.canMotor = motor;
		this.invertDirection = invertDirection;
	}
	
	public CANMotor(CANTalon motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		this.setControlMode(TalonControlMode.Speed);
		
		this.canMotor = motor;
		this.invertDirection = invertDirection;
		this.device = device;
		
		canMotor.setFeedbackDevice(device.whatPIDDevice());
	}

	@Override
	public void setP(double p) 
	{
		canMotor.setP(p);
	}

	@Override
	public void setI(double i) 
	{
		canMotor.setI(i);
	}

	@Override
	public void setD(double d) 
	{
		canMotor.setD(d);
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device) 
	{
		this.device = device;
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice() 
	{
		return device;
	}

	@Override
	public void enablePID() 
	{
		canMotor.enableControl();
	}

	@Override
	public void disablePID() 
	{
		canMotor.disableControl();
	}

	@Override
	public TalonControlMode getControlMode() 
	{
		return canMotor.getControlMode();
	}

	@Override
	public void setControlMode(TalonControlMode mode) 
	{
		canMotor.changeControlMode(mode);
	}

	@Override
	public double getSpeed() 
	{
		return canMotor.get();
	}

	@Override
	public void setSpeed(double speed) 
	{
		invert = 1;
		
		if(invertDirection)
		{
			invert = -1;
		}
			
		if(device != null)
		{
			canMotor.setSetpoint(speed*invert);
		} else
		{
			canMotor.set(speed*invert);
		}
	}

	@Override
	public void stop() 
	{
		if(device != null)
		{
			canMotor.setSetpoint(0.0);
		} else
		{
			canMotor.set(0.0);
		}
	} 
	
	protected void setMotorControlMode(CANTalon.TalonControlMode mode)
	{
		canMotor.changeControlMode(mode);
	}
	
	protected int getDeviceID()
	{
		return canMotor.getDeviceID();
	}
	
	protected void setFollowerDeviceID(int deviceID)
	{
		canMotor.set(deviceID);
	}

	public void PIDOutput() {
		
	}

}
