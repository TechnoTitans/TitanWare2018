package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverSetup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.pneumatics.Solenoid;
import org.usfirst.frc.team1683.robot.InputFilter;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Handles all joystick inputs
 */
public class XboxControls {
	private boolean[][] toggle = new boolean[3][11];
	private boolean[][] joystickCheckToggle = new boolean[3][11];
	private DriveTrain drive;

	private boolean frontMode = true;

	private double rSpeed;
	private double lSpeed;
	private double maxPower = 1.0;

	private final double MAX_JOYSTICK_SPEED = 1.0;
	private final double SECOND_JOYSTICK_SPEED = 0.6;

	private InputFilter rightFilter, leftFilter;
	private PowerDistributionPanel pdp;
	private Solenoid solenoid;
	private XboxController controller;

	public XboxControls(DriveTrain drive, PowerDistributionPanel pdp, Solenoid solenoid, XboxController controller) {
		this.drive = drive;
		this.solenoid = solenoid;
		this.controller = controller;
		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);

		this.pdp = pdp;
	}

	public void run() {
		SmartDashboard.sendData("FIRE Solenoid", controller.getBButtonPressed());
		if(controller.getBButtonPressed()) {
			solenoid.fire();
		}
		else if(controller.getAButtonPressed()){
			solenoid.retract();
		}
		
		SmartDashboard.sendData("Drive Power", maxPower);
		SmartDashboard.sendData("Drive RPM Left", drive.getSpeed()[0]);
		SmartDashboard.sendData("Drive RPM Right", drive.getSpeed()[1]);
		SmartDashboard.flash("Testing", 0.6);

		// brownout protection
		SmartDashboard.sendData("PDP Voltage", pdp.getVoltage());
		SmartDashboard.sendData("PDP Current", pdp.getTotalCurrent());
		
		if (pdp.getVoltage() < 7.2) {
			SmartDashboard.flash("Brownout Protection", 0.3);
			drive.enableBrownoutProtection();
		} else {
			drive.disableBrownoutProtection();
		}

		if (frontMode) {
			lSpeed = -maxPower * controller.getY(Hand.kLeft);
			rSpeed = -maxPower * controller.getY(Hand.kRight);
		} else {
			lSpeed = maxPower * controller.getY(Hand.kLeft);
			rSpeed = maxPower * controller.getY(Hand.kRight);
		}

		// Input filtering to avoid electrical failure
		if (maxPower == MAX_JOYSTICK_SPEED) {
			lSpeed = leftFilter.filterInput(Math.pow(lSpeed, 3));
			rSpeed = rightFilter.filterInput(Math.pow(rSpeed, 3));
		} else if (maxPower == SECOND_JOYSTICK_SPEED) {
			lSpeed = leftFilter.filterInput(lSpeed);
			rSpeed = rightFilter.filterInput(rSpeed);
		}

		/*
		if (DriverSetup.rightStick.getRawButton(HWR.FULL_POWER))
			maxPower = MAX_JOYSTICK_SPEED;
		else if (DriverSetup.leftStick.getRawButton(HWR.SECOND_POWER))
			maxPower = SECOND_JOYSTICK_SPEED;
		*/
		drive.driveMode(lSpeed, rSpeed);
	}
	
	// toggle button
	/*
	public boolean findToggle(int joystick, int button){
		if (checkSingleChange(joystick, button)) {
			toggle[joystick][button - 1] = !toggle[joystick][button - 1];
		}
		return toggle[joystick][button - 1];
	}

	// returns true if the joystick is first pressed
	public boolean checkSingleChange(int joystick, int button) {
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
	}*/
}
