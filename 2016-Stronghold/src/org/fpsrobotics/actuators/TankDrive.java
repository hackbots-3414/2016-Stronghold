package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class TankDrive implements IDriveTrain
{
	double p, i, d;
	DoubleMotor motorLeft, motorRight;
	
	public TankDrive(DoubleMotor motorLeft, DoubleMotor motorRight)
	{
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}
	
	@Override
	public void setSpeed(double leftSpeed, double rightSpeed) 
	{
		motorLeft.setSpeed(leftSpeed);
		motorRight.setSpeed(rightSpeed);
	}

	@Override
	public void turnLeft(double speed) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(-speed);
	}

	@Override
	public void turnRight(double speed) {
		motorLeft.setSpeed(-speed);
		motorRight.setSpeed(speed);
	}

	@Override
	public void goStraight(double speed) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	@Override
	public void goBackward(double speed) {
		motorLeft.setSpeed(-speed);
		motorRight.setSpeed(-speed);
	}

	@Override
	public void driveLeft(double speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void driveRight(double speed) {
		// TODO Auto-generated method stub
		
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

}
