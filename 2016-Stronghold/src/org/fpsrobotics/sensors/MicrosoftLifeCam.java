package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.CameraServer;

public class MicrosoftLifeCam implements ICamera
{
	 private CameraServer server;
	
	public MicrosoftLifeCam(String usbPort)
	{
		server = CameraServer.getInstance();
		server.setQuality(50);
	    server.startAutomaticCapture(usbPort);
	}

	@Override
	public void enable() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() 
	{
		// TODO Auto-generated method stub
		
	}

}
