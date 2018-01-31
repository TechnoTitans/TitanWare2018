package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.pneumatics.Solenoid;
import org.usfirst.frc.team1683.robot.InputFilter;
import org.usfirst.frc.team1683.scoring.Elevator;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class Controls {

	protected static final double MAX_JOYSTICK_SPEED = 1.0;
	protected static final double SECOND_JOYSTICK_SPEED = 0.6;
	protected static final double ELEVATOR_SPEED = 0.5;

	private InputFilter rightFilter, leftFilter;
	private DriveTrain drive;
	private PowerDistributionPanel pdp;
	private TalonSRX grabberLeft;
	private TalonSRX grabberRight;
	private Solenoid grabberSolenoid;
	private Elevator elevator;

	public Controls() {
		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);
	}

	public void init(DriveTrain drive, PowerDistributionPanel pdp, TalonSRX grabberLeft, TalonSRX grabberRight, Elevator elevator) {
			//Solenoid grabberSolenoid, Elevator elevator) {
		this.drive = drive;
		this.pdp = pdp;
		this.grabberLeft = grabberLeft;
		this.grabberRight = grabberRight;
		this.grabberSolenoid = grabberSolenoid;
		this.elevator = elevator;
	}

	public void run() {
		SmartDashboard.sendData("Drive Encoder Left", drive.getLeftEncoder().getDistance());
		SmartDashboard.sendData("Drive Encoder Right", drive.getRightEncoder().getDistance());
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

		// Flywheel
		grabberLeft.set(flyWheel());
		grabberRight.set(flyWheel());

		// Grabber solenoid
//		if (solenoidToggle())
//			grabberSolenoid.fire();
//		else
//			grabberSolenoid.retract();

		// Elevator
		elevator.spin(elevator());
		// solenoids
		// SmartDashboard.sendData("FIRE Solenoid", solenoidToggle());
		// if (solenoidToggle()) {
		// solenoid.fire();
		// } else {
		// solenoid.retract();
		// }
	}

	public abstract double[] drivePower();

	public abstract boolean solenoidToggle();

	public abstract double flyWheel();

	public abstract double elevator();
}
