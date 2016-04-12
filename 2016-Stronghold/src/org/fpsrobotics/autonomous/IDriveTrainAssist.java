package org.fpsrobotics.autonomous;

public interface IDriveTrainAssist
{
	public void centerDriveTrain(double speed);
	
	public void turnToAngle(double desiredDegrees, double speed);
	
	public boolean isTilt();
	
	public boolean isTiltGyro();
	
	public void driveTrainCoast(boolean coast);
}
