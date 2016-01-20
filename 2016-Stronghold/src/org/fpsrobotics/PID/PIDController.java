package org.fpsrobotics.PID;

import CANTalon.FeedbackDevice;

public class PIDController implements IPIDFeedbackDevice 
{

	double P = 6;
	double I = 6;
	double D = 6;
	
	public static final double STOW = 0.0;
	
	public PIDController(){
	setSetpoint(STOW);
	enable();
	}

	private void setSetpoint(double stow2) {
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
	public void resetCount() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public FeedbackDevice whatPIDDevice() {
		// TODO Auto-generated method stub
		return null;
										
	}
}
