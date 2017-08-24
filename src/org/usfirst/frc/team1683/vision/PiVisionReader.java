package org.usfirst.frc.team1683.vision;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/*
 * 
 * Gets values from raspberry pi and returns them
 * 
 */
public class PiVisionReader {
	private NetworkTable table;
	private final String tableName = "SmartDashboard";

	private static class VisionValue {
		private double value = 0;
		private boolean receivedOne = false;

		private String cam;
		private String name;
		private NetworkTable table;
		private double sensitivity;

		VisionValue(String cam, String input, NetworkTable table, double sensitivity) {
			this.cam = cam;
			this.name = (cam + input);
			this.table = table;
			this.sensitivity = sensitivity;
		}

		void update() {
			String confidenceName = cam + "_Confidence";
			double networkValue = table.getNumber(name, -1);
			double confidence = table.getNumber(confidenceName, 0) * this.sensitivity;

			if (name.equals(confidenceName))
				confidence = 1;
			if (confidence > 0) {
				if (!receivedOne) {
					value = networkValue;
					receivedOne = true;
				}
				value += confidence * (networkValue - value);
			}
		}

		double getValue() {
			return value;
		}
	}

	private VisionValue offset1, offset2, distance1, distance2, confidence1, confidence2;
	private final String cam1 = "Cam1";
	private final String cam2 = "Cam2";

	public PiVisionReader() {
		table = NetworkTable.getTable(tableName);

		offset1 = new VisionValue(cam1, "_X_Offset", table, 1.0);
		offset2 = new VisionValue(cam2, "_X_Offset", table, 1.0);
		distance1 = new VisionValue(cam1, "_Distance", table, 0.1);
		distance2 = new VisionValue(cam2, "_Distance", table, 0.1);
		confidence1 = new VisionValue(cam1, "_Confidence", table, 0.9);
		confidence2 = new VisionValue(cam2, "_Confidence", table, 0.9);
	}

	/**
	 * 
	 * @return An offset between -0.5 and 0.5 where negative indicates target is
	 *         on the left
	 */

	public double getOffset() {
		double offsetValue1 = offset1.getValue();
		double offsetValue2 = offset2.getValue();

		double confidenceValue1 = confidence1.getValue();
		double confidenceValue2 = confidence2.getValue();

		double offset = 0.0;
		if (confidenceValue1 == 0.0 && confidenceValue2 == 0.0) {
			offset = 0.0;
		} else if (confidenceValue1 == 0.0) {
			offset = (offsetValue2 + 26.5) / 100;
		} else if (confidenceValue2 == 0.0) {
			offset = (offsetValue1 - 19) / 100;
		} else {
			offset = (offsetValue1 + offsetValue2 + 5) / 100.0;
		}
		return offset;
	}

	public double getDistanceTarget() {
		return (distance1.getValue() + distance2.getValue()) / 2;
	}

	public void update() {
		offset1.update();
		offset2.update();
		distance1.update();
		distance2.update();
		confidence1.update();
		confidence2.update();
	}

	public double getConfidence() {
		double visionConfidence = Math.max(confidence1.getValue(), confidence2.getValue());
		return visionConfidence;
	}
}
