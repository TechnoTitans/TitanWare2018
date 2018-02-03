package org.usfirst.frc.team1683.constants;

public class HWR {

	// Motors
	public static final int RIGHT_DRIVE_TRAIN_FRONT = HWP.CAN_7;
	public static final int RIGHT_DRIVE_TRAIN_MIDDLE = HWP.CAN_8;
	public static final int RIGHT_DRIVE_TRAIN_BACK = HWP.CAN_9;
	public static final int LEFT_DRIVE_TRAIN_FRONT = HWP.CAN_6;
	public static final int LEFT_DRIVE_TRAIN_MIDDLE = HWP.CAN_5;
	public static final int LEFT_DRIVE_TRAIN_BACK = HWP.CAN_4;
	
	public static final int ELEVATOR_FOLLOW = HWP.CAN_2;
	public static final int ELEVATOR_MAIN = HWP.CAN_1;

	// Solenoids
	public static final int PCM = HWP.CAN_15;
	public static final int SOLENOID = HWP.PCM_0;
	public static final int GRABBER_SOLENOID = HWP.PCM_1;
	
	// Encoders
	public static final int LEFT_DRIVE_ENCODER = HWP.CAN_7;
	public static final int RIGHT_DRIVE_ENCODER = HWP.CAN_6;

	// Joysticks
	public static final int LEFT_JOYSTICK = HWP.JOY_0;
	public static final int RIGHT_JOYSTICK = HWP.JOY_1;
	public static final int AUX_JOYSTICK = HWP.JOY_2;
	public static final int XBOX = HWP.XBOX_0;
	
	//Flywheels
	public static final int GRABBER_LEFT = HWP.CAN_1;
	public static final int GRABBER_RIGHT = HWP.CAN_2;
	
	//Piston
	public static final int GRABBER_PISTON = HWP.CAN_3;

	// Sensors
	public static final int GYRO = HWP.ANALOG_1;
	public static final int LIMIT_SWITCH_TOP = HWP.DIO_0;
	public static final int LIMIT_SWITCH_BOTTOM = HWP.DIO_1;

	// Joystick Buttons
	public static final int FULL_POWER = HWP.BUTTON_11;
	public static final int SECOND_POWER = HWP.BUTTON_6;
	public static final int OVERRIDE_TIMER = HWP.BUTTON_9;
	
	public static final int FIRE_SOLENOID = HWP.BUTTON_3;
	public static final int OVERRIDE_LIMIT = HWP.BUTTON_9;
}