package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.actuators.ActuatorConfig;

public class SensorConfig 
{
	private static SensorConfig singleton = null;
	
	/*
	private final int AUTO_SWITCH_ONES = 0;
	private final int AUTO_SWITCH_TWOS = 1;
	private final int AUTO_SWITCH_FOURS = 2;
	
	*/
	
	private final int DIGITAL_LIMIT_SWITCH_CHANNEL = 3;
	
	private final int POTENTIOMETER_CHANNEL = 0;
	
	/*
	private final int HALL_EFFECT_BOTTOM_PORT = 0;
	private final int HALL_EFFECT_TOP_PORT = 1;
	*/
	
	private final int AUGER_BOTTOM_LIMIT_SWITCH = 4;
	private final int AUGER_TOP_LIMIT_SWITCH = 5;
	
	/*
	private final String CAMERA_USB_PORT = "cam0";
	*/
	
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;
	
	IPIDFeedbackDevice leftEncoder;
	IPIDFeedbackDevice rightEncoder;
	
	IPIDFeedbackDevice pot;
	
	/*
	
	private ICounterSwitch autoSwitch;
	
	*/
	private ITimer timer;
	
	
	private ILimitSwitch bottomLimitSwitch;
	private ILimitSwitch augerBottomLimitSwitch;
	private ILimitSwitch augerTopLimitSwitch;
	
	/*
	private IHallEffectSensor bottomLimitSensor, topLimitSensor;
	*/
	
	private IGamepad gamepad;
	
	private IPowerBoard pdp;
	
	/*
	private ICamera camera;
	*/
	
	private SensorConfig()
	{
		timer = new ClockTimer();
		
		leftJoystick = new Logitech3DJoystick(0);
		rightJoystick = new Logitech3DJoystick(1);
		gamepad = new DualShockTwoController(2);
		
		/*
		pdp = new PowerDistributionPanel();
		*/
		
		leftEncoder = new BuiltInCANTalonEncoder(ActuatorConfig.getInstance().getLeftFrontMotor());
		rightEncoder = new BuiltInCANTalonEncoder(ActuatorConfig.getInstance().getRightFrontMotor());
		
		pot = new Potentiometer(POTENTIOMETER_CHANNEL);
		
		/*
		autoSwitch = new AutonomousSwitches(AUTO_SWITCH_ONES, AUTO_SWITCH_TWOS, AUTO_SWITCH_FOURS);
		*/
		
		bottomLimitSwitch = new DigitalLimitSwitch(DIGITAL_LIMIT_SWITCH_CHANNEL);
		augerBottomLimitSwitch = new DigitalLimitSwitch(AUGER_BOTTOM_LIMIT_SWITCH);
		augerTopLimitSwitch = new DigitalLimitSwitch(AUGER_TOP_LIMIT_SWITCH);
		
		/*
		camera = new MicrosoftLifeCam(CAMERA_USB_PORT);
		*/
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
	
	/*
	public ICounterSwitch getAutoSwitch() 
	{
		return autoSwitch;
	}
	
	*/
	
	public ITimer getTimer() {
		return timer;
	}
	
	
	public IPIDFeedbackDevice getLeftEncoder()
	{
		return leftEncoder;
	}

	public IPIDFeedbackDevice getRightEncoder()
	{
		return rightEncoder;
	}
	
	

	public IPIDFeedbackDevice getShooterPot() {
		return pot;
	}

	
	public IGamepad getGamepad()
	{
		return gamepad;
	}

	public IPowerBoard getPdp()
	{
		return pdp;
	}

	public ILimitSwitch getBottomLimitSwitch() {
		return bottomLimitSwitch;
	}

	public ILimitSwitch getAugerBottomLimitSwitch() {
		return augerBottomLimitSwitch;
	}

	public ILimitSwitch getAugerTopLimitSwitch() {
		return augerTopLimitSwitch;
	}
	
	
	
	/*
	public ICamera getCamera()
	{
		return camera;
	}
	
	public ILimitSwitch getAugerBottomLimitSwitch() {
		return augerBottomLimitSwitch;
	}

	public ILimitSwitch getAugerTopLimitSwitch() {
		return augerTopLimitSwitch;
	}
	*/
}
