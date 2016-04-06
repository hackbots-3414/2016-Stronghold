//package org.fpsrobotics.actuators;
//
//import org.fpsrobotics.PID.IPIDFeedbackDevice;
//import org.fpsrobotics.sensors.IGyroscope;
//import org.fpsrobotics.sensors.SensorConfig;
//import org.usfirst.frc.team3414.robot.RobotStatus;
//import org.fpsrobotics.actuators.*;
//
//import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//
///**
// * Creates a drive train that has two double motors on either side with encoders
// * on each gearbox. It also has an optional gyroscope attached in order for
// * straight movement.
// *
// *
// * NOT USED FOR 2016 SEASON
// */
//public class MechanumDrive implements IMechanumDrive
//{
//	private DoubleMotor motorLeft, motorRight;
//
//	private IGyroscope gyro;
//
//	public MechanumDrive(DoubleMotor motorLeft, DoubleMotor motorRight)
//	{
//		this.motorLeft = motorLeft;
//		this.motorRight = motorRight;
//	}
//
//	public MechanumDrive(DoubleMotor motorLeft, DoubleMotor motorRight, IGyroscope gyro)
//	{
//		this.motorLeft = motorLeft;
//		this.motorRight = motorRight;
//		this.gyro = gyro;
//	}
//
//	@Override
//	public void setSpeed(double leftSpeed, double rightSpeed)
//	{
//		motorLeft.setSpeed(leftSpeed);
//		motorRight.setSpeed(rightSpeed);
//	}
//
//	@Override
//	public void turnLeft(double speed)
//	{
//		motorLeft.setSpeed(speed);
//		motorRight.setSpeed(-speed);
//	}
//
//	@Override
//	public void turnRight(double speed)
//	{
//		motorLeft.setSpeed(-speed);
//		motorRight.setSpeed(speed);
//	}
//
//	@Override
//	public void goStraight(double speed)
//	{
//		motorLeft.setSpeed(speed);
//		motorRight.setSpeed(speed);
//	}
//
//	@Override
//	public void goBackward(double speed)
//	{
//		motorLeft.setSpeed(-speed);
//		motorRight.setSpeed(-speed);
//	}
//
//	@Override
//	public void driveLeft(double speed)
//	{
//		turnLeft(speed, 90);
//		goStraight(speed);
//	}
//
//	@Override
//	public void driveRight(double speed)
//	{
//		turnRight(speed, 90);
//		goStraight(speed);
//	}
//
//	@Override
//	public void turnLeft(double speed, double degrees)
//	{
//		gyro.resetCount();
//		disablePID();
//
//		if (-degrees < gyro.getCount())
//		{
//			while (-degrees < gyro.getCount() && RobotStatus.isRunning())
//			{
//				setSpeed(speed, -speed);
//			}
//		}
//
//		setSpeed(0, 0);
//
//		SensorConfig.getInstance().getTimer().waitTimeInMillis(200);
//
//		// enablePID();
//	}
//
//	@Override
//	public void turnRight(double speed, double degrees)
//	{
//		gyro.resetCount();
//		disablePID();
//
//		if (degrees > gyro.getCount())
//		{
//			while (degrees > gyro.getCount() && RobotStatus.isRunning())
//			{
//				setSpeed(-speed, speed);
//			}
//		}
//
//		setSpeed(0, 0);
//
//		SensorConfig.getInstance().getTimer().waitTimeInMillis(200);
//
//		// enablePID();
//	}
//
//	final double Kp = 0.01;
//
//	@Override
//	public void goStraight(double speed, int distance)
//	{
//		double initialCountRight = motorRight.getCANMotorOne().getPIDFeedbackDevice().getCount();
//		double initialCountLeft = motorLeft.getCANMotorOne().getPIDFeedbackDevice().getCount();
//
//		if (gyro != null)
//		{
//			disablePID();
//
//			gyro.resetCount();
//
//			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);
//
//			while (RobotStatus.isRunning()
//					&& ((motorLeft.getCANMotorOne().getPIDFeedbackDevice().getCount() - initialCountLeft) >= -distance)
//					&& ((motorRight.getCANMotorOne().getPIDFeedbackDevice().getCount()
//							- initialCountRight) >= -distance))
//			{
//				drive(-speed, -gyro.getCount() * Kp);
//			}
//
//			enablePID();
//
//			setSpeed(0, 0);
//
//			gyro.resetCount();
//
//		} else
//		{
//			try
//			{
//				throw new Exception("Josh made a PNG (Raul), no gyroscope");
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	@Override
//	public void goBackward(double speed, int distance)
//	{
//		goStraight(-speed, distance);
//	}
//
//	final double m_sensitivity = 0.5;
//
//	private void drive(double outputMagnitude, double curve)
//	{
//		double leftOutput, rightOutput;
//
//		if (curve < 0)
//		{
//			double value = Math.log(-curve);
//			double ratio = (value - m_sensitivity) / (value + m_sensitivity);
//			if (ratio == 0)
//			{
//				ratio = .0000000001;
//			}
//			leftOutput = outputMagnitude / ratio;
//			rightOutput = outputMagnitude;
//		} else if (curve > 0)
//		{
//			double value = Math.log(curve);
//			double ratio = (value - m_sensitivity) / (value + m_sensitivity);
//			if (ratio == 0)
//			{
//				ratio = .0000000001;
//			}
//			leftOutput = outputMagnitude;
//			rightOutput = outputMagnitude / ratio;
//		} else
//		{
//			leftOutput = outputMagnitude;
//			rightOutput = outputMagnitude;
//		}
//
//		setSpeed(leftOutput, rightOutput);
//	}
//
//}

package org.fpsrobotics.actuators;

import org.fpsrobotics.sensors.IAccelerometer;
import org.fpsrobotics.sensors.IGyroscope;

import edu.wpi.first.wpilibj.RobotDrive;

public class MechanumDrive
{
	private double currentVelocity, currentAngle, currentRotation;
	private RobotDrive drive;
	private IGyroscope gyro;
	private IAccelerometer accel;
	// private double devAngle;
	private static final double ROTATE_CONSTANT = 0.5;
	private double ROTATE_SECONDS_PER_DEGREE; // Rotate seconds per degree based on 0.3 power
	private double ROTATE_POWER_INTO_MOTORS = 0.3;

	double Kp = 1.0;

	public MechanumDrive(RobotDrive drive, IGyroscope gyro, IAccelerometer accel)
	{
		this.drive = drive;
		this.gyro = gyro;
		gyro.resetCount();
		this.accel = accel;
	}

	public void rotateDegreesGyroBased(double degrees, boolean clockWise)
	{
		double currentAngle = gyro.getCount();

		if (clockWise)
		{
			drive.mecanumDrive_Polar(0, 0, ROTATE_POWER_INTO_MOTORS);
			while (gyro.getCount() < (currentAngle + degrees))
				;
		} else
		{
			drive.mecanumDrive_Polar(0, 0, -ROTATE_POWER_INTO_MOTORS);
			while (gyro.getCount() > (currentAngle - degrees))
				;
		}
		stop();

	}

	public void rotateDegreesTimeBased(double degrees, boolean clockWise)
	{
		long timeToRotate = System.currentTimeMillis() + (long) (degrees * ROTATE_SECONDS_PER_DEGREE);
		if (clockWise)
		{
			movePolar(0.0, 0.0, ROTATE_POWER_INTO_MOTORS);
			while (System.currentTimeMillis() < timeToRotate)
				;
		} else
		{
			movePolar(0.0, 0.0, -ROTATE_POWER_INTO_MOTORS);
			while (System.currentTimeMillis() < timeToRotate)
				;
		}
		stop();
	}

	public void movePolar(double magnitude, double angle, double rotation)
	{
		this.currentVelocity = magnitude;
		this.currentAngle = angle;
		this.currentRotation = rotation;

		drive.mecanumDrive_Polar(magnitude, angle, rotation);

	}

	/**
	 * Relative to Face Forward
	 * 
	 * @param speed
	 * @param direction
	 */
	public void moveConstantVelocity(double speed, double direction)
	{
		movePolar(speed, direction, 0);
	}

	public void rotateConstantVelocity(boolean clockWise)
	{
		if (clockWise)
		{
			movePolar(0.0, 0.0, ROTATE_CONSTANT);
		} else
		{
			movePolar(0.0, 0.0, -ROTATE_CONSTANT);
		}

	}

	public void stop()
	{
		drive.mecanumDrive_Polar(0.0, 0, 0);
	}
	//
	// int UPPER_BOUND_ACCEL = 5;
	// int LOWER_BOUND_ANGLE = 2;
	//
	// double accelY;
	// double accelX;
	//
	// @Override
	// public void timeEvent(TimeEventArgs timeEvent)
	// {
	// if (timeEvent.getTimeEventID() == eventID)
	// {
	// if (gyro != null)
	// {
	// drive.mecanumDrive_Polar(currentVelocity, currentAngle -
	// gyro.getChangeInDegreesPerSecond() * Kp,
	// currentRotation);
	// }
	//
	// if (accel != null)
	// {
	// accelY = accel.getAccelY();
	// accelX = accel.getAccelZ();
	//
	// if ((accelY < UPPER_BOUND_ACCEL && accelY > -UPPER_BOUND_ACCEL)
	// && (accelX < UPPER_BOUND_ACCEL && accelX > -UPPER_BOUND_ACCEL))
	// {
	// devAngle = (Math.toDegrees(Math.atan(accelY / accelX)));
	// if (devAngle > LOWER_BOUND_ANGLE)
	// {
	// drive.mecanumDrive_Polar(currentVelocity, currentAngle - devAngle,
	// currentRotation);
	// }
	// if (devAngle < -LOWER_BOUND_ANGLE)
	// {
	// drive.mecanumDrive_Polar(currentVelocity, currentAngle - devAngle,
	// currentRotation);
	// }
	// }
	// }
	// }
	//
	// }
}
