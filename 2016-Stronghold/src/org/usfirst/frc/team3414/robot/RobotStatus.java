package org.usfirst.frc.team3414.robot;

public class RobotStatus 
{
	private static boolean isRunning = false;

	public static boolean isRunning() 
	{
		return isRunning;
	}

	protected static void setIsRunning(boolean isRunning) 
	{
		RobotStatus.isRunning = isRunning;
	}

}
