package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * 
 * Controls motors connected to Talon SRX motor controllers that interface with the CAN bus on the RoboRIO.
 *
 */
public class CANMotor implements ICANMotor 
{
	private boolean invertDirection;
	private CANTalon canMotor;
	private IPIDFeedbackDevice device;
	
	private int invert;
	
	public CANMotor(CANTalon motor, boolean invertDirection)
	{
		// Changes the motor controller to accept percentages expressed in decimals of the voltage of the system.
		motor.changeControlMode(TalonControlMode.PercentVbus);
		
		this.canMotor = motor;
		this.invertDirection = invertDirection;
	}
	
	public CANMotor(CANTalon motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		// Calls above constructor
		this(motor, invertDirection);
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
		canMotor.reset();
		canMotor.enable();
		// TODO: implement later?
	}

	@Override
	public void disablePID() 
	{
		canMotor.changeControlMode(TalonControlMode.PercentVbus);
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
		invert = (invertDirection) ? -1 : 1;
			
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

}
