package engine;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Player implements KeyListener, MouseMotionListener {

	private Robot robot;
	
	private final float sensitivity = 0.15f, speed0 = 1.0f, speedMult = 2; 
	private float jumpHeight = 3.0f;
	private double xCenter, yCenter, sinY, cosY;
	private float xMove = 0, yMove = 0, zMove = 0, speed = speed0;
	private boolean controlsOn = true, mouseOn = true;
	private PlayablePhysicsObject appliedObject;
	
	public Player(PlayablePhysicsObject apply) {
		this.appliedObject = apply;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println("Robot could not be initialized - mouse controls not possible!");
			System.exit(0);
		}
	}
	
	public PlayablePhysicsObject getAppliedObject() {
		return appliedObject;
	}

	public void move(double period) {
		if (!appliedObject.isAirborne()) {
			sinY = Math.sin(Math.toRadians(appliedObject.getYAngle()));
			cosY = Math.cos(Math.toRadians(appliedObject.getYAngle()));
			appliedObject.setVX((float)(((cosY*xMove-sinY*yMove))));
			appliedObject.setVY((float)(((cosY*yMove+sinY*xMove))));
			appliedObject.setVZ(zMove*jumpHeight);
			if (xMove != 0 && yMove != 0) {
				appliedObject.setVX((float)(appliedObject.getVX()/Math.sqrt(2)));
				appliedObject.setVY((float)(appliedObject.getVY()/Math.sqrt(2)));
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W: {
				xMove = 1;
				break;
			}
			case KeyEvent.VK_S: {
				xMove = -1;
				break;
			}
			case KeyEvent.VK_A: {
				yMove = 1;
				break;
			}
			case KeyEvent.VK_D: {
				yMove = -1;
				break;
			}
			case KeyEvent.VK_SPACE: {
				zMove = 1;
				break;
			}
			case KeyEvent.VK_SHIFT: {
				speed = speedMult*speed0;
				if (xMove > 0) xMove = speed;
				break;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (controlsOn) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_W: {
					xMove = 0;
					break;
				}
				case KeyEvent.VK_S: {
					xMove = 0;
					break;
				}
				case KeyEvent.VK_A: {
					yMove = 0;
					break;
				}
				case KeyEvent.VK_D: {
					yMove = 0;
					break;
				}
				case KeyEvent.VK_SPACE: {
					zMove = 0;
					break;
				}
				case KeyEvent.VK_SHIFT: {
					speed = speed0;
					if (xMove > 0) xMove = speed;
					break;
				}
				case KeyEvent.VK_M: {
					mouseOn = !mouseOn;
					break;
				}
			}
		}
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		if (controlsOn && mouseOn) {
			double xDiff = xCenter-e.getXOnScreen(), yDiff = e.getYOnScreen()-yCenter;
			double yAngle = appliedObject.getYAngle()+xDiff*sensitivity;
			double zAngle = appliedObject.getZAngle()-yDiff*sensitivity;
			if (yAngle > 360) yAngle -= 360;
			else if (yAngle < 0) yAngle += 360;
			if (zAngle > 180) zAngle = 180;
			else if (zAngle < 0) zAngle = 0;
			appliedObject.setYAngle((float) yAngle);
			appliedObject.setZAngle((float) zAngle);
			mouseOn = false;
			robot.mouseMove((int) xCenter, (int) yCenter);
			mouseOn = true;
		}
	}
	
	public void setCenter(double xCenter, double yCenter) {
		this.xCenter = xCenter;
		this.yCenter = yCenter;
	}
	
	public boolean isMouseOn() {
		return mouseOn;
	}
}
