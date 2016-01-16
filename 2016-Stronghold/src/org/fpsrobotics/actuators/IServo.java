package org.fpsrobotics.actuators;

public interface IServo 
{
	public void engage();
	public void disengage();
	public void set(double value);
	public double get();
}
