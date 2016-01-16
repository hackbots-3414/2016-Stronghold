package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;

public class QuadEncoder implements IPIDFeedbackDevice
{

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
