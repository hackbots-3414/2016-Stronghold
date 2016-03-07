package org.fpsrobotics.sensors;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.fpsrobotics.sensors.loggerstuff.EGamepadDirection;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GamepadLogger implements ILogger
{
	private IGamepad gamepad;
	private PrintWriter writer;
	
	public GamepadLogger(IGamepad gamepad)
	{
		
		try
		{
//			writer = new PrintWriter("gamepadLog_" + getLoggerTimeStamp() + "_.txt", "UTF-8");
			writer = new PrintWriter("gamepadLog.txt", "UTF-8");
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
	public void reportInformation(EOutputDevice device)
	{
		if(device == EOutputDevice.SMARTDASHBOARD)
		{
			SmartDashboard.putNumber("Left X ", gamepad.getAnalogStickValue(EAnalogStickAxis.LEFT, EGamepadDirection.HORIZONTAL));
			SmartDashboard.putNumber("Right X ", gamepad.getAnalogStickValue(EAnalogStickAxis.RIGHT, EGamepadDirection.HORIZONTAL));
			
			SmartDashboard.putNumber("Left Y ", gamepad.getAnalogStickValue(EAnalogStickAxis.LEFT, EGamepadDirection.VERTICAL));
			SmartDashboard.putNumber("Right Y ", gamepad.getAnalogStickValue(EAnalogStickAxis.RIGHT, EGamepadDirection.VERTICAL));
			
		} else if(device == EOutputDevice.FILE)
		{
			
			writer.println("Left X " + gamepad.getAnalogStickValue(EAnalogStickAxis.LEFT, EGamepadDirection.HORIZONTAL));
			writer.println("Right X " + gamepad.getAnalogStickValue(EAnalogStickAxis.RIGHT, EGamepadDirection.HORIZONTAL));
			
			writer.println("Left Y " + gamepad.getAnalogStickValue(EAnalogStickAxis.LEFT, EGamepadDirection.VERTICAL));
			writer.println("Right Y " + gamepad.getAnalogStickValue(EAnalogStickAxis.RIGHT, EGamepadDirection.VERTICAL));
			
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
