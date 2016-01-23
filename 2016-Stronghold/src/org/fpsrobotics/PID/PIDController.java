package org.fpsrobotics.PID;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class PIDController implements IPIDFeedbackDevice 
{
	public static final double p = 1;
	public static final double i = 1;
	public static final double d = 1;
	
	private final CANTalonmotor = new CANTalon(1,2);
	private final AnalogChannel pot = new AnalogChannel(4);
	
	public Wrist()
	{
		
		super(2.0,0,0);
		setSetpointRange(i,p);
		setSetpoint(d);
		enable()	
	}
	
	protected double returnPIDInput()
	{
		return pot.getAverageVoltage()/MAX_VOLTAGE;
	}
	protected void PIDOutput(double output)
	{
		motor.set(output);
	}

}
