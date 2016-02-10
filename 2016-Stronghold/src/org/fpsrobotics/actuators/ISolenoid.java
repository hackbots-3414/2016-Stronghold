package org.fpsrobotics.actuators;

/**
 * Describes a generic pneumatic solenoid
 *
 */
public interface ISolenoid 
{
	public void turnOn();
	public void turnOff();
	public void set(SolenoidValues value);
}
