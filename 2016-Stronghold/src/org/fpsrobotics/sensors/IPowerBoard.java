package org.fpsrobotics.sensors;

public interface IPowerBoard 
{
	double getVoltage();
	double getCurrent(int channel);
	double getPower(int channel);
	double getTemperature();
}
