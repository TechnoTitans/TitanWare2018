package org.usfirst.frc.team1683.motor;

import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.sensors.Encoder;

import java.util.ArrayList;

/*
 * Creates a group of motors (for left and right side)
 */
public class MotorGroup extends ArrayList<Motor> {

	private static final long serialVersionUID = 1L;
	private Encoder encoder;

	private AntiDrift antiDrift;
	private boolean singleMotorDisabled;
	private boolean currentLimited = false;

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
		singleMotorDisabled = false;
	}

	public ArrayList<Motor> getMotor() {
		ArrayList<Motor> motors = new ArrayList<>();
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

	// brownout protection
	public void enableBrownoutProtection() {
		singleMotorDisabled = true;
	}

	// disable brownout protection
	public void disableBrownoutProtection() {
		singleMotorDisabled = false;
	}

	/**
	 * Set collective speed of motors.
	 *
	 * During brownout protection, disable first motor.
	 *
	 * @param speed
	 *            Speed from 0 to 1.
	 */
	public void set(double speed) {
		for (Motor motor : this) {
			if (singleMotorDisabled && this.get(0) == motor) {
				motor.coast();
			} else {
				motor.set(speed);
			}
		}
	}

	/**
	 * Gets collective (average) speed of motors in RPM
	 */
	public double getSpeed() {
		double speed = 0;
		double counter = 0;
		for (Motor motor : this) {
			if (motor.hasEncoder()) {
				speed += motor.getSpeed();
				counter += 1;
			}
		}
		return speed / counter;
	}

	/**
	 * Brakes group.
	 */
	public void brake() {
		for (Motor motor : this) {
			motor.brake();
		}
	}

	public void coast() {
		for (Motor motor : this) {
			motor.coast();
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

	public void enableCurrentLimiting() {

		this.forEach(motor -> {
			if (motor instanceof TalonSRX) {
				TalonSRX talon = (TalonSRX) motor;
				talon.enableCurrentLimiting();
			}
		});
		currentLimited = true;
	}

	public void disableCurrentLimiting() {
		this.forEach(motor -> {
			if (motor instanceof TalonSRX) {
				TalonSRX talon = (TalonSRX) motor;
				talon.disableCurrentLimiting();
			}
		});
	}

	public boolean isCurrentLimited() {
		return currentLimited;
	}

}
