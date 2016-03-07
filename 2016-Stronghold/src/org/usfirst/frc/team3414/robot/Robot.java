package org.usfirst.frc.team3414.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.autonomous.AutonDoNothing;
import org.fpsrobotics.autonomous.AutonLowBar;
import org.fpsrobotics.autonomous.AutonRockWall;
import org.fpsrobotics.autonomous.AutonRoughTerrain;
import org.fpsrobotics.autonomous.IAutonomousControl;
import org.fpsrobotics.autonomous.MullenatorAutonomous;
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
	private ITeleopControl teleop;
	private IAutonomousControl auto;
	private ExecutorService executor;
//	private SendableChooser autoChooser;

	public Robot()
	{
		teleop = new MullenatorTeleop();
		executor = Executors.newFixedThreadPool(2);
	}

	public void robotInit()
	{
//		makeAutoChooser();
	}

	private void makeAutoChooser()
	{
//		autoChooser = new SendableChooser();
//
//		autoChooser.addDefault("Do Nothing", new AutonDoNothing());
//		autoChooser.addObject("Low Bar", new AutonLowBar());
//		autoChooser.addObject("Rock Wall", new AutonRockWall());
//		autoChooser.addObject("Rough Terrain", new AutonRoughTerrain());
//
//		SmartDashboard.putData("Autonomous Chooser", autoChooser);
	}

	public void autonomous()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(true);
		RobotStatus.setIsTeleop(false);

//		executor.submit(() ->
//		{
//			System.out.println("Auto Running");
//
//			((IAutonomousControl) autoChooser.getSelected()).doAuto();
//		});
		MullenatorAutonomous.getInstance().doAuto();
	}

	public void operatorControl()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(true);

		teleop.doTeleop();
	}

	public void disabled()
	{
		RobotStatus.setIsRunning(false);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(false);
	}

	public void test()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(false);
	}
}
