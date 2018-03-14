package org.usfirst.frc.team1683.autonomous;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team1683.driveTrain.TankDrive;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;
import org.usfirst.frc.team1683.motor.TalonSRX;
import org.usfirst.frc.team1683.scoring.Elevator;
import org.usfirst.frc.team1683.sensors.BuiltInAccel;
import org.usfirst.frc.team1683.sensors.Gyro;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class AutonomousSwitcher {
	public Autonomous autoSelected;

	public SendableChooser<Autonomous> chooser;

	private List<SendableChooser<Target>> priorities;
	private SendableChooser<Character> side;
	Gyro gyro;

	// Creates buttons for co driver to pick autonomous
	public AutonomousSwitcher(TankDrive tankDrive, Elevator elevator, TalonSRX grabberLeft, TalonSRX grabberRight,
			BuiltInAccel accel) {
		chooser = new SendableChooser<Autonomous>();
		
		SingleTarget singleTarget = new SingleTarget(tankDrive, elevator, grabberLeft, grabberRight);
		addAuto("Do Nothing", new DoNothing(tankDrive));
		addAuto("Square Auto", new SquareAuto(tankDrive));
		addAuto("Single Target", singleTarget);
		addAuto("Double Target", new DoubleTarget(singleTarget, tankDrive, elevator, grabberLeft, grabberRight));
		setDefault("Drive Straight", new DriveStraight(tankDrive));
		SmartDashboard.putData("Auto", chooser);

		priorities = new ArrayList<>();
		Target[] possTargets = Target.values();
		for (int i = 0; i < 3; ++i) {
			SendableChooser<Target> p = new SendableChooser<Target>();
			p.addDefault("Choose priority " + (i + 1) + " (None)", null);
			for (Target target : possTargets) {
				if (!target.isStartMiddle() || i == 0) {
					p.addObject(target.toString(), target);
				}
			}
			priorities.add(p);
			SmartDashboard.putData("Priority " + (i + 1), p);
		}

		side = new SendableChooser<Character>();
		side.addDefault("Left", 'L');
		side.addObject("Middle Right", 'M');
		side.addObject("Right", 'R');
		SmartDashboard.putData("Side", side);
	}

	private List<Target> getPriorities() {
		List<Target> targets = new ArrayList<Target>();
		for (SendableChooser<Target> p : priorities) {
			if (p.getSelected() != null) {
				targets.add(p.getSelected());
			}
		}
		return targets;
	}

	// When chooser is displayed, this autonomous is autonomatically selected
	public void setDefault(String name, Autonomous auto) {
		chooser.addDefault(name, auto);
	}

	public void addAuto(String name, Autonomous auto) {
		chooser.addObject(name, auto);
	}

	public void getSelected() {
		autoSelected = chooser.getSelected();
		if (autoSelected instanceof ChoosesTarget) {
			ChoosesTarget target = (ChoosesTarget) autoSelected;
			target.setChooser(new TargetChooser(getPriorities(), side.getSelected()));
		}
		autoSelected.resetAuto();
	}

	public void run() {
		autoSelected.run();
	}
}
