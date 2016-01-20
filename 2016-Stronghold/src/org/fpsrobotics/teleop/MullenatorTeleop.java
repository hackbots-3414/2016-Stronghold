package org.fpsrobotics.teleop;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.main.RobotStatus;
import org.fpsrobotics.sensors.SensorConfig;

public class MullenatorTeleop extends Thread implements ITeleopControl
{
	public MullenatorTeleop()
	{
		
	}
	
	@Override
	public void doTeleop() 
	{
		start();
	}
	
	public void run()
	{
		while(RobotStatus.isRunning())
		{
			//TODO: write teleop code
			
			ActuatorConfig.getInstance().getDriveTrain().setSpeed(SensorConfig.getInstance().getLeftJoystick().getY(), SensorConfig.getInstance().getRightJoystick().getY());
		}
	}

}
