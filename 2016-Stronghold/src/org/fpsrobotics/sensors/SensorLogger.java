package org.fpsrobotics.sensors;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.fpsrobotics.actuators.ActuatorConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SensorLogger implements ILogger
{
	private PrintWriter writer;

	// private static SensorLogger singleton;

	// private SensorLogger()
	public SensorLogger()
	{
		try
		{
			writer = new PrintWriter("RobotLog_" + getLoggerTimeStamp() + "_.txt", "UTF-8");
		} catch (FileNotFoundException e)
		{
			System.out.println("Write File Not Found");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			System.out.println("UTF-8 Not Found");
			e.printStackTrace();
		}
		writeAutoModes();
	}

	// public synchronized static SensorLogger getInstance()
	// {
	// if (singleton != null)
	// {
	// singleton = new SensorLogger();
	// }
	// return singleton;
	// }

	@Override
	public void reportInformation(EOutputDevice device)
	{
		if (device == EOutputDevice.SMARTDASHBOARD)
		{

			SmartDashboard.putBoolean("Auger Top Limit Hit",
					SensorConfig.getInstance().getAugerTopLimitSwitch().isHit());
			SmartDashboard.putBoolean("Auger Bottom Limit Hit",
					SensorConfig.getInstance().getAugerBottomLimitSwitch().isHit());
			SmartDashboard.putNumber("Auger Encoder Value", ActuatorConfig.getInstance().getAugerEncoder().getCount());
			SmartDashboard.putBoolean("Shooter Bottom Limit Hit",
					SensorConfig.getInstance().getShooterBottomLimitSwitch().isHit());
			SmartDashboard.putBoolean("Shooter Top Limit Hit",
					SensorConfig.getInstance().getShooterTopLimitSwitch().isHit());
			SmartDashboard.putNumber("Shooter Pot Value", SensorConfig.getInstance().getShooterPot().getCount());
			SmartDashboard.putBoolean("Is Auger Encoder Calibrated",
					ActuatorConfig.getInstance().getLauncher().isAugerCalibrated());

		} else if (device == EOutputDevice.FILE)
		{

			// writer.println("Left X " +
			// gamepad.getAnalogStickValue(AnalogStick.LEFT,
			// GamepadDirection.HORIZONTAL));
			// writer.println("Right X " +
			// gamepad.getAnalogStickValue(AnalogStick.RIGHT,
			// GamepadDirection.HORIZONTAL));
			//
			// writer.println("Left Y " +
			// gamepad.getAnalogStickValue(AnalogStick.LEFT,
			// GamepadDirection.VERTICAL));
			// writer.println("Right Y " +
			// gamepad.getAnalogStickValue(AnalogStick.RIGHT,
			// GamepadDirection.VERTICAL));

		}
	}

	private void writeAutoModes()
	{
		SmartDashboard.putString("Switch - 0; Dashboard - 1", "Do Nothing");
		SmartDashboard.putString("Switch - 1; Dashboard - 2", "Drives Striaght");
	}

	private String getLoggerTimeStamp()
	{
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return "_" + minute + "_:_" + hour + "_:_" + month + "_:_" + day + "_:_" + year + "_";
	}

}
