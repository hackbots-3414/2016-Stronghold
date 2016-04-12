package org.fpsrobotics.autonomous;

public interface IDriveTrainAssist
{
	public void centerDriveTrain(double speed);
	
	public boolean shouldShooterBeRaised();
	
	public void turnToAngle(double desiredDegrees, double speed);
	
	public boolean isTilt();
	
	public boolean isTiltGyro();
	
	public void doChevalAutoActivate();
}
