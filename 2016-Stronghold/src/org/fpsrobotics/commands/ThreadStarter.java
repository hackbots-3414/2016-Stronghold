package org.fpsrobotics.commands;

public class ThreadStarter implements Runnable
{
	private ISequence seq;

	public ThreadStarter(ISequence _seq)
	{
		seq = _seq;
	}

	public void run()
	{
		seq.doSomething();
		seq.stopSomething();
	}
}
