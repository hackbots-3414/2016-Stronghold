package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;
import org.fpsrobotics.sensors.Gyroscope;
import org.fpsrobotics.sensors.IGyroscope;
import org.fpsrobotics.sensors.SensorConfig;
import org.usfirst.frc.team3414.robot.RobotStatus;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a drive train that has two double motors on either side with encoders on each gearbox. 
 * It also has an optional gyroscope attached in order for straight movement.
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
		motorLeft.setSpeed(leftSpeed);
		motorRight.setSpeed(rightSpeed);
	}

	@Override
	public void turnLeft(double speed)
	{
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(-speed);
	}

	@Override
	public void turnRight(double speed)
	{
		motorLeft.setSpeed(-speed);
		motorRight.setSpeed(speed);
	}

	@Override
	public void goStraight(double speed)
	{
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	@Override
	public void goBackward(double speed)
	{
		motorLeft.setSpeed(-speed);
		motorRight.setSpeed(-speed);
	}

	@Override
	public void driveLeft(double speed)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void driveRight(double speed)
	{
		// TODO Auto-generated method stub

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
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device)
	{
		motorLeft.setPIDFeedbackDevice(device);
		motorRight.setPIDFeedbackDevice(device);
	}

	@Override
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
	public TalonControlMode getControlMode()
	{
		return motorLeft.getControlMode();
	}

	@Override
	public void setControlMode(TalonControlMode mode)
	{
		motorLeft.setControlMode(mode);
		motorRight.setControlMode(mode);
	}

	@Override
	public void turnLeft(double speed, double degrees)
	{
		gyro.resetCount();
		disablePID();
		
		if(-degrees < gyro.getCount())
		{
			while(-degrees < gyro.getCount())
			{
				setSpeed(speed, -speed);
			}
		}
		
		setSpeed(0, 0);
		enablePID();
	}

	@Override
	public void turnRight(double speed, double degrees)
	{
		gyro.resetCount();
		disablePID();
		
		if(degrees > gyro.getCount())
		{
			while(degrees > gyro.getCount())
			{
				setSpeed(-speed, speed);
			}
		}
		
		setSpeed(0, 0);
		enablePID();
	}

	final double Kp = 0.03;

	@Override
	public void goStraight(double speed, int distance)
	{
		double initialCountRight = motorRight.getCANMotorOne().getPIDFeedbackDevice().getCount();
		double initialCountLeft = motorLeft.getCANMotorOne().getPIDFeedbackDevice().getCount();
		
		if (gyro != null)
		{
			disablePID();
			
			gyro.resetCount();
			
			while(RobotStatus.isRunning() && 
			((motorLeft.getCANMotorOne().getPIDFeedbackDevice().getCount()-initialCountLeft) >= -distance)
		    && ((motorRight.getCANMotorOne().getPIDFeedbackDevice().getCount()-initialCountRight) >= -distance))
			{
				drive(-speed, -gyro.getCount() * Kp);
			}
			
			enablePID();
			
			setSpeed(0,0);
			
		} else
		{
			try
			{
				throw new Exception("Josh made a PNG (Raul), no gyroscope");
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void goBackward(double speed, int distance)
	{
		goStraight(-speed, distance);
	}

	final double m_sensitivity = 0.5;

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
