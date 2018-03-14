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

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class SingleTarget extends Autonomous implements ChoosesTarget {
	private PathPoint[] points;
	private Path path;
	private Target target;
	private TargetChooser chooser;
	private Elevator elevator;
	private TalonSRX grabberLeft;
	private TalonSRX grabberRight;
	private Timer grabberTimer;
	private DriveTrainMover forward;
	private DriveTrainMover backup;
	private DriveTrainMover grabberFall;
	
	private boolean hasReachedEndOfPath = false;

	private boolean elevatorRaised = false;

	public SingleTarget(DriveTrain drive, Elevator elevator, TalonSRX grabberLeft, TalonSRX grabberRight) {
		super(drive);
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
		if (target == Target.MIDDLE_SWITCH) {
			boolean left = DriverStation.getInstance().getGameSpecificMessage().charAt(0) == 'L';
			points = left ? DrivePathPoints.MiddleRightSwitchLeft : DrivePathPoints.MiddleRightSwitchRight;
		} else if (target == Target.CLOSE_SWITCH) {
			points = DrivePathPoints.LeftSwitchLeft;
		} else if (target == Target.CLOSE_SCALE) {
			points = DrivePathPoints.LeftScaleLeft;
		} else if (target == Target.FAR_SWITCH) {
			points = DrivePathPoints.LeftSwitchRight;
		} else if (target == Target.FAR_SCALE) {
			points = DrivePathPoints.LeftScaleRight;
		}
		SmartDashboard.sendData("chooser pos", chooser.getPosition() + "");
		if (!target.isStartMiddle() && chooser.getPosition() == 'R') {
			for (int i = 0; i < points.length; i++) {
				points[i] = points[i].flipX();
			}
		}
		if (target == Target.CLOSE_SWITCH || target == Target.MIDDLE_SWITCH)
			path = new Path(tankDrive, points, 0.5, 0.4);
		else
			path = new Path(tankDrive, points, 0.8, 0.26);
		path.setEasing(new LinearEasing(15));
		SmartDashboard.putString("path points", Arrays.toString(points));
	}

	private boolean spinElevator() {
		if (target == Target.CLOSE_SWITCH || target == Target.FAR_SWITCH || target == Target.MIDDLE_SWITCH)
			return elevator.spinTo(TechnoTitan.SWITCH_HEIGHT);
		else
			return elevator.spinUp();
	}

	public void run() {
		switch (presentState) {
		case INIT_CASE:
			init();
			nextState = State.DROP_GRABBER_1;
			grabberFall = new DriveTrainMover(tankDrive, 5, 0.5);
			break;
		case DROP_GRABBER_1:
			grabberFall.runIteration();
			if(grabberFall.areAnyFinished()){
				tankDrive.stop();
				grabberFall = new DriveTrainMover(tankDrive, -5, 0.5);
				nextState = State.DROP_GRABBER_2;
			}
			break;
		case DROP_GRABBER_2:
			grabberFall.runIteration();
			if(grabberFall.areAnyFinished()){
				tankDrive.stop();
				grabberTimer.reset();
				grabberTimer.start();
				nextState = State.WAIT_GRABBER;
			}
			break;
		case WAIT_GRABBER:
			grabberLeft.set(-0.5);
			grabberRight.set(-0.5);
			if(grabberTimer.get() > 0.7){
				grabberLeft.set(0);
				grabberRight.set(0);
				nextState = State.LIFT_ALITTLE;
			}
			break;
		case LIFT_ALITTLE:
			if (elevator.spinTo(50)) {
				elevator.stop();
				nextState = State.RUN_PATH;
			}
			break;
		case RUN_PATH:
			if (!path.isDone()) {
				path.run();
			} else {
				tankDrive.stop();
				nextState = State.END_CASE;
			}
			hasReachedEndOfPath = hasReachedEndOfPath || path.getApproxDistLeft() < 220;
			if (hasReachedEndOfPath) {
				if (!elevatorRaised) {
					elevatorRaised = spinElevator();
				} else {
					elevator.stop();
				}
			} else {
				elevator.stop();
			}
			break;
		case LIFT_ELEVATOR:
			if (!elevatorRaised) {
				if (spinElevator())
					elevatorRaised = true;
			} else {
				elevator.stop();
				forward = new DriveTrainMover(tankDrive, 20, 0.4);
				forward.setEasing(new LinearEasing(10));
				nextState = State.DRIVE_FORWARD;
			}
			break;
		case DRIVE_FORWARD:
			forward.runIteration();
			if(forward.areAnyFinished()){
				grabberTimer.reset();
				grabberTimer.start();
				tankDrive.stop();
				nextState = State.RELEASE_CUBE; //target == Target.FAR_SWITCH ? State.END_CASE : State.RELEASE_CUBE;
			}
			break;
		case RELEASE_CUBE:
			elevator.stop();
			grabberLeft.set(0.5);
			grabberRight.set(0.5);
			if(grabberTimer.get() > 1){
				backup = new DriveTrainMover(tankDrive, -20, 0.3);
				nextState = State.BACKUP;
			}
			break;
		case BACKUP:
			grabberLeft.set(0);
			grabberRight.set(0);
			elevator.stop();
			backup.runIteration();
			if(backup.areAnyFinished()){
				tankDrive.stop();
				nextState = State.END_CASE;
			}
			break;
		case END_CASE:
			tankDrive.stop();
			elevator.stop();
			break;
		default:
			break;
		}
		presentState = nextState;
		SmartDashboard.sendData("Auto state", presentState.toString());
		SmartDashboard.sendData("Auto Path", presentState.toString());
		SmartDashboard.putString("path points", Arrays.toString(points));
	}
}
