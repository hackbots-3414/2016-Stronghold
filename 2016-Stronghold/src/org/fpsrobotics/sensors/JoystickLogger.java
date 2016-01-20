package org.fpsrobotics.sensors;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class JoystickLogger implements ILogger
{
	IJoystick joyLeft;
	IJoystick joyRight;
	PrintWriter writer;
	
	public JoystickLogger(IJoystick joyLeft, IJoystick joyRight)
	{
		try
		{
			writer = new PrintWriter("joystickLog.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.joyLeft = joyLeft;
		this.joyRight = joyRight;
	}
	
	@Override
	public void reportInformation(OutputDevice device) 
	{
		if(device == OutputDevice.SMARTDASHBOARD)
		{
			SmartDashboard.putNumber("X Value Left", joyLeft.getX());
			SmartDashboard.putNumber("Y Value Left", joyLeft.getY());
			SmartDashboard.putNumber("Twist Value Left", joyLeft.getTwist());
			
			SmartDashboard.putNumber("X Value Right", joyRight.getX());
			SmartDashboard.putNumber("Y Value Right", joyRight.getY());
			SmartDashboard.putNumber("Twist Value Right", joyRight.getTwist());
			
		} else if(device == OutputDevice.FILE)
		{
			writer.println("X Value Left " + joyLeft.getX());
			writer.println("X Value Right " + joyRight.getX());
			writer.println("Y Value Left " + joyLeft.getY());
			writer.println("Y Value Right " + joyRight.getY());
			writer.println("Twist Value Left " + joyLeft.getTwist());
			writer.println("Twist Value Right " + joyRight.getTwist());
		}
	}

}
