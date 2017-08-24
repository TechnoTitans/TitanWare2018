package org.usfirst.frc.team1683.motor;

import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.robot.InputFilter;
import org.usfirst.frc.team1683.sensors.Encoder;

/*
 * Controlled by drive train mover
 */
public class MotorMover implements Runnable {
	private double distance;
	private double speed;

	private Motor motor;
	private Encoder encoder;
	private AntiDrift anti;
	private boolean running = true;

	private InputFilter inputFilter;

	/**
	 * Class for moving a motor a certain distance based on an encoder
	 * 
	 * @param motor
	 *            The motor that can be moved
	 * @param distance
	 *            The distance to move the motor, negative if motor should be
	 *            moved backwards
	 * @param speed
	 *            Speed between 0 and 1 to move the motor
	 * @param encoder
	 *            Encoder to measure distances
	 * @param anti
	 *            Antidrift, or null if no antidrift is to be used
	 */
	public MotorMover(Motor motor, double distance, double speed, Encoder encoder, AntiDrift anti) {
		this(motor, distance, speed, encoder);
		this.anti = anti;
		if (anti != null) {
			anti.reset();
		}
	}
	
	public MotorMover(Motor motor, double distance, double speed, Encoder encoder) {
		this.motor = motor;
		this.distance = distance;
		this.encoder = encoder;
		this.encoder.reset();
		if (distance < 0)
			this.speed = -speed;
		else
			this.speed = speed;
		encoder.reset();
		if (anti != null) {
			anti.reset();
		}
		inputFilter = new InputFilter(0.9, 0);
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * This is used if you want to create a separate thread moves the motor
	 * Caution: Using a separate thread may cause weird behavior, e.g. when the
	 * mode is disabled, the thread continues to run, because no thread.stop()
	 * is called. This is no longer recommended
	 */
	@Override
	public void run() {
		encoder.reset();
		while (distanceLeft() > 0) {
			runIteration();
		}
		motor.stop();

		encoder.reset();
	}

	/**
	 * Runs a single iteration by setting the motor at a certain speed given
	 * antidrift If it is done, it turns the motors off and returns true When it
	 * finishes, it won't run again and is set permanently off
	 * 
	 * @return True if it is done (distanceLeft() <= 0), false otherwise
	 */
	public boolean runIteration() {
		if (!running || distanceLeft() <= 0) {
			motor.set(0);
			motor.brake();
			running = false;
			return true;
		}
		double correctSpeed = speed;
		if (anti != null)
			correctSpeed = anti.antiDrift(speed);
		motor.set(inputFilter.filterInput(correctSpeed));
		return false;
	}

	/**
	 * 
	 * @return The amount of distance left to travel (negative if it has been
	 *         overshot); if distanceLeft() > 0, runIteration() still needs to
	 *         be called
	 */

	public double distanceLeft() {
		return Math.abs(distance) - Math.abs(encoder.getDistance());
	}
}
