package org.fpsrobotics.actuators;

public class Servo implements IServo
{
	private static final double SERVO_LATCHED = 0.6;
	private static final double SERVO_UNLATCHED = 1.0;
	
	edu.wpi.first.wpilibj.Servo servo;
	
	public Servo(int channel)
	{
		servo = new edu.wpi.first.wpilibj.Servo(channel);
	}
	
	@Override
	public void engage() 
	{
		servo.set(SERVO_LATCHED);
	}

	@Override
	public void disengage() 
	{
		servo.set(SERVO_UNLATCHED);
	}

	@Override
	public void set(double value) 
	{
		servo.set(value);
	}

	@Override
	public double get() 
	{
		return servo.get();
	}

}
