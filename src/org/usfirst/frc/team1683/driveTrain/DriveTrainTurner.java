package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Gyro;

/**
 * Turns robot a certain number of degrees
 */
public class DriveTrainTurner {
	private DriveTrain driveTrain;
	private double initialHeading;
	private Gyro gyro;
	private double speed;
	private double angle;
	private boolean done = false;
	private final double ANGLE_TOLERANCE = 4;

	/**
	 * Creates a DriveTrainTurner
	 * 
	 * @param driveTrain
	 *            The drive train
	 * @param angle
	 *            The angle, if above 180 or below -180, will be adjusted to be
	 *            in that range (will not do multiple revolutions); positive
	 *            indicates counter-clockwise
	 * @param speed
	 *            Speed between 0 and 1 normally
	 */
	public DriveTrainTurner(DriveTrain driveTrain, double angle, double speed) {
		// positive angle = counter clockwise, negative = clockwise
		this.driveTrain = driveTrain;
		gyro = driveTrain.getGyro();
		gyro.reset();
		initialHeading = gyro.getAngle();
		angle = normalizeAngle(angle);
		this.angle = angle;
		this.speed = speed;
		// If the angle is close to zero, no need to turn, we are already done
		done = Math.abs(angle) < ANGLE_TOLERANCE;
	}

	/**
	 * Takes an angle and returns the angle between -180 and 180 that is
	 * equivalent to it
	 * 
	 * @param angle
	 * @return An equivalent angle between -180 and 180
	 */
	public double normalizeAngle(double angle) {
		angle %= 360;
		if (angle < -180)
			angle += 360;
		if (angle > 180)
			angle -= 360;
		return angle;
	}

	/**
	 * Turns in place as long as the heading is less than the angle (within
	 * ANGLE_TOLERANCE)
	 */
	public void run() {
		double heading = angleDiff(gyro.getAngle(), initialHeading);
		if (!done && Math.abs(heading) <= Math.abs(angle) - ANGLE_TOLERANCE) {
			// If angle > 0, then it should turn counterclockwise so the "right"
			// parameter should be false
			driveTrain.turnInPlace(angle < 0, speed);
		} else {
			driveTrain.set(0);
			done = true;
		}
	}

	public double angleLeft() {
		return Math.abs(angle) - Math.abs(angleDiff(gyro.getAngle(), initialHeading));
	}

	/**
	 * 
	 * @return Whether it is done turning
	 */
	public boolean isDone() {
		return done;
	}

	private double angleDiff(double a, double b) {
		return normalizeAngle(a - b);
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
