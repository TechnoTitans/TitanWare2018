package org.usfirst.frc.team1683.driveTrain;

/**
 * A point on a path
 * 
 * Contains useful data such as the angle and distance
 */
public class PathPoint {
	private double x, y;
	private boolean isRelative;

	/**
	 * Creates a point on a path
	 * 
	 * @param x
	 *            The x coordinate, in inches
	 * @param y
	 *            The y coordinate, in inches Note: isRelative defaults to true
	 */
	public PathPoint(double x, double y) {
		this(x, y, true);
	}

	/**
	 * Creates a point on a path
	 * 
	 * @param x
	 *            The x coordinate, in inches
	 * @param y
	 *            The y coordinate, in inches
	 * @param isRelative
	 *            True if the point should be considered relative to the
	 *            previous point (or the origin if it is the first point) in the
	 *            path, false if it should be considered absolutely on the
	 *            plane.
	 */
	public PathPoint(double x, double y, boolean isRelative) {
		this.x = x;
		this.y = y;
		this.isRelative = isRelative;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	static void convertAbsoluteToRelative(PathPoint[] path) {
		for (int i = path.length - 1; i > 0; --i) {
			if (!path[i].isRelative()) {
				path[i].setRelativeTo(path[i - 1]);
			}
		}
	}

	/**
	 * 
	 * @return The angle this point is on, in degrees, where 0 degrees is
	 *         horizontal and moving counterclockwise
	 * 
	 */
	public double getAngle() {
		return Math.toDegrees(Math.atan2(y, x));
	}

	public double getDistance() {
		return Math.hypot(x, y);
	}

	public static double getDistance(PathPoint point1, PathPoint point2) {
		return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
	}
	

	public static double getAngleTwoPoints(PathPoint currPoint, PathPoint nextPoint, double radius) {
		if (getDistance(currPoint, nextPoint) >= (2 * Math.abs(radius)))
			return Math.PI;
		return 2 * Math.asin(getDistance(currPoint, nextPoint) / (2 * Math.abs(radius)));
	}

	public static double atan2(PathPoint center, PathPoint nexPoint) {
		double angle = Math.atan2(nexPoint.getY() - center.getY(), nexPoint.getX() - center.getX());
		if (angle > 0)
			return angle;
		return 2 * Math.PI + angle;
	}

	public boolean isRelative() {
		return isRelative;
	}

	public String toString() {
		return x + ", " + y + ", " + getAngle();
	}

	void setRelativeTo(PathPoint other) {
		x -= other.x;
		y -= other.y;
		isRelative = true;
	}

	public PathPoint subtract(PathPoint other) {
		return new PathPoint(x - other.x, y - other.y, true);
	}
}
