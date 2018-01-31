package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

public class Elevator {

	private TalonSRX elevatorMain;
	private final double liftSpeedMax = 0.7, liftSpeedMin = 0.4;
	private LimitSwitch limitTop;
	private LimitSwitch limitBottom;
	
	// We start raised
	private final double MAX_DISTANCE = 60; // from when we start at the bottom TODO: find actual value
	private final double START_DISTANCE = 30; // from when we start 55 in extended TODO: findd actual value
	private final double SLOW_DOWN_DISTANCE = 20; // start slowing down with this many in left
	private double distance = START_DISTANCE;
	
	private boolean override;

	public Elevator(TalonSRX motorMain, TalonSRX motorFollow, LimitSwitch limitTop, LimitSwitch limitBottom) {
		elevatorMain = motorMain;
		motorFollow.follow(elevatorMain);
		
		this.limitBottom = limitBottom;
		this.limitTop = limitTop;
		this.override = false;
	}

	public boolean spinUp() {
		double distLeft = distance - Math.abs(elevatorMain.getEncoder().getDistance());
		if (distLeft <= 0) {
			elevatorMain.brake();
			elevatorMain.getEncoder().reset();
			return true;
		}
		else {
			spin(getLiftSpeed(distLeft));
			return false;
		}
	}
	
	public void overrideLimit(boolean override) {
		this.override = override;
	}
	
	public void spin(double speed) {
		if (!override && ((limitTop.isPressed() && speed > 0) || (limitBottom.isPressed() && speed < 0)))
			elevatorMain.brake();
		else
			elevatorMain.set(speed);
	}
	
	/**
	 * When distLeft > SLOW_DOWN_DIST gives max speed, when distLeft < 0, gives minSpeed,
	 * and when in between it gives something in between (this is to slow down as you reach the top/bottom)
	 * @param distLeft The distance left
	 * @return The speed to move the motors
	 */
	private double getLiftSpeed(double distLeft) {
		if (distLeft > SLOW_DOWN_DISTANCE) {
			return liftSpeedMax;
		} else if (distLeft < 0) {
			return liftSpeedMin;
		}
		return liftSpeedMin + distLeft * (liftSpeedMax - liftSpeedMin) / SLOW_DOWN_DISTANCE;
	}

	public boolean spinDown() {
		if (limitBottom.isPressed()) {
			elevatorMain.getEncoder().reset();
			elevatorMain.brake();
			distance = MAX_DISTANCE; // we have hit the bottom so reset
			return true;
		}
		else {
			double distLeft = MAX_DISTANCE - Math.abs(elevatorMain.getEncoder().getDistance());
			spin(-getLiftSpeed(distLeft));
			return false;
		}
	}

	public TalonSRX getMotor() {
		return elevatorMain;
	}

}
