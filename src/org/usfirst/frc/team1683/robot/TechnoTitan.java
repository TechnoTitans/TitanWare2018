
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.autonomous.DrivePathPoints;
import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.controls.Controls;
import org.usfirst.frc.team1683.controls.JoystickXBox;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.DriveTrainTurner;
import org.usfirst.frc.team1683.driveTrain.LinearEasing;
import org.usfirst.frc.team1683.driveTrain.Path;
import org.usfirst.frc.team1683.driveTrain.PathPoint;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.scoring.Elevator;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LimitSwitch;
import org.usfirst.frc.team1683.sensors.QuadEncoder;
import org.usfirst.frc.team1683.sensors.TimeEncoder;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * 
 * Main class
 *
 */
public class TechnoTitan extends IterativeRobot {
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	public static final double WHEEL_RADIUS = 2.8999;
	public static final double SWITCH_HEIGHT = 30;

	Autonomous auto;
	AutonomousSwitcher autoSwitch;

	Timer waitTeleop;
	Timer waitAuto;
	
	LimitSwitch limitTop;
	LimitSwitch limitBottom;
	BuiltInAccel accel;
	PowerDistributionPanel pdp;
	Gyro gyro;

	TalonSRX grabberLeft;
	TalonSRX grabberRight;
	
	Elevator elevator;

	TankDrive drive;
	TalonSRX leftETalonSRX, rightETalonSRX;
	Controls controls;
	
//	Solenoid grabberSolenoid;

	CameraServer server;
	
//	DriveTrainMover mover;

	boolean teleopReady = false;

	private TimeEncoder encLeft, encRight, encElev;
	@Override
	public void robotInit() {
		SmartDashboard.initFlashTimer();
		waitTeleop = new Timer();
		waitAuto = new Timer();

		accel = new BuiltInAccel();
		gyro = new Gyro(HWR.GYRO);
		limitTop = new LimitSwitch(HWR.LIMIT_SWITCH_TOP, false);
		limitBottom = new LimitSwitch(HWR.LIMIT_SWITCH_BOTTOM, false);

//		grabberSolenoid = new Solenoid(HWR.PCM, HWR.GRABBER_SOLENOID);
		grabberLeft = new TalonSRX(HWR.GRABBER_LEFT, false);
		grabberRight = new TalonSRX(HWR.GRABBER_RIGHT, false);
		
		TalonSRX elevatorTalon = new TalonSRX(HWR.ELEVATOR_MAIN, false);
//		elevatorTalon.setEncoder(new QuadEncoder(elevatorTalon, 0.7292, true)); // TODO: find wheel radius
		elevatorTalon.setEncoder(encElev = new TimeEncoder(elevatorTalon, 5));
		elevator = new Elevator(elevatorTalon, limitTop, limitBottom);
		elevator.overrideLimit(true);
		AntiDrift left = new AntiDrift(gyro, -1);
		AntiDrift right = new AntiDrift(gyro, 1);
		leftETalonSRX = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_FRONT, LEFT_REVERSE, left);
		rightETalonSRX = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_FRONT, RIGHT_REVERSE, right);
//		leftETalonSRX.setEncoder(new QuadEncoder(leftETalonSRX, WHEEL_RADIUS, true));
//		rightETalonSRX.setEncoder(new QuadEncoder(rightETalonSRX, WHEEL_RADIUS, true));
		leftETalonSRX.setEncoder(encLeft = new TimeEncoder(leftETalonSRX, 100));
		rightETalonSRX.setEncoder(encRight = new TimeEncoder(rightETalonSRX, 100));
		TalonSRX leftFollow1 = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_MIDDLE, LEFT_REVERSE),
				 leftFollow2 = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_BACK, LEFT_REVERSE),
				 rightFollow1 = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_MIDDLE, RIGHT_REVERSE),
				 rightFollow2 = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_BACK, RIGHT_REVERSE);
		leftFollow1.follow(leftETalonSRX);
		leftFollow2.follow(leftETalonSRX);
		rightFollow1.follow(rightETalonSRX);
		rightFollow2.follow(rightETalonSRX);
		
		
		// MARK - Current Limiting
		leftETalonSRX.setupCurrentLimiting();
		rightETalonSRX.setupCurrentLimiting();
		
		leftFollow1.setupCurrentLimiting();
		leftFollow2.setupCurrentLimiting();
		rightFollow1.setupCurrentLimiting();
		rightFollow2.setupCurrentLimiting();

		drive = new TankDrive(leftETalonSRX, rightETalonSRX, gyro);
		
		pdp = new PowerDistributionPanel();
		autoSwitch = new AutonomousSwitcher(drive, elevator, grabberLeft, grabberRight, accel);

		controls = new JoystickXBox();
		controls.init(drive, pdp, grabberLeft, grabberRight, elevator);
		
		SmartDashboard.prefDouble("kP", 0.05);
		
		CameraServer.getInstance().startAutomaticCapture();
	}
	private Path path;
	@Override
	public void autonomousInit() {
		CameraServer.getInstance().startAutomaticCapture();
		drive.stop();
		autoSwitch.getSelected();
//		elevator.getMotor().getEncoder().reset();
		encElev.reset();
		elevator.overrideLimit(true);
		gyro.reset();
//		turner.start();
	}

	@Override
	public void autonomousPeriodic() {
		encLeft.update();
		encRight.update();
		encElev.update();
		SmartDashboard.sendData("Gyro", gyro.getAngle());
//		turner.run();
//		path.run();
		
		autoSwitch.run();
//		mover.runIteration();
	}

	@Override
	public void teleopInit() {
		drive.stop();
		elevator.getMotor().getEncoder().reset();
		waitTeleop.start();
		drive.resetEncoders();
	}

	@Override
	public void teleopPeriodic() {
		encLeft.update();
		encRight.update();
		encElev.update();
		SmartDashboard.sendData("Match Time", DriverStation.getInstance().getMatchTime());
		
		if (waitTeleop.get() > 0.2)
			teleopReady = true;
		if (teleopReady)
			controls.run();
		SmartDashboard.sendData("Left encoder", drive.getLeftEncoder().getDistance());
		SmartDashboard.sendData("Right encoder", drive.getRightEncoder().getDistance());
		SmartDashboard.sendData("Limit Switch bottom", limitBottom.isPressed());
		SmartDashboard.sendData("Limit switch top", limitTop.isPressed());
		SmartDashboard.sendData("Gyro", gyro.getAngle());
		SmartDashboard.sendData("Elevator encoder", elevator.getMotor().getEncoder().getDistance());
		SmartDashboard.sendData("Left encoder velocity", drive.getLeftEncoder().getSpeed());
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}
}