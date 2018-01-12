package org.usfirst.frc.team1683.pneumatics;

public class Solenoid extends edu.wpi.first.wpilibj.Solenoid {

	public Solenoid(int moduleNumber, int channel) {
		super(moduleNumber, channel);
	}

	public boolean isExtended() {
		return super.get();
	}
	
	public void fire() {
		super.set(true); 
	}
	
	public void retract() {
		super.set(false); 
	}
}