package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class Launcher implements ILauncher
{
//	private double p, i, d;
	
	private static double INTAKE_SPEED;
	private static double SHOOT_SPEED;
	
	private CANMotor motorLeft, motorRight;
	
	public Launcher(CANMotor motorLeft, CANMotor motorRight)
	{
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}
	
	@Override
	public void inTake()
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

}
