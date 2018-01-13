package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.driverStation.DriverSetup;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.sensors.Encoder;

public class Elevator {
	
	private TalonSRX elevatorMotor;	
	private double liftSpeed = .7;
	
	public Elevator(TalonSRX motor) {
		this.elevatorMotor = motor;
	}
	
	public void run() {
		if(DriverSetup.auxStick.getRawButton(8)) //fix
			elevatorMotor.set(liftSpeed);
		else
			elevatorMotor.set(0);
		
		if(DriverSetup.auxStick.getRawButton(2))
			elevatorMotor.set(-liftSpeed);
		else
			elevatorMotor.set(0);
	}
	
	public TalonSRX getMotor() {
		return elevatorMotor;
	}

}
