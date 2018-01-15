package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.DriverSetup;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.pneumatics.Solenoid;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Handles all joystick inputs
 */
public class Joysticks extends Controls{
	private boolean frontMode = true;

	private double rSpeed;
	private double lSpeed;
	
	private boolean solenoidToggle = false;
	private double maxPower = 1.0;

	public Joysticks(DriveTrain drive, PowerDistributionPanel pdp, Solenoid solenoid) {
		super(drive, pdp, solenoid);
	}

	public void run() {
		SmartDashboard.sendData("FIRE Solenoid", findToggle(HWR.RIGHT_JOYSTICK, HWR.FIRE_SOLENOID));
		if(findToggle(HWR.RIGHT_JOYSTICK, HWR.FIRE_SOLENOID)) {
			solenoidToggle = true;
		}
		else {
			solenoidToggle = false;
		}
		
		if (frontMode) {
			lSpeed = -maxPower * DriverSetup.leftStick.getRawAxis(DriverSetup.YAxis);
			rSpeed = -maxPower * DriverSetup.rightStick.getRawAxis(DriverSetup.YAxis);
		} else {
			lSpeed = maxPower * DriverSetup.rightStick.getRawAxis(DriverSetup.YAxis);
			rSpeed = maxPower * DriverSetup.leftStick.getRawAxis(DriverSetup.YAxis);
		}

		if (DriverSetup.rightStick.getRawButton(HWR.FULL_POWER))
			maxPower = Controls.MAX_JOYSTICK_SPEED;
		else if (DriverSetup.leftStick.getRawButton(HWR.SECOND_POWER))
			maxPower = Controls.SECOND_JOYSTICK_SPEED;

		super.run(new double[] {lSpeed, rSpeed}, solenoidToggle);
	}
}
