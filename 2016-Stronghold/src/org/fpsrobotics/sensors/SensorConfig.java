package org.fpsrobotics.sensors;

public class SensorConfig 
{
	private static SensorConfig singleton = null;
	
	private final int AUTO_SWITCH_ONES = 0;
	private final int AUTO_SWITCH_TWOS = 1;
	private final int AUTO_SWITCH_FOURS = 2;
	
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;
	
	private ICounterSwitch autoSwitch;
	
	private SensorConfig()
	{
		leftJoystick = new Logitech3DJoystick(0);
		rightJoystick = new Logitech3DJoystick(1);
		
		autoSwitch = new AutonomousSwitches(AUTO_SWITCH_ONES, AUTO_SWITCH_TWOS, AUTO_SWITCH_FOURS);
	}

	public static synchronized SensorConfig getInstance()
	{
		if (singleton == null)
		{
			singleton = new SensorConfig();
		}

		return singleton;
	}

	public IJoystick getLeftJoystick() 
	{
		return leftJoystick;
	}

	public IJoystick getRightJoystick() 
	{
		return rightJoystick;
	}
	
	public ICounterSwitch getAutoSwitch() 
	{
		return autoSwitch;
	}
	
}
