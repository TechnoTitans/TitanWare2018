package org.usfirst.frc.team1683.sensors;


import edu.wpi.first.wpilibj.AnalogGyro;

public class Gyro extends AnalogGyro{

	public static double GYRO_SENSITIVITY = 0.00665; /// TODO find real value

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

	/**
	 * @return The gyro angle between -359 and 359 degrees
	 */
	@Override
	public double getAngle() {
		return super.getAngle() % 360;
	}
	
	/**
	 * 
	 * @return The raw angle of the gyro (not between -360 and 360)
	 */
	public double getRawAngle() {
		return super.getAngle();
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
