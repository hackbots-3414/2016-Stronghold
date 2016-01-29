package org.fpsrobotics.PID;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDController extends PIDSubsystem implements IPIDFeedbackDevice{ 

	public PIDController(double p , double i, double d) {
		super("PIDController",2, 0, 0);
		setSetpoint(d);
		enable();
		// TODO Auto-generated constructor stub
	}

	public static final double p = .001;
	public static final double i = 1;
	public static final double d = 1;
	private final AnalogInput pot = new AnalogInput(2);
	
	private final CANTalon motor = new CANTalon(1,2);
	//private final AnalogChannel pot = new AnalogChannel(4);
	
	protected double returnPIDInput()
	{
		return pot.getAverageVoltage();
	}
	protected void PIDOutput(double output)
	{
		motor.set(output);
		motor.pidWrite(output);
	}

	public double getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void enable() {
		// TODO Auto-generated method stub
		
	}

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