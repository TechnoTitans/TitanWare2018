package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;

public class SingleTarget extends Autonomous{
	private PathPoint[] points;
	private Path path;
	private Target target;
	
	public SingleTarget(DriveTrain drive, TargetChooser chooser) {
		super(drive);
		
		target = chooser.getCorrectTarget();
		if(target == Target.CLOSE_SWITCH) {
			//TODO
		}
		else if (target == Target.CLOSE_SCALE) {
			//TODO
		}
		else if (target == Target.FAR_SWITCH) {
			//TODO
		}
		else {
			//TODO
		}
		path = new Path(tankDrive, points, 0.3, 0.3);
	}	
	public void run() {
		if (!path.isDone()) {
			path.run();
		} else {
			tankDrive.stop();
		}
	}
}
