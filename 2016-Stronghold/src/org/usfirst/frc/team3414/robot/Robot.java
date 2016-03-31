package org.usfirst.frc.team3414.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fpsrobotics.actuators.ActuatorConfig;
import org.fpsrobotics.autonomous.*;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.ITeleopControl;
import org.fpsrobotics.teleop.MullenatorTeleop;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot
{
	private ITeleopControl teleop;
	private ExecutorService executor;
	private SendableChooser autoChooser;

	public Robot()
	{
		teleop = new MullenatorTeleop();
		executor = Executors.newFixedThreadPool(1);
	}

	public void robotInit()
	{
		makeAutoChooser();
		RobotStatus.checkIsAlpha();
		SensorConfig.getInstance();
		ActuatorConfig.getInstance();
	}

	private void makeAutoChooser()
	{
		autoChooser = new SendableChooser();

		autoChooser.addObject("Do Nothing", new AutonDoNothing());
		autoChooser.addObject("Reach Defenses", new AutonReachDefenses());
		autoChooser.addObject("Breach Standard Defenses", new AutonBreachDefenses());
		autoChooser.addDefault("Low Bar NO SHOOT", new AutonLowBar());
		autoChooser.addObject("Low Bar and Shoot Low", new AutonLowBarAndShootLow());
		autoChooser.addObject("Low Bar and Shoot High-PICK THIS ALMOST ALWAYS", new AutonLowBarAndShootHigh());
		autoChooser.addObject("Fourty Kai", new FourtyKai());
//		autoChooser.addDefault("Chevel De Friz", new AutonChevelDeFriz());	//TODO: Untested

		SmartDashboard.putData("Autonomous Chooser", autoChooser);
	}

	public void autonomous()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(true);
		RobotStatus.setIsTeleop(false);

		//SensorConfig.getInstance().getGyro().resetCount();
		
		executor.submit(() ->
		{
			System.out.println("Auto Running");

			((IAutonomousControl) autoChooser.getSelected()).doAuto();
		});
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
