package org.usfirst.frc.team1683.constants;

public class HWR {

	// Motors
	public static final int RIGHT_DRIVE_TRAIN_FRONT = HWP.CAN_11;
	public static final int RIGHT_DRIVE_TRAIN_MIDDLE = HWP.CAN_12;
	public static final int RIGHT_DRIVE_TRAIN_BACK = HWP.CAN_13;
	public static final int LEFT_DRIVE_TRAIN_FRONT = HWP.CAN_5;
	public static final int LEFT_DRIVE_TRAIN_MIDDLE = HWP.CAN_4;
	public static final int LEFT_DRIVE_TRAIN_BACK = HWP.CAN_3;
	
	public static final int ELEVATOR_FOLLOW = HWP.CAN_6;
	public static final int ELEVATOR_MAIN = HWP.CAN_10;

	// Solenoids
	public static final int PCM = HWP.CAN_15;
	public static final int GRABBER_SOLENOID = 0;
	
	// Encoders
//	public static final int LEFT_DRIVE_ENCODER = HWP.CAN_7;
//	public static final int RIGHT_DRIVE_ENCODER = HWP.CAN_6;

	// Joysticks
	public static final int LEFT_JOYSTICK = HWP.JOY_0;
	public static final int RIGHT_JOYSTICK = HWP.JOY_1;
	public static final int AUX_JOYSTICK = HWP.JOY_2;
	public static final int XBOX = HWP.XBOX_2;
	
	//Flywheels
	public static final int GRABBER_LEFT = HWP.CAN_7;
	public static final int GRABBER_RIGHT = HWP.CAN_9;
	
	//Piston
	public static final int GRABBER_PISTON = HWP.CAN_2;

	// Sensors
	public static final int GYRO = HWP.ANALOG_1;
	public static final int LIMIT_SWITCH_TOP = HWP.DIO_1;
	public static final int LIMIT_SWITCH_BOTTOM = HWP.DIO_0;

	// Joystick Buttons
	public static final int FULL_POWER = HWP.BUTTON_11;
	public static final int SECOND_POWER = HWP.BUTTON_6;
	public static final int OVERRIDE_TIMER = HWP.BUTTON_9;
	public static final int INVERT_CONTROLS = HWP.BUTTON_7;
	public static final int REGULAR_CONTROLS = HWP.BUTTON_7;
	
	public static final int FIRE_SOLENOID = HWP.BUTTON_3;
	public static final int OVERRIDE_LIMIT = HWP.BUTTON_9;
}