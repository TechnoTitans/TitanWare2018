package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

public class Elevator {

	private TalonSRX elevatorMotor;
	private LimitSwitch limitBottom;

	public Elevator(TalonSRX motor, LimitSwitch limitBottom) {
		this.elevatorMotor = motor;
		this.limitBottom = limitBottom;
	}

	public void spin(double speed) {
		if (elevatorMotor.getEncoder().getDistance() >= 60) // find actual value
			elevatorMotor.brake();
		else if (limitBottom.isPressed()) {
			elevatorMotor.getEncoder().reset();
		} 
		else if(limitBottom.isPressed() && speed < 0)
			elevatorMotor.brake();
		else
			elevatorMotor.set(speed);
	}
}
