package org.fpsrobotics.commands;

public class Main
{

	private ThreadStarter starter1;
	private ThreadStarter starter2;
	private ThreadStarter starter3;
	
	public void main()
	{
		starter1 = new ThreadStarter(SequencingConfig.getInstance().getExample());
		starter2 = new ThreadStarter(SequencingConfig.getInstance().getExample());
		starter3 = new ThreadStarter(SequencingConfig.getInstance().getExample());
	}
	
}
