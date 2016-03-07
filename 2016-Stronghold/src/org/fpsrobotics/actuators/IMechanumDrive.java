package org.fpsrobotics.actuators;

/**
 * NOT USED FOR 2016 SEASON
 */
public interface IMechanumDrive
{
	
	public void setSpeed(double leftSpeed, double rightSpeed);

	public void turnLeft(double speed);

	public void turnLeft(double speed, double degrees);

	public void turnRight(double speed);

	public void turnRight(double speed, double degrees);

	public void goStraight(double speed);

	public void goStraight(double speed, int distance);

	public void goBackward(double speed);

	public void goBackward(double speed, int distance);

	public void driveLeft(double speed);

	public void driveRight(double speed);
	
}
