package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.BuiltInCANTalonEncoder;
import org.fpsrobotics.sensors.Gyroscope;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class ActuatorConfig
{
	private static ActuatorConfig singleton = null;
	
	private static final int MOTOR_LEFT_FRONT = 3;
	private static final int MOTOR_LEFT_REAR = 4;
	private static final int MOTOR_RIGHT_FRONT = 1;
	private static final int MOTOR_RIGHT_REAR = 2;
	
	private static final int LEFT_SHOOTER_MOTOR = 5;
	private static final int RIGHT_SHOOTER_MOTOR = 6;
	
	private static final int LINEAR_ACTUATOR_MOTOR = 7;
	private static final int AUGER_INTAKE_MOTOR = 8;
	private static final int AUGER_LIFTER_MOTOR = 9;
	
	private final int SERVO_PORT = 9;
	
	/*
	private static final double driveTrainP = 0.0001; 
	private static final double driveTrainI = 0.0;
	private static final double driveTrainD = 0.0;
	*/
	
	/*
	private static final int GYROSCOPE_CHANNEL = 0;
	*/
	
	private IDriveTrain driveTrain;
	
	private ILauncher launcher;
	
	private CANTalon leftFrontMotor;
	private CANTalon leftRearMotor;
	private CANTalon rightFrontMotor;
	private CANTalon rightRearMotor;
	
	private DoubleMotor leftDoubleMotor;
	private DoubleMotor rightDoubleMotor;
	
	private CANTalon leftShooterMotor;
	private CANTalon rightShooterMotor;
	
	private CANTalon augerIntakeMotor;
	private CANTalon augerLifterMotor;
	
	private CANTalon linearActuator;
	
	private CANMotor leftFrontCANMotor;
	private CANMotor leftRearCANMotor;
	private CANMotor rightFrontCANMotor;
	private CANMotor rightRearCANMotor;
	
	IPIDFeedbackDevice leftEncoder;
	IPIDFeedbackDevice rightEncoder;
	
	private IServo servo;
	
	private ActuatorConfig()
	{
		leftFrontMotor = new CANTalon(MOTOR_LEFT_FRONT);
		leftRearMotor = new CANTalon(MOTOR_LEFT_REAR);
		rightFrontMotor = new CANTalon(MOTOR_RIGHT_FRONT);
		rightRearMotor = new CANTalon(MOTOR_RIGHT_REAR);
		
		leftEncoder = new BuiltInCANTalonEncoder(leftFrontMotor);
		rightEncoder = new BuiltInCANTalonEncoder(rightFrontMotor);
		
		leftFrontCANMotor = new CANMotor(leftFrontMotor, true, leftEncoder);
		leftRearCANMotor = new CANMotor(leftRearMotor, true);
		
		rightFrontCANMotor = new CANMotor(rightFrontMotor, false, rightEncoder);
		rightRearCANMotor = new CANMotor(rightRearMotor, false);
		
		rightFrontMotor.reverseSensor(true);
		leftFrontMotor.reverseSensor(true);
		
		leftDoubleMotor = new DoubleMotor(leftFrontCANMotor, leftRearCANMotor);
		rightDoubleMotor = new DoubleMotor(rightFrontCANMotor, rightRearCANMotor);
		
		driveTrain = new TankDrive(leftDoubleMotor, rightDoubleMotor);
		
		driveTrain.setControlMode(TalonControlMode.Speed);
		
		leftShooterMotor = new CANTalon(LEFT_SHOOTER_MOTOR);
		rightShooterMotor = new CANTalon(RIGHT_SHOOTER_MOTOR);
		
		linearActuator = new CANTalon(LINEAR_ACTUATOR_MOTOR);
		
		augerIntakeMotor = new CANTalon(AUGER_INTAKE_MOTOR);
		augerLifterMotor = new CANTalon(AUGER_LIFTER_MOTOR);
		
		servo = new Servo(SERVO_PORT);
		
		launcher = new Launcher(
				new CANMotor(leftShooterMotor, true),
				new CANMotor(rightShooterMotor, false),
				new CANMotor(linearActuator, true),
				new CANMotor(augerIntakeMotor, false), 
				new CANMotor(augerLifterMotor, false), 
				SensorConfig.getInstance().getBottomLimitSwitch(),
				SensorConfig.getInstance().getAugerBottomLimitSwitch(), 
				SensorConfig.getInstance().getAugerTopLimitSwitch(), 
				SensorConfig.getInstance().getShooterPot(),
			    servo
				);
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

	public CANTalon getLeftFrontMotor()
	{
		return leftFrontMotor;
	}

	public CANTalon getRightFrontMotor()
	{
		return rightFrontMotor;
	}
	
	public ILauncher getLauncher()
	{
		return launcher;
	}

	public IServo getServo() {
		return servo;
	}

	public IPIDFeedbackDevice getLeftEncoder() {
		return leftEncoder;
	}

	public IPIDFeedbackDevice getRightEncoder() {
		return rightEncoder;
	}
	
	
	
}