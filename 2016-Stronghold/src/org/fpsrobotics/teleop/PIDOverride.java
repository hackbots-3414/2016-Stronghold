package org.fpsrobotics.teleop;

/**
 * This class is used so TankDrive goStraight, turnRight, and turnLeft methods
 * won't enable PID after they finish, if driver already disabled PID
 */
public class PIDOverride
{
	private static PIDOverride singleton = null;

	private boolean isDisablePID = false;

	private PIDOverride()
	{

	}

	public static synchronized PIDOverride getInstance()
	{
		if (singleton == null)
		{
			singleton = new PIDOverride();
		}
		return singleton;
	}

	public void setTeleopDisablePID(boolean isDisablePID)
	{
		this.isDisablePID = isDisablePID;
	}

	public boolean isTeleopDisablePID()
	{
		return this.isDisablePID;
	}

}
