package org.fpsrobotics.PID;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDController extends PIDSubsystem implements IPIDFeedbackDevice{ 

	public PIDController(double p , double i, double d) {
		super("PIDController",2, 0, 0);
		setSetpoint(d);
		setAbsoluteTolerance(.2);
		getPIDController().setContinuous(true);
		enable();
		// TODO Auto-generated constructor stub
	}

	public static final double p = .1;
	public static final double i = .01;
	public static final double d = .001;
	private final AnalogInput pot = new AnalogInput(1);
	private final CANTalon motor = new CANTalon(1,2);
	//private final AnalogChannel pot = new AnalogChannel(4);
	
	protected double returnPIDInput()
	{
		return pot.getAverageVoltage();
	}
	protected void usePIDOutput(double output)
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
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}