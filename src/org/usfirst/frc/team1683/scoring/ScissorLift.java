package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.motor.TalonSRX;

public class ScissorLift {

	TalonSRX scissorMotor;
	
	private double scissorSpeed = .5;
	
	public ScissorLift(int channel) {
		this.scissorMotor = new TalonSRX(channel, true);
	}
	
	//set speed of motor
	public void setSpeed(double speed) {
		if (speed > 1)
			speed = 1;
		if (speed < 0)
			speed = 0;
		scissorSpeed = speed;
	}
	
	// turn the motor - press and hold to turn
	public void On() {
		scissorMotor.set(liftSpeed);
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
