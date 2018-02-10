package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.sensors.Encoder;
import org.usfirst.frc.team1683.sensors.Gyro;

/*
 * Controls tank drive
 */
public class TankDrive implements DriveTrain {

	private TalonSRX left;
	private TalonSRX right;
	private Gyro gyro;

	public TankDrive(TalonSRX leftETalonSRX, TalonSRX rightETalonSRX) {
		this.left = leftETalonSRX;
		this.right = rightETalonSRX;
	}

	/**
	 * Creates a tank drive
	 * @param leftETalonSRX The main talon on the left (with encoder)
	 * @param rightETalonSRX The main talon on the right (with encoder)
	 * @param gyro
	 */
	public TankDrive(TalonSRX leftETalonSRX, TalonSRX rightETalonSRX, Gyro gyro) {
		this.left = leftETalonSRX;
		this.right = rightETalonSRX;
		this.gyro = gyro;
		// this.gyro.reset();
	}

	@Override
	public void set(double speed) {
		left.set(speed);
		right.set(speed);
	}

	public void setLeft(double speed) {
		left.set(speed);
	}

	public void setRight(double speed) {
		right.set(speed);
	}

	public void set(double leftSpeed, double rightSpeed) {
		left.set(leftSpeed);
		right.set(rightSpeed);
	}

	public void turnInPlace(boolean right, double speed) {
		if (right) {
			set(speed, -speed);
		} else {
			set(-speed, speed);
		}
	}

	@Override
	public double[] getSpeed() {
		return new double[] { left.getSpeed(), right.getSpeed() };
	}

	@Override
	public void stop() {
		left.brake();
		right.brake();
	}

	@Override
	public void coast() {
		left.coast();
		right.coast();
	}

	@Override
	public void driveMode(double leftSpeed, double rightSpeed) {
		left.set(leftSpeed);
		right.set(rightSpeed);
	}

	@Override
	public Encoder getLeftEncoder() {
		return left.getEncoder();
	}

	@Override
	public Encoder getRightEncoder() {
		return right.getEncoder();
	}

	@Override
	public void resetEncoders() {
		left.getEncoder().reset();
		right.getEncoder().reset();
	}

	@Override
	public TalonSRX getLeft() {
		return left;
	}

	@Override
	public TalonSRX getRight() {
		return right;
	}

	@Override
	public void enableBrownoutProtection() {
		left.enableBrownoutProtection();
		right.enableBrownoutProtection();
	}

	@Override
	public void disableBrownoutProtection() {
		left.disableBrownoutProtection();
		right.disableBrownoutProtection();
	}

	// public void enableAntiDrift() {
	/// left.enableAntiDrift(antiDrift);
	// right.enableAntiDrift(antiDrift);
	// }

	// public void disableAntiDrift() {
	// left.disableAntiDrift();
	// right.disableAntiDrift();
	// }

	// public boolean isAntiDriftEnabled() {
	// return left.isAntiDriftEnabled() && right.isAntiDriftEnabled();
	// }

	public Gyro getGyro() {
		return gyro;
	}

	public boolean hasGyro() {
		return !(gyro == null);
	}
}
