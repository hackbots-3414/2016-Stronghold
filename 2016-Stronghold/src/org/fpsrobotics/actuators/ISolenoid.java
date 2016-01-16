package org.fpsrobotics.actuators;

public interface ISolenoid 
{
	public void turnOn();
	public void turnOff();
	public void set(SolenoidValues value);
}
