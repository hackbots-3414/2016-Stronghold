package org.fpsrobotics.sensors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DashboardLogger implements ILogger
{
	private static DashboardLogger singleton = null;
	private PrintWriter writer;
	private HashMap<String, Boolean> boolMap = new HashMap<String, Boolean>();
	private HashMap<String, Double> numberMap = new HashMap<String, Double>();
	private HashMap<String, String> stringMap = new HashMap<String, String>();
	private Set set;
	private Iterator iterator;
	private Map.Entry mentry;

	private DashboardLogger()
	{

		try
		{
			// writer = new PrintWriter("dashboardLogger_" +
			// getLoggerTimeStamp() + "_.txt", "UTF-8");
			writer = new PrintWriter("dashboardLogger.txt", "UTF-8");
		} catch (FileNotFoundException e)
		{
			System.out.println("Write File Not Found");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			System.out.println("UTF-8 Not Found");
			e.printStackTrace();
		}
	}

	public static synchronized DashboardLogger getInstance()
	{
		if (singleton == null)
		{
			singleton = new DashboardLogger();
		}

		return singleton;
	}

	@Override
	public void reportInformation(EOutputDevice device)
	{

		// Booleans
		set = boolMap.entrySet();
		iterator = set.iterator();

		while (iterator.hasNext())
		{
			mentry = (Map.Entry) iterator.next();

			if (device == EOutputDevice.SMARTDASHBOARD)
			{
				SmartDashboard.putBoolean(mentry.getKey(), mentry.getValue());

			} else if (device == EOutputDevice.FILE)
			{
				writer.println(mentry.getKey(), mentry.getValue());

			}

		}

		// Number
		set = numberMap.entrySet();
		iterator = set.iterator();

		while (iterator.hasNext())
		{
			mentry = (Map.Entry) iterator.next();

			if (device == EOutputDevice.SMARTDASHBOARD)
			{
				SmartDashboard.putBoolean(mentry.getKey(), mentry.getValue());

			} else if (device == EOutputDevice.FILE)
			{
				writer.println(mentry.getKey(), mentry.getValue());

			}

		}

		// String
		set = stringMap.entrySet();
		iterator = set.iterator();

		while (iterator.hasNext())
		{
			mentry = (Map.Entry) iterator.next();

			if (device == EOutputDevice.SMARTDASHBOARD)
			{
				SmartDashboard.putBoolean(mentry.getKey(), mentry.getValue());

			} else if (device == EOutputDevice.FILE)
			{
				writer.println(mentry.getKey(), mentry.getValue());

			}

		}

	}

	private String getLoggerTimeStamp()
	{
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return "_" + minute + "_:_" + hour + "_:_" + month + "_:_" + day + "_:_" + year + "_";
	}

	public void putBoolean(String key, boolean value)
	{
		boolMap.put(key, value);
	}
	
	public void putNumber(String key, double value)
	{
		numberMap.put(key, value);
	}
	
	public void putNumber(String key, int value)
	{
		numberMap.put(key, (double)value);
	}
	
	public void putNumber(String key, long value)
	{
		numberMap.put(key, (double)value);
	}
	
	public void putNumber(String key, float value)
	{
		numberMap.put(key, (double)value);
	}
	
	public void putString(String key, String value)
	{
		stringMap.put(key, value);
	}

}
