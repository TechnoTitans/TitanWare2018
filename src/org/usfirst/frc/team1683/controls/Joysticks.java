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

	private TalonSRX grabberLeft;
	private TalonSRX grabberRight;
	private Solenoid grabberSolenoid;
	private Elevator elevator;

	private int YAxis = 1;

	private double rSpeed;
	private double lSpeed;

	private double maxPower = 1.0;

	public Joysticks(DriveTrain drive, PowerDistributionPanel pdp, TalonSRX grabberLeft, TalonSRX grabberRight,
			Solenoid grabberSolenoid, Elevator elevator) {// , Solenoid solenoid) {
		super(drive, pdp);// , //solenoid);
		leftStick = new Joystick(HWR.LEFT_JOYSTICK);
		rightStick = new Joystick(HWR.RIGHT_JOYSTICK);
		auxStick = new Joystick(HWR.AUX_JOYSTICK);

		this.grabberRight = grabberRight;
		this.grabberLeft = grabberLeft;
		this.grabberSolenoid = grabberSolenoid;
		this.elevator = elevator;

	}

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

	@Override
	public void flyWheel() {
		if (leftStick.getRawButton(2)) {
			grabberLeft.set(-0.5);
			grabberRight.set(-0.5);
		} else if (leftStick.getRawButton(3)) {
			grabberLeft.set(0.5);
			grabberRight.set(0.5);
		} else {
			grabberLeft.set(0);
			grabberRight.set(0);
		}
	}
	
	public void elevator() {
//		if (auxStick.getRawButton(2)) //pick actual button
//			elevator.spinUp(.5);
//		else if (auxStick.getRawButton(3))
//			elevator.spinDown(.5);
		// The reason we should use the joystick itself for the elevator is that 
		// finer control is needed (e.g. adjustments)
		// whereas the grabber is either forward, backward,or stopped and find adjustment isn't as needed
		// TODO confirm this/change
		// elevator.spin(auxStick.getRawAxis(YAxis));
	}

	@Override
	public void pistonWheel() {
		if (leftStick.getRawButton(1)) {
			if (grabberSolenoid.isExtended()) {
				grabberSolenoid.retract();
			} else {
				grabberSolenoid.fire();
			}
		}

	}
}
