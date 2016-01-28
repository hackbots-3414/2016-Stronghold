package org.fpsrobotics.commands;

public class SequencingConfig
{
	private static SequencingConfig singleton = null;
	
	private Example example;
	
	private SequencingConfig()
	{
		example = new Example();
	}
	
	public static synchronized SequencingConfig getInstance()
	{
		if (singleton == null)
		{
			singleton = new SequencingConfig();
		}

		return singleton;
	}
	
	public ISequence getExample()
	{
		return example;
	}
	
}
