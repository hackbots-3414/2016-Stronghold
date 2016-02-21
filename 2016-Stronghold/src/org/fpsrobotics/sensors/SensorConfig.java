package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.actuators.ActuatorConfig;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

public class SensorConfig 
{
	private static SensorConfig singleton = null;
	
	private final int AUTO_SWITCH_ONES = 2;
	private final int AUTO_SWITCH_TWOS = 3;
	private final int AUTO_SWITCH_FOURS = 4;
	
	private final int SHOOTER_BOTTOM_LIMIT_CHANNEL = 0;
	private final int SHOOTER_TOP_LIMIT_CHANNEL = 1;
	
	private final int POTENTIOMETER_CHANNEL = 0;
	
	private final int AUGER_BOTTOM_LIMIT_SWITCH = 5;
	private final int AUGER_TOP_LIMIT_SWITCH = 6;
	
	private final String CAMERA_USB_PORT = "cam0";
	private final String CAMERA_USB_PORT_TWO = "cam1";
	
	//private ICamera cameraTwo;
	//private ICamera cameraOne;
	
	
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;
	
	IPIDFeedbackDevice pot;
	
	private ICounterSwitch autoSwitch;
	
	private ITimer timer;
	
	private ILimitSwitch bottomLimitSwitch;
	private ILimitSwitch topLimitSwitch;
	private ILimitSwitch augerBottomLimitSwitch;
	private ILimitSwitch augerTopLimitSwitch;
	
	private IGamepad gamepad;
	
	private IPowerBoard pdp;
	
	private IGyroscope gyro;
	
	private SensorConfig()
	{
		timer = new ClockTimer();
		
		try
		{
			leftJoystick = new Logitech3DJoystick(0);
			rightJoystick = new Logitech3DJoystick(1);
			gamepad = new DualShockTwoController(2);
		} catch(Exception e)
		{
			System.err.println("Joystick failed to initialize");
		}
		
		try
		{
			pot = new Potentiometer(POTENTIOMETER_CHANNEL);
		} catch(Exception e)
		{
			System.err.println("Potentiometer failed to initialize");
		}
		
		try
		{
			autoSwitch = new AutonomousSwitches(AUTO_SWITCH_ONES, AUTO_SWITCH_TWOS, AUTO_SWITCH_FOURS, true);
		} catch(Exception e)
		{
			System.err.println("Autonomous switches failed to initialize");
		}
		
		try
		{
			bottomLimitSwitch = new DigitalLimitSwitch(SHOOTER_BOTTOM_LIMIT_CHANNEL, true);
			topLimitSwitch = new DigitalLimitSwitch(SHOOTER_TOP_LIMIT_CHANNEL, true);
			augerBottomLimitSwitch = new DigitalLimitSwitch(AUGER_BOTTOM_LIMIT_SWITCH, true);
			augerTopLimitSwitch = new DigitalLimitSwitch(AUGER_TOP_LIMIT_SWITCH, true);
		} catch (Exception e)
		{
			System.err.println("A limit switch failed to initialize, stopping to avoid damage");
			System.exit(1);
		}
		
		try
		{
			AHRS ahrs = new AHRS(SPI.Port.kMXP);
			gyro = new GyroscopeNavX(ahrs);
		} catch(Exception e)
		{
			System.err.println("No NavX MXP board found, or plugged into the wrong spot");
		}
		
		//cameraOne = new MicrosoftLifeCam(CAMERA_USB_PORT);
		
		//cameraOne.enable();
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
	
	public ILimitSwitch getTopLimitSwitch()
	{
		return topLimitSwitch;
	}
	
	public IGyroscope getGyro()
	{
		return gyro;
	}
	
	/*
	public ICamera getCameraOne()
	{
		return cameraOne;
	}
	*/
	
}
