package org.usfirst.frc.team1683.sensors;

/**
 * Limit Switch
 */
public class LimitSwitch extends edu.wpi.first.wpilibj.DigitalInput {

	private boolean isInverted = false;
	
	public LimitSwitch(int portNumber, boolean isInverted) {
		super(portNumber);
		this.isInverted = isInverted;
	}

	public boolean isPressed() {
		return isInverted ? !super.get() : super.get();
	}
}
