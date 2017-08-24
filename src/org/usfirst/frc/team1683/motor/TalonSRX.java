package org.usfirst.frc.team1683.motor;

import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.sensors.Encoder;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.interfaces.Gyro;

/*
 * Motor control (talonSRX)
 */
public class TalonSRX extends CANTalon implements Motor {

	private Encoder encoder;
	AntiDrift anti;
	Gyro gyro;

	/**
	 * Constructor for a TalonSRX motor
	 *
	 * @param channel
	 *            The port where the TalonSRX is plugged in.
	 * @param reversed
	 *            If the TalonSRX should invert the signal.
	 */
	public TalonSRX(int channel, boolean reversed) {
		super(channel);
		super.setInverted(reversed);
	}

	public TalonSRX(int channel, boolean reversed, AntiDrift anti) {
		super(channel);
		super.setInverted(reversed);
		this.anti = anti;
	}

	public TalonSRX(int channel, boolean reversed, Encoder encoder) {
		super(channel);
		super.setInverted(reversed);

		this.encoder = encoder;
	}

	/**
	 * Constructor
	 *
	 * @param channel
	 *            The port where the TalonSRX is plugged in.
	 * @param reversed
	 *            If the TalonSRX should invert the signal.
	 * @param encoder
	 *            Encoder to attach to this TalonSRX.
	 */
	public TalonSRX(int channel, boolean reversed, AntiDrift anti, Encoder encoder) {
		super(channel);
		super.setInverted(reversed);
		this.anti = anti;
		this.encoder = encoder;
	}

	/**
	 * Set the speed of the TalonSRX.
	 *
	 * @param speed
	 *            -- Speed from 0 to 1.
	 */
	@Override
	public void set(double speed) {
		super.changeControlMode(TalonControlMode.PercentVbus);
		super.set(speed);
		super.enableControl();
	}

	/**
	 * Gets speed of the TalonSRX in RPM
	 */
	// speed = enc counts / 100 ms
	// (speed * 60 secs)
	// --------------------------------------
	// 4096 encoder counts * 100 milliseconds

	@Override
	public double getSpeed() {
		return (super.getSpeed() * 60) / (4096 * 0.1);
	}

	@Override
	public void stop() {
		// super.enableBrakeMode(true);
		super.disableControl();
	}

	@Override
	public void brake() {
		super.enableBrakeMode(true);
		super.disableControl();
	}

	@Override
	public void coast() {
		super.enableBrakeMode(false);
		super.disableControl();
	}

	@Override
	public boolean hasEncoder() {
		return !(encoder == null);
	}

	@Override
	public Encoder getEncoder() {
		return encoder;
	}

	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

	// TODO: make sure this works.
	@Override
	public int getChannel() {
		return super.getDeviceID();
	}

	@Override
	public double getError() {
		return super.getError();
	}

	@Override
	public boolean isReversed() {
		return super.getInverted();
	}
}
