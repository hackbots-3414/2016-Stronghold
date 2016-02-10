package org.fpsrobotics.sensors;

/**
 * Defines a gamepad, a controller with analog sticks.
 *
 */
public interface IGamepad 
{
	public double getAnalogStickValue(AnalogStick stick, GamepadDirection direction);
	public boolean getButtonValue(ButtonGamepad button);
}
