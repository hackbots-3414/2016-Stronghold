package org.fpsrobotics.actuators;

import org.fpsrobotics.sensors.BuiltInCANTalonEncoder;
import org.fpsrobotics.sensors.Gyroscope;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.CANTalon;

public class ActuatorConfig
{
	private static ActuatorConfig singleton = null;
	
	private static final int MOTOR_LEFT_FRONT = 1;
	private static final int MOTOR_LEFT_REAR = 2;
	private static final int MOTOR_RIGHT_FRONT = 3;
	private static final int MOTOR_RIGHT_REAR = 4;
	
	private static final int LEFT_SHOOTER_MOTOR = 5;
	private static final int RIGHT_SHOOTER_MOTOR = 6;
	
	private static final int LINEAR_ACTUATOR_MOTOR = 7;
	
	private static final double driveTrainP = 0.0000001;
	private static final double driveTrainI = 0.0;
	private static final double driveTrainD = 0.0;
	
	private static final int GYROSCOPE_CHANNEL = 0;
	
	private IDriveTrain driveTrain;
	private ILauncher launcher;
	
	private CANTalon leftFrontMotor;
	private CANTalon leftRearMotor;
	private CANTalon rightFrontMotor;
	private CANTalon rightRearMotor;
	
	private CANTalon leftShooterMotor;
	private CANTalon rightShooterMotor;
	
	private CANTalon linearActuator;
	
	private ActuatorConfig()
	{
		leftFrontMotor = new CANTalon(MOTOR_LEFT_FRONT);
		leftRearMotor = new CANTalon(MOTOR_LEFT_REAR);
		rightFrontMotor = new CANTalon(MOTOR_RIGHT_FRONT);
		rightRearMotor = new CANTalon(MOTOR_RIGHT_REAR);
		
		leftShooterMotor = new CANTalon(LEFT_SHOOTER_MOTOR);
		rightShooterMotor = new CANTalon(RIGHT_SHOOTER_MOTOR);
		
		linearActuator = new CANTalon(LINEAR_ACTUATOR_MOTOR);
		
		driveTrain = new TankDrive(new DoubleMotor(new CANMotor(leftFrontMotor, false, new BuiltInCANTalonEncoder(leftFrontMotor)), new CANMotor(leftRearMotor, false, new BuiltInCANTalonEncoder(leftRearMotor))), new DoubleMotor(new CANMotor(rightFrontMotor, true, new BuiltInCANTalonEncoder(rightFrontMotor)), new CANMotor(rightRearMotor, true, new BuiltInCANTalonEncoder(rightRearMotor))), new Gyroscope(GYROSCOPE_CHANNEL));
		
		driveTrain.setP(driveTrainP);
		driveTrain.setI(driveTrainI);
		driveTrain.setD(driveTrainD);
		
		launcher = new Launcher(new CANMotor
				(leftShooterMotor, false, new BuiltInCANTalonEncoder(leftShooterMotor))
				, new CANMotor(rightShooterMotor, true, new BuiltInCANTalonEncoder(rightShooterMotor))
				, new CANMotor(linearActuator, false)
				, SensorConfig.getInstance().getLimitSwitch()
				, SensorConfig.getInstance().getShooterPot()); //One inverse, one not...accounts for shooting and in-take opposite directions
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