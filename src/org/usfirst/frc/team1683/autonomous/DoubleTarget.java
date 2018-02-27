package org.usfirst.frc.team1683.autonomous;

import java.util.Arrays;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.LinearEasing;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.robot.TechnoTitan;
import org.usfirst.frc.team1683.scoring.Elevator;

import edu.wpi.first.wpilibj.Timer;

public class DoubleTarget extends Autonomous {
	private SingleTarget single;
	private PathPoint[] points;
	private Path path;
	private Target target;
	private TargetChooser chooser;
	private Elevator elevator;
	private TalonSRX grabberLeft;
	private TalonSRX grabberRight;
	private Timer grabberTimer;
	private DriveTrainMover forward;

	private boolean singleTarget = false;

	private boolean hasReachedEndOfPath = false;

	private boolean elevatorRaised = false;

	public DoubleTarget(SingleTarget single, DriveTrain drive, Elevator elevator, TalonSRX grabberLeft, TalonSRX grabberRight) {
		super(drive);
		this.single = single;
		this.elevator = elevator;
		this.grabberLeft = grabberLeft;
		this.grabberRight = grabberRight;
		presentState = State.INIT_CASE;
		grabberTimer = new Timer();
	}

	public void setChooser(TargetChooser chooser) {
		this.chooser = chooser;
	}

	public void init() {
		elevatorRaised = false;
		hasReachedEndOfPath = false;
		grabberTimer.reset();
		target = chooser.getCorrectTarget();
		SmartDashboard.sendData("target", target.toString());
		// default paths assume everything is on left, so multiply by -1 if
		// otherwise
		if (target == Target.CLOSE_SWITCH) {
			points = DrivePathPoints.LeftScaleLeftDouble;
		} else {
			singleTarget = true;
		}
		SmartDashboard.sendData("chooser pos", chooser.getPosition() + "");
		if (!target.isStartMiddle() && chooser.getPosition() == 'R') {
			for (int i = 0; i < points.length; i++) {
				points[i] = points[i].flipX();
			}
		}
		path = new Path(tankDrive, points, 0.4, 0.4);
		path.setEasing(new LinearEasing(15));
		SmartDashboard.putString("path points", Arrays.toString(points));
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				init();
				if (singleTarget) {
					nextState = State.SINGLE_TARGET_INSTEAD;
				} else {
					nextState = State.RUN_PATH;
				}
				break;
			case SINGLE_TARGET_INSTEAD:
				single.run();
				break;
			case RUN_PATH:
				if (!path.isDone()) {
					path.run();
				} else {
					tankDrive.stop();
					nextState = State.LIFT_ELEVATOR;
				}
				hasReachedEndOfPath = true;
				if (hasReachedEndOfPath) {
					if (!elevatorRaised) {
						elevatorRaised = elevator.spinTo(TechnoTitan.SWITCH_HEIGHT);
					} else {
						elevator.stop();
					}
				}
				break;
			case LIFT_ELEVATOR:
				if (!elevatorRaised) {
					if (elevator.spinTo(TechnoTitan.SWITCH_HEIGHT))
						elevatorRaised = true;
				} else {
					elevator.stop();
					grabberTimer.start();
					nextState = State.RELEASE_CUBE;
				}
				break;
			case RELEASE_CUBE:
				elevator.stop();
				grabberLeft.set(1.0);
				grabberRight.set(1.0);
				if (grabberTimer.get() > 2) {
					nextState = State.LOWER_ELEVATOR;
				}
				break;
			case LOWER_ELEVATOR:
				if (elevator.spinDown()) {
					elevator.stop();
					forward = new DriveTrainMover(tankDrive, 15, 0.3);
					
					grabberTimer.reset();
					grabberTimer.start();
					nextState = State.GRAB_CUBE;
				}
				break;
			case GRAB_CUBE:
				forward.runIteration();
				grabberLeft.set(-0.5);
				grabberRight.set(-0.5);
				if (forward.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.LIFT_ELEVATOR_2;
				}
				break;
			case LIFT_ELEVATOR_2:
				if (elevator.spinTo(TechnoTitan.SWITCH_HEIGHT)) {
					elevator.stop();
					grabberTimer.reset();
					grabberTimer.start();
					nextState = State.RELEASE_CUBE_2;
				}
				break;
			case RELEASE_CUBE_2:
				elevator.stop();
				grabberLeft.set(1.0);
				grabberRight.set(1.0);
				if (grabberTimer.get() > 2) {
					nextState = State.END_CASE;
				}
				break;
			case END_CASE:
				tankDrive.stop();
				
			default:
				break;
		}
		presentState = nextState;
		SmartDashboard.sendData("Auto state", presentState.toString());
	}
}
