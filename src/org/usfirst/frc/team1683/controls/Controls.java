package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.robot.InputFilter;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class Controls {

	protected static final double MAX_JOYSTICK_SPEED = 1.0;
	protected static final double SECOND_JOYSTICK_SPEED = 0.6;

	private InputFilter rightFilter, leftFilter;
	private PowerDistributionPanel pdp;
//	private Solenoid solenoid;
	private DriveTrain drive;

	public Controls(DriveTrain drive, PowerDistributionPanel pdp) {//, Solenoid solenoid) {
		this.drive = drive;
//		this.solenoid = solenoid;
		this.pdp = pdp;

		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);
	}

	public void run() {
		SmartDashboard.sendData("Drive Encoder Left", drive.getLeftEncoder().getSpeed());
		SmartDashboard.sendData("Drive Encoder Right", drive.getRightEncoder().getSpeed());
		
		// brownout protection
		SmartDashboard.sendData("PDP Voltage", pdp.getVoltage());
		SmartDashboard.sendData("PDP Current", pdp.getTotalCurrent());

		if (pdp.getVoltage() < 7.2) {
			SmartDashboard.flash("Brownout Protection", 0.3);
			drive.enableBrownoutProtection();
		} else {
			SmartDashboard.sendData("Brownout Protection", false);
			drive.disableBrownoutProtection();
		}
		
		// drive
		double[] speeds = drivePower();
		double lSpeed = leftFilter.filterInput(Math.pow(speeds[0], 3));
		double rSpeed = rightFilter.filterInput(Math.pow(speeds[1], 3));
		if (lSpeed != 0) SmartDashboard.putNumber("Drive RPM left ratio", drive.getLeftEncoder().getSpeed() / lSpeed);
		if (rSpeed != 0) SmartDashboard.putNumber("Drive RPM right ratio", drive.getRightEncoder().getSpeed() / rSpeed);
		drive.driveMode(lSpeed, rSpeed);
//		drive.getLeft().setSpeedRPM(100);
//		drive.getRight().setSpeedRPM(100);
		pistonWheel();
		flyWheel();
		elevator();
		// solenoids
//		SmartDashboard.sendData("FIRE Solenoid", solenoidToggle());
//		if (solenoidToggle()) {
//			solenoid.fire();
//		} else {
//			solenoid.retract();
//		}
	}
	
	public abstract double[] drivePower();
	
	public abstract boolean solenoidToggle();
	
	public abstract void flyWheel();
	
	public abstract void pistonWheel();
	
	public abstract void elevator();
}
