package org.fpsrobotics.actuators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.autonomous.DriveTrainAssist;
import org.fpsrobotics.sensors.BuiltInCANTalonEncoder;
import org.fpsrobotics.sensors.Gyroscope;
import org.fpsrobotics.sensors.ILimitSwitch;
import org.fpsrobotics.sensors.SensorConfig;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * Singleton class that creates and distributes all of the actuators (motors,
 * solenoids, linear actuators, etc.) to the rest of the program. Encoders are
 * included in this class as well because of the close integration with motors.
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

	private final int SHOOTER_SOLENOID_PORT = 0;

	// Shooter Solenoid
	private ISolenoid shooterSolenoid;

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
	private IPIDFeedbackDevice leftDriveEncoder;
	private IPIDFeedbackDevice rightDriveEncoder;

	// Auger encoder
	private IPIDFeedbackDevice augerPotentiometer;

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
		boolean isAlpha = true;

		// distinguish alpha and beta
		FileReader fileReader = null;
		try
		{
			fileReader = new FileReader("/AlphaOrBeta.txt"); // make sure file
																// exists at
																// this exact
																// path
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try
		{
			BufferedReader textReader = new BufferedReader(fileReader);

			if (textReader.readLine().equals("alpha"))
			{
				isAlpha = true;
			} else if (textReader.readLine().equals("beta"))
			{
				isAlpha = false;
			} else
			{
				System.err.println(
						"File is openable but doesn't specify alpha or beta on the first line, assuming alpha.");
			}

			textReader.close();

		} catch (Exception e)
		{
			System.err.println("Cannot determine if alpha or beta, assuming alpha");
			isAlpha = true;
		}

		try
		{
			// Instantiate CANTalons
			leftFrontMotor = new CANTalon(MOTOR_LEFT_FRONT);
			leftRearMotor = new CANTalon(MOTOR_LEFT_REAR);
			rightFrontMotor = new CANTalon(MOTOR_RIGHT_FRONT);
			rightRearMotor = new CANTalon(MOTOR_RIGHT_REAR);
		} catch (Exception e)
		{
			System.err.println("Drive Train CANTalons failed to initialize");
		}

		try
		{
			// Instantiate encoders
			leftDriveEncoder = new BuiltInCANTalonEncoder(leftFrontMotor);
			rightDriveEncoder = new BuiltInCANTalonEncoder(rightFrontMotor);
		} catch (Exception e)
		{
			System.err.println("Drive encoders failed to initialize");
		}

		// Instantiate CANMotors
		leftFrontCANMotor = new CANMotor(leftFrontMotor, true, leftDriveEncoder);
		leftRearCANMotor = new CANMotor(leftRearMotor, true);

		rightFrontCANMotor = new CANMotor(rightFrontMotor, false, rightDriveEncoder);
		rightRearCANMotor = new CANMotor(rightRearMotor, false);

		// Reverse the encoders because they are installed backward relative to
		// our setSpeed methods
		rightFrontMotor.reverseSensor(true);
		leftFrontMotor.reverseSensor(true);

		// Create the double motors
		leftDoubleMotor = new DoubleMotor(leftFrontCANMotor, leftRearCANMotor);
		rightDoubleMotor = new DoubleMotor(rightFrontCANMotor, rightRearCANMotor);

		// Create the whole drivetrain
		driveTrain = new TankDrive(leftDoubleMotor, rightDoubleMotor, SensorConfig.getInstance().getGyro());
		// driveTrain = new TankDrive(leftDoubleMotor, rightDoubleMotor);

		// Create drive assist
		driveAssist = new DriveTrainAssist(driveTrain, SensorConfig.getInstance().getGyro());

		// set it to use PID by default
		driveTrain.enablePID();
		driveTrain.setControlMode(TalonControlMode.Speed); // Speed means use
															// encoder rates
															// to control how
															// fast the robot is
															// moving

		try
		{
			// Instantiate shooter motors
			leftShooterMotor = new CANTalon(LEFT_SHOOTER_MOTOR);
			rightShooterMotor = new CANTalon(RIGHT_SHOOTER_MOTOR);

			// Instantiate shooter lifter
			linearActuator = new CANTalon(LINEAR_ACTUATOR_MOTOR);
		} catch (Exception e)
		{
			System.err.println("Shooter motor failed to initalize");
		}

		try
		{
			// Instantiate auger motors
			augerIntakeMotor = new CANTalon(AUGER_INTAKE_MOTOR);
			augerLifterMotor = new CANTalon(AUGER_LIFTER_MOTOR);
		} catch (Exception e)
		{
			System.err.println("Auger motors failed to initalize");
		}

		try
		{
			// Instantiate auger encoder
			augerPotentiometer = new BuiltInCANTalonEncoder(augerLifterMotor);
		} catch (Exception e)
		{
			System.err.println("Auger encoder failed to initialize");
		}

		try
		{
			// Instantiate solenoid
			shooterSolenoid = new SingleSolenoid(SHOOTER_SOLENOID_PORT);
		} catch (Exception e)
		{
			System.err.println("Shooter Solenoid failed to initialize");
		}

		augerPotentiometer.resetCount();

		// Instantiate the launcher itself
		launcher = new Launcher(
				new DoubleMotor(new CANMotor(leftShooterMotor, true), new CANMotor(rightShooterMotor, false)),
				new CANMotor(linearActuator, true), shooterSolenoid,
				SensorConfig.getInstance().getShooterBottomLimitSwitch(),
				SensorConfig.getInstance().getShooterTopLimitSwitch(), SensorConfig.getInstance().getShooterPot(),
				new CANMotor(augerIntakeMotor, false), SensorConfig.getInstance().getAugerBottomLimitSwitch(),
				SensorConfig.getInstance().getAugerTopLimitSwitch(), augerPotentiometer, isAlpha);
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

	public CANTalon getFrontLeftDriveMotor()
	{
		return leftFrontMotor;
	}

	public CANTalon getFrontRightDriveMotor()
	{
		return rightFrontMotor;
	}

	public ILauncher getLauncher()
	{
		return launcher;
	}

	public ISolenoid getShooterSolenoid()
	{
		return shooterSolenoid;
	}

	public IPIDFeedbackDevice getLeftDriveEncoder()
	{
		return leftDriveEncoder;
	}

	public IPIDFeedbackDevice getRightDriveEncoder()
	{
		return rightDriveEncoder;
	}

	public DriveTrainAssist getDriveTrainAssist()
	{
		return driveAssist;
	}

	public IPIDFeedbackDevice getAugerEncoder()
	{
		return augerPotentiometer;
	}

}