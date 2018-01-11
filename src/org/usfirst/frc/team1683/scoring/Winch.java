package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.motor.TalonSRX;

public class Winch {
	
	TalonSRX winchMotor;
	
	private double liftSpeed = .5;
	
	public Winch(int channel) {
		this.winchMotor = new TalonSRX(channel, true);
	}
	
	//set speed of winch
	public void setSpeed(double speed) {
		if (speed > 1)
			speed = 1;
		if (speed < 0)
			speed = 0;
		liftSpeed = speed;
	}
	
	// turn the winch
	public void turnOn() {
		winchMotor.set(liftSpeed);
	}

	public void turnOtherWay() {
		winchMotor.set(-liftSpeed);
	}

	public void stop() {
		winchMotor.brake();
	}
	
	public void coast() {
		winchMotor.coast();
	}

	public TalonSRX getMotor() {
		return winchMotor;
	}
}
