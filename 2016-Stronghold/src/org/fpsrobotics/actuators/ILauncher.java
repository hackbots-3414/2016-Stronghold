package org.fpsrobotics.actuators;

public interface ILauncher
{
	public void moveShooterToBottomLimit();
	public void goToPresetPosition();
	public void moveShooterToPosition(int position);
	public void moveShooterUp();
	public void moveShooterDown();
	public void intakeBoulder();
	public void shootSequence();
	public void stopShooterWheels();
	public void moveShooterToTopLimit();
	public void raiseAuger();
	public void lowerAuger();
	public void spinShooterUp();
	public void launchBoulder();
	public void augerGoToPosition(int position);

}
