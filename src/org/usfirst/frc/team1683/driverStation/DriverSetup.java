package org.usfirst.frc.team1683.driverStation;

import org.usfirst.frc.team1683.constants.HWR;

import edu.wpi.first.wpilibj.Joystick;

/**
 * 
 * Contains joysticks and the axises of rotation
 *
 */
public class DriverSetup {
	// the axis the joystick could be rotated
	public static final int XAxis = 0;
	public static final int YAxis = 1;
	public static final int ZAxis = 2;
	// small wheel located near base of joytick
	public static final int dialAxis = 3;
	// joysticks
	public static Joystick leftStick = new Joystick(HWR.LEFT_JOYSTICK);
	public static Joystick rightStick = new Joystick(HWR.RIGHT_JOYSTICK);
	public static Joystick auxStick = new Joystick(HWR.AUX_JOYSTICK);
}
