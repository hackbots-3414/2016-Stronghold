package org.fpsrobotics.autonomous;

import org.fpsrobotics.actuators.EAugerPresets;
import org.fpsrobotics.actuators.EShooterPresets;

public interface IDriveTrainAssist
{
	public void centerDriveTrain(double speed);
	
	/**
	 * Warning: If at -170 and want to go to 170, it will turn RIGHT the long way around
	 * WARNING: Overshoots by +/- 5 degrees (with coast on)
	 */
	public void turnNumberOfDegrees(double desiredDegrees, double speed);
	
	public void turnToAngle(double desiredDegrees, double speed);
	
	public boolean isTilt();
	
	public boolean isTiltGyro();
	
	/**
	 * True - Coast
	 * False - Break
	 * @param coast
	 */
	public void driveTrainCoast(boolean coast);
	
	public void setDriveForwardBreak(boolean driveBreaker);

	public void turnToAngleAndMoveShooterAndAugerToPreset(double desiredDegrees, double speed,
			EShooterPresets desiredShooter, EAugerPresets desiredAuger);

	/**
	 * 
	 * @param desiredDegrees
	 * @param speed
	 * @param desiredShooter
	 * @param desiredAuger
	 */
	void turnToAngleAndMoveShooterAndAugerToPreset(double desiredDegrees, double speed,
			int desiredShooter, EAugerPresets desiredAuger);

	/**
	 * 
	 * @param desiredDegrees
	 * @param speed
	 * @param desiredShooter
	 * @param desiredAuger
	 */
	void turnToAngleAndMoveShooterAndAugerToPreset(double desiredDegrees, double speed,
			EShooterPresets desiredShooter, int desiredAuger);

	/**
	 * 
	 * @param desiredDegrees
	 * @param speed
	 * @param desiredShooter
	 * @param desiredAuger
	 */
	void turnToAngleAndMoveShooterAndAugerToPreset(double desiredDegrees, double speed, int desiredShooter,
			int desiredAuger);
}
