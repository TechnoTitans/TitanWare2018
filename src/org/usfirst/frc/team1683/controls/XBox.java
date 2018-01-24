package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Handles all joystick inputs
 */
public class XBox extends Controls {
	private boolean frontMode = true;

	private double rSpeed;
	private double lSpeed;

	private double maxPower = 1.0;

	private XboxController controller;

	public XBox(DriveTrain drive, PowerDistributionPanel pdp) {//, Solenoid solenoid) {
		super(drive, pdp);//, solenoid);
		controller = new XboxController(0); // TODO
	}

	public double[] drivePower() {
		if (frontMode) {
			lSpeed = -maxPower * controller.getY(Hand.kLeft);
			rSpeed = -maxPower * controller.getY(Hand.kRight);
		} else {
			lSpeed = maxPower * controller.getY(Hand.kLeft);
			rSpeed = maxPower * controller.getY(Hand.kRight);
		}
		return new double[] { lSpeed, rSpeed };
	}
	
	public boolean solenoidToggle() {
		SmartDashboard.sendData("FIRE Solenoid", controller.getBButtonPressed());
		if (controller.getBButtonPressed()) {
			return true;
		}
		return false;
	}
}