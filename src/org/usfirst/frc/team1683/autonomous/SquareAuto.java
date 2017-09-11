package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;

/*
 * 
 * Square autonomous for testing gyro not for competition
 * 
 */
public class SquareAuto extends Autonomous {
	private PathPoint[] points = { new PathPoint(0, 50), new PathPoint(50, 0), new PathPoint(0, -50),
			new PathPoint(-50, 0) };
	private Path path;

	public SquareAuto(DriveTrain tankDrive) {
		super(tankDrive);
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
