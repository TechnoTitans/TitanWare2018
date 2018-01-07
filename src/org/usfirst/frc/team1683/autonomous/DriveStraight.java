package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;
import org.usfirst.frc.team1683.driveTrain.DriveTrainMover;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

import edu.wpi.first.wpilibj.Timer;

public class DriveStraight extends Autonomous{
	DriveTrain driveTrain;
	DriveTrainMover mover;
	private Timer timer;
	
	public DriveStraight(DriveTrain driveTrain) {
		super(driveTrain);
		this.driveTrain = driveTrain;
		presentState = State.INIT_CASE;
	}

	@Override
	public void run() {
		switch (presentState) {
			case INIT_CASE:
				timer = new Timer();
				timer.start();
				
				mover = new DriveTrainMover(driveTrain, 2000, 0.4);
				nextState = State.DRIVE_FORWARD;
				break;
			case DRIVE_FORWARD:
				mover.runIteration();
				if (mover.areAnyFinished()) {
					tankDrive.stop();
					nextState = State.END_CASE;
				}
				break;
			case END_CASE:
				nextState = State.END_CASE;
				break;
			default:
				break;
		}
		SmartDashboard.sendData("Auto State", presentState.toString(), true);
		SmartDashboard.sendData("Auto Timer", timer.get(), true);
		presentState = nextState;
	}
}
