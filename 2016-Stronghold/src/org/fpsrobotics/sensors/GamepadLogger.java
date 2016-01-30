package org.fpsrobotics.sensors;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GamepadLogger implements ILogger
{
	private IGamepad gamepad;
	private PrintWriter writer;
	
	public GamepadLogger(IGamepad gamepad)
	{
		
		try
		{
			writer = new PrintWriter("gamepadLog_" + getLoggerTimeStamp() + "_.txt", "UTF-8");
		} catch (FileNotFoundException e)
		{
			System.out.println("Write File Not Found");
			e.printStackTrace();
		 }
		 catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			 System.out.println("UTF-8 Not Found");
			 e.printStackTrace();
		}
		
		
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
			
			writer.println("Left X " + gamepad.getAnalogStickValue(AnalogStick.LEFT, GamepadDirection.HORIZONTAL));
			writer.println("Right X " + gamepad.getAnalogStickValue(AnalogStick.RIGHT, GamepadDirection.HORIZONTAL));
			
			writer.println("Left Y " + gamepad.getAnalogStickValue(AnalogStick.LEFT, GamepadDirection.VERTICAL));
			writer.println("Right Y " + gamepad.getAnalogStickValue(AnalogStick.RIGHT, GamepadDirection.VERTICAL));
			
		}
	}
	
	private String getLoggerTimeStamp()
	{
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); 
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return "_" + minute + "_:_" + hour + "_:_" + month + "_:_" + day + "_:_" + year + "_";
	}

}
