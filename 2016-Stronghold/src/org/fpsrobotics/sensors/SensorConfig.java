package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

public class SensorConfig 
{
	private static SensorConfig singleton = null;
	
	private final int AUTO_SWITCH_ONES = 0;
	private final int AUTO_SWITCH_TWOS = 1;
	private final int AUTO_SWITCH_FOURS = 2;
	
	private final int DIGITAL_LIMIT_SWITCH_CHANNEL = 3; //TODO: Fix later
	
	private final int POTENTIOMETER_CHANNEL = 2;
	
	private final int HALL_EFFECT_BOTTOM_PORT = 0;
	private final int HALL_EFFECT_TOP_PORT = 1;
	
	private final String CAMERA_USB_PORT = "cam0";
	
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;
	
	private ICounterSwitch autoSwitch;
	
	private ITimer timer;
	
	private ILimitSwitch digitalLimitSwitch;
	
	private IPIDFeedbackDevice launcherPot;
	
	private IHallEffectSensor bottomLimitSensor, topLimitSensor;
	
	private IGamepad gamepad;
	
	private ICamera camera;
	
	private SensorConfig()
	{
		timer = new ClockTimer();
		
		leftJoystick = new Logitech3DJoystick(0);
		rightJoystick = new Logitech3DJoystick(1);
		gamepad = new DualShockTwoController(2);
		
		autoSwitch = new AutonomousSwitches(AUTO_SWITCH_ONES, AUTO_SWITCH_TWOS, AUTO_SWITCH_FOURS);
		
		digitalLimitSwitch = new DigitalLimitSwitch(DIGITAL_LIMIT_SWITCH_CHANNEL);
		
		launcherPot = new Potentiometer(POTENTIOMETER_CHANNEL);
		
		bottomLimitSensor = new AndyMarkHallEffect(HALL_EFFECT_BOTTOM_PORT);
		topLimitSensor = new AndyMarkHallEffect(HALL_EFFECT_TOP_PORT);
		
		camera = new MicrosoftLifeCam(CAMERA_USB_PORT);
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
	
	public ITimer getTimer() {
		return timer;
	}
	
	public ILimitSwitch getLimitSwitch() {
		return digitalLimitSwitch;
	}
	
	public IPIDFeedbackDevice getShooterPot()
	{
		return launcherPot;
	}
	
	public IHallEffectSensor getBottomLimitSensor()
	{
		return bottomLimitSensor;
	}

	public IHallEffectSensor getTopLimitSensor()
	{
		return topLimitSensor;
	}
	
	public IGamepad getGamepad()
	{
		return gamepad;
	}
	
	public ICamera getCamera()
	{
		return camera;
	}
}
