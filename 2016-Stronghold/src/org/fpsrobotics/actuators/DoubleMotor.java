package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class DoubleMotor implements IMotor
{
	Motor motorOne, motorTwo;
	
	public DoubleMotor(Motor motorOne, Motor motorTwo)
	{
		this.motorOne = motorOne;
		this.motorTwo = motorTwo;
		
		motorTwo.setControlMode(TalonControlMode.Follower);
		motorTwo.getCANTalon().set(motorOne.getCANTalon().getDeviceID());
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
	}

	@Override
	public void stop() 
	{
		motorOne.stop();
	}

	@Override
	public void setP(double p) 
	{
		motorOne.setP(p);
	}

	@Override
	public void setI(double i) {
		motorOne.setI(i);
	}

	@Override
	public void setD(double d) 
	{
		motorOne.setD(d);
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device) {
		motorOne.setPIDFeedbackDevice(device);
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice() 
	{
		return motorOne.getPIDFeedbackDevice();
	}

	@Override
	public void enablePID() {
		motorOne.enablePID();
	}

	@Override
	public void disablePID() {
		motorOne.disablePID();
	}

	@Override
	public TalonControlMode getControlMode() {
		return motorOne.getControlMode();
	}

	@Override
	public void setControlMode(TalonControlMode mode) {
		motorOne.setControlMode(mode);
	}

}
