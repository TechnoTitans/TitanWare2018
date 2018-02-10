package org.usfirst.frc.team1683.autonomous;

import java.util.Arrays;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.LinearEasing;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.DriverStation;

public class SingleTarget extends Autonomous {
	private PathPoint[] points;
	private Path path;
	private Target target;
	private TargetChooser chooser;

	public SingleTarget(DriveTrain drive) {
		super(drive);
		presentState = State.INIT_CASE;
	}
	
	public void setChooser(TargetChooser chooser) {
		this.chooser = chooser;
	}
	
	public void init() {
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
		if (!target.isStartMiddle() && chooser.getPosition() == 'R') {
			for (int i = 0; i < points.length; ++i) {
				points[i] = points[i].flipX();
			}
		}
		path = new Path(tankDrive, points, 0.8, 0.4);
		path.setEasing(new LinearEasing(10));
		SmartDashboard.putString("path points", Arrays.toString(points));
	}

	public void run() {
		switch (presentState) {
		case INIT_CASE:
			init();
			nextState = State.RUN_PATH;
			break;
		case RUN_PATH:
			if (!path.isDone()) {
				path.run();
			} else {
				tankDrive.stop();
				nextState = State.LIFT_ELEVATOR;
			}
			break;
		case LIFT_ELEVATOR:
			break;
		default:
			break;
		}
		presentState = nextState;
		SmartDashboard.sendData("Auto state", presentState.toString());
	}
}
