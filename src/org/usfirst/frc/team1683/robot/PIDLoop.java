package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.motor.MotorGroup;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PIDLoop extends PIDSubsystem {
	private final double TOLERANCE = 0.05;

	private DriveTrain drive;
	private TalonSRX talon;
	private MotorGroup group;

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

	public PIDLoop(double p, double i, double d, MotorGroup group) {
		super(p, i, d);
		this.group = group;
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
				drive.getLeftGroup().set(speed * (1 - output));
				drive.getRightGroup().set(speed * (1 + output));
			} else if (talon != null) {
				talon.set(output);
			}
		}
	}

	public double getPIDPosition() {
		return getPosition();
	}

	// return 0 if neither found, 1 if drivetrain, -1 if talon
	public int getType() {
		if (drive != null)
			return 1;
		if (talon != null)
			return 2;
		if (group != null)
			return 3;
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
