package org.fpsrobotics.sensors;

public interface IGamepad 
{
	public double getAnalogStickValue(AnalogStick stick, GamepadDirection direction);
	public boolean getButtonValue(ButtonGamepad button);
}
