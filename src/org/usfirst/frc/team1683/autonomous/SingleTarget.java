package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driverStation.DriverSetup;

import edu.wpi.first.wpilibj.DriverStation;

public class SingleTarget extends Autonomous{
	private PathPoint[] points;
	private Path path;
	private Target target;
	
	public SingleTarget(DriveTrain drive, TargetChooser chooser) {
		super(drive);
		
		target = chooser.getCorrectTarget();
		// default paths assume everything is on left, so multiply by -1 if otherwise
		boolean isLeft = target.isStartMiddle() ? 
				DriverStation.getInstance().getGameSpecificMessage().charAt(target.getSwitchScale()) == 'L'
				: chooser.getPosition() == 'L';
		int side = isLeft ? 1 : -1;
		if(target == Target.MIDDLE_SWITCH) {
			side = DriverStation.getInstance().getGameSpecificMessage().charAt(0) == 'L' ? 1 : -1;
		}
		else if (target == Target.MIDDLE_SCALE) {
			// TODO
		}
		else if(target == Target.CLOSE_SWITCH) {
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
