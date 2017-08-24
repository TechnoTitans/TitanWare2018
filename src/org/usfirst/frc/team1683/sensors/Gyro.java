package org.usfirst.frc.team1683.sensors;

import edu.wpi.first.wpilibj.AnalogGyro;

public class Gyro extends AnalogGyro{

	public static double GYRO_SENSITIVITY = 0.0065693; /// TODO find real value

	public Gyro(int channel) {
		super(channel);
		super.initGyro();
		super.reset();
		super.setSensitivity(GYRO_SENSITIVITY);
	}

	@Override
	public void calibrate() {
		// TODO Auto-generated method stub
		super.calibrate();
	}

	@Override
	public double getAngle() {
		return super.getAngle() % 360;
	}

	@Override
	public double getRate() {
		// TODO Auto-generated method stub
		return super.getRate();
	}

	@Override
	public void reset() {
		super.reset();
	}
}
