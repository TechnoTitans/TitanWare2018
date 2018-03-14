package org.usfirst.frc.team1683.autonomous;

import java.util.ArrayList;
import java.util.List;

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
	private boolean secondTarget = false;
	private TargetChooser chooser;
	private Elevator elevator;
	private TalonSRX grabberLeft;
	private TalonSRX grabberRight;
	private Timer grabberTimer;
	private DriveTrainMover forward;

	private boolean hasReachedEndOfPath = false;

	private boolean elevatorRaised = false;
	
	private char side;

	public DoubleTarget(SingleTarget single, DriveTrain drive, Elevator elevator, TalonSRX grabberLeft, TalonSRX grabberRight) {
		super(drive);
		this.single = single;
		this.elevator = elevator;
		this.grabberLeft = grabberLeft;
		this.grabberRight = grabberRight;
		presentState = State.INIT_CASE;
		grabberTimer = new Timer();
	}

	public void setSide(char side) {
		this.side = side;
	}

	public void init() {
		List<Target> priorities = new ArrayList<>();
		// double target has constant priorities
		priorities.add(Target.CLOSE_SCALE);
		priorities.add(Target.CLOSE_SWITCH);
		priorities.add(Target.FAR_SCALE);
		chooser = new TargetChooser(priorities, side);
		single.setChooser(chooser);
		single.run(); // initialize
		elevatorRaised = false;
		hasReachedEndOfPath = false;
		grabberTimer.reset();
		if (chooser.isSwitchOurs()) {
			secondTarget = true;
		}
		if (chooser.getPosition() == 'R') {
			for (int i = 0; i < points.length; i++) {
				points[i] = points[i].flipX();
			}
		}
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
					boolean scale = chooser.isScaleOurs();
					points = scale ? DrivePathPoints.LeftScaleLeftDouble : DrivePathPoints.LeftSwitchLeftDouble;
					double heading = scale ? DrivePathPoints.headingScaleDouble : DrivePathPoints.headingSwitchDouble;
					path = new Path(tankDrive, points, 0.8, 0.4, heading);
					path.setEasing(new LinearEasing(15));
				}
				break;
			case RUN_PATH:
				if (!path.isDone()) {
					path.run();
				} else {
					forward = new DriveTrainMover(tankDrive, 15, 0.3);
					tankDrive.stop();
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
