package org.usfirst.frc.team1683.motor;

import org.usfirst.frc.team1683.sensors.Encoder;

public interface Motor {

	public void set(double speed);

	public double get();

	public void stop();

	public void brake();

	public void coast();

	public boolean hasEncoder();

	public Encoder getEncoder();

	// public void setBrakeMode(boolean enable);

	public int getChannel();

	public boolean isReversed();
}
