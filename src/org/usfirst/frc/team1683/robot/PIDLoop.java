package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.motor.TalonSRX;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDLoop extends PIDSubsystem {
	private final double TOLERANCE = 0.05;

	private DriveTrain drive;
	private TalonSRX talon;

	private double input;
	private boolean disabled = true;
	private double speed;

	public PIDLoop(double p, double i, double d, DriveTrain drive, double speed) {
		super(p, i, d);
		this.drive = drive;
		this.speed = speed;
	}

	public PIDLoop(double p, double i, double d, TalonSRX talon) {
		super(p, i, d);
		this.talon = talon;
	}

	public void setInput(double input) {
		this.input = input;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	protected void initDefaultCommand() {
		setAbsoluteTolerance(TOLERANCE);
		getPIDController().setContinuous(false);
	}

	@Override
	protected double returnPIDInput() {
		return input;
	}

	@Override
	protected void usePIDOutput(double output) {
		if (getType() == 1)
			runDrive(output);
	}

	private void runDrive(double output) {
		if (!disabled) {
			if (drive != null) {
				drive.getLeft().set(speed * (1 - output));
				drive.getRight().set(speed * (1 + output));
			} else if (talon != null) {
				talon.set(output);
			}
		}
	}

	public double getPIDPosition() {
		return getPosition();
	}

	// return 0 if neither found, 1 if drivetrain, 2 if talon
	public int getType() {
		if (drive != null)
			return 1;
		if (talon != null)
			return 2;
		return 0;
	}

	public void stopPID() {
		disable();
		disabled = true;
	}

	public void enablePID() {
		enable();
		disabled = false;
	}

	public void setPID(double p, double i, double d) {
		getPIDController().setPID(p, i, d);
	}
}
