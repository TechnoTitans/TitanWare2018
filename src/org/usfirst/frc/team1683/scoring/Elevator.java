package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driveTrain.LinearEasing;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.robot.InputFilter;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

import edu.wpi.first.wpilibj.Timer;

public class Elevator {

	private TalonSRX elevatorMain;

	private final double LIFT_SPEED_MAX_DOWN = 0.5;
	private final double LIFT_SPEED_MAX_UP = 1;
//	private final double LIFT_RATIO = 0.9;
	private LimitSwitch limitTop;
	private LimitSwitch limitBottom;
	private InputFilter filter;
	

	// We start raised
	private final double START_DISTANCE = 0; // from when we start 55 in
												// extended TODO: findd actual
												// value
	private static final double MAX_DIST = 20;
	private double distance = START_DISTANCE;

	private boolean override;

	private Timer elevatorTimer;
	public Elevator(TalonSRX motorMain, LimitSwitch limitTop, LimitSwitch limitBottom) {
		filter = new InputFilter(0.8, 0);
		elevatorMain = motorMain;
//		elevatorFollow = motorFollow; getEncoder()

		this.limitBottom = limitBottom;
		this.limitTop = limitTop;
		this.override = false;
		
		
		elevatorTimer = new Timer();
		elevatorTimer.start();
	}

	public boolean spinUp() {
		if (limitTop.isPressed()) {
			stop();
//			elevatorFollow.stop();
			return true;
		} else {
			spin(1);
			return false;
		}
	}

	public boolean spinDown() {
		if (limitBottom.isPressed()) {
			elevatorMain.stop();
//			elevatorFollow.stop();
			return true;
		} else {
			spin(-0.5);
			return false;
		}
	}

	public void resetTimer() {
		elevatorTimer.reset();
		elevatorTimer.start();
	}
	
//	public boolean spinTo(double d) {
//		double distLeft = d - getHeight();
//		SmartDashboard.sendData("Elevator height", getHeight());
//		if (Math.abs(distLeft) <= 2) {
//			stop();
//			return true;
//		} else {
//			spin(distLeft > 0 ? 1 : -0.5);
//			return false;
//		}
//	}
	
	public boolean spinFor(boolean up, double time) {
		SmartDashboard.sendData("elevator timer", elevatorTimer.get());
		if (elevatorTimer.get() > time) {
			stop();
			return true;
		} else {
			spin(up ? 1 : -0.8);
			return false;
		}
	}

	public void overrideLimit(boolean override) {
		this.override = override;
	}

	public void spin(double speed) {
		SmartDashboard.sendData("Override", override);
		if (!override && ((limitTop.isPressed() && speed > 0) || (limitBottom.isPressed() && speed < 0))) {
			elevatorMain.stop();
		} else if (Math.abs(speed) < 0.09) {
			stop();
		} else {
			double rawSpeed = Math.abs(speed) * (speed > 0 ? LIFT_SPEED_MAX_UP : -LIFT_SPEED_MAX_DOWN);
			elevatorMain.set(rawSpeed);
//			elevatorMain.set(LIFT_RATIO * rawSpeed);
//			SmartDashboard.sendData("Elevator speed", LIFT_RATIO * rawSpeed);
			SmartDashboard.sendData("Elevator speed", rawSpeed);
		}
	}

	public void stop() {
		// double error = initEncValue - getHeight();
		// double correction = kP * error;
		boolean isNotAtBottom = !limitBottom.isPressed();
		SmartDashboard.sendData("StopRunning", Math.random() + ", " + isNotAtBottom);
		if (isNotAtBottom) {
			SmartDashboard.sendData("IsRunningHold", true);
			elevatorMain.set(0.1);//filter.filterInput(0.1));
//			elevatorFollow.set(filter.filterInput(0.1));
		} else {
			SmartDashboard.sendData("IsRunningHold", false);
			elevatorMain.set(0);
//			elevatorFollow.set(0);
		}
	}

	public TalonSRX getMotor() {
		return elevatorMain;
	}

}
