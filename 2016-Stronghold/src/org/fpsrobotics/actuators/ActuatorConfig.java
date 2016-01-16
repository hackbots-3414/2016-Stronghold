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
	
	private IDriveTrain driveTrain;
	

	private ActuatorConfig()
	{
		CANTalon leftFrontMotor = new CANTalon(MOTOR_LEFT_FRONT);
		CANTalon leftRearMotor = new CANTalon(MOTOR_LEFT_REAR);
		CANTalon rightFrontMotor = new CANTalon(MOTOR_RIGHT_FRONT);
		CANTalon rightRearMotor = new CANTalon(MOTOR_RIGHT_REAR);
		
		driveTrain = new TankDrive(new DoubleMotor(new CANMotor(leftFrontMotor, false, new BuiltInCANTalonEncoder(leftFrontMotor)), new CANMotor(leftRearMotor, false, new BuiltInCANTalonEncoder(leftRearMotor))), new DoubleMotor(new CANMotor(rightFrontMotor, true, new BuiltInCANTalonEncoder(rightFrontMotor)), new CANMotor(rightRearMotor, true, new BuiltInCANTalonEncoder(rightRearMotor))));
		
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
}