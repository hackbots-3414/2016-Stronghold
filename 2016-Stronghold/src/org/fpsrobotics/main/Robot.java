
package org.fpsrobotics.main;


import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.ITeleopControl;

import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot 
{
	ITeleopControl teleop;
	
    public Robot() 
    {

    }
    
    public void robotInit() 
    {
		//This must always get run at the start of init. Do not perform any init before this is called
		RobotStatus.setIsRunning(true);
    }

    public void autonomous() 
    {
    	// TODO: implement different autonomous modes
    	
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
    }

    public void operatorControl() 
    {
    	teleop.doTeleop();
    }
    
    public void disabledInit()
    {
    	RobotStatus.setIsRunning(false);
    }
    
    public void test() 
    {
    	
    }
}
