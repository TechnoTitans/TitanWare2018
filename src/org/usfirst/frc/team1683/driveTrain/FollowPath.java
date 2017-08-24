package org.usfirst.frc.team1683.driveTrain;

import java.util.ArrayList;

import org.usfirst.frc.team1683.constants.Constants;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class FollowPath {
	private TankDrive drive;
	private DriveTrainMover mover;

	private ArrayList<PathPoint> pathPoints = new ArrayList<PathPoint>();
	private PathPoint currPoint, nexPoint;
	private int index = 0;
	private double SPEED = 0.2;
	private double lastSlope = 90;
	private boolean lastDirection = true;
	private boolean lastCurve;

	public FollowPath(TankDrive drive) {
		this.drive = drive;
		pathPoints.add(new PathPoint(63.0,158.0));
		pathPoints.add(new PathPoint(90.0,184.0));
		pathPoints.add(new PathPoint(140.0,129.0));
		pathPoints.add(new PathPoint(134.0,115.0));
		pathPoints.add(new PathPoint(136.0,90.0));
		pathPoints.add(new PathPoint(145.0,88.0));
		pathPoints.add(new PathPoint(150.0,93.0));
		pathPoints.add(new PathPoint(148.0,211.0));
		pathPoints.add(new PathPoint(102.0,110.0));

		currPoint = pathPoints.get(0);
		nexPoint = pathPoints.get(1);
		lastDirection = (nexPoint.getX() - currPoint.getX()) > 0;
		mover = new DriveTrainMover(drive, calDistTravel()[0], calDistTravel()[1],
				lastCurve ? SPEED : Math.abs(calDistTravel()[0] / calDistTravel()[1]) * SPEED,
				lastCurve ? Math.abs(calDistTravel()[1] / calDistTravel()[0]) * SPEED : SPEED);
	}

	public void run() {
		SmartDashboard.sendData("CurrentlyRunning", index, true);
		SmartDashboard.sendData(index + " CalDistance1", calDistTravel()[0], true);
		SmartDashboard.sendData(index + " CalDistance2", calDistTravel()[1], true);
		SmartDashboard.sendData(index + " Speed1", lastCurve ? SPEED : calDistTravel()[0] / calDistTravel()[1] * SPEED,
				true);
		SmartDashboard.sendData(index + " Speed2", lastCurve ? calDistTravel()[1] / calDistTravel()[0] * SPEED : SPEED,
				true);
		SmartDashboard.sendData(index + " CurveDirection", lastCurve, true);
		SmartDashboard.sendData(index + " Radius", calRadius(currPoint, nexPoint, lastSlope), true);
		SmartDashboard.sendData(index + " Angle",
				PathPoint.getAngleTwoPoints(currPoint, nexPoint, calRadius(currPoint, nexPoint, lastSlope)), true);
		if (index <= pathPoints.size() - 2) {
			mover.runIteration();
			if (mover.areAllFinished()) {
				drive.coast();
				index++;
				if (index < pathPoints.size() - 2) {
					currPoint = pathPoints.get(index);
					nexPoint = pathPoints.get(index + 1);
					update();
					mover = new DriveTrainMover(drive, calDistTravel()[0], calDistTravel()[1],
							lastCurve ? SPEED : Math.abs(calDistTravel()[0] / calDistTravel()[1]) * SPEED,
							lastCurve ? Math.abs(calDistTravel()[1] / calDistTravel()[0]) * SPEED : SPEED);
				}
			}
		}
	}

	private double[] calDistTravel() {
		double radius = calRadius(currPoint, nexPoint, lastSlope);
		double angle = PathPoint.getAngleTwoPoints(currPoint, nexPoint, radius);

		double leftDistance = (radius + (lastCurve ? 1 : -1) * Constants.ROBOT_WIDTH / 2) * angle;
		double rightDistance = (radius + (lastCurve ? -1 : 1) * Constants.ROBOT_WIDTH / 2) * angle;
		return new double[] { leftDistance, rightDistance };
	}

	// return true if right and false if left
	// lastDirection is false for left
	public static boolean curveDirection(PathPoint currPoint, PathPoint nextPoint, double slope, boolean direction) {
		double offset = nextPoint.getY() - (currPoint.getY() + slope * (nextPoint.getX() - currPoint.getX()));
		System.out.println("curve" + (0 < offset * (direction ? -1 : 1)));
		return 0 < offset * (direction ? -1 : 1);
	}

	public static boolean robotDirection(PathPoint currPoint, PathPoint nextPoint, double slope,
			boolean curveDirection) {
		double[] center = calCenter(currPoint, nextPoint, slope);
		double offset = nextPoint.getY() - center[1];
		System.out.println("robot" + !((offset > 0) ^ curveDirection));
		return !((offset > 0) ^ curveDirection);
	}

	private void update() {
		lastCurve = FollowPath.curveDirection(currPoint, nexPoint, lastSlope, lastDirection);
		lastDirection = FollowPath.robotDirection(currPoint, nexPoint, lastSlope, lastCurve);
		lastSlope = (calCenter(currPoint, nexPoint, lastSlope)[0] - nexPoint.getX())
				/ (nexPoint.getY() - calCenter(currPoint, nexPoint, lastSlope)[1]);
	}

	public static double calRadius(PathPoint currPoint, PathPoint nextPoint, double slope) {
		double[] center = calCenter(currPoint, nextPoint, slope);
		return PathPoint.getDistance(new PathPoint(center[0], center[1]), currPoint);
	}

	public static double[] calCenter(PathPoint point1, PathPoint point2, double slope) {
		double centerY = (0.5
				* (point1.getX() * point1.getX() + point1.getY() * point1.getY() - point2.getX() * point2.getX()
						- point2.getY() * point2.getY())
				- (point1.getX() - point2.getX()) * (point1.getX() + slope * point1.getY()))
				/ (point1.getY() - point2.getY() - slope * (point1.getX() - point2.getX()));
		double centerX = point1.getX() + point1.getY() * slope - slope * centerY;
		return new double[] { centerX, centerY };
	}

	public static double atan2(PathPoint center, PathPoint point) {
		return PathPoint.atan2(center, point);
	}
}
