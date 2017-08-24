package org.usfirst.frc.team1683.sensors;
/*
 * Ultrasonic sensor (analog)
 */
public class AnalogUltra extends edu.wpi.first.wpilibj.AnalogInput {
	private final double kp = 3;

	public AnalogUltra(int channel) {
		super(channel);
	}

	public double getDistance() {
		return kp * super.getVoltage();
	}
}
