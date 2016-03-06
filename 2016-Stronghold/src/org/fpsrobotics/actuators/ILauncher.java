package org.fpsrobotics.actuators;

/**
 * Describes a launcher to be used in the 2016 season for the game Stronghold.
 *
 */
public interface ILauncher
{
	public void moveShooterToBottomLimit();
	public void moveShooterToPosition(double position);
	public void moveShooterUp();
	public void moveShooterDown();
	public void intakeBoulder();
	public void shootSequence();
	public void shootSequence(double speed);
	public void stopShooterWheels();
	public void moveShooterToTopLimit();
	public void raiseAuger();
	public void lowerAuger();
	public void spinShooterUp();
	public void launchBoulder();
	public void augerGoToPosition(int position);
	public void stopShooterLifter();
	public void stopAugerLifter();
	public void raiseAugerToTopLimit();
	public void lowerAugerToBottomLimit();
	public void spinAugerUp();
	public void stopAuger();
}
