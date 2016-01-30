package org.usfirst.frc.team3414.robot;




import org.fpsrobotics.autonomous.AutonDoNothing;
import org.fpsrobotics.autonomous.IAutonomousControl;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.ITeleopControl;
import org.fpsrobotics.teleop.MullenatorTeleop;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot 
{
	ITeleopControl teleop;
	IAutonomousControl auto;
	SendableChooser autoChooser;
	
    public Robot() 
    {
    	teleop = new MullenatorTeleop();
    }
    
    public void robotInit() 
    {
		//This must always get run at the start of init. Do not perform any init before this is called
		RobotStatus.setIsRunning(true);
    }

    public void autonomous() 
    {
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
    	
    	makeAutoChooser();
    	
    	((IAutonomousControl) autoChooser.getSelected()).doAuto();
    	
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
    
    private void makeAutoChooser() {
    	autoChooser = new SendableChooser();
        
    	autoChooser.addDefault("Do Nothing", new AutonDoNothing());
    	// TODO: Add more auton things later
        SmartDashboard.putData("Autonomous Chooser", autoChooser);
    }
}
