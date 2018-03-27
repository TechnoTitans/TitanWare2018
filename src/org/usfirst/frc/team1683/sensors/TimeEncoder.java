package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.motor.TalonSRX;

import edu.wpi.first.wpilibj.Timer;

// To use when testing or when encoders brake
// For emergencies
public class TimeEncoder implements Encoder {

	/**
	 * Simulates encoder based on time
	 * @param timeScale  The number of inches per second at 100% speed
	 */
	private Timer time;
	private TalonSRX talon;
	
	private double ticks = 0;
	private double lastTime = 0;
	private double timeScale;
	
	public TimeEncoder(TalonSRX talon, double timeScale) {
		time = new Timer();
		time.reset();
		time.start();
		lastTime = time.get();
		this.talon = talon;
		this.timeScale = timeScale;
	}
	
	@Override
	public double getDistance() {
		return ticks;
	}

	@Override
	public double getSpeed() {
		return timeScale * talon.getPercentSpeed();
	}

	public void update() {
		ticks += (time.get() - lastTime) * getSpeed();
	}
	
	@Override
	public void reset() {
		ticks = 0;
	}
}
