package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.motor.TalonSRX;

public class Elevator {

	private TalonSRX elevatorMotor;
	private final double liftSpeed = 0.7;

	public Elevator(TalonSRX motor) {
		this.elevatorMotor = motor;
	}

	public void spinUp(double speed) {
		elevatorMotor.set(liftSpeed);
	}

	public void spinDown(double speed) {
		elevatorMotor.set(-liftSpeed);
	}

	public TalonSRX getMotor() {
		return elevatorMotor;
	}

}
