package org.usfirst.frc.team1683.sensors;

import org.usfirst.frc.team1683.robot.InputFilter;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class BuiltInAccel extends BuiltInAccelerometer {
	public static final double MAX_FLAT_ANGLE = 3.0;
	public static final double FILTER_K = 0.5;
	public static final double THRESHOLD = 0.4;

	private InputFilter filter;

	public BuiltInAccel() {
		super();
		filter = new InputFilter(FILTER_K);
	}

	@Override
	public double getX() {
		return super.getX();
	}

	@Override
	public double getY() {
		return super.getY();
	}

	@Override
	public double getZ() {
		return super.getZ();
	}
	
	/**
	 * Axis: 0 for x, 1 for y, 2 for z
	 */
	public boolean isOverThreshold(int axis){
		switch(axis){
			case 0:
				return Math.abs(getX()) > THRESHOLD;
			case 1:
				return Math.abs(getY()) > THRESHOLD;
			case 2:
				return Math.abs(getZ()) > THRESHOLD;
			default:
				return false;
		}
	}

	public boolean isFlat() {
		return filter.filterInput(Math.abs(getAngleXZ())) < MAX_FLAT_ANGLE
				&& filter.filterInput(Math.abs(getAngleYZ())) < MAX_FLAT_ANGLE;
	}

	public double getAngleXZ() {
		return Math.atan2(getX(), getZ()) * 180 / Math.PI;
	}

	public double getAngleYZ() {
		return Math.atan2(getY(), getZ()) * 180 / Math.PI;
	}
}
