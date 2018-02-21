package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class JoystickXBox extends Controls {
	private boolean frontMode = true;

	private Joystick leftStick;
	private Joystick rightStick;
	private XboxController controller;

	private int YAxis = 1;

	private double rSpeed;
	private double lSpeed;

	private double maxPower = 1.0;

	public JoystickXBox() {
		super();
		leftStick = new Joystick(HWR.LEFT_JOYSTICK);
		rightStick = new Joystick(HWR.RIGHT_JOYSTICK);
		controller = new XboxController(HWR.XBOX);
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
		return -controller.getY(Hand.kLeft);
	}

	@Override
	public double elevator() {
		return -controller.getY(Hand.kRight);
	}

	private boolean yIsPressed = false;

	@Override
	public boolean overrideElevatorLimit() {
		if (controller.getYButtonPressed())
			yIsPressed = !yIsPressed;
		return yIsPressed;
	}

	private boolean solenoidDeployed = false;

	@Override
	public boolean solenoidToggle() {
		if (controller.getBumperPressed(Hand.kLeft))
			solenoidDeployed = !solenoidDeployed;
		SmartDashboard.sendData("SOlenoid", solenoidDeployed);
		return solenoidDeployed;
	}

	@Override
	public boolean hasXBox() {
		return true;
	}

	@Override
	public void shakeXBox(double amount) {
//		controller.setRumble(RumbleType.kRightRumble, amount);
	}

	@Override
	public boolean getMidElevButton() {
		return controller.getBButtonPressed();
	}
}