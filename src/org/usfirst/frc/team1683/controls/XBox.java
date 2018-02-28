//package org.usfirst.frc.team1683.controls;
//
//import org.usfirst.frc.team1683.driverStation.SmartDashboard;
//
//import edu.wpi.first.wpilibj.GenericHID.Hand;
//import edu.wpi.first.wpilibj.XboxController;
//
///**
// * Handles all joystick inputs
// */
//public class XBox extends Controls {
//	private boolean frontMode = true;
//
//	private double rSpeed;
//	private double lSpeed;
//
//	private double maxPower = 1.0;
//
//	private XboxController controller;
//
//	public XBox() {
//		super();
//		controller = new XboxController(0); // TODO
//	}
//
//	public double[] drivePower() {
//		if (frontMode) {
//			lSpeed = -maxPower * controller.getY(Hand.kLeft);
//			rSpeed = -maxPower * controller.getY(Hand.kRight);
//		} else {
//			lSpeed = maxPower * controller.getY(Hand.kLeft);
//			rSpeed = maxPower * controller.getY(Hand.kRight);
//		}
//		return new double[] { lSpeed, rSpeed };
//	}
//
//	public boolean solenoidToggle() {
//		SmartDashboard.sendData("FIRE Solenoid", controller.getBButtonPressed());
//		if (controller.getBButtonPressed()) {
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public double flyWheel() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public double elevator() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public boolean overrideElevatorLimit() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean hasXBox() {
//		return true;
//	}
//
//	@Override
//	public void shakeXBox(double amount) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public boolean getMidElevButton() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean getHighElevButton() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean getLowElevButton() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean correctCube() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//}