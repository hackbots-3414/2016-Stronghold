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
	private SendableChooser positionChooser;

	public Robot()
	{
		RobotStatus.checkIsAlpha();
		SensorConfig.getInstance();
		ActuatorConfig.getInstance();

		teleop = new MullenatorTeleop();
		executor = Executors.newFixedThreadPool(2);
	}

	public void robotInit()
	{

		makeAutoChooser();
		makePositionChooser();
	}

	private void makeAutoChooser()
	{
		autoChooser = new SendableChooser();

		autoChooser.addObject("Do Nothing", new AutonDoNothing());
		autoChooser.addObject("Reach Defenses", new AutonReachDefenses());
		autoChooser.addObject("Breach Standard Defenses", new AutonBreachDefenses());
//		autoChooser.addDefault("Low Bar NO SHOOT", new AutonLowBar());
		autoChooser.addObject("Low Bar and Shoot Low", new AutonLowBarAndShootLow());
		autoChooser.addObject("Low Bar and Shoot High-PICK THIS ALMOST ALWAYS", new AutonLowBarAndShootHigh());
		autoChooser.addObject("Fourty Kai", new FourtyKai());
		autoChooser.addObject("Cheval De Frise", new AutonChevalDeFrise());
		autoChooser.addObject("Portcullis", new AutonPortcullis());
		
		SmartDashboard.putData("Autonomous Chooser", autoChooser);
	}
	
	private void makePositionChooser()
	{
		positionChooser = new SendableChooser();
		
		positionChooser.addObject("Position One (Low Bar)", EAutoPositions.ONE);
		positionChooser.addObject("Position Two (Something Else)", EAutoPositions.TWO);
		positionChooser.addObject("Position Three (Something Else)", EAutoPositions.THREE);
		positionChooser.addObject("Position Four (Something Else)", EAutoPositions.FOUR);
		positionChooser.addObject("Position Five (Something Else)", EAutoPositions.FIVE);
		
		SmartDashboard.putData("Autonomous Position Chooser", positionChooser);
	}

	public void autonomous()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(true);
		RobotStatus.setIsTeleop(false);

		SensorConfig.getInstance().getGyro().resetCount();

		executor.submit(() ->
		{
			while (RobotStatus.isAuto())
			{
				teleop.printToSmartDashboard();
				
				SensorConfig.getInstance().getTimer().waitTimeInMillis(50);
			}
		});
		executor.submit(() ->
		{
			System.out.println("Auto Running");

			ActuatorConfig.getInstance().getDriveTrainAssist().driveTrainCoast(false); // Should do the same thing
			
//			ActuatorConfig.getInstance().getFrontLeftDriveMotor().enableBrakeMode(true);
//			ActuatorConfig.getInstance().getFrontRightDriveMotor().enableBrakeMode(true);
//			ActuatorConfig.getInstance().getBackLeftDriveMotor().enableBrakeMode(true);
//			ActuatorConfig.getInstance().getBackRightDriveMotor().enableBrakeMode(true);
			
			((IAutonomousControl) autoChooser.getSelected()).doAuto((EAutoPositions)positionChooser.getSelected());
		});
	}

	public void operatorControl()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(true);
		
		ActuatorConfig.getInstance().getDriveTrainAssist().driveTrainCoast(true);
		
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
