package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.IGyroscope;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.PIDOverride;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a drive train that has two double motors on either side with encoders on each gearbox. It also has an
 * optional gyroscope attached in order to enable straight movement.
 *
 */
public class TankDrive implements IDriveTrain
{
	private DoubleMotor motorLeft, motorRight;

	private IGyroscope gyro;

	private boolean autoGyroDriveActivated;

	public TankDrive(DoubleMotor motorLeft, DoubleMotor motorRight)
	{
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}

	public TankDrive(DoubleMotor motorLeft, DoubleMotor motorRight, IGyroscope gyro)
	{
		this(motorLeft, motorRight);
		this.gyro = gyro;
	}

	@Override
	/**
	 * Sets left and right motors to leftSpeed and righSpeed Respectively
	 */
	public void setSpeed(double leftSpeed, double rightSpeed)
	{
		// TODO: Use Smart Dashboard to Tune Alpha
		// SmartDashboard.putNumber("Left Encoder", motorLeft.getPIDFeedbackDevice().getDistance());
		// SmartDashboard.putNumber("Right Encoder", motorRight.getPIDFeedbackDevice().getDistance());

		// Caution: May want to disable PID
		motorLeft.setSpeed(leftSpeed);
		motorRight.setSpeed(rightSpeed);
	}

	@Override
	/**
	 * Sets left and right motors to speed
	 */
	public void setSpeed(double speed)
	{

		// Caution: May want to disable PID
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	@Override
	public void stopDrive()
	{
		// Caution: May want to disable PID
		motorLeft.stop();
		motorRight.stop();
	}

	@Override
	/**
	 * Turns left at speed
	 */
	public void turnLeft(double speed)
	{
		// Caution: May want to disable PID
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(-speed);
	}

	@Override
	/**
	 * Turns right at speed
	 */
	public void turnRight(double speed)
	{
		// Caution: May want to disable PID
		motorLeft.setSpeed(-speed);
		motorRight.setSpeed(speed);
	}

	@Override
	/**
	 * Drives at Absolute Value of Speed
	 */
	public void goForward(double speed)
	{
		// Caution: May want to disable PID
		motorLeft.setSpeed(Math.abs(speed));
		motorRight.setSpeed(Math.abs(speed));
	}

	@Override
	/**
	 * Drives at Negative Absolute Value of Speed
	 */
	public void goBackward(double speed)
	{
		// Caution: May want to disable PID
		motorLeft.setSpeed(-Math.abs(speed));
		motorRight.setSpeed(-Math.abs(speed));
	}

	@Override
	/**
	 * Turns left 90 degrees (in while loop) then drives forward
	 */
	public void driveLeft(double speed)
	{
		turnLeft(speed, 90);
		goForward(speed);
	}

	@Override
	/**
	 * Turns right 90 degrees (in while loop) then drives forward
	 */
	public void driveRight(double speed)
	{
		turnRight(speed, 90);
		goForward(speed);
	}

	@Override
	/**
	 * CAUTION: Use Silverlight
	 */
	public void setP(double p)
	{
		motorLeft.setP(p);
		motorRight.setP(p);
	}

	@Override
	/**
	 * CAUTION: Use Silverlight
	 */
	public void setI(double i)
	{
		motorLeft.setI(i);
		motorRight.setI(i);
	}

	@Override
	/**
	 * CAUTION: Use Silverlight
	 */
	public void setD(double d)
	{
		motorLeft.setD(d);
		motorRight.setD(d);
	}

	@Override
	/**
	 * For both sides, motorLeft and motorRight
	 */
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device)
	{
		motorLeft.setPIDFeedbackDevice(device);
		motorRight.setPIDFeedbackDevice(device);
	}

	@Override
	/**
	 * Defaults to return motorLeft
	 */
	public IPIDFeedbackDevice getPIDFeedbackDevice()
	{
		return motorLeft.getPIDFeedbackDevice();
	}

	@Override
	public void enablePID()
	{
		motorLeft.enablePID();
		motorRight.enablePID();
	}

	@Override
	public void disablePID()
	{
		motorLeft.disablePID();
		motorRight.disablePID();
	}

	@Override
	/**
	 * Defaults to return motorLeft
	 */
	public TalonControlMode getControlMode()
	{
		return motorLeft.getControlMode();
	}

	@Override
	/**
	 * For both sides, motorLeft and motorRight
	 */
	public void setControlMode(TalonControlMode mode)
	{
		motorLeft.setControlMode(mode);
		motorRight.setControlMode(mode);
	}

	@Override
	/**
	 * Use in Autonomous Mode (Uses while loop)
	 */
	public void turnLeft(double speed, double degrees)
	{
		if (gyro != null)
		{
			double initialGyro = gyro.getHardCount();
			disablePID();

			if ((initialGyro - degrees) < gyro.getHardCount())
			{
				while (((initialGyro - degrees) < gyro.getHardCount()) && RobotStatus.isRunning())
				{
					setSpeed(speed, -speed);
				}
			}

			stopDrive();

			SensorConfig.getInstance().getTimer().waitTimeInMillis(200);
		}
		if (!PIDOverride.getInstance().isTeleopDisablePID())
		{
			enablePID();
		}
	}

	@Override
	/**
	 * Use in Autonomous Mode (Uses while loop)
	 */
	public void turnRight(double speed, double degrees)
	{
		if (gyro != null)
		{
			double initialGyro = gyro.getHardCount();
			disablePID();

			if ((initialGyro + degrees) > gyro.getHardCount())
			{
				while ((initialGyro + degrees) > gyro.getHardCount() && RobotStatus.isRunning())
				{
					setSpeed(-speed, speed);
				}
			}

			stopDrive();

			SensorConfig.getInstance().getTimer().waitTimeInMillis(200);
		}
		if (!PIDOverride.getInstance().isTeleopDisablePID())
		{
			enablePID();
		}
	}

	private final double KpAuton = 0.005; // used to be .01

	@Override
	/**
	 * Use in Autonomous Mode (Uses while loop)
	 * 
	 * @Param int distance: in inches
	 */
	public void goForward(double speed, double distance, boolean resetGyro)
	{
		// double initialCountRight = motorRight.getCANMotorOne().getPIDFeedbackDevice().getCount();
		// double initialCountLeft = motorLeft.getCANMotorOne().getPIDFeedbackDevice().getCount();
		double initialCountRight = motorRight.getCANMotorOne().getPIDFeedbackDevice().getDistance();
		double initialCountLeft = motorLeft.getCANMotorOne().getPIDFeedbackDevice().getDistance();

		if (gyro != null)
		{
			disablePID();

			if (resetGyro)
			{
				gyro.softResetCount();
			}
			
			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);

			if (RobotStatus.isAuto())
			{
				while (RobotStatus.isRunning() && RobotStatus.isAuto()
						&& ((motorLeft.getCANMotorOne().getPIDFeedbackDevice().getDistance()
								- initialCountLeft) >= -distance)
						&& ((motorRight.getCANMotorOne().getPIDFeedbackDevice().getDistance()
								- initialCountRight) >= -distance))
				{
					if (speed > 0)
					{
						drive(-speed, -gyro.getHardCount() * KpAuton);
					} else
					{
						drive(-speed, gyro.getHardCount() * KpAuton);
					}

					// drive(-speed, -gyro.getCount() * KpAuton);
				}
			} else
			{
				while (RobotStatus.isRunning()
						&& ((motorLeft.getCANMotorOne().getPIDFeedbackDevice().getDistance()
								- initialCountLeft) >= -distance)
						&& ((motorRight.getCANMotorOne().getPIDFeedbackDevice().getDistance()
								- initialCountRight) >= -distance))
				{
					if (speed > 0)
					{
						drive(-speed, -gyro.getHardCount() * KpAuton);
					} else
					{
						drive(-speed, gyro.getHardCount() * KpAuton);
					}

					// drive(-speed, -gyro.getCount() * KpAuton);
				}
			}

			stopDrive();

			if (!PIDOverride.getInstance().isTeleopDisablePID())
			{
				enablePID();
			}
		} else
		{
			try
			{
				throw new Exception("No Gyroscope: goStraight method unavailable");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void goForward(double speed, double distance)
	{
		goForward(speed, distance, true);
	}

	@Override
	/**
	 * Use in Autonomous Mode (Uses while loop)
	 */
	public void goBackward(double speed, double distance, boolean resetGyro)
	{
		goForward(-Math.abs(speed), distance, resetGyro);
	}

	@Override
	public void goBackward(double speed, double distance)
	{
		goBackward(speed, distance, true);
	}

	private final double kpTeleop = 0.00425;

	/**
	 * Resets the gyro
	 */
	@Override
	public void driveStraight(double speed)
	{
		if (gyro != null)
		{

			if (speed > 0)
			{
				drive(speed, gyro.getHardCount() * kpTeleop);
			} else
			{
				drive(speed, -gyro.getHardCount() * kpTeleop);
			}

		} else
		{
			setSpeed(speed);
		}
	}

	private final double m_sensitivity = 0.5;

	/**
	 * Use for PID Driving
	 * 
	 * @param outputMagnitude
	 * @param curve
	 */
	private void drive(double outputMagnitude, double curve)
	{
		double leftOutput, rightOutput;

		if (curve < 0)
		{
			double value = Math.log(-curve);
			double ratio = (value - m_sensitivity) / (value + m_sensitivity);
			if (ratio == 0)
			{
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude / ratio;
			rightOutput = outputMagnitude;
		} else if (curve > 0)
		{
			double value = Math.log(curve);
			double ratio = (value - m_sensitivity) / (value + m_sensitivity);
			if (ratio == 0)
			{
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude / ratio;
		} else
		{
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude;
		}

		setSpeed(leftOutput, rightOutput);
	}

}
