package org.usfirst.frc.team1683.sensors;

/**
 * Limit Switch
 */
public class LimitSwitch extends edu.wpi.first.wpilibj.DigitalInput {

	public LimitSwitch(int portNumber) {
		super(portNumber);
	}

	public boolean isPressed() {
		return !super.get();
	}
}
