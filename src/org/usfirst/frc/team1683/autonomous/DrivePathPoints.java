package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class DrivePathPoints extends Autonomous {

	// Define the paths from the starting points to the target points.
	// The convention for the path names is <start><target><target left or
	// right>
	// start is Left, Middle, or Right
	// target is Switch or Scale

	public static PathPoint[] LeftSwitchLeft = { new PathPoint(0, 168), new PathPoint(55.5, 168, false)},
					LeftSwitchRight = { new PathPoint(0, 228), new PathPoint(260, 228, false),
										new PathPoint(260, 168, false), new PathPoint(208, 168, false) },
					LeftScaleLeft = { new PathPoint(0, 323), new PathPoint(41.9, 323, false) },
					LeftScaleRight = { new PathPoint(0, 228), new PathPoint(260, 228, false), 
										new PathPoint(260, 323, false), new PathPoint(218, 323, false) },
					MiddleSwitchLeft = { new PathPoint(0, 60), new PathPoint(-132, 68, false),
										new PathPoint(-132, 168, false), new PathPoint(-76.5, 168, false) },
					MiddleSwitchRight = { new PathPoint(0, 60), new PathPoint(132, 68, false),
										new PathPoint(132, 168, false), new PathPoint(76.5, 168, false) },
					MiddleScaleLeft = { new PathPoint(0, 60), new PathPoint(-132, 60, false),
										new PathPoint(-132, 323, false), new PathPoint(-90.1, 323, false) },
					MiddleScaleRight = { new PathPoint(0, 60), new PathPoint(132, 60, false),
										new PathPoint(132, 323, false), new PathPoint(90.1, 323, false) },
					RightSwitchRight = { new PathPoint(0, 168), new PathPoint(-55.5, 168, false) },
					RightSwitchLeft = { new PathPoint(0, 228), new PathPoint(-260, 228, false),
										new PathPoint(-260, 168, false), new PathPoint(-208, 168, false) },
					RightScaleRight = { new PathPoint(0, 323), new PathPoint(-41.9, 323, false) },
					RightScaleLeft = { new PathPoint(0, 228), new PathPoint(-260, 228, false),
										new PathPoint(-260, 323, false), new PathPoint(-218, 323, false) };

	private Path path;

	public DrivePathPoints(DriveTrain tankDrive) {
		super(tankDrive);
		tankDrive.getLeftEncoder().reset();
		tankDrive.getRightEncoder().reset();
		SmartDashboard.sendData("TEst AUto", "true");
		path = new Path(tankDrive, LeftSwitchLeft, 0.3, 0.2);
	}

	public void run() {
		SmartDashboard.sendData("Encoder Left1", tankDrive.getLeftEncoder().getDistance());
		SmartDashboard.sendData("Encoder Right1", tankDrive.getRightEncoder().getDistance());
		if (!path.isDone()) {
			path.run();
			SmartDashboard.sendData("RunningAuto", true);
		} else {
			tankDrive.stop();
			SmartDashboard.sendData("RunningAuto", false);
		}
	}
}