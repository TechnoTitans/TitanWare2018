package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.constants.HWR;

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
		if(leftStick.getRawButtonPressed(10)){
			frontMode = !frontMode;
		}
		if (frontMode) {
			lSpeed = -maxPower * leftStick.getRawAxis(YAxis);
			rSpeed = -maxPower * rightStick.getRawAxis(YAxis);
		} else {
			lSpeed = maxPower * rightStick.getRawAxis(YAxis);
			rSpeed = maxPower * leftStick.getRawAxis(YAxis);
		}

		maxPower = 1;

		return new double[] { lSpeed, rSpeed };
	}

	@Override
	public double flyWheel() {
		return -0.8 * controller.getY(Hand.kLeft);
	}

	@Override
	public double elevator() {
		return -controller.getY(Hand.kRight);
	}

	private boolean xIsPressed = false;

	@Override
	public boolean overrideElevatorLimit() {
		if (controller.getXButtonPressed())
			xIsPressed = !xIsPressed;
		return xIsPressed;
	}
	
	@Override
	public boolean correctCube(){
		return controller.getBumperPressed(Hand.kLeft);
	}

//	private boolean solenoidDeployed = false;

//	@Override
//	public boolean solenoidToggle() {
//		if (controller.getBumperPressed(Hand.kLeft))
//			solenoidDeployed = !solenoidDeployed;
//		SmartDashboard.sendData("SOlenoid", solenoidDeployed);
//		return solenoidDeployed;
//	}

	@Override
	public boolean hasXBox() {
		return true;
	}

	@Override
	public void shakeXBox(double amount) {
//		controller.setRumble(RumbleType.kRightRumble, amount);
	}
	
	@Override
	public boolean getLowElevButton() {
		return controller.getAButtonPressed();
	}

	@Override
	public boolean getMidElevButton() {
		return controller.getBButtonPressed();
	}

	@Override
	public boolean getHighElevButton() {
		return controller.getYButtonPressed();
	}
}