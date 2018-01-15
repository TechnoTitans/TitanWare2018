package org.usfirst.frc.team1683.driverStation;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;

public class SmartDashboard extends edu.wpi.first.wpilibj.smartdashboard.SmartDashboard {
	private static Boolean flashValue = true;
	private static Timer flashTimer;

	/**
	 * Sends the value to SmartDashboard
	 *
	 * @param key
	 *            Value name
	 * @param val
	 *            double Input value
	 * @param isForDriver
	 *            true if you want to driver to see this value
	 */
	public static void sendData(String key, double val) {
		try {
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(key, val);
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Sends the value to SmartDashboard
	 *
	 * @param key
	 *            Value name
	 * @param val
	 *            int Input value
	 * @param isForDriver
	 *            true if you want to driver to see this value
	 */
	public static void sendData(String key, int val) {
		try {
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(key, val);
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Sends the value to SmartDashboard
	 *
	 * @param key
	 *            Value name
	 * @param val
	 *            String Input value
	 * @param isForDriver
	 *            true if you want to driver to see this value
	 */
	public static void sendData(String key, String val) {
		try {
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putString(key, val);
		} catch (IllegalArgumentException e) {
		}
	}

	public static void initFlashTimer() {
		flashTimer = new Timer();
		flashTimer.start();
	}

	/**
	 * 
	 * Init before starting flash!!!!!!!! Flashing green and red message Boolean
	 * only
	 */
	public static void flash(String key, double pause) {
		if (flashTimer.get() > pause) {
			flashValue = !flashValue;
			flashTimer.reset();
		}
		try {
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putBoolean(key, flashValue);
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Sends the value to SmartDashboard
	 *
	 * @param key
	 *            Value name
	 * @param val
	 *            boolean Input value
	 * @param isForDriver
	 *            true if you want to driver to see this value
	 */
	public static void sendData(String key, boolean val) {
		try {
			edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putBoolean(key, val);
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * Sends the integer to be stored in Preferences in the roboRIO
	 *
	 * @param key
	 *            Name of the value
	 * @param val
	 *            int Value to be sent
	 */
	public static void prefInt(String key, int val) {
		Preferences.getInstance().putInt(key, val);
	}

	/**
	 * Sends the boolean to be stored in Preferences in the roboRIO
	 *
	 * @param key
	 *            Name of the value
	 * @param val
	 *            boolean Value to be sent
	 */
	public static void prefBoolean(String key, boolean val) {
		Preferences.getInstance().putBoolean(key, val);
	}

	/**
	 * Sends the String to be stored in Preferences in the roboRIO
	 *
	 * @param key
	 *            Name of the value
	 * @param val
	 *            String Value to be sent
	 */
	public static void prefString(String key, String val) {
		Preferences.getInstance().putString(key, val);
	}

	/**
	 * Sends the double to be stored in Preferences in the roboRIO
	 *
	 * @param key
	 *            Name of the value
	 * @param val
	 *            double value to be sent
	 */
	public static void prefDouble(String key, double val) {
		Preferences.getInstance().putDouble(key, val);
	}

	/*
	 * Methods to get data from the roboRIO
	 */

	/**
	 * Receives the value from roboRIO Default is false if no boolean value is
	 * found with the key
	 *
	 * @param key
	 *            Value name
	 */
	public static boolean getBoolean(String key) {
		return Preferences.getInstance().getBoolean(key, false);
	}

	/**
	 * Receives the value from roboRIO Default is "null" if no string is found
	 * with the key
	 *
	 * @param key
	 *            Value name
	 * @return null if no string is found
	 */
	public static String getString(String key) {
		return Preferences.getInstance().getString(key, null);
	}

	/**
	 * Receives the value from roboRIO Default is 0.0 if no double is found with
	 * the key
	 *
	 * @param key
	 *            Value name
	 * @return 0.0 if key is not found
	 */
	public static double getDouble(String key) {
		return Preferences.getInstance().getDouble(key, 0.0);
	}

	/**
	 * Receives the value from roboRIO Default is 0 if no integer found with the
	 * key
	 *
	 * @param key
	 *            Value name
	 * @return 0 if key is not found
	 */
	public static int getInt(String key) {
		return Preferences.getInstance().getInt(key, 0);
	}
}
