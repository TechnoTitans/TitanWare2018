package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.sensors.Gyro;

/*
 * Keeps robot moving in a line.
 * 
 * Realigns robot based on gyro
 */
public class AntiDrift {

	private double antidriftangle = 0;
	private final double KP = 0.016;
	private Gyro gyro;
	// 1 if right, -1 if left, 0 if no correction should be applied
	private int right;

	public AntiDrift(Gyro gyro, int right) {
		this.gyro = gyro;
		this.right = right;
	}

	/**
	 * 
	 * @param speed
	 *            The current speed of the motor
	 * @return The new speed of the motor that should be set to make the angle
	 *         of the gyro closer to zero
	 */
	public double antiDrift(double speed) {
		double error = antidriftangle - gyro.getAngle();

		double correction = KP * error / 2.0;
		return limitSpeed(speed - correction * right);
	}

	public void reset() {
		gyro.reset();
		antidriftangle = gyro.getAngle();
	}

	private static double limitSpeed(double speed) {
		if (speed > 1.0) {
			return 1.0;
		} else if (speed < -1.0) {
			return -1.0;
		} else {
			return speed;
		}
	}
}
