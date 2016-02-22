package org.usfirst.frc.team3414.robot;

/**
 * A class that allows other parts of the code to tell if the robot is in operation.
 *
 */
public class RobotStatus 
{
	private static boolean isRunning = false;
	private static boolean isAuto = false;
	private static boolean isTeleop = false;
	
	public static boolean isRunning() 
	{
		return isRunning;
	}

	public static boolean isAuto() 
	{
		return isAuto;
	}
	
	public static boolean isTeleop() 
	{
		return isTeleop;
	}
	
	protected static void setIsRunning(boolean isRunning) 
	{
		RobotStatus.isRunning = isRunning;
	}

	protected static void setIsAuto(boolean isRunning) 
	{
		RobotStatus.isAuto = isRunning;
	}
	
	protected static void setIsTeleop(boolean isRunning) 
	{
		RobotStatus.isTeleop = isRunning;
	}
}
