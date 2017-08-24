package org.usfirst.frc.team1683.autonomous;

import org.usfirst.frc.team1683.driveTrain.TankDrive;

public class DoNothing extends Autonomous {

	public DoNothing(TankDrive driveTrain) {
		super(driveTrain);
	}

	@Override
	public void run() {
		switch (presentState) {
			case INIT_CASE:
				nextState = State.END_CASE;
				break;
			case END_CASE:
				break;
			default:
				break;
		}
		presentState = nextState;
	}
}
