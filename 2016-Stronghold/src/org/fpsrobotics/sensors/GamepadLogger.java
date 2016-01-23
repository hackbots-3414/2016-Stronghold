package org.fpsrobotics.sensors;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GamepadLogger implements ILogger
{
	IGamepad gamepad;
	//PrintWriter writer;
	
	public GamepadLogger(IGamepad gamepad)
	{
		/*
		try
		{
			writer = new PrintWriter("gamepadLog.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		this.gamepad = gamepad;
	}

	@Override
	public void reportInformation(OutputDevice device)
	{
		if(device == OutputDevice.SMARTDASHBOARD)
		{
			SmartDashboard.putNumber("Left X ", gamepad.getAnalogStickValue(AnalogStick.LEFT, GamepadDirection.HORIZONTAL));
			SmartDashboard.putNumber("Right X ", gamepad.getAnalogStickValue(AnalogStick.RIGHT, GamepadDirection.HORIZONTAL));
			
			SmartDashboard.putNumber("Left Y ", gamepad.getAnalogStickValue(AnalogStick.LEFT, GamepadDirection.VERTICAL));
			SmartDashboard.putNumber("Right Y ", gamepad.getAnalogStickValue(AnalogStick.RIGHT, GamepadDirection.VERTICAL));
			
		} else if(device == OutputDevice.FILE)
		{
			/*
			writer.println("Left X " + gamepad.getAnalogStickValue(AnalogStick.LEFT, GamepadDirection.HORIZONTAL));
			writer.println("Right X " + gamepad.getAnalogStickValue(AnalogStick.RIGHT, GamepadDirection.HORIZONTAL));
			
			writer.println("Left Y " + gamepad.getAnalogStickValue(AnalogStick.LEFT, GamepadDirection.VERTICAL));
			writer.println("Right Y " + gamepad.getAnalogStickValue(AnalogStick.RIGHT, GamepadDirection.VERTICAL));
			*/
		}
	}

}
