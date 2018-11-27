package engine;

import object.Ball;
import world.Point3D;
import world.Vector3D;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Player implements KeyListener, MouseMotionListener {

	private Robot robot;
	
	private static final float SENSITIVITY = 0.15f, speed0 = 1.0f, speedMult = 2, jumpHeight = 1.0f;
	private double mouseXCenter, mouseYCenter;
	private Point3D position = new Point3D(Config.INIT_PLAYER_POS);
	private Vector3D velocity = new Vector3D();
	private float ya = 0, za = 90, xMove = 0, yMove = 0, zMove = 0, speed = speed0;
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
		attachPlayerObject(playerObject);
	}
	
	public void attachPlayerObject(PlayableObject playerObject) {
		if (playerObject != null) {
			this.playerObject = playerObject;
			if (playerObject.getPlayer() != this)
				playerObject.attachPlayer(this);
		}
	}

	public void detachPlayerObject() {
		PlayableObject detached = this.playerObject;
		this.playerObject = null;
		if (detached.getPlayer() == this)
			detached.detachPlayer();

		position = detached.getAnchor();
		ya = detached.getYAngle();
		za = detached.getZAngle();
		velocity.set(0, 0, 0);
	}
	
	public PlayableObject getPlayerObject() {
		return playerObject;
	}

	public void control(float tick) {
		Vector3D velocity = getVelocity();
		double speedFactor = 1 / Math.max(1, Math.sqrt((xMove == 0 ? 0 : 1) + (yMove == 0 ? 0 : 1)));
		double yAngle = Math.toRadians(getYAngle());
		double sinY = Math.sin(yAngle);
		double cosY = Math.cos(yAngle);

		velocity.setX((float) (((cosY * xMove - sinY * yMove) * speedFactor)));
		velocity.setY((float) (((cosY * yMove + sinY * xMove) * speedFactor)));
		if (!isGravitational() || velocity.getZ() == 0)
			velocity.setZ(zMove * jumpHeight);

		setVelocity(velocity);

		if (isSpectator())
			position.move(Vector3D.product(velocity, tick));
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
				case KeyEvent.VK_R: {
					reset();
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
				case KeyEvent.VK_E: {
					double[] ap = getAngleProperties();
					Point3D point = new Point3D((float)(getCameraX()+ap[1]*ap[2]), (float)(getCameraY()+ap[1]*ap[0]), (float)(getCameraZ()-ap[3]));
					(new Ball(point, 0.25f)).setVelocity(getVelocity());
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
			double xDiff = mouseXCenter -e.getXOnScreen(), yDiff = e.getYOnScreen()- mouseYCenter;
			double yAngle = getYAngle()+xDiff*SENSITIVITY;
			double zAngle = getZAngle()-yDiff*SENSITIVITY;
			if (yAngle > 360) yAngle -= 360;
			else if (yAngle < 0) yAngle += 360;
			if (zAngle > 180) zAngle = 180;
			else if (zAngle < 0) zAngle = 0;
			setYAngle((float) yAngle);
			setZAngle((float) zAngle);
			mouseOn = false;
			robot.mouseMove((int) mouseXCenter, (int) mouseYCenter);
			mouseOn = true;
		}
	}
	
	public void setMouseCenter(double xCenter, double yCenter) {
		this.mouseXCenter = xCenter;
		this.mouseYCenter = yCenter;
	}
	
	public void noClip() {
		if (noClip) {
		} else {
			
		}
		toggleGravitational();
		noClip = !noClip;
	}
	
	public boolean isMouseOn() {
		return mouseOn;
	}

	public double[] getAngleProperties() {
		double sinY = Math.sin(Math.toRadians(getYAngle()));
		double sinZ = Math.sin(Math.toRadians(getZAngle()));
		double cosY = Math.cos(Math.toRadians(getYAngle()));
		double cosZ = Math.cos(Math.toRadians(getZAngle()));

		return new double[] {sinY, sinZ, cosY, cosZ};
	}
	
	public boolean isCameraZLocked() {
		return cameraZLocked;
	}

	public boolean isSpectator() {
		return (playerObject == null);
	}


	// playerObject

	public void reset() {
		if (isSpectator()) {
			position.moveTo(Config.INIT_PLAYER_POS);
			velocity.set(0, 0, 0);
		} else {
			playerObject.setAnchor(new Point3D(Config.INIT_PLAYER_POS));
			playerObject.stop();
		}
	}

	public float getCameraX() {
		if (isSpectator())
			return position.getX();
		else
			return playerObject.getCameraX();
	}

	public float getCameraY() {
		if (isSpectator())
			return position.getY();
		else
			return playerObject.getCameraY();
	}

	public float getCameraZ() {
		if (isSpectator())
			return position.getZ();
		else
			return playerObject.getCameraZ();
	}

	public void setYAngle(float ya) {
		if (isSpectator())
			this.ya = ya;
		else
			playerObject.setYAngle(ya);
	}

	public float getYAngle() {
		if (isSpectator())
			return ya;
		else
			return playerObject.getYAngle();
	}

	public void setZAngle(float za) {
		if (isSpectator())
			this.za = za;
		else
			playerObject.setZAngle(za);
	}

	public float getZAngle() {
		if (isSpectator())
			return za;
		else
			return playerObject.getZAngle();
	}

	public Vector3D getVelocity() {
		if (isSpectator())
			return velocity;
		else
			return playerObject.getVelocity();
	}

	public void setVelocity(Vector3D velocity) {
		if (isSpectator())
			this.velocity = velocity;
		else
			playerObject.setVelocity(velocity);
	}

	public void setVisible() {
		if (!isSpectator())
			playerObject.setVisible();
	}

	public void setInvisible() {
		if (!isSpectator())
			playerObject.setInvisible();
	}

	public void toggleGravitational() {
		if (!isSpectator())
			playerObject.toggleGravitational();
	}

	public boolean isGravitational() {
		if (isSpectator())
			return false;
		else
			return playerObject.isGravitational();
	}

	public String toString() {
		if (isSpectator())
			return "Spectator[position="+position.toString()+", ya="+ya+", za="+za+", velocity:"+velocity.toString()+"]";
		else
			return playerObject.toString();
	}
}
