package org.usfirst.frc.team1683.robot;

/**
 * 
 * Filters input from joysticks
 * 
 * Avoid electrical failure
 *
 */
public class InputFilter {

	private double filterK;
	private double oldOutput;
	private boolean setInitOldOutput;

	public InputFilter(double sensitivity) {
		this.filterK = sensitivity;
		setInitOldOutput = false;
	}

	public InputFilter(double sensitivity, double initOldOutput) {
		this(sensitivity);
		this.oldOutput = initOldOutput;
		setInitOldOutput = true;
	}

	public double getFilterK() {
		return filterK;
	}

	public void setFilterK(double k) {
		if (k > 1)
			k = 1;
		else if (k < 0)
			k = 0;
		this.filterK = k;
	}

	public double filterInput(double input) {
		if (!setInitOldOutput) {
			// If no default old output was set, set it to the first input that
			// comes in
			oldOutput = input;
			setInitOldOutput = true;
		}
		double output = input + filterK * (oldOutput - input);
		oldOutput = output;
		return output;
	}

	public double getLastOutput() {
		return oldOutput;
	}
}
