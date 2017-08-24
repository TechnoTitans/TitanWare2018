package org.usfirst.frc.team1683.motor;

import java.util.ArrayList;

import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.sensors.Encoder;

/*
 * Creates a group of motors (for left and right side)
 */
public class MotorGroup extends ArrayList<Motor> {

	private static final long serialVersionUID = 1L;
	private Encoder encoder;

	private AntiDrift antiDrift;

	/**
	 * Basically a list of motors.
	 *
	 * @param encoder
	 *            The encoder attached to this MotorGroup; will automatically
	 *            attach this encoder to any TalonSRX motor in the motor group
	 * @param motors
	 *            The motors.
	 */
	public MotorGroup(Encoder encoder, Motor... motors) {
		this.encoder = encoder;
		for (Motor motor : motors) {
			if (motor instanceof TalonSRX) {
				((TalonSRX) motor).setEncoder(encoder);
			}
			super.add(motor);
		}
	}

	public ArrayList<Motor> getMotor() {
		ArrayList<Motor> motors = new ArrayList<Motor>();
		for (Motor motor : this) {
			motors.add(motor);
		}
		return motors;
	}

	/**
	 * Constructor -- just a list of motors
	 *
	 * @param motors
	 *            The motors.
	 */
	public MotorGroup(Motor... motors) {
		for (Motor motor : motors) {
			super.add(motor);
		}
	}

	/**
	 * Set collective speed of motors.
	 *
	 * @param speed
	 *            Speed from 0 to 1.
	 */
	public void set(double speed) {
		for (Motor motor : this) {
			((TalonSRX) motor).set(speed);
		}
	}

	/**
	 * Gets collective (average) speed of motors
	 */
	public double getSpeed() {
		double speed = 0;
		for (Motor motor : this) {
			speed += motor.get();
		}
		return speed / this.size();
	}

	/**
	 * Stops group.
	 */
	public void stop() {
		for (Motor motor : this) {
			motor.stop();
		}
	}

	/**
	 * @return If there is an encoder associated with the group.
	 */
	public boolean hasEncoder() {
		return !(encoder == null);
	}

	/**
	 * 
	 * @return The average error of all the motors
	 */
	public double getError() {
		double error = 0;
		for (Motor motor : this) {
			error += ((TalonSRX) motor).getError();
		}
		error /= this.size();
		return error;
	}

	/**
	 * @return The encoder associated with the group.
	 */
	public Encoder getEncoder() {
		return encoder;
	}

	public void enableBrakeMode(boolean enabled) {
		for (Motor motor : this) {
			if (motor instanceof TalonSRX) {
				((TalonSRX) motor).enableBrakeMode(enabled);
			}
		}
	}

	// TODO: probably want to find a better way to use antidrift than this
	// way.
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	public void enableAntiDrift(AntiDrift antiDrift) {
		this.antiDrift = antiDrift;
	}

	public void disableAntiDrift() {
		this.antiDrift = null;
	}

	public boolean isAntiDriftEnabled() {
		return !(antiDrift == null);
	}

	public AntiDrift getAntiDrift() {
		return antiDrift;
	}
}
