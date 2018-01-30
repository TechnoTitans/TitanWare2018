package org.usfirst.frc.team1683.driveTrain;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1683.motor.Motor;
import org.usfirst.frc.team1683.motor.MotorMover;
import org.usfirst.frc.team1683.motor.TalonSRX;

/**
 * Mover for robot to move for a set distance in a line
 */
public class DriveTrainMover {
	private MotorMover leftMover, rightMover;

	public DriveTrainMover(DriveTrain driveTrain, double distance, double speed) {
		TalonSRX left = driveTrain.getLeft(),
				right = driveTrain.getRight();
		leftMover = new MotorMover(left, distance, speed, left.getEncoder());
	}

	public DriveTrainMover(DriveTrain driveTrain, double leftDistance, double rightDistance, double leftSpeed, double rightSpeed) {
		TalonSRX left = driveTrain.getLeft(),
				right = driveTrain.getRight();
		leftMover = new MotorMover(left, leftDistance, leftSpeed, left.getEncoder());
	}

//	private void addMotorGroup(MotorGroup group) {
//		for (Motor m : group) {
//			motorMovers.add(new MotorMover(m, distance, speed, group.getEncoder(), group.getAntiDrift()));
//		}
//	}
	
//	private void addMotorGroup(MotorGroup group, double moveDistance, double moveSpeed) {
//		for (Motor m : group) {
//			motorMovers.add(new MotorMover(m, moveDistance, moveSpeed, group.getEncoder()));
//		}
//	}

	/**
	 * Runs an iteration of all the motor movers
	 */
	public void runIteration() {
		leftMover.runIteration();
		rightMover.runIteration();
	}

	/**
	 * Tests if all motor movers have distance more than zero Equivalent to
	 * getAverageDistanceLeft() == 0
	 * 
	 * @return True if all motor movers are finished, false otherwise
	 */
	public boolean areAllFinished() {
		return leftMover.distanceLeft() <= 0 && rightMover.distanceLeft() <= 0;
	}

	/**
	 * @return True if any (even one) motors are finished, false otherwise
	 */
	public boolean areAnyFinished() {
		return leftMover.distanceLeft() <= 0 || rightMover.distanceLeft() <= 0;
	}

	/**
	 * Note: motor mover distance left is capped at 0, so if it has finished (is
	 * negative), it doesn't cancel out something that has not finished
	 * 
	 * @return The average distance that all motor movers have traveled
	 */
	public double getAverageDistanceLeft() {
		double leftDist = Math.max(0, leftMover.distanceLeft());
		double rightDist = Math.max(0, rightMover.distanceLeft());
		return (leftDist + rightDist) / 2.0;
	}
}