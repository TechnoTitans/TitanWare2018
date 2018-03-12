package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.robot.InputFilter;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

public class Elevator {

	private TalonSRX elevatorMain;
	private TalonSRX elevatorFollow;

	private final double LIFT_SPEED_MAX = 0.8;
	private final double LIFT_RATIO = 0.9;
	private LimitSwitch limitTop;
	private LimitSwitch limitBottom;
	private InputFilter filter;

	// We start raised
	private final double START_DISTANCE = 0; // from when we start 55 in
												// extended TODO: findd actual
												// value
	private double distance = START_DISTANCE;

	private boolean override;

	public Elevator(TalonSRX motorMain, TalonSRX motorFollow, LimitSwitch limitTop, LimitSwitch limitBottom) {
		filter = new InputFilter(0.99, 0);
		elevatorMain = motorMain;
		elevatorMain.getEncoder().reset();
		elevatorFollow = motorFollow;

		this.limitBottom = limitBottom;
		this.limitTop = limitTop;
		this.override = false;
	}

	public boolean spinUp() {
		if (limitTop.isPressed()) {
			elevatorMain.stop();
			elevatorFollow.stop();
			return true;
		} else {
			spin(1);
			return false;
		}
	}

	public boolean spinDown() {
		if (limitBottom.isPressed()) {
			elevatorMain.stop();
			elevatorFollow.stop();
			return true;
		} else {
			spin(-0.5);
			return false;
		}
	}

	public boolean spinTo(double d) {
		double distLeft = d - getHeight();
		if (Math.abs(distLeft) <= 30) {
			stop();
			return true;
		} else {
			spin(distLeft > 0 ? 1 : -0.5);
			return false;
		}
	}

	public void overrideLimit(boolean override) {
		this.override = override;
	}

	public void spin(double speed) {
		SmartDashboard.sendData("ELevator Speed speed", speed);
		if (limitBottom.isPressed())
			elevatorMain.getEncoder().reset();
		if (!override && ((limitTop.isPressed() && speed > 0) || (limitBottom.isPressed() && speed < 0))) {
			elevatorMain.stop();
			elevatorFollow.stop();
		} else if (Math.abs(speed) < 0.09) {
			stop();
		} else {
			double rawSpeed = filter.filterInput(speed * LIFT_SPEED_MAX);
			elevatorFollow.set(rawSpeed);
			elevatorMain.set(LIFT_RATIO * rawSpeed);
			SmartDashboard.sendData("Elevator speed", LIFT_RATIO * rawSpeed);
			SmartDashboard.sendData("Elevator Ratio2", rawSpeed);
		}
	}

	public void stop() {
		// double error = initEncValue - getHeight();
		// double correction = kP * error;
		if (!limitBottom.isPressed()) {
			elevatorMain.set(filter.filterInput(0.1));
			elevatorFollow.set(filter.filterInput(0.1));
		} else {
			elevatorMain.set(0);
			elevatorFollow.set(0);
		}
	}

	/*
	 * public boolean spinDown() { if (limitBottom.isPressed()) {
	 * elevatorMain.getEncoder().reset(); elevatorMain.brake(); distance =
	 * MAX_DISTANCE; // we have hit the bottom so reset return true; } else {
	 * double distLeft = MAX_DISTANCE -
	 * Math.abs(elevatorMain.getEncoder().getDistance());
	 * spin(-getLiftSpeed(distLeft)); return false; } }
	 */

	public TalonSRX getMotor() {
		return elevatorMain;
	}

	protected double getHeight() {
		return distance + elevatorMain.getEncoder().getDistance();
	}

}
