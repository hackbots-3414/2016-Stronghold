package org.fpsrobotics.sensors;

public class PowerDistributionPanel implements IPowerBoard
{
	edu.wpi.first.wpilibj.PowerDistributionPanel pdp;
	
	public PowerDistributionPanel()
	{
		pdp = new edu.wpi.first.wpilibj.PowerDistributionPanel();
	}

	@Override
	public double getVoltage() 
	{
		return pdp.getVoltage();
	}

	@Override
	public double getCurrent(int channel) 
	{
		return pdp.getCurrent(channel);
	}

	@Override
	public double getPower(int channel) 
	{
		return pdp.getCurrent(channel) * pdp.getVoltage();
	}

	@Override
	public double getTemperature() 
	{
		return pdp.getTemperature();
	}

}
