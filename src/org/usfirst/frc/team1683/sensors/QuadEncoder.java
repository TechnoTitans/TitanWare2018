package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.motor.TalonSRX;

import com.ctre.CANTalon.FeedbackDevice;

/**
 * Encoder class. Used to measure how far the robot traveled
 */

public class QuadEncoder implements Encoder {

	private TalonSRX talonSRX;
	private double wheelRadius;

	public QuadEncoder(TalonSRX talonSRX, double wheelRadius) {
		this.talonSRX = talonSRX;
		this.talonSRX.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);

		this.wheelRadius = wheelRadius;
	}

	/**
	 * The total distance that the motor has traveled Multiplies rotations by
	 * 2*pi*r where r = wheel radius
	 * 
	 * @return total distance
	 */
	@Override
	public double getDistance() {
		return talonSRX.getPosition() * 2 * Math.PI * wheelRadius;
	}

	/**
	 * Just calls talonSRX.getSpeed()
	 * 
	 * @return The speed of the talon in RPM
	 */
	@Override
	public double getSpeed() {
		return talonSRX.getSpeed();
	}

	@Override
	public void reset() {
		talonSRX.setPosition(0);
	}

	public TalonSRX getTalon() {
		return talonSRX;
	}
}
