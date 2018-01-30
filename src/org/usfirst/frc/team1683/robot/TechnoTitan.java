


package org.usfirst.frc.team1683.robot;

import org.usfirst.frc.team1683.autonomous.Autonomous;
import org.usfirst.frc.team1683.autonomous.AutonomousSwitcher;
import org.usfirst.frc.team1683.constants.HWR;
import org.usfirst.frc.team1683.controls.Joysticks;
import org.usfirst.frc.team1683.driveTrain.AntiDrift;
import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.MotorGroup;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.pneumatics.Solenoid;
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
	public static final double WHEEL_RADIUS = 2.0356;

	TankDrive drive;
	Joysticks controls;
	Solenoid grabberSolenoid;
	
	TalonSRX grabberLeft;
	TalonSRX grabberRight;

	Timer waitTeleop;
	Timer waitAuto;

	CameraServer server;

	Autonomous auto;
	AutonomousSwitcher autoSwitch;
	LimitSwitch limitSwitch;
	Gyro gyro;
//
//	MotorGroup leftGroup;
//	MotorGroup rightGroup;
	TalonSRX leftETalonSRX, rightETalonSRX;
	PowerDistributionPanel pdp;

	Elevator elevator;

	Solenoid solenoid;
	BuiltInAccel accel;

	boolean teleopReady = false;

	@Override
	public void robotInit() {
		grabberSolenoid = new Solenoid(HWR.PCM, HWR.GRABBER_SOLENOID);
		grabberLeft = new TalonSRX(HWR.GRABBER_LEFT, false);
		grabberRight = new TalonSRX(HWR.GRABBER_RIGHT, false);
		
		SmartDashboard.initFlashTimer();
		waitTeleop = new Timer();
		waitAuto = new Timer();

		solenoid = new Solenoid(HWR.PCM, HWR.SOLENOID);
		accel = new BuiltInAccel();

		gyro = new Gyro(HWR.GYRO);
		limitSwitch = new LimitSwitch(HWR.LIMIT_SWITCH);

		AntiDrift left = new AntiDrift(gyro, -1);
		AntiDrift right = new AntiDrift(gyro, 1);
		leftETalonSRX = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_FRONT, LEFT_REVERSE, left);
		rightETalonSRX = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_FRONT, RIGHT_REVERSE, right);
		leftETalonSRX.setEncoder(new QuadEncoder(leftETalonSRX, WHEEL_RADIUS));
		rightETalonSRX.setEncoder(new QuadEncoder(rightETalonSRX, WHEEL_RADIUS));
		TalonSRX leftFollow1 = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_MIDDLE, LEFT_REVERSE),
				 leftFollow2 = new TalonSRX(HWR.LEFT_DRIVE_TRAIN_BACK, LEFT_REVERSE),
				 rightFollow1 = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_MIDDLE, RIGHT_REVERSE),
				 rightFollow2 = new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_BACK, RIGHT_REVERSE);
		leftFollow1.follow(leftETalonSRX);
		leftFollow2.follow(leftETalonSRX);
		rightFollow1.follow(rightETalonSRX);
		rightFollow2.follow(rightETalonSRX);
//		leftGroup = new MotorGroup(new QuadEncoder(leftETalonSRX, WHEEL_RADIUS), leftETalonSRX,
//				new TalonSRX(HWR.LEFT_DRIVE_TRAIN_BACK, LEFT_REVERSE),
//				new TalonSRX(HWR.LEFT_DRIVE_TRAIN_MIDDLE, LEFT_REVERSE));
//		rightGroup = new MotorGroup(new QuadEncoder(rightETalonSRX, WHEEL_RADIUS), rightETalonSRX,
//				new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_BACK, RIGHT_REVERSE),
//				new TalonSRX(HWR.RIGHT_DRIVE_TRAIN_MIDDLE, RIGHT_REVERSE));
		drive = new TankDrive(leftETalonSRX, rightETalonSRX, gyro);
		//leftGroup.enableAntiDrift(left);
		//rightGroup.enableAntiDrift(right);

		elevator = new Elevator(new TalonSRX(HWR.ELEVATOR, false), limitSwitch);

		pdp = new PowerDistributionPanel();
		autoSwitch = new AutonomousSwitcher(drive, accel);

		controls = new Joysticks(drive, pdp, grabberLeft, grabberRight, grabberSolenoid, elevator);
		CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void autonomousInit() {
		drive.stop();
		autoSwitch.getSelected();
	}

	@Override
	public void autonomousPeriodic() {
		autoSwitch.run();
	}

	@Override
	public void teleopInit() {
		drive.stop();
		waitTeleop.start();
	}

	@Override
	public void teleopPeriodic() {
		if (waitTeleop.get() > 0.2)
			teleopReady = true;
		if (teleopReady)
			controls.run();
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}
}