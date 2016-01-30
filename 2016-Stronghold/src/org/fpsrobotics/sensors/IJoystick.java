package org.fpsrobotics.sensors;

public interface IJoystick 
{
	public double getX();
	public double getY();
	public double getTwist();
	public boolean getButtonValue(ButtonJoystick value);
}
