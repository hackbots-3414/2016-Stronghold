package org.fpsrobotics.main;

public class RobotStatus 
{

	private static boolean isRunning;

	public static boolean isRunning() 
	{
		return isRunning;
	}

	protected static void setIsRunning(boolean isRunning) 
	{
		RobotStatus.isRunning = isRunning;
	}

}
