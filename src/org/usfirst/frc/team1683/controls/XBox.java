package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.pneumatics.Solenoid;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Handles all joystick inputs
 */
public class XBox extends Controls{
	private boolean frontMode = true;

	private double rSpeed;
	private double lSpeed;
	
	private boolean solenoidToggle = false;
	private double maxPower = 1.0;

	private XboxController controller;

	public XBox(DriveTrain drive, PowerDistributionPanel pdp, Solenoid solenoid) {
		super(drive, pdp, solenoid);
		controller = new XboxController(0); //TODO
	}

	public void run() {
		SmartDashboard.sendData("FIRE Solenoid", controller.getBButtonPressed());
		if (controller.getBButtonPressed()) {
			solenoidToggle = true;
		} else if (controller.getAButtonPressed()) {
			solenoidToggle = false;
		}

		if (frontMode) {
			lSpeed = -maxPower * controller.getY(Hand.kLeft);
			rSpeed = -maxPower * controller.getY(Hand.kRight);
		} else {
			lSpeed = maxPower * controller.getY(Hand.kLeft);
			rSpeed = maxPower * controller.getY(Hand.kRight);
		}
		super.run(new double[] {lSpeed, rSpeed}, solenoidToggle);
	}
}