package org.fpsrobotics.PID;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDController extends PIDSubsystem 
{
	public static final double p = 1;
	public static final double i = 1;
	public static final double d = 1;
	
	
	private final CANTalon motor = new CANTalon(1,2);
	private final AnalogChannel pot = new AnalogChannel(4);
	
	public PIDController()
	{
		super(2.0,0,0);
		setSetpointRange(i,p);
		setSetpoint(d);
		enable();	
	}
	
	protected double returnPIDInput()
	{
		return pot.getAverageVoltage()/MAX_VOLTAGE;
	}
	protected void PIDOutput(double output)
	{
		motor.set(output);
	}

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

	public void resetCount() {
		// TODO Auto-generated method stub
		
	}

	public FeedbackDevice whatPIDDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
