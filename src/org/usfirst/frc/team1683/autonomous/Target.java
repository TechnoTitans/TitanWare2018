package org.usfirst.frc.team1683.autonomous;

public enum Target {
	CLOSE_SWITCH(0, true), FAR_SWITCH(0, false), CLOSE_SCALE(1, true), FAR_SCALE(1, false);
	private boolean isClose;
	private int switchScale;
	
	/**
	 * Creates a target for autonomous
	 * @param switchScale
	 * 			0 if it is switch, 1 if it is scale
	 * @param isClose
	 * 			true for close side, false otherwise
	 */
	private Target(int switchScale, boolean isClose) {
		this.isClose = isClose;
		this.switchScale = switchScale;
	}
	
	public int getSwitchScale() {
		return switchScale;
	}
	
	public boolean getIsClose() {
		return isClose;
	}
}
