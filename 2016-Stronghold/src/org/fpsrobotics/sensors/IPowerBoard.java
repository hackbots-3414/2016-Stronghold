package org.fpsrobotics.sensors;

public interface IPowerBoard 
{
	public double getVoltage();
	public double getCurrent(int channel);
	public double getPower(int channel);
	public double getTemperature();
}
