package org.fpsrobotics.sensors;

public interface IGamepad 
{
	double getAnalogStickValue(AnalogStick stick);
	boolean getButtonValue(ButtonGamepad button);
}
