package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonomousSwitcher {
	public Autonomous autoSelected;

	public SendableChooser<Autonomous> chooser;
	private TargetChooser targetChooser;

	BuiltInAccel accel;
	Gyro gyro;

	// Creates buttons for co driver to pick autonomous
	public AutonomousSwitcher(TankDrive tankDrive) {
		chooser = new SendableChooser<Autonomous>();
//		targetChooser = new TargetChooser(
//				new Target[] { Target.CLOSE_SWITCH, Target.CLOSE_SCALE, Target.FAR_SWITCH, Target.FAR_SCALE }, 'L');

		addAuto("Do Nothing", new DoNothing(tankDrive));
		addAuto("Square Auto", new SquareAuto(tankDrive));
		addAuto("Drive Straight", new DriveStraight(tankDrive));
//		setDefault("Priority (Single)", new SingleTarget(tankDrive, targetChooser));
		SmartDashboard.putData("Auto", chooser);
	}

	// When chooser is displayed, this autonomous is autonomatically selected
	public void setDefault(String name, Autonomous auto) {
		chooser.addDefault(name, auto);
	}

	// Adds auto to chooser only if it is for competition
	public void addAuto(String name, Autonomous auto) {
		chooser.addObject(name, auto);
	}

	public void getSelected() {
		autoSelected = (Autonomous) chooser.getSelected();
	}

	public void run() {
		autoSelected.run();
	}
}
