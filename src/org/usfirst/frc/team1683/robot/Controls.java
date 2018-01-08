package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverSetup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

/**
 * Handles all joystick inputs
 */
public class Controls {
	public static boolean[] toggle = new boolean[11];
	public static boolean[][] joystickCheckToggle = new boolean[3][11];
	DriveTrain drive;
	
	private boolean frontMode = true;

	private double rSpeed;
	private double lSpeed;
	private double maxPower = 1.0;

	private final double MAX_JOYSTICK_SPEED = 0.5; //TODO
	private final double SECOND_JOYSTICK_SPEED = 0.35; //TODO

	private InputFilter rightFilter, leftFilter;

	public Controls(DriveTrain drive) {
		this.drive = drive;
		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);
	}

	public void run() {
		SmartDashboard.sendData("Drive Power", maxPower);
		if (frontMode) {
			lSpeed = -maxPower * DriverSetup.leftStick.getRawAxis(DriverSetup.YAxis);
			rSpeed = -maxPower * DriverSetup.rightStick.getRawAxis(DriverSetup.YAxis);
		} else {
			lSpeed = maxPower * DriverSetup.rightStick.getRawAxis(DriverSetup.YAxis);
			rSpeed = maxPower * DriverSetup.leftStick.getRawAxis(DriverSetup.YAxis);
		}

		// Input filtering to avoid electrical failure
		if (maxPower == MAX_JOYSTICK_SPEED) {
			lSpeed = leftFilter.filterInput(Math.pow(lSpeed, 3));
			rSpeed = rightFilter.filterInput(Math.pow(rSpeed, 3));
		} else if (maxPower == SECOND_JOYSTICK_SPEED) {
			lSpeed = leftFilter.filterInput(lSpeed);
			rSpeed = rightFilter.filterInput(rSpeed);
		}

		if (DriverSetup.rightStick.getRawButton(HWR.FULL_POWER))
			maxPower = MAX_JOYSTICK_SPEED;
		else if (DriverSetup.leftStick.getRawButton(HWR.SECOND_POWER))
			maxPower = SECOND_JOYSTICK_SPEED;

		drive.driveMode(lSpeed, rSpeed);
	}

	public static boolean checkToggle(int joystick, int button) {
		boolean pressed = false;

		switch (joystick) {
			case HWR.AUX_JOYSTICK:
				pressed = DriverSetup.auxStick.getRawButton(button);
				break;
			case HWR.RIGHT_JOYSTICK:
				pressed = DriverSetup.rightStick.getRawButton(button);
				break;
			case HWR.LEFT_JOYSTICK:
				pressed = DriverSetup.leftStick.getRawButton(button);
				break;
			default:
				break;
		}

		if (pressed && !joystickCheckToggle[joystick][button - 1]) {
			joystickCheckToggle[joystick][button - 1] = true;
			return true;
		} else if (pressed && joystickCheckToggle[joystick][button - 1]) {
			return false;
		} else {
			joystickCheckToggle[joystick][button - 1] = false;
			return false;
		}
	}
}
