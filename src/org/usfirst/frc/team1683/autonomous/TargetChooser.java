package org.usfirst.frc.team1683.autonomous;

import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;

public class TargetChooser {
	private List<Target> priorities;
	private char startingSide;

	public TargetChooser(List<Target> priorities, char startingSide) {
		this.priorities = priorities;
		this.startingSide = startingSide;
	}

	public Target getCorrectTarget() {
		String sides = DriverStation.getInstance().getGameSpecificMessage();
		if (startingSide == 'M') {
			return Target.MIDDLE_SWITCH;
		}
		for (Target poss : priorities) {
			boolean isSameSide = sides.charAt(poss.getSwitchScale()) == startingSide;
			// if it is on the same side matches with whether the target is on
			// the same side
			if (isSameSide == poss.getIsClose()) {
				return poss;
			}
		}
		throw new Error("No target works");
	}

	public char getPosition() {
		return startingSide;
	}
}
