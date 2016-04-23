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
	private boolean didAuto = false;

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
		autoChooser.addObject("Fourty Kai", new FourtyKai());
		autoChooser.addObject("Reach Defenses", new AutonReachDefenses());
		autoChooser.addObject("Breach Standard Defenses", new AutonBreachDefensesAndShoot());
		// autoChooser.addObject("Low Bar and Shoot Low", new AutonLowBarAndShootLow());
		autoChooser.addObject("Low Bar and Shoot High-PICK THIS ALMOST ALWAYS", new AutonLowBarAndShootHigh());
		autoChooser.addObject("Cheval De Frise", new AutonChevalDeFrise());
		autoChooser.addObject("Portcullis (This drives forward)", new AutonPortcullis());

		SmartDashboard.putData("Autonomous Chooser", autoChooser);
	}

	private void makePositionChooser()
	{
		positionChooser = new SendableChooser();

		positionChooser.addObject("Don't shoot at Raul", EAutoPositions.ZERO);
		positionChooser.addObject("Position Two", EAutoPositions.TWO);
		positionChooser.addObject("Position Three", EAutoPositions.THREE);
		positionChooser.addObject("Position Four", EAutoPositions.FOUR);
		positionChooser.addObject("Position Five", EAutoPositions.FIVE);

		SmartDashboard.putData("Autonomous Position Chooser", positionChooser);
	}

	public void autonomous()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(true);
		RobotStatus.setIsTeleop(false);
		didAuto = true;

		SensorConfig.getInstance().getGyro().hardResetCount();

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

			ActuatorConfig.getInstance().getDriveTrainAssist().driveTrainCoast(false);

			switch ((EAutoPositions) positionChooser.getSelected())
			{
			case ZERO:
				SmartDashboard.putNumber("Desired Position", 00);
				break;
			case TWO:
				SmartDashboard.putNumber("Desired Position", 33);
				break;
			case THREE:
				SmartDashboard.putNumber("Desired Position", 15);
				break;
			case FOUR:
				SmartDashboard.putNumber("Desired Position", -5);
				break;
			case FIVE:
				SmartDashboard.putNumber("Desired Position", -22);
				break;
			default:
				System.out.println("Didn't know how to do it! - Raul");
			}
			((IAutonomousControl) autoChooser.getSelected()).doAuto((EAutoPositions) positionChooser.getSelected());
		});
	}

	public void operatorControl()
	{
		System.out.println("Teleop Running");

		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(true);

		ActuatorConfig.getInstance().getDriveTrainAssist().driveTrainCoast(true);
		ActuatorConfig.getInstance().getDriveTrain().setDriveForwardBreak(false);

		if (!didAuto)
		{
			SensorConfig.getInstance().getGyro().hardResetCount();
			ActuatorConfig.getInstance().getLauncher().setAugerOverride(false);
			ActuatorConfig.getInstance().getDriveTrain().setDriveForwardBreak(false);
			ActuatorConfig.getInstance().getDriveTrainAssist().setDriveForwardBreak(false);
			ActuatorConfig.getInstance().getLauncher().stopAugerWheels();
			ActuatorConfig.getInstance().getLauncher().stopShooterWheels();
		}
		teleop.doTeleop();
	}

	public void disabled()
	{
		System.out.println("Robot Disabled");

		didAuto = false;
		
		RobotStatus.setIsRunning(false);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(false);

		ActuatorConfig.getInstance().getLauncher().setAugerOverride(false);
		ActuatorConfig.getInstance().getDriveTrain().setDriveForwardBreak(false);
		ActuatorConfig.getInstance().getDriveTrainAssist().setDriveForwardBreak(false);
		ActuatorConfig.getInstance().getLauncher().stopAugerWheels();
		ActuatorConfig.getInstance().getLauncher().stopShooterWheels();
	}

	public void test()
	{
		RobotStatus.setIsRunning(true);
		RobotStatus.setIsAuto(false);
		RobotStatus.setIsTeleop(false);
	}
}
