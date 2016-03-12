package org.fpsrobotics.actuators;

import org.fpsrobotics.PID.IPIDFeedbackDevice;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

/**
 * Controls motors connected to Talon SRX Motor Controllers that interface with
 * the CAN Bus on the RoboRIO.
 */
public class CANMotor implements ICANMotor
{
	private boolean invertDirection;
	private CANTalon canMotor;
	private IPIDFeedbackDevice device;

	public CANMotor(CANTalon motor, boolean invertDirection)
	{
		// Changes the motor controller to accept percentages expressed in
		// decimals of the voltage of the system.
		motor.changeControlMode(TalonControlMode.PercentVbus);
		// motor.setInverted(invertDirection);
		this.canMotor = motor;
		this.invertDirection = invertDirection;
	}

	/**
	 * CANMotor Constructor which uses PID
	 */
	public CANMotor(CANTalon motor, boolean invertDirection, IPIDFeedbackDevice device)
	{
		this(motor, invertDirection);
		this.device = device;

		canMotor.setFeedbackDevice(device.whatPIDDevice());
	}

	@Override
	public void setP(double p)
	{
		canMotor.setP(p);
	}

	@Override
	public void setI(double i)
	{
		canMotor.setI(i);
	}

	@Override
	public void setD(double d)
	{
		canMotor.setD(d);
	}

	@Override
	public void setPIDFeedbackDevice(IPIDFeedbackDevice device)
	{
		this.device = device;
	}

	@Override
	public IPIDFeedbackDevice getPIDFeedbackDevice()
	{
		return device;
	}

	@Override
	public void enablePID()
	{
//		canMotor.setFeedbackDevice(device.whatPIDDevice());	//I had this in Josh's Version
		canMotor.reset();
		canMotor.enable();
		// TODO: implement later?
	}

	@Override
	public void disablePID()
	{
		canMotor.changeControlMode(TalonControlMode.PercentVbus);
	}

	@Override
	public TalonControlMode getControlMode()
	{
		return canMotor.getControlMode();
	}

	@Override
	public void setControlMode(TalonControlMode mode)
	{
		canMotor.changeControlMode(mode);
	}

	@Override
	/**
	 * In PercentVbus and Follower modes: returns current applied throttle.
	 */
	public double getSpeed()
	{
		return canMotor.get();
	}

	@Override
	public void setSpeed(double speed)
	{
		if (device != null)
		{
			if (invertDirection)
			{
				canMotor.setSetpoint(-speed);
			} else
			{
				canMotor.setSetpoint(speed);
			}
		} else
		{
			if (invertDirection)
			{
				canMotor.set(-speed);
			} else
			{
				canMotor.set(speed);
			}
		}
	}

	@Override
	public void stop()
	{
		if (device != null)
		{
			canMotor.setSetpoint(0.0);
		} else
		{
			canMotor.set(0.0);
		}
	}

	protected void setMotorControlMode(CANTalon.TalonControlMode mode)
	{
		canMotor.changeControlMode(mode);
	}

	protected int getDeviceID()
	{
		return canMotor.getDeviceID();
	}

	protected void setFollowerDeviceID(int deviceID)
	{
		canMotor.set(deviceID);
	}

}
