package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Handles all joystick inputs
 */
public class TwistJoystick extends Controls {
	private boolean[][] toggle = new boolean[3][11];
	private boolean[][] joystickCheckToggle = new boolean[3][11];

	private Joystick rightStick;
	private Joystick auxStick;

	private int YAxis = 1;

	private double rSpeed;
	private double lSpeed;

	private double maxPower = 1.0;

	public TwistJoystick() {
		super();
		rightStick = new Joystick(HWR.RIGHT_JOYSTICK);
		auxStick = new Joystick(HWR.AUX_JOYSTICK);
	}

	public double[] drivePower() {
		lSpeed = -maxPower * rightStick.getRawAxis(YAxis);
		rSpeed = -maxPower * rightStick.getRawAxis(YAxis);

		SmartDashboard.sendData("Controls LSpeed", lSpeed);
		SmartDashboard.sendData("Controls RSpeed", rSpeed);

		lSpeed += 0.7 * rightStick.getTwist();
		rSpeed -= 0.7 * rightStick.getTwist();

		SmartDashboard.sendData("Twist LSpeed", 0.6 * rightStick.getTwist());
		SmartDashboard.sendData("Twist RSpeed", -0.6 * rightStick.getTwist());

		lSpeed *= 0.9;
		rSpeed *= 0.9;

		lSpeed = normalize(lSpeed);
		rSpeed = normalize(rSpeed);

		if (rightStick.getRawButton(HWR.FULL_POWER))
			maxPower = Controls.MAX_JOYSTICK_SPEED;
		else if (rightStick.getRawButton(HWR.SECOND_POWER))
			maxPower = Controls.SECOND_JOYSTICK_SPEED;

		SmartDashboard.sendData("Twist LSpeed1", lSpeed);
		SmartDashboard.sendData("Twist RSpeed1", rSpeed);

		return new double[] { lSpeed, rSpeed };
	}

	public double normalize(double input) {
		if (input > 1) {
			return 1;
		}
		if (input < -1) {
			return -1;
		}
		return input;
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
			case HWR.RIGHT_JOYSTICK:
				pressed = rightStick.getRawButton(button);
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

	@Override
	public double flyWheel() {
		if (auxStick.getRawButton(2)) { // TODO
			return -1;
		} else if (auxStick.getRawButton(3)) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public double elevator() {
		if (auxStick.getRawButton(2)) // pick actual button
			return Controls.ELEVATOR_SPEED;
		else if (auxStick.getRawButton(3))
			return -Controls.ELEVATOR_SPEED;
		return 0;
	}

//	@Override
//	public boolean solenoidToggle() {
//		SmartDashboard.sendData("FIRE Solenoid", findToggle(HWR.AUX_JOYSTICK, HWR.FIRE_SOLENOID));
//		if (findToggle(HWR.AUX_JOYSTICK, HWR.FIRE_SOLENOID)) {
//			return true;
//		}
//		return false;
//	}

	@Override
	public boolean overrideElevatorLimit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasXBox() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shakeXBox(double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getMidElevButton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getHighElevButton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getLowElevButton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean correctCube() {
		// TODO Auto-generated method stub
		return false;
	}
}
