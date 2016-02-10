package org.usfirst.frc.team3414.robot;

/**
 * A class that allows other parts of the code to tell if the robot is in operation.
 *
 */
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
