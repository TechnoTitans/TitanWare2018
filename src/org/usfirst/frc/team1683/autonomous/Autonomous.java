package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.sensors.Encoder;

import edu.wpi.first.wpilibj.Timer;

public abstract class Autonomous {
	public static final double GYRO_ANGLE_TOLERANCE = 15.0;

	protected DriveTrain tankDrive;
	protected Encoder leftEncoder;
	protected Encoder rightEncoder;

	protected Timer timer;
	protected Timer timeout;

	public State presentState = State.INIT_CASE;
	public State nextState;

	public Autonomous(DriveTrain driveTrain) {
		this.tankDrive = driveTrain;
		leftEncoder = driveTrain.getLeftEncoder();
		rightEncoder = driveTrain.getRightEncoder();
		resetAuto();
	}

	// Different states the autonomous could take
	public static enum State {
		INIT_CASE, END_CASE, WAIT, DRIVE_FORWARD;
	}

	public boolean isAtEndCase() {
		return presentState == State.END_CASE;
	}

	public void resetAuto() {
		presentState = State.INIT_CASE;
	}

	public void stop() {
		presentState = State.END_CASE;
	}

	public abstract void run();
}
