package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.autonomous.DriveTrainAssist;
import org.fpsrobotics.sensors.BuiltInCANTalonEncoder;
import org.fpsrobotics.sensors.Gyroscope;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
/**
 * Singleton class that creates and distributes all of the actuators (motors, solenoids, linear actuators) to the rest of the program.
 * Encoders are included in this class as well because of the close integration with motors.
 */
public class ActuatorConfig
{
	private static ActuatorConfig singleton = null;
	
	// Port definitions
	private final int MOTOR_LEFT_FRONT = 3;
	private final int MOTOR_LEFT_REAR = 4;
	private final int MOTOR_RIGHT_FRONT = 1;
	private final int MOTOR_RIGHT_REAR = 2;
	
	private final int LEFT_SHOOTER_MOTOR = 5;
	private final int RIGHT_SHOOTER_MOTOR = 6;
	
	private final int LINEAR_ACTUATOR_MOTOR = 7;
	private final int AUGER_INTAKE_MOTOR = 8;
	private final int AUGER_LIFTER_MOTOR = 9;
	
	private final int SOLENOID_PORT = 0;
	
	// Solenoid
	ISolenoid shooterSolenoid;
	
	// Drive base classes
	private CANTalon leftFrontMotor;
	private CANTalon leftRearMotor;
	private CANTalon rightFrontMotor;
	private CANTalon rightRearMotor;
	
	// Drive CAN Motors
	private CANMotor leftFrontCANMotor;
	private CANMotor leftRearCANMotor;
	private CANMotor rightFrontCANMotor;
	private CANMotor rightRearCANMotor;
	
	// Drive double motors
	private DoubleMotor leftDoubleMotor;
	private DoubleMotor rightDoubleMotor;
	
	// Drive encoders
	private IPIDFeedbackDevice leftEncoder;
	private IPIDFeedbackDevice rightEncoder;
	
	// Drive train
	private IDriveTrain driveTrain;
	
	// Drive assist
	private DriveTrainAssist driveAssist;
	
	// Shooter motors
	private CANTalon leftShooterMotor;
	private CANTalon rightShooterMotor;
	
	// Auger motors
	private CANTalon augerIntakeMotor;
	private CANTalon augerLifterMotor;
	
	// Shooter lifter motor
	private CANTalon linearActuator;
	
	// Launcher
	private ILauncher launcher;
	
	private ActuatorConfig()
	{
		// Instantiate CANTalons
		leftFrontMotor = new CANTalon(MOTOR_LEFT_FRONT);
		leftRearMotor = new CANTalon(MOTOR_LEFT_REAR);
		rightFrontMotor = new CANTalon(MOTOR_RIGHT_FRONT);
		rightRearMotor = new CANTalon(MOTOR_RIGHT_REAR);
		
		// Instantiate encoders
		leftEncoder = new BuiltInCANTalonEncoder(leftFrontMotor);
		rightEncoder = new BuiltInCANTalonEncoder(rightFrontMotor);
		
		// Instantiate CANMotors
		leftFrontCANMotor = new CANMotor(leftFrontMotor, true, leftEncoder);
		leftRearCANMotor = new CANMotor(leftRearMotor, true);
		
		rightFrontCANMotor = new CANMotor(rightFrontMotor, false, rightEncoder);
		rightRearCANMotor = new CANMotor(rightRearMotor, false);
		
		// Reverse the encoders because they are installed backward relative to our setspeed methods
		rightFrontMotor.reverseSensor(true);
		leftFrontMotor.reverseSensor(true);
		
		// Create the double motors
		leftDoubleMotor = new DoubleMotor(leftFrontCANMotor, leftRearCANMotor);
		rightDoubleMotor = new DoubleMotor(rightFrontCANMotor, rightRearCANMotor);
		
		// Create the whole drivetrain
		driveTrain = new TankDrive(leftDoubleMotor, rightDoubleMotor, SensorConfig.getInstance().getGyro());
		
		// Create drive assist
		driveAssist = new DriveTrainAssist(driveTrain, SensorConfig.getInstance().getGyro());
				
		//driveTrain = new TankDrive(leftDoubleMotor, rightDoubleMotor);
		
		// set it to use PID by default
		driveTrain.enablePID();
		driveTrain.setControlMode(TalonControlMode.Speed); // Speed means use encoder rates 
														   // to control how fast the robot is moving
		
		// Instantiate shooter motors
		leftShooterMotor = new CANTalon(LEFT_SHOOTER_MOTOR);
		rightShooterMotor = new CANTalon(RIGHT_SHOOTER_MOTOR);
		
		// Instantiate shooter lifter
		linearActuator = new CANTalon(LINEAR_ACTUATOR_MOTOR);
		
		// Instantiate auger motors
		augerIntakeMotor = new CANTalon(AUGER_INTAKE_MOTOR);
		augerLifterMotor = new CANTalon(AUGER_LIFTER_MOTOR);
		
		// Instantiate solenoid
		shooterSolenoid = new SingleSolenoid(SOLENOID_PORT);
		
		// Instantiate the launcher itself
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
			    shooterSolenoid
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

	public ISolenoid getShooterSolenoid() {
		return shooterSolenoid;
	}

	public IPIDFeedbackDevice getLeftEncoder() {
		return leftEncoder;
	}

	public IPIDFeedbackDevice getRightEncoder() {
		return rightEncoder;
	}
	
	public DriveTrainAssist getDriveTrainAssist()
	{
		return driveAssist;
	}
	
	
	
}