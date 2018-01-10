package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.robot.TechnoTitan;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CollisionDetector {
	static Accelerometer accel; 
	
	public CollisionDetector() {
		accel = new BuiltInAccelerometer();
	}
	
	public static void printAcceleration() {
		SmartDashboard.putNumber("X speed", accel.getX());
		SmartDashboard.putNumber("Y Speed", accel.getY());
	}
}
