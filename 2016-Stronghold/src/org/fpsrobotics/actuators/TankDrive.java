package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.IGyroscope;
import org.fpsrobotics.sensors.SensorConfig;
import org.fpsrobotics.teleop.PIDOverride;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a drive train that has two double motors on either side with encoders
 * on each gearbox. It also has an optional gyroscope attached in order to
 * enable straight movement.
 *
 */
public class TankDrive implements IDriveTrain
{
	private DoubleMotor motorLeft, motorRight;

	private IGyroscope gyro;

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
	public void setSpeed(double leftSpeed, double rightSpeed)
	{
		//Caution: May want to disable PID
		motorLeft.setSpeed(leftSpeed);
		motorRight.setSpeed(rightSpeed);
	}

	@Override
	public void setSpeed(double speed)
	{
		//Caution: May want to disable PID
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	@Override
	public void stop()
	{
		//Caution: May want to disable PID
		motorLeft.stop();
		motorRight.stop();
	}

	@Override
	public void turnLeft(double speed)
	{
		//Caution: May want to disable PID
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(-speed);
	}

	@Override
	public void turnRight(double speed)
	{
		//Caution: May want to disable PID
		motorLeft.setSpeed(-speed);
		motorRight.setSpeed(speed);
	}

	@Override
	public void goForward(double speed)
	{
		//Caution: May want to disable PID
		motorLeft.setSpeed(Math.abs(speed));
		motorRight.setSpeed(Math.abs(speed));
	}

	@Override
	public void goBackward(double speed)
	{
		//Caution: May want to disable PID
		motorLeft.setSpeed(-Math.abs(speed));
		motorRight.setSpeed(-Math.abs(speed));
	}

	@Override
	public void driveLeft(double speed)
	{
		turnLeft(speed, 90);
		goForward(speed);
	}

	@Override
	public void driveRight(double speed)
	{
		turnRight(speed, 90);
		goForward(speed);
	}

	@Override
	public void setP(double p)
	{
		motorLeft.setP(p);
		motorRight.setP(p);
	}

	@Override
	public void setI(double i)
	{
		motorLeft.setI(i);
		motorRight.setI(i);
	}

	@Override
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

			stop();

			SensorConfig.getInstance().getTimer().waitTimeInMillis(200);
		}
		if (!PIDOverride.getInstance().isTeleopDisablePID())
		{
			enablePID();
		}
	}

	@Override
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

			stop();

			SensorConfig.getInstance().getTimer().waitTimeInMillis(200);
		}
		if (!PIDOverride.getInstance().isTeleopDisablePID())
		{
			enablePID();
		}
	}

	private final double Kp = 0.005; // used to be .01

	@Override
	public void goStraight(double speed, int distance)
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

			stop();

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
	public void goBackward(double speed, int distance)
	{
		goStraight(-speed, distance);
	}

	private final double m_sensitivity = 0.5;

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
