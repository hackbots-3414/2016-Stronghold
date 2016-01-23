package org.fpsrobotics.sensors;

public interface IGamepad 
{
	double getAnalogStickValue(AnalogStick stick, GamepadDirection direction);
	boolean getButtonValue(ButtonGamepad button);
}
