package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverSetup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Handles all joystick inputs
 */
public class Controls {
	public static boolean[] toggle = new boolean[11];
	public static boolean[][] joystickCheckToggle = new boolean[3][11];
	private DriveTrain drive;

	private boolean frontMode = true;

	private double rSpeed;
	private double lSpeed;
	private double maxPower = 1.0;

	private final double MAX_JOYSTICK_SPEED = 1.0; // TODO
	private final double SECOND_JOYSTICK_SPEED = 0.6; // TODO

	private InputFilter rightFilter, leftFilter;
	private PowerDistributionPanel pdp;

	public Controls(DriveTrain drive, PowerDistributionPanel pdp) {
		this.drive = drive;
		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);

		this.pdp = pdp;
	}

	public void run() {
		SmartDashboard.sendData("Drive Power", maxPower);
		SmartDashboard.sendData("Drive RPM Left", drive.getSpeed()[0]);
		SmartDashboard.sendData("Drive RPM Right", drive.getSpeed()[1]);
		SmartDashboard.flash("Testing", 1);

		// brownout protection
		SmartDashboard.sendData("PDP Voltage", pdp.getVoltage());
		if (pdp.getVoltage() < 7.2) { // TODO Test
			SmartDashboard.flash("Brownout Protection", 0.8);
			drive.enableBrownoutProtection();
		} else {
			drive.disableBrownoutProtection();
		}

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
