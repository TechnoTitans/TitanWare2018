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
	private Timer backupTimer = new Timer();
	
	private boolean hasReachedEndOfPath = false;

	private boolean elevatorRaised = false;

	public SingleTarget(DriveTrain drive, Elevator elevator, TalonSRX grabberLeft, TalonSRX grabberRight) {
		super(drive);
		this.elevator = elevator;
		this.grabberLeft = grabberLeft;
		this.grabberRight = grabberRight;
		presentState = State.INIT_CASE;
		backupTimer = new Timer();
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
		boolean right = !target.isStartMiddle() && chooser.getPosition() == 'R';
		if (target == Target.MIDDLE_SWITCH) {
			right = DriverStation.getInstance().getGameSpecificMessage().charAt(0) == 'R';
			points = DrivePathPoints.MiddleCenterSwitch;
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
		if (right) {
			for (int i = 0; i < points.length; i++) {
				points[i] = points[i].flipX();
			}
		}
		path = new Path(tankDrive, points, 0.72, 0.4);
		path.setEasing(new LinearEasing(15));
		path.setTurnEasing(new LinearEasing(45, 45, 0.5));
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
			grabberFall = new DriveTrainMover(tankDrive, 12, 0.5);
			break;
		case DROP_GRABBER_1:
			grabberFall.runIteration();
			if(grabberFall.areAnyFinished()){
				tankDrive.stop();
				backupTimer.reset();
				backupTimer.start();
//				grabberFall = new DriveTrainMover(tankDrive, -5, 0.3);
				nextState = State.DROP_GRABBER_2;
			}
			break;
		case DROP_GRABBER_2:
			tankDrive.set(-0.3);
			if(backupTimer.get() > 0.6){
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
				nextState = State.LIFT_ALITTLE; //TODO change to lift a little
			}
			break;
		case LIFT_ALITTLE:
			if (elevator.spinTo(3)) {
				elevator.stop();
				nextState = State.RUN_PATH;
			}
			break;
		case RUN_PATH:
			boolean shouldLiftElevator = target != Target.FAR_SWITCH;
			if (!path.isDone()) {
				path.run();
			} else {
				tankDrive.stop();
				if (shouldLiftElevator) nextState = State.LIFT_ELEVATOR;
				else nextState = State.END_CASE;
			}
			hasReachedEndOfPath = hasReachedEndOfPath || path.getApproxDistLeft() < 80;
			if (hasReachedEndOfPath && shouldLiftElevator) {
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
			SmartDashboard.sendData("Moving forward enc speed", (tankDrive.getLeftEncoder().getSpeed() + tankDrive.getRightEncoder().getSpeed()) / 2);
			if(forward.areAnyFinished() || DriverStation.getInstance().getMatchTime() <= 2
					|| (Math.abs(tankDrive.getLeftEncoder().getSpeed()) + Math.abs(tankDrive.getRightEncoder().getSpeed()) < 10 && forward.getAverageDistanceLeft() < 10)){
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
		SmartDashboard.sendData("Auto state (single)", presentState.toString());
	}
}
