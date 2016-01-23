package org.fpsrobotics.sensors;

public interface IJoystick 
{
	double getX();
	double getY();
	double getTwist();
	boolean getButtonValue(ButtonJoystick value);
}
