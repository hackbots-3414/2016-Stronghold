package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class JoystickLogger implements ILogger
{
	IJoystick joyLeft;
	IJoystick joyRight;
	
	public JoystickLogger(IJoystick joyLeft, IJoystick joyRight)
	{
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
			/*
			bw.write("X Value Left " + joyLeft.getX());
			bw.write("X Value Right " + joyRight.getX());
			bw.write("Y Value Left " + joyLeft.getY());
			bw.write("Y Value Right " + joyRight.getY());
			bw.write("Twist Value Left " + joyLeft.getTwist());
			
			try
			{
				bw.write("Twist Value Right " + joyRight.getTwist());
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			*/
		}
	}

}
