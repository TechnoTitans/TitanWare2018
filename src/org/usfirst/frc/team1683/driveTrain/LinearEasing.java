package org.usfirst.frc.team1683.driveTrain;

public class LinearEasing {
	private static final double DEFAULT_START_SPEED = 0.2;
	private double easingDistanceStart, easingDistanceEnd, startSpeed;
	
	/**
	 * Creates a linear easing object, which grows the motor speed linearly
	 * As the distance increases from 0 to easingDistance (in inches), the speed grows
	 * linearly from startSpeed (default: 0.2) to 1. As the robot comes to a stop, for the last
	 * easingDistance inches, the speed falls linearly from 1 to startSpeed.
	 * @param easingDistance The distance for which the easing applies at the beginning and end
	 */
	public LinearEasing(double easingDistance) {
		this(easingDistance, easingDistance, DEFAULT_START_SPEED);
	}
	
	/**
	 * Creates the same thing as with a single parameter but with more flexibility.
	 * If you want to make the start easing distance different from the end easing distance
	 * or modify the starting speed when the distance is 0, use this.
	 * @param easingDistanceStart
	 * @param easingDistanceEnd
	 * @param startSpeed
	 */
	public LinearEasing(double easingDistanceStart, double easingDistanceEnd, double startSpeed) {
		this.easingDistanceStart = easingDistanceStart;
		this.easingDistanceEnd = easingDistanceEnd;
		this.startSpeed = startSpeed;
	}
	
	public double getSpeed(double currentDistance, double totalDistance) {
		double eStart = easingDistanceStart, eEnd = easingDistanceEnd;
		// Cap the sum of eStart and eEnd at totalDistance
		// so that we always get the smaller of the start or end easing
		// Math is a bit hard to explain
		double eSum = eStart + eEnd;
		if (eSum > totalDistance) {
			eStart *= totalDistance / eSum;
			eEnd *= totalDistance / eSum;
		}
		if (currentDistance < eStart) {
			return currentDistance / easingDistanceStart * (1 - startSpeed) + startSpeed;
		} else if (currentDistance > totalDistance - eEnd) {
			return (totalDistance - currentDistance) / easingDistanceEnd * (1 - startSpeed) + startSpeed;
		} else {
			return 1;
		}
	}

}
