package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDEnabledDevice;
import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class Motor implements IMotor, IPIDEnabledDevice, IPIDFeedbackDevice
{
	double p, i, d, invert;
	private IPIDFeedbackDevice device;
	private boolean invertDirection;
	
	private SpeedController motor;
	
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
	}
	
	@Override
	public double getSpeed() 
	{	
		if(device == null && motor != null)
		{ 
			return motor.get();
		} else if(device != null)
		{
			// implement later
		}
		
		return 0.0;
	}

	@Override
	public void setSpeed(double speed) 
	{
		invert = 1;
		
		if(invertDirection)
		{
			invert = -1;
		}
		
		if(device == null && motor != null)
		{
			motor.set(speed * invert);
		} else if(device != null)
		{
			// implement later
		}
	}

	@Override
	public void stop() 
	{
		if(device == null && motor != null)
		{
			motor.set(0.0);
		} else if(device != null)
		{
			// implement later
		}
	}

	@Override
	public void setP(double p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setI(double i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setD(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void PIDOutput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enablePID() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disablePID() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetCount() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TalonControlMode getControlMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setControlMode(TalonControlMode mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FeedbackDevice whatPIDDevice() {
		// TODO Auto-generated method stub
		return null;
	}
}
