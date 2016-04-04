package org.fpsrobotics.actuators;

/**
 * Describes a launcher to be used in the 2016 season for the game Stronghold.
 *
 */
public interface ILauncher
{

	// Lifter Functions
	/**
	 * 
	 * @param slow
	 *            - Used for Driver Control - Gamepad Button 1
	 */
	public void raiseShooter(boolean slow);

	/**
	 * 
	 * @param slow
	 *            - Used for Driver Control - Gamepad Button 1
	 */
	public void lowerShooter(boolean slow);

	public void stopShooterLifter();

	// private boolean isShooterAtTopLimit()

	// private boolean isShooterAtBottomLimit()

	public void moveShooterToPosition(double position);

	// Shooter Functions

	public void intakeBoulder();

	public void stopIntakeBoulder();

	public void launchBoulder();

	/**
	 * Used for Teleop if Sami wants manual spinUp
	 */
	public void spinShooterWheelsHigh();

	/**
	 * Used for Teleop if Sami wants manual spinUp
	 */
	public void spinShooterWheelsLow();

	// private void spinShooterWheels(double speed)

	// private void jostle()

	public void stopShooterWheels();

	// Auger Functions

	public void raiseAuger();

	// private void raiseAuger(double speed)

	// private void rampUpMotor(double speed)

	public void lowerAuger();

	// private void lowerAuger(double speed)

	// private void rampDownMotor(double speed)

	public void stopAugerLifter(boolean ramp);

	public void spinAugerWheels();

	public void stopAugerWheels();

	// private void raiseAugerToTopLimit();

	// private void lowerAugerToBottomLimit();

	// private boolean isAugerAtBottomLimit()

	// private boolean isAugerAtTopLimit()

	public void moveAugerToPosition(int position);

	// Sequences

	public void moveShooterToPreset(EShooterPresets preset);

	public void moveAugerToPreset(EAugerPresets preset);

	public void shootSequenceHigh();

	public void shootSequenceLow();

	public void shootSequenceLowAuto();

	public void shootSequenceHighAuto();

}
