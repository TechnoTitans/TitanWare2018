package org.usfirst.frc.team1683.driveTrain;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.Gyro;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.PIDCommand;


/**
 * Turns robot a certain number of degrees
 */
public class DriveTrainTurner {
	private DriveTrain driveTrain;
	private double initialHeading;
	private Gyro gyro;
	private double speed;
	private double angle;
	private boolean done = false;
	private LinearEasing easing;
	// tolerance in degrees / 10% speed
	
	private PIDController controller;
	Preferences prefs = Preferences.getInstance();
	private double ANGLE_TOLERANCE = prefs.getDouble("ANGLE_TOLERANCE", 7); // original value : 4
	// tolerance at 20% speed
	private double BASE_TOLERANCE = prefs.getDouble("BASE_TOLERANCE", 1);

	/**
	 * Creates a DriveTrainTurner
	 * 
	 * @param driveTrain
	 *            The drive train
	 * @param angle
	 *            The angle, if above 180 or below -180, will be adjusted to be
	 *            in that range (will not do multiple revolutions); positive
	 *            indicates counter-clockwise
	 * @param speed
	 *            Speed between 0 and 1 normally
	 */
	public DriveTrainTurner(DriveTrain driveTrain, double angle, double speed) {
		BASE_TOLERANCE = prefs.getDouble("BASE_TOLERANCE", 1);
		ANGLE_TOLERANCE = prefs.getDouble("ANGLE_TOLERANCE", 7);
		// positive angle = counter clockwise, negative = clockwise
		this.driveTrain = driveTrain;
		gyro = driveTrain.getGyro();
		gyro.reset();
//		controller = new PIDController(prefs.getDouble("kP_turn", 0.05), prefs.getDouble("kI_turn", 0), prefs.getDouble("kD_turn", 0.5), gyro, this);
//		controller.setContinuous(false);
//		controller.setOutputRange(-1, 1);
		initialHeading = gyro.getAngle();
		angle = normalizeAngle(angle);
		this.angle = angle;
		this.speed = speed;
		// If the angle is close to zero, no need to turn, we are already done
		done = Math.abs(angle) < getTolerance();
//		controller.setSetpoint(angle);
	}
	
	public void setEasing(LinearEasing easing) {
		this.easing = easing;
	}

	/**
	 * Takes an angle and returns the angle between -180 and 180 that is
	 * equivalent to it
	 * 
	 * @param angle
	 * @return An equivalent angle between -180 and 180
	 */
	public static double normalizeAngle(double angle) {
		angle %= 360;
		if (angle < -180)
			angle += 360;
		if (angle > 180)
			angle -= 360;
		return angle;
	}

	private double getTolerance() {
		return BASE_TOLERANCE + (speed - 0.2) * ANGLE_TOLERANCE * 10;
	}
	
	/**
	 * Turns in place as long as the heading is less than the angle (within
	 * ANGLE_TOLERANCE)
	 */
	public void run() {
		double heading = angleDiff(gyro.getAngle(), initialHeading);
		if (!done && Math.abs(heading) <= Math.abs(angle) - getTolerance()) {
			// If angle > 0, then it should turn counterclockwise so the "right"
			// parameter should be false
			double easingVal = easing == null ? 1 : easing.getSpeed(Math.abs(heading), Math.abs(angle) - getTolerance());
			driveTrain.turnInPlace(angle < 0, speed * easingVal);
			SmartDashboard.putNumber("prev angle", heading);
			SmartDashboard.putNumber("easing val", easingVal);
			SmartDashboard.putNumber("Angle speed", gyro.getRate());

		} else {
			driveTrain.set(0);
			driveTrain.stop();
			done = true;
		}
	}

	public double angleLeft() {
		return Math.abs(angle) - Math.abs(angleDiff(gyro.getAngle(), initialHeading));
	}

	/**
	 * 
	 * @return Whether it is done turning
	 */
	public boolean isDone() {
		return done;
	}

	private double angleDiff(double a, double b) {
		return normalizeAngle(a - b);
	}

	public double getPercentSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

//	@Override
//	public void pidWrite(double output) {
//		double easingVal = easing == null ? 1 : easing.getSpeed(Math.abs(gyro.getRawAngle()), Math.abs(angle));
//		driveTrain.setLeft(output * speed);
//		driveTrain.setRight(-output * speed);
//	}
}
