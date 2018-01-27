package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

public class Elevator {

	private TalonSRX elevatorMotor;
	private final double liftSpeed = 0.7;
	private LimitSwitch limitBottom;

	public Elevator(TalonSRX motor, LimitSwitch limitBottom) {
		this.elevatorMotor = motor;
		this.limitBottom = limitBottom;
	}

	public void spinUp(double speed) {
		if (elevatorMotor.getEncoder().getDistance() >= 60) //find actual value
			elevatorMotor.brake();
		else
			elevatorMotor.set(liftSpeed);
	}

	public void spinDown(double speed) {
		if (limitBottom.isPressed()) {
			elevatorMotor.getEncoder().reset();
			elevatorMotor.brake();
		}
		else
			elevatorMotor.set(-liftSpeed);
	}

	public TalonSRX getMotor() {
		return elevatorMotor;
	}

}
