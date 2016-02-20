package org.usfirst.frc.team3414.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.autonomous.AutonDoNothing;
import org.fpsrobotics.autonomous.AutonLowGoal;
import org.fpsrobotics.autonomous.AutonRockWall;
import org.fpsrobotics.autonomous.AutonRoughTerrain;
import org.fpsrobotics.autonomous.IAutonomousControl;
import org.fpsrobotics.sensors.IVision;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.sensors.VisionCenterTarget;
import org.fpsrobotics.teleop.ITeleopControl;
import org.fpsrobotics.teleop.MullenatorTeleop;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot 
{
	ITeleopControl teleop;
	IAutonomousControl auto;
	SendableChooser autoChooser;
	private ExecutorService executor;
	
    public Robot() 
    {
    	teleop = new MullenatorTeleop();
    	executor = Executors.newFixedThreadPool(2);
    }
    
    public void robotInit() 
    {

    }
    
    IVision vision = new VisionCenterTarget();
    
    private void makeAutoChooser() 
    {
    	autoChooser = new SendableChooser();
        
    	autoChooser.addDefault("Do Nothing", new AutonDoNothing());
    	autoChooser.addDefault("Low Goal", new AutonLowGoal());
    	autoChooser.addDefault("Rock Wall", new AutonRockWall());
    	autoChooser.addDefault("Rough Terrain", new AutonRoughTerrain());
    	
        SmartDashboard.putData("Autonomous Chooser", autoChooser);
    }
    
    public void autonomous() 
    {
	    
    	RobotStatus.setIsRunning(true);
    	
		executor.submit(() ->
		{
			while(RobotStatus.isRunning())
			{
				vision.runSequence();
				
				//System.out.println(SensorConfig.getInstance().getAutoSwitch().getValue());
				
				//SensorConfig.getInstance().getTimer().waitTimeInMillis(300);
				
				//SmartDashboard.putNumber("Autonomous Switches", SensorConfig.getInstance().getAutoSwitch().getValue());
			}
			
			//ActuatorConfig.getInstance().getDriveTrain().turnLeft(0.2, 90);
			//SensorConfig.getInstance().getTimer().waitTimeInSeconds(1.0);
			//ActuatorConfig.getInstance().getDriveTrain().turnRight(0.2, 90);
			
			/*
	    	switch (SensorConfig.getInstance().getAutoSwitch().getValue())
	    	{
	    	case 0:
	    		auto = new AutonDoNothing();
	    		break;
	    	case 1:
	    		auto = new AutonRockWall();
	    		break;
	    	case 2:
	    		auto = new AutonRoughTerrain();
	    		break;
	    	case 3:
	    		auto = new AutonLowGoal();
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
			
			/*
			auto = new AutonRockWall();
	    	
	    	auto.doAuto();
	    	*/
	    	
		});
    	
    	/*
    	System.out.println("Run");
    	ActuatorConfig.getInstance().getDriveTrain().setSpeed(0.1, 0.1);
    	*/
    	
    	//makeAutoChooser();
    	
    	//((IAutonomousControl) autoChooser.getSelected()).doAuto();
    	
    }

    public void operatorControl() 
    {
    	RobotStatus.setIsRunning(true);
    	teleop.doTeleop();
    }
    
    public void disabled()
    {
    	RobotStatus.setIsRunning(false);
    }
    
    public void test() 
    {
    	RobotStatus.setIsRunning(true);
    }
}
