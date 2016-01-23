package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.ILauncher;
import org.fpsrobotics.main.RobotStatus;
import org.fpsrobotics.sensors.ButtonGamepad;
import org.fpsrobotics.sensors.GamepadLogger;
import org.fpsrobotics.sensors.IGamepad;
import org.fpsrobotics.sensors.ILogger;
import org.fpsrobotics.sensors.JoystickLogger;
import org.fpsrobotics.sensors.OutputDevice;
import org.fpsrobotics.sensors.SensorConfig;

public class MullenatorTeleop implements ITeleopControl
{
	private ExecutorService executor;
	
	public MullenatorTeleop()
	{
		executor = Executors.newFixedThreadPool(2);
	}
	
	@Override
	public void doTeleop() 
	{
		executor.submit(() ->
		{
			while(RobotStatus.isRunning())
			{
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(SensorConfig.getInstance().getLeftJoystick().getY(), SensorConfig.getInstance().getRightJoystick().getY());
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
			}
		});
		
		executor.submit(() ->
		{
			ILauncher launcher;
			IGamepad gamepad;
			
			launcher = ActuatorConfig.getInstance().getLauncher();
			gamepad = SensorConfig.getInstance().getGamepad();
			
			while(RobotStatus.isRunning())
			{
				if(gamepad.getButtonValue(ButtonGamepad.TWO))
				{
					launcher.goDown();
				}
				
				if(gamepad.getButtonValue(ButtonGamepad.FOUR))
				{
					launcher.goUp();
				}
				
				if(gamepad.getButtonValue(ButtonGamepad.ONE))
				{
					launcher.shoot();
				}
				
				if(gamepad.getButtonValue(ButtonGamepad.THREE))
				{
					launcher.intake();
				}
			}
		});
		
		executor.submit(() ->
		{
			ILogger joyLogger, gamepadLogger;
			
			joyLogger = new JoystickLogger(SensorConfig.getInstance().getLeftJoystick(), SensorConfig.getInstance().getRightJoystick());
			gamepadLogger = new GamepadLogger(SensorConfig.getInstance().getGamepad());
			
			while(RobotStatus.isRunning())
			{
				joyLogger.reportInformation(OutputDevice.SMARTDASHBOARD);
				gamepadLogger.reportInformation(OutputDevice.SMARTDASHBOARD);
			}
		});
	}
}
