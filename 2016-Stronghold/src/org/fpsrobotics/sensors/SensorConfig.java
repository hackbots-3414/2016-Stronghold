package org.fpsrobotics.sensors;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.actuators.ActuatorConfig;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;

public class SensorConfig
{
	private static SensorConfig singleton = null;

	private final int SHOOTER_BOTTOM_LIMIT_CHANNEL = 0;
	private final int SHOOTER_TOP_LIMIT_CHANNEL = 1;
	private ILimitSwitch shooterBottomLimitSwitch;
	private ILimitSwitch shooterTopLimitSwitch;

	private final int POTENTIOMETER_CHANNEL = 0;
	private IPIDFeedbackDevice pot;

	private final int AUGER_BOTTOM_LIMIT_SWITCH = 2;
	private final int AUGER_TOP_LIMIT_SWITCH = 3;
	private ILimitSwitch augerBottomLimitSwitch;
	private ILimitSwitch augerTopLimitSwitch;

	private final String CAMERA_USB_PORT = "cam0";
	private ICamera cameraOne;

	private final int PRESSURE_SWITCH_CHANNEL = 9;
	private ILimitSwitch pressureSwitch;

	private final int LEFT_JOY_CHANNEL = 0;
	private final int RIGHT_JOY_CHANNEL = 1;
	private final int GAMEPAD_CHANNEL = 2;
	private IJoystick leftJoystick;
	private IJoystick rightJoystick;
	private IGamepad gamepad;
	
	private CompassNavX compassNavX;
	private AccelerometerNavX accelNavX;

	private ITimer timer;

	private IPowerBoard pdp;
	private AHRS ahrs;
	private IGyroscope gyro;

	// TODO
	private final int AUTO_SWITCH_ONE_CHANNEL = 0;
	private final int AUTO_SWITCH_TWO_CHANNEL = 0;
	private final int AUTO_SWITCH_FOUR_CHANNEL = 0;
	private ILimitSwitch autoSwitchOnes;
	private ILimitSwitch autoSwitchTwos;
	private ILimitSwitch autoSwitchFours;

	private SensorConfig()
	{
		timer = new ClockTimer();

		try
		{
			leftJoystick = new Logitech3DJoystick(new Joystick(LEFT_JOY_CHANNEL));
			rightJoystick = new Logitech3DJoystick(new Joystick(RIGHT_JOY_CHANNEL));
			gamepad = new DualShockTwoController(new Joystick(GAMEPAD_CHANNEL));
		} catch (Exception e)
		{
			System.err.println("Joystick failed to initialize");
		}

		try
		{
			pot = new Potentiometer(new AnalogInput(POTENTIOMETER_CHANNEL));
		} catch (Exception e)
		{
			System.err.println("Potentiometer failed to initialize");
		}

		try
		{
			shooterBottomLimitSwitch = new DigitalLimitSwitch(new DigitalInput(SHOOTER_BOTTOM_LIMIT_CHANNEL), true);
			shooterTopLimitSwitch = new DigitalLimitSwitch(new DigitalInput(SHOOTER_TOP_LIMIT_CHANNEL), true);

			augerBottomLimitSwitch = new DigitalLimitSwitch(new DigitalInput(AUGER_BOTTOM_LIMIT_SWITCH), true);
			augerTopLimitSwitch = new DigitalLimitSwitch(new DigitalInput(AUGER_TOP_LIMIT_SWITCH), true);

			pressureSwitch = new IS1000PressureSwitch(PRESSURE_SWITCH_CHANNEL, true);
		} catch (Exception e)
		{
			System.err.println("A limit switch failed to initialize; Stopping to avoid damage");
			System.exit(1);
		}

		try
		{
			ahrs = new AHRS(SPI.Port.kMXP);
			gyro = new GyroscopeNavX(ahrs);
		} catch (Exception e)
		{
			System.err.println("No NavX MXP board found, or plugged into the wrong spot");
		}

		try
		{
			autoSwitchOnes = new DigitalLimitSwitch(new DigitalInput(AUTO_SWITCH_ONE_CHANNEL), true);
			autoSwitchTwos = new DigitalLimitSwitch(new DigitalInput(AUTO_SWITCH_TWO_CHANNEL), true);
			autoSwitchFours = new DigitalLimitSwitch(new DigitalInput(AUTO_SWITCH_FOUR_CHANNEL), true);
		} catch (Exception e)
		{
			System.err.println("Auto Switches failed to initialize: Make sure to set SmartDashboard to not 0");
		}

		pdp = new PowerDistributionPanel();
		
		cameraOne = new MicrosoftLifeCam(CAMERA_USB_PORT);

		cameraOne.enable();
		
		accelNavX = new AccelerometerNavX(ahrs);
		compassNavX = new CompassNavX(ahrs);
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

	public ITimer getTimer()
	{
		return timer;
	}

	public IPIDFeedbackDevice getShooterPot()
	{
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

	public ILimitSwitch getShooterBottomLimitSwitch()
	{
		return shooterBottomLimitSwitch;
	}

	public ILimitSwitch getShooterTopLimitSwitch()
	{
		return shooterTopLimitSwitch;
	}

	public ILimitSwitch getAugerBottomLimitSwitch()
	{
		return augerBottomLimitSwitch;
	}

	public ILimitSwitch getAugerTopLimitSwitch()
	{
		return augerTopLimitSwitch;
	}

	public IGyroscope getGyro()
	{
		return gyro;
	}

	public ILimitSwitch getPressureSwitch()
	{
		return pressureSwitch;
	}

	public ILimitSwitch getAutoSwitchOnes()
	{
		return autoSwitchOnes;
	}

	public ILimitSwitch getAutoSwitchTwos()
	{
		return autoSwitchTwos;
	}

	public ILimitSwitch getAutoSwitchFours()
	{
		return autoSwitchFours;
	}
	
	public AccelerometerNavX getAccelerometer()
	{
		return accelNavX;
	}
	
	public CompassNavX getCompass()
	{
		return compassNavX;
	}

	/*
	 * public ICamera getCameraOne() { return cameraOne; }
	 */

}
