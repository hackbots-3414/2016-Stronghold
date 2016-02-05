package org.fpsrobotics.teleop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.actuators.ILauncher;
import org.fpsrobotics.sensors.ButtonGamepad;
import org.fpsrobotics.sensors.GamepadLogger;
import org.fpsrobotics.sensors.IGamepad;
import org.fpsrobotics.sensors.ILogger;
import org.fpsrobotics.sensors.JoystickLogger;
import org.fpsrobotics.sensors.OutputDevice;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
				/* Try static P I and D values first
				ActuatorConfig.getInstance().getDriveTrain().setP(SmartDashboard.getNumber("p-value", 0.000001));
				ActuatorConfig.getInstance().getDriveTrain().setI(SmartDashboard.getNumber("i-value", 0.0));
				ActuatorConfig.getInstance().getDriveTrain().setD(SmartDashboard.getNumber("d-value", 0.0));
				*/
				
				// This won't work until we transform it linearly.
				ActuatorConfig.getInstance().getDriveTrain().setSpeed(
						SensorConfig.getInstance().getLeftJoystick().getY(),
						SensorConfig.getInstance().getRightJoystick().getY());
				
				System.out.println("Drive Loop Completed");
				
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
				while(gamepad.getButtonValue(ButtonGamepad.TWO))
				{
					launcher.moveShooterDown();
				}
	
				while(gamepad.getButtonValue(ButtonGamepad.FOUR))
				{
					launcher.moveShooterUp();
				}
				
				launcher.stopShooterLifter();
				
				if(gamepad.getButtonValue(ButtonGamepad.ONE))
				{
					launcher.spinShooterUp();
				} else
				{
					launcher.stopShooterWheels();
				}
				
				if(gamepad.getButtonValue(ButtonGamepad.THREE))
				{
					launcher.intakeBoulder();
				} else
				{
					launcher.stopShooterWheels();
				}
				
				if(gamepad.getButtonValue(ButtonGamepad.SIX))
				{
					launcher.launchBoulder();
				}
				
				System.out.println(SensorConfig.getInstance().getShooterPot().getCount() + " Potentiometer");
			}
		});

		executor.submit(() ->
		{
			while(RobotStatus.isRunning())
			{
				System.out.println(SensorConfig.getInstance().getLeftEncoder().getCount() + " Left Encoder");
				System.out.println(SensorConfig.getInstance().getRightEncoder().getCount() + " Right Encoder");
				SensorConfig.getInstance().getTimer().waitTimeInMillis(1000);
			}
		});
		
		/*
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
		*/
		
	}
}
