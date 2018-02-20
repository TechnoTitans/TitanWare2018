
package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.controls.Controls;
import org.usfirst.frc.team1683.controls.JoystickXBox;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driveTrain.DriveTrainTurner;
import org.usfirst.frc.team1683.driveTrain.LinearEasing;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.scoring.Elevator;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;
import org.usfirst.frc.team1683.sensors.LimitSwitch;
import org.usfirst.frc.team1683.sensors.QuadEncoder;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;

/**
 * 
 * Main class
 *
 */
public class TechnoTitan extends IterativeRobot {
	public static final boolean LEFT_REVERSE = false;
	public static final boolean RIGHT_REVERSE = true;
	public static final double WHEEL_RADIUS = 2.8999;

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
	
	DriveTrainMover mover;

	boolean teleopReady = false;

	@Override
	public void robotInit() {
		SmartDashboard.initFlashTimer();
		waitTeleop = new Timer();
		waitAuto = new Timer();

		accel = new BuiltInAccel();
		gyro = new Gyro(HWR.GYRO);
		limitTop = new LimitSwitch(HWR.LIMIT_SWITCH_TOP, false);
		limitBottom = new LimitSwitch(HWR.LIMIT_SWITCH_BOTTOM, false);

//		grabberSolenoid = new Solenoid(HWR.PCM, HWR.SOLENOID);
		grabberLeft = new TalonSRX(HWR.GRABBER_LEFT, false);
		grabberRight = new TalonSRX(HWR.GRABBER_RIGHT, false);
		
		TalonSRX elevatorTalon = new TalonSRX(HWR.ELEVATOR_MAIN, true);
		elevatorTalon.setEncoder(new QuadEncoder(elevatorTalon, 5, false)); // TODO: find wheel radius
		elevator = new Elevator(elevatorTalon, new TalonSRX(HWR.ELEVATOR_FOLLOW, true), limitTop, limitBottom);

		AntiDrift left = new AntiDrift(gyro, -1);
		AntiDrift right = new AntiDrift(gyro, 1);
		leftETalonSRX = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_FRONT, LEFT_REVERSE, left);
		rightETalonSRX = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_FRONT, RIGHT_REVERSE, right);
		leftETalonSRX.setEncoder(new QuadEncoder(leftETalonSRX, WHEEL_RADIUS, true));
		rightETalonSRX.setEncoder(new QuadEncoder(rightETalonSRX, WHEEL_RADIUS, true));
		TalonSRX leftFollow1 = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_MIDDLE, LEFT_REVERSE),
				 leftFollow2 = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_BACK, LEFT_REVERSE),
				 rightFollow1 = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_MIDDLE, RIGHT_REVERSE),
				 rightFollow2 = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_BACK, RIGHT_REVERSE);
		leftFollow1.follow(leftETalonSRX);
		leftFollow2.follow(leftETalonSRX);
		rightFollow1.follow(rightETalonSRX);
		rightFollow2.follow(rightETalonSRX);

		drive = new TankDrive(leftETalonSRX, rightETalonSRX, gyro);

		pdp = new PowerDistributionPanel();
		autoSwitch = new AutonomousSwitcher(drive, elevator, grabberLeft, accel);

		controls = new JoystickXBox();
		controls.init(drive, pdp, grabberLeft, grabberRight, elevator); //grabberSolenoid
		
		SmartDashboard.prefDouble("kP", 0.05);

		// CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void autonomousInit() {
		drive.stop();
//		autoSwitch.getSelected();
		mover = new DriveTrainMover(drive, 96, 0.6);
		mover.setEasing(new LinearEasing(48));
	}

	@Override
	public void autonomousPeriodic() {
//		autoSwitch.run();
		mover.runIteration();
	}

	@Override
	public void teleopInit() {
		drive.stop();
		waitTeleop.start();
		drive.resetEncoders();
	}

	@Override
	public void teleopPeriodic() {
		if (waitTeleop.get() > 0.2)
			teleopReady = true;
		if (teleopReady)
			controls.run();
		SmartDashboard.sendData("Left encoder", drive.getLeftEncoder().getDistance());
		SmartDashboard.sendData("Right encoder", drive.getRightEncoder().getDistance());
		SmartDashboard.sendData("Limit Switch bottom", limitBottom.isPressed());
		SmartDashboard.sendData("Limit switch top", limitTop.isPressed());
		SmartDashboard.sendData("Gyro", gyro.getAngle());
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}
}