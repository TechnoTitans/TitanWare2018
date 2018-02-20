package org.usfirst.frc.team1683.scoring;

import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.sensors.LimitSwitch;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Elevator {

	private TalonSRX elevatorMain;
	private final double liftSpeedMax = 0.7;
	private final double kP = 0.05;
	private LimitSwitch limitTop;
	private LimitSwitch limitBottom;
	
	// We start raised
	private final double MAX_DISTANCE = 50; // todo find
	private final double START_DISTANCE = 0; // from when we start 55 in extended TODO: findd actual value
	private double distance = START_DISTANCE;
	
	private boolean override;

	public Elevator(TalonSRX motorMain, TalonSRX motorFollow, LimitSwitch limitTop, LimitSwitch limitBottom) {
		elevatorMain = motorMain;
		elevatorMain.getEncoder().reset();
		motorFollow.follow(elevatorMain);
		
		this.limitBottom = limitBottom;
		this.limitTop = limitTop;
		this.override = false;
	}

	public boolean spinUp() {
		if (limitTop.isPressed()) {
			elevatorMain.brake();
			elevatorMain.getEncoder().reset();
			distance = MAX_DISTANCE;
			return true;
		}
		else {
			spin(liftSpeedMax);
			return false;
		}
	}
	
	public boolean spinTo(double d) {
		double distLeft = d - getHeight();
		if (distLeft <= 0.5 && -0.5 <= distLeft) {
			stop(d);
			return true;
		} else {
			spin(distLeft < 0 ? liftSpeedMax : -liftSpeedMax);
			return false;
		}
	}
	
	public void overrideLimit(boolean override) {
		this.override = override;
	}
	
	Double initEncValue = null;
	public void spin(double speed) {
		if (!override && ((limitTop.isPressed() && speed > 0) || (limitBottom.isPressed() && speed < 0))) {
			elevatorMain.stop();
		} else if(Math.abs(speed) < 0.09) {
			if(initEncValue == null) {
				initEncValue = getHeight();
			}
			stop(initEncValue);
		}
		else {
			elevatorMain.set(speed * liftSpeedMax);
			initEncValue = null;
		}
	}
	
	public void stop(double initEncValue) {
		double error = initEncValue - getHeight();
		double correction = kP * error;
//		elevatorMain.set(correction);
	}

	/*public boolean spinDown() {
		if (limitBottom.isPressed()) {
			elevatorMain.getEncoder().reset();
			elevatorMain.brake();
			distance = MAX_DISTANCE; // we have hit the bottom so reset
			return true;
		}
		else {
			double distLeft = MAX_DISTANCE - Math.abs(elevatorMain.getEncoder().getDistance());
			spin(-getLiftSpeed(distLeft));
			return false;
		}
	}*/

	public TalonSRX getMotor() {
		return elevatorMain;
	}

	protected double getHeight() {
		return distance + elevatorMain.getEncoder().getDistance();
	}

}
