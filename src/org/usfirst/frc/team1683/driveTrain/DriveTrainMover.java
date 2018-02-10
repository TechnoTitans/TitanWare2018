package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.motor.MotorMover;
import org.usfirst.frc.team1683.motor.TalonSRX;

/**
 * Mover for robot to move for a set distance in a line
 */
public class DriveTrainMover {
	private MotorMover leftMover, rightMover;
	private LinearEasing easing;

	public DriveTrainMover(DriveTrain driveTrain, double distance, double speed) {
		this(driveTrain, distance, distance, speed, speed);
	}

	public DriveTrainMover(DriveTrain driveTrain, double leftDistance, double rightDistance, double leftSpeed, double rightSpeed) {
		TalonSRX left = driveTrain.getLeft(),
				right = driveTrain.getRight();
		leftMover = new MotorMover(left, leftDistance, leftSpeed, left.getEncoder(), left.getAnti());
		rightMover = new MotorMover(right, rightDistance, rightSpeed, right.getEncoder(), right.getAnti());
	}

	public void setEasing(LinearEasing easing) {
		this.easing = easing;
		leftMover.setEasing(easing);
		rightMover.setEasing(easing);
	}
	
	public LinearEasing getEasing() {
		return easing;
	}

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