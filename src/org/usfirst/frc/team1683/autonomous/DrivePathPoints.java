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
	
	// robot w/o bumpers is 27 in by 33 in
	// bumpers are 3.25 in thick
	// front of grabber is 5.5 in in front of the bumpers
	// overall robot rectangle: (33.5 in by 45 in)
	
	
	
	
	public static final double headingScaleDouble = 90,
							   headingSwitchDouble = 90;
	public static final PathPoint[] LeftSwitchLeft = {new PathPoint(0, 148), new PathPoint(7, 0)},
					// 214.7; turn; 215; turn; 65; turn; 4-6 in
					LeftSwitchRight = { new PathPoint(0, 220), new PathPoint(235, 0), new PathPoint(0,-65), new PathPoint(-4,0)},
					// 335.69; turn; ~1 in
					LeftScaleLeft = { new PathPoint(0, 176), new PathPoint(15.62, 64.77) },
					LeftScaleRight = { new PathPoint(0, 218.7), new PathPoint(260, 228, false), 
										new PathPoint(260, 323, false), new PathPoint(218, 323, false) },
					LeftScaleLeftDouble = {},
					LeftSwitchLeftDouble = {},
//					MiddleSwitchLeft = { new PathPoint(0, 60), new PathPoint(-132, 68, false),
//										new PathPoint(-132, 168, false), new PathPoint(-76.5, 168, false) },
//					MiddleSwitchRight = { new PathPoint(0, 60), new PathPoint(132, 68, false),
//										new PathPoint(132, 168, false), new PathPoint(76.5, 168, false) },
//					MiddleScaleLeft = { new PathPoint(0, 60), new PathPoint(-132, 60, false),
//										new PathPoint(-132, 323, false), new PathPoint(-90.1, 323, false) },
//					MiddleScaleRight = { new PathPoint(0, 60), new PathPoint(132, 60, false),
//										new PathPoint(132, 323, false), new PathPoint(90.1, 323, false) },
//					RightSwitchRight = { new PathPoint(0, 168), new PathPoint(-55.5, 168, false) },
//					RightSwitchLeft = { new PathPoint(0, 228), new PathPoint(-260, 228, false),
//										new PathPoint(-260, 168, fa	`lse), new PathPoint(-208, 168, false) },
//					RightScaleRight = { new PathPoint(0, 323), new PathPoint(-41.9, 323, false) },
//					RightScaleLeft = { new PathPoint(0, 228), new PathPoint(-260, 228, false),
//										new PathPoint(-260, 323, false), new PathPoint(-218, 323, false) },
					MiddleRightSwitchRight = { new PathPoint(0, 80) },
					MiddleRightSwitchLeft = { new PathPoint(0, 25), new PathPoint(-90, 80, false), new PathPoint(-90, 100, false) };

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