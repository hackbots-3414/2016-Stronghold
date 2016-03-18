//package org.fpsrobotics.autonomous;
//
//import org.fpsrobotics.sensors.SensorConfig;
//
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//
//public class MullenatorAutonomous
//{
//	private IAutoSwitches autoSwitches;
//
//	private static MullenatorAutonomous singleton;
//
//	private MullenatorAutonomous()
//	{
//		autoSwitches = new AutonomousSwitches(SensorConfig.getInstance().getAutoSwitchOnes(),
//				SensorConfig.getInstance().getAutoSwitchTwos(), SensorConfig.getInstance().getAutoSwitchFours());
//	}
//
//	public synchronized static MullenatorAutonomous getInstance()
//	{
//		if (singleton != null)
//		{
//			singleton = new MullenatorAutonomous();
//		}
//		return singleton;
//	}
//
//	public void doAuto()
//	{
//		if (SmartDashboard.getNumber("Auto Mode Input", 0) == 0)
//		{
//			switch (autoSwitches.getAutoState())
//			{
//			case ZERO:
//				SmartDashboard.putString("Auto Mode Set", "Do Nothing");
//				doThis(new AutonDoNothing());
//				break;
//			case ONE:
//				SmartDashboard.putString("Auto Mode Set", "Drive Straight");
//				doThis(new AutonDriveStraight());
//				break;
//			case TWO:
//				SmartDashboard.putString("Auto Mode Set", "Low Bar");
//				doThis(new AutonLowBar());
//				break;
//			case THREE:
//				SmartDashboard.putString("Auto Mode Set", "Rock Wall");
//				doThis(new AutonRockWall());
//				break;
//			case FOUR:
//				SmartDashboard.putString("Auto Mode Set", "Rough Terrain");
//				doThis(new AutonRoughTerrain());
//				break;
//			default:
//				SmartDashboard.putString("Auto Mode Set", "Do Nothing");
//				doThis(new AutonDoNothing());
//			}
//		} else
//		{
//			switch ((int) SmartDashboard.getNumber("Auto Mode Input", 0))
//			{
//			case 0:
//				SmartDashboard.putString("Auto Mode Set", "Do Nothing");
//				doThis(new AutonDoNothing());
//				break;
//			case 1:
//				SmartDashboard.putString("Auto Mode Set", "Drive Straight");
//				doThis(new AutonDriveStraight());
//				break;
//			case 2:
//				SmartDashboard.putString("Auto Mode Set", "Low Bar");
//				doThis(new AutonLowBar());
//				break;
//			case 3:
//				SmartDashboard.putString("Auto Mode Set", "Rock Wall");
//				doThis(new AutonRockWall());
//				break;
//			case 4:
//				SmartDashboard.putString("Auto Mode Set", "Rough Terrain");
//				doThis(new AutonRoughTerrain());
//				break;
//			default:
//				SmartDashboard.putString("Auto Mode Set", "Do Nothing");
//				doThis(new AutonDoNothing());
//			}
//		}
//
//	}
//
//	private void doThis(IAutonomousControl auto)
//	{
//		auto.doAuto();
//	}
//
//}
