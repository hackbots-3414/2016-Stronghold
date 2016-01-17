package org.fpsrobotics.actuators;

import org.fpsrobotics.sensors.BuiltInCANTalonEncoder;

import edu.wpi.first.wpilibj.CANTalon;

public class ActuatorConfig
{
	private static ActuatorConfig singleton = null;
	
	private static final int MOTOR_LEFT_FRONT = 1;
	private static final int MOTOR_LEFT_REAR = 2;
	private static final int MOTOR_RIGHT_FRONT = 3;
	private static final int MOTOR_RIGHT_REAR = 4;
	
	private static final double driveTrainP = 0.0000001;
	private static final double driveTrainI = 0.0;
	private static final double driveTrainD = 0.0;
	
	private IDriveTrain driveTrain;
	private ILauncher launcher;
	
	private CANTalon leftFrontMotor;
	private CANTalon leftRearMotor;
	private CANTalon rightFrontMotor;
	private CANTalon rightRearMotor;
	
	private CANTalon leftShooterMotor;
	private CANTalon rightShooterMotor;
	
	private ActuatorConfig()
	{
		leftFrontMotor = new CANTalon(MOTOR_LEFT_FRONT);
		leftRearMotor = new CANTalon(MOTOR_LEFT_REAR);
		rightFrontMotor = new CANTalon(MOTOR_RIGHT_FRONT);
		rightRearMotor = new CANTalon(MOTOR_RIGHT_REAR);
		
		driveTrain = new TankDrive(new DoubleMotor(new CANMotor(leftFrontMotor, false, new BuiltInCANTalonEncoder(leftFrontMotor)), new CANMotor(leftRearMotor, false, new BuiltInCANTalonEncoder(leftRearMotor))), new DoubleMotor(new CANMotor(rightFrontMotor, true, new BuiltInCANTalonEncoder(rightFrontMotor)), new CANMotor(rightRearMotor, true, new BuiltInCANTalonEncoder(rightRearMotor))));
		
		driveTrain.setP(driveTrainP);
		driveTrain.setI(driveTrainI);
		driveTrain.setD(driveTrainD);
		
		launcher = new Launcher(new CANMotor(leftShooterMotor, false, new BuiltInCANTalonEncoder(leftShooterMotor)), new CANMotor(rightShooterMotor, true, new BuiltInCANTalonEncoder(rightShooterMotor))); //One inverse, one not...accounts for shooting and intake opposite directions
	}

	public static synchronized ActuatorConfig getInstance()
	{
		if (singleton == null)
		{
			singleton = new ActuatorConfig();
		}

		return singleton;
	}
	
	public IDriveTrain getDriveTrain()
	{
		return driveTrain;
	}
	
	public ILauncher getLauncher()
	{
		return launcher;
	}
}