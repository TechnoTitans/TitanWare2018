package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.motor.TalonSRX;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

/**
 * Encoder class. Used to measure how far the robot traveled
 */

public class QuadEncoder implements Encoder {

	private TalonSRX talonSRX;
	private double wheelRadius;
	private final double PULSES_PER_ROTATION = 4096;
	private boolean reversed = false;
	
	public QuadEncoder(TalonSRX talonSRX, double wheelRadius, boolean reversed) {
		this.talonSRX = talonSRX;
		// this.talonSRX.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		this.talonSRX.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		this.wheelRadius = wheelRadius;
		this.reversed = reversed;
	}

	/**
	 * The total distance that the motor has traveled Multiplies rotations by
	 * 2*pi*r where r = wheel radius
	 * 
	 * @return total distance
	 */
	@Override
	public double getDistance() {
		return talonSRX.getSelectedSensorPosition(0) * 2 * Math.PI * wheelRadius / PULSES_PER_ROTATION * (reversed ? -1 : 1);
	}

	/**
	 * Gets speed of the TalonSRX in RPM
	 */
	// speed = enc counts / 100 ms
	// (speed * 60 secs)
	// --------------------------------------
	// 4096 encoder counts * 100 milliseconds
	@Override
	public double getSpeed() {
		return (talonSRX.getSelectedSensorVelocity(0) * 60) / (PULSES_PER_ROTATION * 0.1);
	}

	@Override
	public void reset() {
		talonSRX.setSelectedSensorPosition(0, 0, 0);
	}

	public TalonSRX getTalon() {
		return talonSRX;
	}
}
