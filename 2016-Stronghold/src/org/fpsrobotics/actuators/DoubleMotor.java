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
		motorTwo.setSpeed(speed);
	}

	@Override
	public void stop() 
	{
		motorOne.stop();
		motorTwo.stop();
	}

	@Override
	public void setP(double p) 
	{
		motorOne.setP(p);
		motorTwo.setP(p);
	}

	@Override
	public void setI(double i) {
		motorOne.setI(i);
		motorTwo.setI(i);
	}

	@Override
	public void setD(double d) 
	{
		motorOne.setD(d);
		motorTwo.setD(d);
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device) {
		motorOne.setPIDFeedbackDevice(device);
		motorTwo.setPIDFeedbackDevice(device);
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice() 
	{
		return motorOne.getPIDFeedbackDevice();
	}

	@Override
	public void enablePID() {
		motorOne.enablePID();
		motorTwo.enablePID();
	}

	@Override
	public void disablePID() {
		motorOne.disablePID();
		motorTwo.disablePID();
	}

	@Override
	public TalonControlMode getControlMode() {
		return motorOne.getControlMode();
	}

	@Override
	public void setControlMode(TalonControlMode mode) {
		motorOne.setControlMode(mode);
		motorTwo.setControlMode(mode);
	}

}
