package org.usfirst.frc.team1683.controls;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.robot.InputFilter;
import org.usfirst.frc.team1683.robot.TechnoTitan;
import org.usfirst.frc.team1683.scoring.Elevator;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;

public abstract class Controls {

	protected static final double MAX_JOYSTICK_SPEED = 1.0;
	protected static final double SECOND_JOYSTICK_SPEED = 0.6;
	protected static final double ELEVATOR_SPEED = 0.5;
	private static final double GRABBER_CORRECTION_RIGHT = 1;
	private InputFilter rightFilter, leftFilter;
	private DriveTrain drive;
	private PowerDistributionPanel pdp;
	private TalonSRX grabberLeft;
	private TalonSRX grabberRight;
//	private Solenoid grabberSolenoid
	private Timer grabCorrection;
	private Elevator elevator;
	
	private ElevTarget elevTarget = ElevTarget.MANUAL;
	private Grabber grabState = Grabber.MANUAL;

	public Controls() {
		rightFilter = new InputFilter(0.86);
		leftFilter = new InputFilter(0.86);
	}

	public void init(DriveTrain drive, PowerDistributionPanel pdp, TalonSRX grabberLeft, TalonSRX grabberRight, Elevator elevator) {
		this.drive = drive;
		this.pdp = pdp;
		this.grabberLeft = grabberLeft;
		this.grabberRight = grabberRight;
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
		drive.driveMode(lSpeed, rSpeed);

		// Flywheel
		if (Math.abs(flyWheel()) > 0.1) grabState = Grabber.MANUAL;
		else if (correctCube()) grabState = Grabber.CORRECTION_OUTTAKE;
		
		if(grabState == Grabber.MANUAL){
			double fly = flyWheel();
			grabberLeft.set(fly);
			grabberRight.set(fly * GRABBER_CORRECTION_RIGHT);
			grabCorrection = null;
		}
		else if(grabState == Grabber.CORRECTION_OUTTAKE){
			if(grabCorrection == null){
				grabCorrection = new Timer();
				grabCorrection.start();
			}
			grabberLeft.set(0.5);
			grabberRight.set(0.5);
			if (grabCorrection.get() > 0.17){
				grabCorrection = null;
				grabState = Grabber.CORRECTION_INTAKE;
			}
		}
		else if (grabState == Grabber.CORRECTION_INTAKE) {
			if(grabCorrection == null){
				grabCorrection = new Timer();
				grabCorrection.start();
			}
			grabberLeft.set(-0.5);
			grabberRight.set(-0.5);
			if (grabCorrection.get() > 0.4){
				grabCorrection = null;
				grabState = Grabber.MANUAL;
			}
		}

//		// Grabber solenoid
//		SmartDashboard.sendData("Solenoid Toggle", solenoidToggle());
//		if (solenoidToggle())
//			grabberSolenoid.fire();
//		else
//			grabberSolenoid.retract();
		
		SmartDashboard.sendData("LeftCan Elev", pdp.getCurrent(2));
		SmartDashboard.sendData("RightCan Elev", pdp.getCurrent(4));
		// Elevator
		
		double elevSpeed = elevator();
		if (Math.abs(elevSpeed) > 0.1) elevTarget = ElevTarget.MANUAL;
		else if (getMidElevButton()) {
			elevator.resetTimer();
			elevTarget = ElevTarget.MID;
		}
		else if (getHighElevButton()) elevTarget = ElevTarget.TOP;
		else if (getLowElevButton()) elevTarget = ElevTarget.BOTTOM;

		if (elevTarget == ElevTarget.MANUAL) {
			elevator.spin(elevSpeed);
			elevator.overrideLimit(overrideElevatorLimit());
		} else if (elevTarget == ElevTarget.MID) {
			elevator.spinFor(true, TechnoTitan.SWITCH_HEIGHT);
		} else if (elevTarget == ElevTarget.TOP) {
			if (elevator.spinUp()) elevTarget = ElevTarget.MANUAL;
		} else if (elevTarget == ElevTarget.BOTTOM){
			if (elevator.spinDown()) elevTarget = ElevTarget.MANUAL;
		}
		SmartDashboard.sendData("Elevator target", elevTarget.toString());
//
//		if (hasXBox())
//			shakeXBox(0.7); // TODO
	}

	public abstract boolean getHighElevButton();

	public abstract boolean getMidElevButton();
	
	public abstract boolean getLowElevButton();

	public abstract double[] drivePower();
	
	public abstract boolean holdElevator();

//	public abstract boolean solenoidToggle();

	public abstract double flyWheel();

	public abstract double elevator();

	public abstract boolean overrideElevatorLimit();

	public abstract boolean hasXBox();
	
	public abstract void shakeXBox(double amount);

	public abstract boolean correctCube();

}
