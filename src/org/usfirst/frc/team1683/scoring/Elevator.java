package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

public class Elevator {

	private TalonSRX elevatorMain;
	private final double liftSpeedMax = 0.7;
	private LimitSwitch limitTop;
	private LimitSwitch limitBottom;

	// We start raised
	private final double START_DISTANCE = 0; // from when we start 55 in
												// extended TODO: findd actual
												// value
	private double distance = START_DISTANCE;

	private boolean override;

	public Elevator(TalonSRX motorMain, TalonSRX motorFollow, LimitSwitch limitTop, LimitSwitch limitBottom) {
		elevatorMain = motorMain;
		elevatorMain.getEncoder().reset();
		motorFollow.follow(elevatorMain);

		this.limitBottom = limitBottom;
		this.limitTop = limitTop;
		this.override = false;
	}

	public boolean spinUp() {
		if (limitTop.isPressed()) {
			elevatorMain.stop();
			return true;
		} else {
			spin(liftSpeedMax);
			return false;
		}
	}

	public boolean spinDown() {
		if (limitBottom.isPressed()) {
			elevatorMain.stop();
			return true;
		} else {
			spin(-liftSpeedMax / 2);
			return false;
		}
	}

	public boolean spinTo(double d) {
		double distLeft = d - getHeight();
		if (Math.abs(distLeft) <= 10) {
			stop();
			return true;
		} else {
			spin(distLeft > 0 ? liftSpeedMax : -liftSpeedMax);
			return false;
		}
	}

	public void overrideLimit(boolean override) {
		this.override = override;
	}

	Double initEncValue = null;

	public void spin(double speed) {
		SmartDashboard.sendData("ELevator Speed speed", speed);
		if(limitBottom.isPressed()) 
			elevatorMain.getEncoder().reset();
		if (!override && ((limitTop.isPressed() && speed > 0) || (limitBottom.isPressed() && speed < 0))) {
			elevatorMain.stop();
		}
		else if (Math.abs(speed) < 0.09) {
			if (initEncValue == null) {
				initEncValue = getHeight();
			}
			stop();
		} else {
			elevatorMain.set(speed * liftSpeedMax);
			initEncValue = null;
		}
	}

	public void stop() {
		// double error = initEncValue - getHeight();
		// double correction = kP * error;
		if (!limitBottom.isPressed())
			elevatorMain.set(0.1);
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
