
package org.usfirst.frc.team3414.robot;


import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.autonomous.IAutonomousControl;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.ITeleopControl;
import org.fpsrobotics.teleop.MullenatorTeleop;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot 
{
	ITeleopControl teleop;
	IAutonomousControl auto;
	
    public Robot() 
    {
    	teleop = new MullenatorTeleop();
    }
    
    public void robotInit() 
    {
		
    }

    public void autonomous() 
    {
    	enabled();
    	
    	// TODO: implement different autonomous modes
    	
    	/*
    	switch (SensorConfig.getInstance().getAutoSwitch().getValue())
    	{
    	case 0:
    		break;
    	case 1:
    		break;
    	case 2:
    		break;
    	case 3:
    		break;
    	case 4:
    		break;
    	case 5:
    		break;
    	case 6:
    		break;
    	case 7:
    		break;
    	}
    	*/
    }
    
    public void operatorControl() 
    {
    	enabled();
    	teleop.doTeleop();
    }
    
    public void disabled()
    {
    	RobotStatus.setIsRunning(false);
    }
    
    private void enabled()
    {
    	RobotStatus.setIsRunning(true);
    }
    
    public void test() 
    {
    	enabled();
    }
}
