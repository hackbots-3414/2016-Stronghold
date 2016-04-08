package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.IGyroscope;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.PIDOverride;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

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
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
		this.gyro = gyro;
	}

	@Override
	/**
	 * Sets left and right motors to leftSpeed and righSpeed Respectively
	 */
	public void setSpeed(double leftSpeed, double rightSpeed)
	{
		// Caution: May want to disable PID
		autoGyroDriveActivated = false;
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
		autoGyroDriveActivated = false;
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	@Override
	public void stopDrive()
	{
		// Caution: May want to disable PID
		autoGyroDriveActivated = false;
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
		autoGyroDriveActivated = false;
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
		autoGyroDriveActivated = false;
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
		autoGyroDriveActivated = false;
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
		autoGyroDriveActivated = false;
		motorLeft.setSpeed(-Math.abs(speed));
		motorRight.setSpeed(-Math.abs(speed));
	}

	@Override
	/**
	 * Turns left 90 degrees (in while loop) then drives forward
	 */
	public void driveLeft(double speed)
	{
		autoGyroDriveActivated = false;
		turnLeft(speed, 90);
		goForward(speed);
	}

	@Override
	/**
	 * Turns right 90 degrees (in while loop) then drives forward
	 */
	public void driveRight(double speed)
	{
		autoGyroDriveActivated = false;
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
			double initialGyro = gyro.getCount();
			disablePID();

			if ((initialGyro - degrees) < gyro.getCount())
			{
				while (((initialGyro - degrees) < gyro.getCount()) && RobotStatus.isRunning())
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
			double initialGyro = gyro.getCount();
			disablePID();

			if ((initialGyro + degrees) > gyro.getCount())
			{
				while ((initialGyro + degrees) > gyro.getCount() && RobotStatus.isRunning())
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

	private final double Kp = 0.005; // used to be .01

	@Override
	/**
	 * Use in Autonomous Mode (Uses while loop)
	 */
	public void goForward(double speed, int distance)
	{
		double initialCountRight = motorRight.getCANMotorOne().getPIDFeedbackDevice().getCount();
		double initialCountLeft = motorLeft.getCANMotorOne().getPIDFeedbackDevice().getCount();

		if (gyro != null)
		{
			disablePID();

			gyro.resetCount();

			SensorConfig.getInstance().getTimer().waitTimeInMillis(300);

			while (RobotStatus.isRunning()
					&& ((motorLeft.getCANMotorOne().getPIDFeedbackDevice().getCount() - initialCountLeft) >= -distance)
					&& ((motorRight.getCANMotorOne().getPIDFeedbackDevice().getCount()
							- initialCountRight) >= -distance))
			{
				drive(-speed, -gyro.getCount() * Kp);
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
	/**
	 * Use in Autonomous Mode (Uses while loop)
	 */
	public void goBackward(double speed, int distance)
	{
		goForward(-speed, distance);
	}

	/**
	 * Resets the gyro
	 */
	@Override
	public void driveStraight(double speed)
	{
		if (gyro != null)
		{
			if (!autoGyroDriveActivated)
			{
				disablePID();

				gyro.resetCount();
				System.out.println("Gyro Reset");

//				 SensorConfig.getInstance().getTimer().waitTimeInMillis(300);
				// TODO: Should driveStraight() wait 300 ms like driveForward() does?

				autoGyroDriveActivated = true;
			}
			drive(speed, -gyro.getCount() * Kp);
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
