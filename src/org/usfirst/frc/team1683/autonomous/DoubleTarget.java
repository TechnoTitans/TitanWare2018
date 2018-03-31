package org.usfirst.frc.team1683.autonomous;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1683.autonomous.Autonomous.State;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.LinearEasing;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.robot.TechnoTitan;
import org.usfirst.frc.team1683.scoring.Elevator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class DoubleTarget extends Autonomous implements ChoosesTarget {
	private SingleTarget single;
	private PathPoint[] points;
	private Path path;
	private boolean secondTarget = false;
	private TargetChooser chooser;
	private Elevator elevator;
	private TalonSRX grabberLeft;
	private TalonSRX grabberRight;
	private Timer grabberTimer;
	private DriveTrainMover forward;
	private DriveTrainMover backward;
	private Target target;
	
	private boolean hasReachedEndOfPath = false;

	private boolean elevatorRaised = false;
	
	private boolean areWeMiddle = false;

	public DoubleTarget(SingleTarget single, DriveTrain drive, Elevator elevator, TalonSRX grabberLeft, TalonSRX grabberRight) {
		super(drive);
		this.single = single;
		this.elevator = elevator;
		this.grabberLeft = grabberLeft;
		this.grabberRight = grabberRight;
		presentState = State.INIT_CASE;
		grabberTimer = new Timer();
	}

	public void init() {
		target = chooser.getCorrectTarget();
		single.setChooser(chooser);
		single.run(); // initialize
		elevatorRaised = false;
		hasReachedEndOfPath = false;
		grabberTimer.reset();
		if (chooser.isSwitchOurs() && target.getIsClose()) {
			secondTarget = true;
		}
		areWeMiddle = target.isStartMiddle();
		SmartDashboard.putBoolean("2nd target", secondTarget);
	}

	public void run() {
		switch (presentState) {
			case INIT_CASE:
				init();
				nextState = State.RUN_SINGLE_TARGET;
				break;
			case RUN_SINGLE_TARGET:
				single.run();
				if (single.isAtEndCase()) {
					if (!secondTarget) nextState = State.END_CASE;
					else nextState = State.LOWER_ELEVATOR;
				}
				break;
			case LOWER_ELEVATOR:
				if (elevator.spinDown()) {
					elevator.stop();
					nextState = State.RUN_PATH;
					double heading;
					if(areWeMiddle) { // presumably this means that we started in the middle + we have already finished a single middle switch auto
						points = DrivePathPoints.MiddleCenterSwitchDouble; // these points should back up the robot and return it to the original middle starting position
						heading = DrivePathPoints.headingCenterSwitchDouble; // TODO get actual value
					} else if (target == Target.CLOSE_SCALE) {
						points = DrivePathPoints.LeftScaleLeftDouble;	
						heading = DrivePathPoints.headingScaleDouble;
					} else {
						points = DrivePathPoints.LeftSwitchLeftDouble;	
						heading = DrivePathPoints.headingSwitchDouble;
					}
					if (chooser.getPosition() == 'R' || (areWeMiddle && DriverStation.getInstance().getGameSpecificMessage().charAt(0) == 'R')) {
						for (int i = 0; i < points.length; i++) {
							points[i] = points[i].flipX();
						}
					}
					path = new Path(tankDrive, points, 0.8, 0.4, heading);
					path.setEasing(new LinearEasing(15));
				}
				break;
			case RUN_PATH:
				if (!path.isDone()) {
					path.run();
				} else {
					tankDrive.stop();
					forward = new DriveTrainMover(tankDrive, 10, 0.3); // TODO find actual distance to drive to instead of 10
					nextState = State.GRAB_CUBE;
				}
				break;
			case GRAB_CUBE:
				forward.runIteration();
				grabberLeft.set(-0.5);
				grabberRight.set(-0.5);
				if (forward.areAnyFinished()) {
					tankDrive.stop();
					double heading;
					if (areWeMiddle) {
					}
					nextState = State.RUN_PATH_2;
				}
				break;
			case RUN_PATH_2:
				if (!path.isDone()) {
					path.run();
				} else {
					tankDrive.stop();
				}
			case LIFT_ELEVATOR_2:
				if (elevator.spinTo(TechnoTitan.SWITCH_HEIGHT)) {
					elevator.stop();
					forward = new DriveTrainMover(tankDrive, 10, 0.3);
					nextState = State.DRIVE_FORWARD_2;
				}
				break;
			case DRIVE_FORWARD_2:
				forward.runIteration();
				if (forward.areAnyFinished()) {
					grabberTimer.reset();
					grabberTimer.start();
					tankDrive.stop();
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
				break;
			default:
				break;
		}
		presentState = nextState;
		SmartDashboard.sendData("Auto state", presentState.toString());
	}

	@Override
	public void setChooser(TargetChooser chooser) {
		this.chooser = chooser;
	}
}
