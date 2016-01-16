package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDEnabledDevice;

public interface IDriveTrain extends IPIDEnabledDevice
{
	public void setSpeed(double leftSpeed, double rightSpeed);
	public void turnLeft(double speed);
	public void turnRight(double speed);
	public void goStraight(double speed);
	public void goBackward(double speed);
	public void driveLeft(double speed);
	public void driveRight(double speed);
}
