package engine;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;

import object.Ball;
import object.Pawn;
import object.Tile;
import object.Wall;
import world.Octree;
import world.Point3D;

public class Engine extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Animator animator = null;
	
	public Engine() {
		super("Maze Chaos");

		GLCapabilities cap = new GLCapabilities(null);
		cap.setDoubleBuffered(true);
		WinRenderer canvas = new WinRenderer(cap);
		canvas.addGLEventListener(canvas);
		animator = new Animator(canvas);
		animator.setUpdateFPSFrames(120, System.out);

		this.add(canvas);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
		        animator.stop();
				System.exit(0);
			}
		});
		this.setSize(800, 600);
		this.setVisible(true);
		animator.start();
		canvas.requestFocus();
	}
	
	public static void main(String[] args) {
		new Engine();
	}
	
	static class WinRenderer extends GLCanvas implements GLEventListener, KeyListener {

		private GL2 gl;
		private GLU glu;
		private GLUT glut;
		private TextRenderer renderer = new TextRenderer(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		private Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
		private enum CamMode {FP, TP, BIRD}
		private CamMode cm;
		
		private final int boardSize = 12;
		private final float panelSize = 0.5f;
		private double camX, camY, camZ, lookX, lookY, lookZ, distance = 0.6f;
		private double zRad, sinY, sinZ, cosY, cosZ, uSinZ, uCosZ;
		private float yAngle, zAngle;
		private boolean pause = false;
		private long newTick, lastTick = System.currentTimeMillis();
		private Random random = new Random();
		private ObjectContainer container = ObjectContainer.get();
		private ArrayList<Collision> collisions;
		private Octree octree = Octree.get();
		private Player player;
		private GLAnimatorControl animator;

		public WinRenderer(GLCapabilities cap) {
			super(cap);
			glu = new GLU();
			glut = new GLUT();
			cm = CamMode.FP;
			player = new Player(new Pawn(new Point3D(Config.INIT_PLAYER_POS),0.4f,0.1f));
			new Tile(new Point3D(0.25f, 0.25f, 0.0f), panelSize, 0, 0, 0.5f);
			new Wall(new Point3D(0, 0.25f, 0.4f), panelSize, 0.8f, false);
			new Wall(new Point3D(0.25f, 0, 0.4f), panelSize, 0.8f, true);
			//initMaze(boardSize);
            this.addKeyListener(this);
            this.addKeyListener(player);
            this.addMouseMotionListener(player);
		}
		
		public void init(GLAutoDrawable gLDrawable) {
			gl = gLDrawable.getGL().getGL2();
            gl.glEnable(GL.GL_DEPTH_TEST);
            //gl.glEnable(GL2.GL_LIGHTING);
            //gl.glEnable(GL2.GL_COLOR_MATERIAL);
            //gl.glEnable(GL2.GL_NORMALIZE);
            //gl.glShadeModel(GL2.GL_SMOOTH);
            
            animator = gLDrawable.getAnimator();
		}

		public void display(GLAutoDrawable gLDrawable) {
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
			gl.glLoadIdentity();
			
			if (player.isMouseOn()) this.setCursor(blankCursor);	
			else this.setCursor(Cursor.getDefaultCursor());
            player.setCenter(this.getLocationOnScreen().getX()+this.getSize().getWidth()/2, this.getLocationOnScreen().getY()+this.getSize().getHeight()/2);

			yAngle = player.getPlayerObject().getYAngle();
			zAngle = player.getPlayerObject().getZAngle();
			sinY = Math.sin(Math.toRadians(yAngle));
			sinZ = Math.sin(Math.toRadians(zAngle));
			cosY = Math.cos(Math.toRadians(yAngle));
			cosZ = Math.cos(Math.toRadians(zAngle));
			
			switch(cm) {
				case FP: {
					player.getPlayerObject().setInvisible();
					camX = player.getPlayerObject().getCamX();
					camY = player.getPlayerObject().getCamY();
					camZ = player.getPlayerObject().getCamZ();
					lookX = camX+sinZ*cosY;
					lookY = camY+sinZ*sinY;
					lookZ = camZ-cosZ;
					uSinZ = Math.sin(Math.toRadians(zAngle+90));
					uCosZ = Math.cos(Math.toRadians(zAngle+90));
					break;
				}
				case TP: {
					player.getPlayerObject().setVisible();
					lookX = player.getPlayerObject().getCamX();
					lookY = player.getPlayerObject().getCamY();
					lookZ = player.getPlayerObject().getCamZ();
					camX = lookX-sinZ*cosY*distance;
					camY = lookY-sinZ*sinY*distance;
					camZ = (player.isCameraZLocked()?Math.max(0.1f, lookZ+cosZ*distance):lookZ+cosZ*distance);
					uSinZ = Math.sin(Math.toRadians(zAngle+90));
					uCosZ = Math.cos(Math.toRadians(zAngle+90));
					break;
				}
				case BIRD: {
					player.getPlayerObject().setVisible();
					zRad = Math.toRadians(15);
					lookX = boardSize*panelSize/2;
					lookY = boardSize*panelSize/2;
					lookZ = 0;
					camX = lookX+Math.sin(zRad)*boardSize/2;
					camY = lookY;
					camZ = lookZ+Math.cos(zRad)*boardSize/2;
					uSinZ = 0;
					uCosZ = -1;
					break;
				}
				default: {}
			}
			glu.gluLookAt(camX, camY, camZ, lookX, lookY, lookZ, uSinZ*cosY, uSinZ*sinY, -uCosZ);

			for (EngineObject e : container) {
				e.drawObject(gl, glut);
			}
			octree.draw(gl, glut);
			
			newTick = System.currentTimeMillis();
			double tick = (double)(newTick-lastTick)/1000;
			lastTick = newTick;
			if (!pause) {
				player.control(tick);
				for (EngineObject e : container) {
					e.update(tick);
				}
				ArrayList<EngineObject> outsiders = octree.update();
				for (EngineObject outsider : outsiders) {
					if (outsider == player.getPlayerObject()) {
						player.reset();
						octree.insertNode(outsider);
					} else
						outsider.destroy();
				}
				collisions = octree.detectCollisions();
				for (Collision c : collisions)
					c.resolve();
				for (EngineObject e : container) {
					if (e instanceof PhysicalObject) {
						((PhysicalObject)e).move();
					}
				}
				System.out.println(container.size());
			}

			float center = boardSize*panelSize/2;
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] {0.2f, 0.2f, 0.2f, 0.2f}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {0.3f, 0.3f, 0.3f, 0.3f}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {center, center, 2.0f, 0.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, new float[] {90}, 0);
			gl.glEnable(GL2.GL_LIGHT0);

		    int line = 0, spacing = 16;
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
			renderer.beginRendering(gLDrawable.getSurfaceWidth(), gLDrawable.getSurfaceHeight());
		    renderer.setColor(0.0f, 1.0f, 0.0f, 0.8f);
		    renderer.draw("FPS: "+animator.getLastFPS(), 5, gLDrawable.getSurfaceHeight()-spacing*(++line));
			renderer.draw("Player: "+player.getPlayerObject().toString(), 5, gLDrawable.getSurfaceHeight()-spacing*(++line));
		    renderer.endRendering();

			gl.glFlush();
		}
		
		public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {}

		public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPerspective(60, 4.0f/3.0f, 0.01, 10);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
		}
		
		public void dispose(GLAutoDrawable drawable) {}

		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_V: {
					switch (cm) {
						case FP: {
							cm = CamMode.TP;
							break;
						} 
						case TP: {
							cm = CamMode.BIRD;
							break;
						}
						case BIRD: {
							cm = CamMode.FP;
							break;
						}
					}
					break;
				}
				case KeyEvent.VK_E: {
					Point3D point = new Point3D((float)(player.getPlayerObject().getCamX()+sinZ*cosY), (float)(player.getPlayerObject().getCamY()+sinZ*sinY), (float)(player.getPlayerObject().getCamZ()-cosZ));
					(new Ball(point, 0.25f)).setMomentum(player.getPlayerObject().getMomentum());
					break;
				}
				case KeyEvent.VK_R: {
					player.reset();
					break;
				}
				case KeyEvent.VK_G: {
					player.getPlayerObject().toggleGravitational();
					break;
				}
				case KeyEvent.VK_B: {
					EngineObject.toggleShowBounding();
					Octree.toggleShowBounding();
					break;
				}
				case KeyEvent.VK_P: {
					pause = !pause;
					break;
				}
				case KeyEvent.VK_Q: {
					System.exit(0);
				}
			}
		}
		
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
		
		public void initMaze(int size) {
			float x, y, half = panelSize/2, wallHeight = 0.8f;
			Maze m = Maze.createRandomMaze(size);
			for (int i = 0; i < size; i++) {
				x = i*panelSize;
				for (int j = 0; j < size; j++) {
					y = j*panelSize;
					
					if (i == 0 && j == 0)
						new Tile(new Point3D(x+half, y+half, 0.0f), panelSize, 0, 0, 0.5f);
					else if (i == size-1 && j == size-1)
						new Tile(new Point3D(x+half, y+half, 0.0f), panelSize, 0, 0.5f, 0);
					else if (random.nextInt(size) != 0)
						new Tile(new Point3D(x+half, y+half, 0.0f), panelSize);
					
					if (m.getNorth()[j+1][i+1] && (i == 0 || !m.getSouth()[j+1][i]))
						new Wall(new Point3D(x, y+half, wallHeight/2), panelSize, wallHeight, false);
					if (m.getSouth()[j+1][i+1])
						new Wall(new Point3D(x+panelSize, y+half, wallHeight/2), panelSize, wallHeight, false);
					if (m.getWest()[j+1][i+1] && (j == 0 || !m.getEast()[j][i+1]))
						new Wall(new Point3D(x+half, y, wallHeight/2), panelSize, wallHeight, true);
					if (m.getEast()[j+1][i+1])
						new Wall(new Point3D(x+half, y+panelSize, wallHeight/2), panelSize, wallHeight, true);
				}
			}
		}
	}
}
