package org.usfirst.frc.team1683.driverStation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.usfirst.frc.team1683.driveTrain.FollowPath;
import org.usfirst.frc.team1683.driveTrain.PathPoint;

public class Draw extends JPanel {
	private static final long serialVersionUID = 1L;
	private static int WIDTH = 500;
	private static int HEIGHT = 500;
	private int scale = 2;

	private static ArrayList<PathPoint> points = new ArrayList<PathPoint>();
	private static ArrayList<Double> slopes = new ArrayList<>(Arrays.asList(999.00));
	private static ArrayList<Boolean> directions = new ArrayList<>(Arrays.asList(true));
	private static ArrayList<Boolean> curves = new ArrayList<Boolean>();

	private int[] currentCenter = { -10, -10 };
	private int currentRadius = 0;

	public Draw() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					points.add(new PathPoint(gToP(e.getX(), true), gToP(e.getY(), false)));
					if (points.size() >= 2)
						update(points.get(points.size() - 2), points.get(points.size() - 1));
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					if (points.size() >= 1) {
						currentCenter[0] = pToG(FollowPath.calCenter(points.get(points.size() - 1),
								new PathPoint(gToP(e.getX(), true), gToP(e.getY(), false)),
								slopes.get(slopes.size() - 1))[0], true);
						currentCenter[1] = pToG(FollowPath.calCenter(points.get(points.size() - 1),
								new PathPoint(gToP(e.getX(), true), gToP(e.getY(), false)),
								slopes.get(slopes.size() - 1))[1], false);
						currentRadius = (int) (scale * FollowPath.calRadius(points.get(points.size() - 1),
								new PathPoint(gToP(e.getX(), true), gToP(e.getY(), false)),
								slopes.get(slopes.size() - 1)));
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					currentCenter[0] = -10;
					currentCenter[1] = -10;
					currentRadius = 0;
				}
			}
		});
		// addMouseMotionListener(new MouseInputAdapter() {
		// public void mouseMoved(MouseEvent e) {
		// super.mouseMoved(e);
		// System.out.println(e.getX() / scale + "," + (HEIGHT - e.getY()) /
		// scale);
		// }
		// });
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		for (int i = 0; i < WIDTH; i += scale * 10) {
			g.drawLine(i, 0, i, HEIGHT);
		}
		for (int i = 0; i < HEIGHT; i += scale * 10) {
			g.drawLine(0, i, WIDTH, i);
		}
		g.setColor(Color.GREEN);
		for (int i = 0; i < points.size(); i++) {
			g.fillOval(pToG(points.get(i).getX(), true) - 5, pToG(points.get(i).getY(), false) - 5, 10, 10);
			if (i < points.size() - 1) {
				g.setColor(Color.YELLOW);
				int centerX = (int) FollowPath.calCenter(points.get(i), points.get(i + 1), slopes.get(i))[0];
				int centerY = (int) FollowPath.calCenter(points.get(i), points.get(i + 1), slopes.get(i))[1];
				int radius = (int) FollowPath.calRadius(points.get(i), points.get(i + 1), slopes.get(i));
				int degreeStart = (int) Math
						.toDegrees(FollowPath.atan2(new PathPoint(centerX, centerY), points.get(i)));
				int degreeDelta = (int) Math
						.toDegrees(PathPoint.getAngleTwoPoints(points.get(i), points.get(i + 1), radius));
				g.drawArc(pToG(centerX, true) - scale * radius, pToG(centerY, false) - scale * radius,
						2 * scale * radius, 2 * scale * radius, degreeStart, (curves.get(i) ? -1 : 1) * degreeDelta);
			}
		}
		g.setColor(Color.RED);
		if (currentCenter.length != 0) {
			g.drawOval(currentCenter[0] - currentRadius - 5, currentCenter[1] - currentRadius - 5, 2 * currentRadius,
					2 * currentRadius);
		}
		repaint();
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame("Draw Robot Path");
		frame.getContentPane().add(new Draw(), BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				for (PathPoint point : points) {
					System.out.println("pathPoints.add(new PathPoint(" + point.getX() + "," + point.getY() + "));");
				}
			}
		});
	}

	private void update(PathPoint currPoint, PathPoint nexPoint) {
		curves.add(FollowPath.curveDirection(currPoint, nexPoint, slopes.get(slopes.size() - 1),
				directions.get(directions.size() - 1)));
		directions.add(FollowPath.robotDirection(currPoint, nexPoint, slopes.get(slopes.size() - 1),
				curves.get(curves.size() - 1)));
		slopes.add((FollowPath.calCenter(currPoint, nexPoint, slopes.get(slopes.size() - 1))[0] - nexPoint.getX())
				/ (nexPoint.getY() - FollowPath.calCenter(currPoint, nexPoint, slopes.get(slopes.size() - 1))[1]));
	}

	private int gToP(double input, boolean isX) {
		if (isX)
			return (int) input / scale;
		return (int) (HEIGHT - input) / scale;
	}

	private int pToG(double input, boolean isX) {
		if (isX)
			return (int) (scale * input);
		return (int) (HEIGHT - scale * input);
	}
}