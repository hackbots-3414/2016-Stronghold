package org.fpsrobotics.actuators;

/**
 * Describes a launcher to be used in the 2016 season for the game Stronghold.
 *
 */
public interface ILauncher
{
	public boolean isAugerCalibrated();
	
	// Lifter Functions
//	public void lowerShooterToBottomLimit();
	
//	public void raiseShooterToTopLimit();
	
	public void raiseShooter();

	public void lowerShooter();
	
	public void stopShooterLifter();
	
	public void moveShooterToPosition(double position);
	
	// Shooter Functions
	
	public void intakeBoulder();
	
	public void stopIntakeBoulder();
	
	public void launchBoulder();
	
	public void spinShooterWheels();
	
	public void stopShooterWheels();
	
	// Auger Functions
	
	public void raiseAuger();

	public void lowerAuger();
	
	public void stopAugerLifter();
	
	public void spinAugerWheels();

	public void stopAugerWheels();
	
//	public void raiseAugerToTopLimit();

//	public void lowerAugerToBottomLimit();
	
	public void moveAugerToPosition(int position);
	
	//Sequences

	public void moveShooterToPreset(EShooterPresets preset);
	
	public void moveAugerToPreset(EAugerPresets preset);

	public void shootSequence();

	public void shootSequence(double speed);

	

	

	

}
