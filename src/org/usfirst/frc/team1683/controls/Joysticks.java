package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.pneumatics.Solenoid;
import org.usfirst.frc.team1683.scoring.Elevator;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Handles all joystick inputs
 */
public class Joysticks extends Controls {
	private boolean[][] toggle = new boolean[3][11];
	private boolean[][] joystickCheckToggle = new boolean[3][11];
	private boolean frontMode = true;

	private Joystick leftStick;
	private Joystick rightStick;
	private Joystick auxStick;

	private int YAxis = 1;

	private double rSpeed;
	private double lSpeed;

	private double maxPower = 1.0;

	public Joysticks() {
		super();// , //solenoid);
		leftStick = new Joystick(HWR.LEFT_JOYSTICK);
		rightStick = new Joystick(HWR.RIGHT_JOYSTICK);
		auxStick = new Joystick(HWR.AUX_JOYSTICK);
	}

	@Override
	public double[] drivePower() {
		if (frontMode) {
			lSpeed = -maxPower * leftStick.getRawAxis(YAxis);
			rSpeed = -maxPower * rightStick.getRawAxis(YAxis);
		} else {
			lSpeed = maxPower * rightStick.getRawAxis(YAxis);
			rSpeed = maxPower * leftStick.getRawAxis(YAxis);
		}

		if (rightStick.getRawButton(HWR.FULL_POWER))
			maxPower = Controls.MAX_JOYSTICK_SPEED;
		else if (leftStick.getRawButton(HWR.SECOND_POWER))
			maxPower = Controls.SECOND_JOYSTICK_SPEED;

		return new double[] { lSpeed, rSpeed };
	}

	@Override
	public double flyWheel() {
		if (auxStick.getRawButton(4)) { // TODO
			return -1;
		} else if (auxStick.getRawButton(5)) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public double elevator() {
//		if (auxStick.getRawButton(2)) // pick actual button
//			return Controls.ELEVATOR_SPEED;
//		else if (auxStick.getRawButton(3))
//			return -Controls.ELEVATOR_SPEED;
//		return 0;
		return auxStick.getRawAxis(YAxis);
	}

	@Override
	public boolean solenoidToggle() {
		SmartDashboard.sendData("FIRE Solenoid", findToggle(HWR.AUX_JOYSTICK, HWR.FIRE_SOLENOID));
		if (findToggle(HWR.AUX_JOYSTICK, HWR.FIRE_SOLENOID)) {
			return true;
		}
		return false;
	}

	// toggle button
	public boolean findToggle(int joystick, int button) {
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
				pressed = auxStick.getRawButton(button);
				break;
			case HWR.RIGHT_JOYSTICK:
				pressed = rightStick.getRawButton(button);
				break;
			case HWR.LEFT_JOYSTICK:
				pressed = leftStick.getRawButton(button);
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
