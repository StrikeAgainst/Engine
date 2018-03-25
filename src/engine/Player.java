package engine;

import world.Point3D;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Player implements KeyListener, MouseMotionListener {

	private Robot robot;
	
	private static final float SENSITIVITY = 0.15f, speed0 = 1.0f, speedMult = 2, jumpHeight = 1.0f;
	private double xCenter, yCenter, sinY, cosY;
	private float xMove = 0, yMove = 0, zMove = 0, speed = speed0;
	private boolean cameraZLocked = false, controlsOn = true, mouseOn = true, noClip = false;
	private PlayableObject playerObject;
	
	public Player() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println("Robot could not be initialized - mouse controls not possible!");
			System.exit(0);
		}
	}
	
	public Player(PlayableObject playerObject) {
		this();
		setPlayerObject(playerObject);
	}
	
	public void setPlayerObject(PlayableObject playerObject) {
		this.playerObject = playerObject;
		if (playerObject.getPlayer() != this)
			playerObject.setPlayer(this);
	}
	
	public PlayableObject getPlayerObject() {
		return playerObject;
	}

	public void control(double tick) {
		double speedFactor = 1/Math.max(1, Math.sqrt((xMove == 0?0:1)+(yMove == 0?0:1)));
		double yAngle = Math.toRadians(playerObject.getYAngle());
		sinY = Math.sin(yAngle);
		cosY = Math.cos(yAngle);
		playerObject.getMomentum().setVX((float)(((cosY*xMove-sinY*yMove)*speedFactor)));
		playerObject.getMomentum().setVY((float)(((cosY*yMove+sinY*xMove)*speedFactor)));
		if (!playerObject.isGravitational() || playerObject.getMomentum().getVZ() == 0)
			playerObject.getMomentum().setVZ(zMove*jumpHeight);
	}

	public void keyPressed(KeyEvent e) {
		if (controlsOn)
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
				case KeyEvent.VK_CONTROL: {
					zMove = -1;
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
		if (controlsOn)
			switch (e.getKeyCode()) {
				case KeyEvent.VK_W: 
				case KeyEvent.VK_S: {
					xMove = 0;
					break;
				}
				case KeyEvent.VK_A: 
				case KeyEvent.VK_D: {
					yMove = 0;
					break;
				}
				case KeyEvent.VK_SPACE: 
				case KeyEvent.VK_CONTROL: {
					zMove = 0;
					break;
				}
				case KeyEvent.VK_SHIFT: {
					speed = speed0;
					if (xMove > 0) xMove = speed;
					break;
				}
				case KeyEvent.VK_C: {
					noClip();
					break;
				}
				case KeyEvent.VK_M: {
					mouseOn = !mouseOn;
					break;
				}
			}
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		if (controlsOn && mouseOn) {
			double xDiff = xCenter-e.getXOnScreen(), yDiff = e.getYOnScreen()-yCenter;
			double yAngle = playerObject.getYAngle()+xDiff*SENSITIVITY;
			double zAngle = playerObject.getZAngle()-yDiff*SENSITIVITY;
			if (yAngle > 360) yAngle -= 360;
			else if (yAngle < 0) yAngle += 360;
			if (zAngle > 180) zAngle = 180;
			else if (zAngle < 0) zAngle = 0;
			playerObject.setYAngle((float) yAngle);
			playerObject.setZAngle((float) zAngle);
			mouseOn = false;
			robot.mouseMove((int) xCenter, (int) yCenter);
			mouseOn = true;
		}
	}
	
	public void setCenter(double xCenter, double yCenter) {
		this.xCenter = xCenter;
		this.yCenter = yCenter;
	}
	
	public void noClip() {
		if (noClip) {
		} else {
			
		}
		playerObject.toggleGravitational();
		noClip = !noClip;
	}
	
	public void reset() {
		playerObject.setAnchor(new Point3D(Config.INIT_PLAYER_POS));
		playerObject.stop();
	}
	
	public boolean isMouseOn() {
		return mouseOn;
	}
	
	public boolean isCameraZLocked() {
		return cameraZLocked;
	}
}
