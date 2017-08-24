package org.usfirst.frc.team1683.driveTrain;

/**
 * Moves robot in a path based on coordinates
 */
public class Path {
	private PathPoint[] path;
	private double currentHeading;
	private DriveTrainMover mover;
	private DriveTrainTurner turner;
	private DriveTrain driveTrain;
	private boolean isTurning = true;
	private int pathIndex = 0;
	private double speed;
	private double turnSpeed;
	private boolean stopCondition;

	/**
	 * Creates a new path object
	 * 
	 * @param driveTrain
	 *            The driveTrain that controls the motors
	 * @param path
	 *            An array of PathPoint objects indicating where to go
	 * @param speed
	 *            The speed with which to move and turn Note: default heading is
	 *            90 degrees
	 */
	public Path(DriveTrain driveTrain, PathPoint[] path, double speed, double turnSpeed) {
		this(driveTrain, path, speed, turnSpeed, 90);
	}

	/**
	 * Creates a new path object
	 * 
	 * @param driveTrain
	 *            The driveTrain that controls the motors
	 * @param path
	 *            An array of PathPoint objects indicating where to go
	 * @param speed
	 *            The speed with which to move and turn
	 * @param currentHeading
	 *            The current heading in degrees, where 0 degrees is horizontal,
	 *            and 90 degrees is pointing up (towards positive y coordinates)
	 *            The current heading is used to determine how much to initially
	 *            turn.
	 */
	public Path(DriveTrain driveTrain, PathPoint[] path, double speed, double turnSpeed, double currentHeading) {
		this.driveTrain = driveTrain;
		this.path = path;
		this.currentHeading = currentHeading;
		if (path.length > 0) {
			turner = new DriveTrainTurner(driveTrain, path[0].getAngle() - currentHeading, speed);
		}
		this.speed = speed;
		this.turnSpeed = turnSpeed;
		setStopCondition(false);
		PathPoint.convertAbsoluteToRelative(path);
	}

	/**
	 * Sometimes the path has to move straight ahead In this case, this method
	 * sets whether the stop condition should be all motors are done or any In
	 * some cases one side might finish before the other side, so what should it
	 * do? If all, then it will call driveTrain.areAllFinished() If any, it will
	 * call driveTrain.areAnyFinished() The default is any
	 * 
	 * @param allFinished
	 *            true if the robot should wait until *all* motors are done
	 *            before moving to the next point, false otherwise
	 */
	public void setStopCondition(boolean allFinished) {
		stopCondition = allFinished;
	}

	private boolean isMoverDone() {
		if (stopCondition) {
			return mover.areAllFinished();
		} else {
			return mover.areAnyFinished();
		}
	}

	public boolean isDone() {
		return pathIndex >= path.length;
	}

	public void run() {
		if (isDone()) {
			return;
		}
		if (isTurning) {
			if (turner.isDone()) {
				mover = new DriveTrainMover(driveTrain, path[pathIndex].getDistance(), speed);
				isTurning = false;
				currentHeading = path[pathIndex].getAngle();
				driveTrain.stop();
			} else {
				turner.run();
			}
		} else {
			mover.runIteration();
			if (isMoverDone()) {
				pathIndex++;
				if (!isDone()) {
					turner = new DriveTrainTurner(driveTrain, path[pathIndex].getAngle() - currentHeading,
							Math.abs(turnSpeed));
					isTurning = true;
				}
			}
		}
	}
}
