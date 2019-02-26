package engine;

import core.Point3;
import core.Quaternion;
import core.Vector3;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Player implements KeyListener, MouseMotionListener {

	private Robot robot;
	private static final float SENSITIVITY = 0.15f, walk_speed = 1.0f, sprint_speed = 2.0f, jump_force = 1.0f;

	private double mouseXCenter, mouseYCenter;
	private Point3 position = new Point3(Config.INIT_PLAYER_POS);
	private Vector3 velocity = new Vector3();
	private float cameraYaw = 0, cameraPitch = 0, xMove = 0, yMove = 0, zMove = 0;
	private double sinYaw = Math.sin(cameraYaw), sinPitch = Math.sin(cameraPitch), cosYaw = Math.cos(cameraYaw), cosPitch = Math.cos(cameraPitch);
	private boolean controlsEnabled = true, mouseControlsEnabled = true, zeroGravityControlsEnabled = true, mouseYInverted = false, noClip = false, sprinting = false;
	private PlayableObject playerObject, buffer;
	private Perspective perspective;
	
	public Player() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println("Robot could not be initialized - mouse controls not possible!");
			System.exit(0);
		}
	}

	public Player(Perspective perspective) {
		this();
		this.perspective = perspective;
	}

	public Player(PlayableObject playerObject) {
		this();
		attachPlayerObject(playerObject);
	}

	public Player(PlayableObject playerObject, Perspective perspective) {
		this();
		this.perspective = perspective;
		attachPlayerObject(playerObject);
	}
	
	public void attachPlayerObject(PlayableObject playerObject) {
		if (playerObject != null) {
			this.playerObject = playerObject;
			if (playerObject.getPlayer() != this)
				playerObject.attachPlayer(this);

			setCameraYaw(playerObject.getOrientation().getTwist(new Vector3(0, 0, 1)).toAngle());
			//setCameraPitch(0);
		}
	}

	public void detachPlayerObject() {
		if (this.playerObject != null) {
			this.playerObject.setVisible();
			PlayableObject detached = this.playerObject;
			this.playerObject = null;
			if (detached.getPlayer() == this)
				detached.detachPlayer();

			position.set(detached.getPosition());
			velocity.nullify();
		}
	}
	
	public PlayableObject getPlayerObject() {
		return playerObject;
	}

	public void update(float tick) {
		control(tick);
	}

	public void control(float tick) {
		Vector3 velocity = getVelocity();
		float speed = ((sprinting && xMove > 0) ? sprint_speed : walk_speed);
		double rootFactor = 1 / Math.max(1, Math.sqrt((xMove == 0 ? 0 : 1) + (yMove == 0 ? 0 : 1)));
		double speedFactor = rootFactor * speed;

		float x = (float) ((cosYaw * xMove - sinYaw * yMove) * speedFactor);
		float y = (float) ((cosYaw * yMove + sinYaw * xMove) * speedFactor);
		float z = velocity.getZ();

		if (isGravitated()) {
			if (playerObject.hasRestingContact())
				z = zMove * jump_force;
		} else if (zeroGravityControlsEnabled)
			z = zMove * speed;

		velocity.set(new Vector3(x, y, z));

		if (isSpectator())
			position.add(velocity.scaled(tick));
	}

	public void keyPressed(KeyEvent e) {
		if (controlsEnabled)
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
					sprinting = true;
					break;
				}
				case KeyEvent.VK_E: {
					Point3 point = position.offset(getLookingVector());
					ObjectFactory.createBall(point, 0.5f, 1f).setVelocity(getVelocity());
					break;
				}
				case KeyEvent.VK_T: {
					if (isSpectator())
						attachPlayerObject(buffer);
					else {
						buffer = playerObject;
						detachPlayerObject();
					}
					break;
				}
			}
	}

	public void keyReleased(KeyEvent e) {
		if (controlsEnabled)
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
					sprinting = false;
					break;
				}
				case KeyEvent.VK_M: {
					mouseControlsEnabled = !mouseControlsEnabled;
					break;
				}
			}
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		if (controlsEnabled && mouseControlsEnabled) {
			float xDiff = (float) (mouseXCenter-e.getXOnScreen());
			float yDiff = (float) (e.getYOnScreen()-mouseYCenter);

			yaw(xDiff*SENSITIVITY, false);
			pitch(yDiff*SENSITIVITY*(mouseYInverted?-1:1), false);

			if (!isSpectator())
				playerObject.setOrientation(Quaternion.fromYaw(cameraYaw));
				//playerObject.setOrientation(Quaternion.fromYawAndPitch(cameraYaw, cameraPitch));

			mouseControlsEnabled = false;
			robot.mouseMove((int) mouseXCenter, (int) mouseYCenter);
			mouseControlsEnabled = true;
		}
	}
	
	public void setMouseCenter(double xCenter, double yCenter) {
		this.mouseXCenter = xCenter;
		this.mouseYCenter = yCenter;
	}
	
	public boolean mouseControlsEnabled() {
		return mouseControlsEnabled;
	}

	public boolean isSpectator() {
		return (playerObject == null);
	}

	public void nextPerspective() {
		this.perspective = this.perspective.next();
	}

	public Perspective getPerspective() {
		if (isSpectator())
			return Perspective.spectator;
		else
			return this.perspective;
	}

	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}


	// playerObject

	public void reset() {
		if (isSpectator()) {
			position.set(Config.INIT_PLAYER_POS);
			velocity.nullify();
			setCameraYaw(0);
			setCameraPitch(0);
		} else {
			playerObject.setPosition(new Point3(Config.INIT_PLAYER_POS));
			playerObject.stop();
		}
	}

	public Point3 getCameraPosition() {
		if (isSpectator())
			return position;
		else
			return playerObject.getCameraPosition();
	}

	public Vector3 getLookingVector() {
		return new Vector3((float)(cosPitch*cosYaw), (float)(cosPitch*sinYaw), (float)(-sinPitch));
	}

	public Vector3 getUpVector() {
		double camUp = cameraPitch-Physics.R90;
		double sinUp = Math.sin(camUp);
		double cosUp = Math.cos(camUp);

		return new Vector3((float)(cosUp*cosYaw), (float)(cosUp*sinYaw), (float)(-sinUp));
	}

	public void yaw(float angle, boolean isRadian) {
		if (!isRadian)
			angle = (float) Math.toRadians(angle);

		setCameraYaw(cameraYaw+angle);
	}

	public void setCameraYaw(float yaw) {
		cameraYaw = Physics.boundAngleFull(yaw);
		sinYaw = Math.sin(cameraYaw);
		cosYaw = Math.cos(cameraYaw);
	}

	public float getCameraYaw() {
		return cameraYaw;
	}

	public void pitch(float angle, boolean isRadian) {
		if (!isRadian)
			angle = (float) Math.toRadians(angle);

		setCameraPitch(cameraPitch+angle);
	}

	public void setCameraPitch(float pitch) {
		cameraPitch = Physics.boundAngleHalf(pitch);
		sinPitch = Math.sin(cameraPitch);
		cosPitch = Math.cos(cameraPitch);
	}

	public float getCameraPitch() {
		return cameraPitch;
	}

	public Point3 getPosition() {
		if (isSpectator())
			return position;
		else
			return playerObject.getPosition();
	}

	public void setPosition(Point3 position) {
		if (isSpectator())
			this.position = position;
		else
			playerObject.setPosition(position);
	}

	public Quaternion getOrientation() {
		if (isSpectator())
			return Quaternion.fromYawAndPitch(cameraYaw, cameraPitch);
		else
			return playerObject.getOrientation();
	}

	public void setOrientation(Quaternion orientation) {
		if (isSpectator()) {
			Quaternion yawtwist = orientation.getYawTwist();
			setCameraYaw(yawtwist.toAngle());
			setCameraPitch(orientation.getSwing(yawtwist).toAngle());
		} else
			playerObject.setOrientation(orientation);
	}

	public Vector3 getVelocity() {
		if (isSpectator())
			return velocity;
		else
			return playerObject.getVelocity();
	}

	public void setVelocity(Vector3 velocity) {
		if (isSpectator())
			this.velocity = velocity;
		else
			playerObject.setVelocity(velocity);
	}

	public boolean isGravitated() {
		if (isSpectator())
			return false;
		else
			return playerObject.isGravitated();
	}

	public void setVisible() {
		if (!isSpectator())
			playerObject.setVisible();
	}

	public void setInvisible() {
		if (!isSpectator())
			playerObject.setInvisible();
	}

	public String toString() {
		if (isSpectator())
			return "Spectator[position="+position.toString()+", cameraYaw="+ cameraYaw +", cameraPitch="+ cameraPitch +", velocity:"+velocity.toString()+"]";
		else
			return playerObject.toString();
	}
}
