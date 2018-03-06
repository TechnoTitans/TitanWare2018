package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.sensors.Encoder;

import edu.wpi.first.wpilibj.Timer;

public abstract class Autonomous {
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
		INIT_CASE, END_CASE, WAIT, DRIVE_FORWARD, RUN_PATH, LIFT_ELEVATOR, RELEASE_CUBE, BACKUP, SINGLE_TARGET_INSTEAD, 
		LIFT_ELEVATOR_2, LOWER_ELEVATOR, GRAB_CUBE, RELEASE_CUBE_2, DROP_GRABBER_2, DROP_GRABBER_1, WAIT_GRABBER, LIFT_ALITTLE;
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
