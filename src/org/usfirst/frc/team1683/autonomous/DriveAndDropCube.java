package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.autonomous.Autonomous.State;
import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.LinearEasing;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.robot.TechnoTitan;
import org.usfirst.frc.team1683.scoring.Elevator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class DriveAndDropCube extends Autonomous {
	private Path path;
	private Target target;
	private boolean hasReachedEndOfPath = false;
	private boolean elevatorRaised = false;
	private Elevator elevator;
	private DriveTrainMover forward, backup;
	private Timer grabberTimer;
	private TalonSRX grabberLeft, grabberRight;
	public DriveAndDropCube(DriveTrain drive, Path path, Target target, Elevator elev, TalonSRX grabberLeft, TalonSRX grabberRight) {
		super(drive);
		this.path = path;
		this.target = target;
		elevator = elev;
		grabberTimer = new Timer();
		this.grabberLeft = grabberLeft;
		this.grabberRight = grabberRight;
	}

	public void run() {
		switch (presentState) {
		case INIT_CASE:
			nextState = State.LIFT_ALITTLE;
			elevator.resetTimer();
			break;
		case LIFT_ALITTLE:
			if (elevator.spinFor(true, 0.2)) {
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
				if (shouldLiftElevator) {
					elevator.resetTimer();
					nextState = State.LIFT_ELEVATOR;
				}
				else nextState = State.END_CASE;
			}
			boolean shouldStartRaise = path.getApproxDistLeft() < 80;
			SmartDashboard.sendData("should start raise", shouldStartRaise);
			SmartDashboard.sendData("has reached end", hasReachedEndOfPath);
			if (shouldStartRaise && !hasReachedEndOfPath) {
				hasReachedEndOfPath = true;
				elevator.resetTimer();
			}
			if (hasReachedEndOfPath && shouldLiftElevator) {
				if (!elevatorRaised ) {
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
			break;
		default:
			break;
		}
		SmartDashboard.sendData("Drive and drop cube state", presentState.toString());
		presentState = nextState;
	}

	private boolean spinElevator() {
		if (target == Target.CLOSE_SWITCH || target == Target.FAR_SWITCH || target == Target.MIDDLE_SWITCH)
			return elevator.spinFor(true, TechnoTitan.SWITCH_HEIGHT);
		else
			return elevator.spinUp();
	}
}
