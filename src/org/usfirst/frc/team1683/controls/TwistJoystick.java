package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Handles all joystick inputs
 */
public class TwistJoystick extends Controls {
	private boolean[][] toggle = new boolean[3][11];
	private boolean[][] joystickCheckToggle = new boolean[3][11];

	private Joystick rightStick;

	private int YAxis = 1;

	private double rSpeed;
	private double lSpeed;

	private double maxPower = 1.0;

	public TwistJoystick(DriveTrain drive, PowerDistributionPanel pdp) {//, Solenoid solenoid) {
		super(drive, pdp);//, solenoid);
		rightStick = new Joystick(HWR.RIGHT_JOYSTICK);
	}

	public double[] drivePower() {
		lSpeed = -maxPower * rightStick.getRawAxis(YAxis);
		rSpeed = -maxPower * rightStick.getRawAxis(YAxis);
		
		SmartDashboard.sendData("Controls LSpeed", lSpeed);
		SmartDashboard.sendData("Controls RSpeed", rSpeed);

		lSpeed += 0.4 * rightStick.getTwist();
		rSpeed -= 0.4 * rightStick.getTwist();
		
		SmartDashboard.sendData("Twist LSpeed", 0.4 * rightStick.getTwist());
		SmartDashboard.sendData("Twist RSpeed", -0.4 * rightStick.getTwist());

		lSpeed = normalize(lSpeed);
		rSpeed = normalize(rSpeed);
		
		SmartDashboard.sendData("LLSpeed", lSpeed);
		SmartDashboard.sendData("RRSpeed", rSpeed);

		if (rightStick.getRawButton(HWR.FULL_POWER))
			maxPower = Controls.MAX_JOYSTICK_SPEED;
		else if (rightStick.getRawButton(HWR.SECOND_POWER))
			maxPower = Controls.SECOND_JOYSTICK_SPEED;

		return new double[] { lSpeed, rSpeed };
	}
	
	public boolean solenoidToggle() {
		SmartDashboard.sendData("FIRE Solenoid", findToggle(HWR.AUX_JOYSTICK, HWR.FIRE_SOLENOID));
		if (findToggle(HWR.AUX_JOYSTICK, HWR.FIRE_SOLENOID)) {
			return true;
		}
		return false;
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
}
