package org.usfirst.frc.team1683.sensors;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CollisionDetector {
	static Accelerometer accel; 
	static double previousAccel;
	static double currentAccel;
	public CollisionDetector() {
		accel = new BuiltInAccelerometer();
		
	}
	
	public static void printAcceleration() {
		SmartDashboard.putNumber("Z speed", accel.getZ());
	}
}
