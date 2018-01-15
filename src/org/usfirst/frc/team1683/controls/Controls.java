package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.pneumatics.Solenoid;
import org.usfirst.frc.team1683.robot.InputFilter;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class Controls {

	protected static final double MAX_JOYSTICK_SPEED = 1.0;
	protected static final double SECOND_JOYSTICK_SPEED = 0.6;

	private InputFilter rightFilter, leftFilter;
	private PowerDistributionPanel pdp;
	private Solenoid solenoid;
	private DriveTrain drive;

	public Controls(DriveTrain drive, PowerDistributionPanel pdp, Solenoid solenoid) {
		this.drive = drive;
		this.solenoid = solenoid;
		this.pdp = pdp;

		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);
	}

	public void run(double[] drivePower, boolean solenoidPosition) {
		// drive
		SmartDashboard.sendData("Drive RPM Left", drive.getSpeed()[0]);
		SmartDashboard.sendData("Drive RPM Right", drive.getSpeed()[1]);
		SmartDashboard.flash("Testing", 0.6);

		// brownout protection
		SmartDashboard.sendData("PDP Voltage", pdp.getVoltage());
		SmartDashboard.sendData("PDP Current", pdp.getTotalCurrent());

		if (pdp.getVoltage() < 7.2) {
			SmartDashboard.flash("Brownout Protection", 0.3);
			drive.enableBrownoutProtection();
		} else {
			drive.disableBrownoutProtection();
		}

		double lSpeed = leftFilter.filterInput(Math.pow(drivePower[0], 3));
		double rSpeed = rightFilter.filterInput(Math.pow(drivePower[1], 3));
		drive.driveMode(lSpeed, rSpeed);

		// solenoids
		SmartDashboard.sendData("FIRE Solenoid", solenoidPosition);
		if (solenoidPosition) {
			solenoid.fire();
		} else {
			solenoid.retract();
		}
	}
}
