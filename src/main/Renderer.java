package main;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Point3;
import core.RGB;
import engine.Engine;
import engine.Player;
import engine.collision.ObjectContact;
import engine.collision.bounding.BroadPhase;
import engine.collision.bounding.ObjectBounding;
import engine.collision.bounding.OctreeBounding;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Renderer extends GLCanvas implements GLEventListener, KeyListener {

    public final static int[][] QUAD_LINE_MAP = new int[][] {{0,1,2,3},{2,3,4,5},{4,5,6,7},{6,7,0,1}};
    public final static int[][] QUAD_FILL_MAP = new int[][] {{0,1,2,3},{2,3,4,5},{4,5,6,7},{6,7,0,1},{0,3,4,7},{6,1,2,5}};

    private GL2 gl;
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private TextRenderer textrenderer = new TextRenderer(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    private Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
    private Engine engine;
    private Player player;

    private double speed, speedSecond;
    private static long frame = 0;
    private long lastTick;
    private boolean paused, showHUD = false;
    private GLAnimatorControl animator;

    public Renderer(GLCapabilities cap, Engine engine, Player player, double speed) {
        super(cap);
        this.engine = engine;
        this.player = player;
        setSpeed(speed);

        this.addGLEventListener(this);
    }

    public Renderer(GLCapabilities cap, Engine engine, Player player) {
        this(cap, engine, player, 1);
    }

    public void init(GLAutoDrawable gLDrawable) {
        gl = gLDrawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);
        //gl.glEnable(GL2.GL_LIGHTING);
        //gl.glEnable(GL2.GL_COLOR_MATERIAL);
        //gl.glEnable(GL2.GL_NORMALIZE);
        //gl.glShadeModel(GL2.GL_SMOOTH);

        this.addKeyListener(this);
        this.addKeyListener(engine);
        this.addKeyListener(engine.getScenario());
        this.addKeyListener(player);
        this.addMouseMotionListener(player);

        engine.init();

        animator = gLDrawable.getAnimator();
        lastTick = System.currentTimeMillis();
    }

    public void display(GLAutoDrawable gLDrawable) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        frame++;

        // frame duration
        long newTick = System.currentTimeMillis();
        double tick = (double)(newTick-lastTick)*speedSecond;
        lastTick = newTick;

        // mouse
        this.setCursor(player.mouseControlsEnabled()?blankCursor:Cursor.getDefaultCursor());
        player.setMouseCenter(this.getLocationOnScreen().getX()+this.getSize().getWidth()/2, this.getLocationOnScreen().getY()+this.getSize().getHeight()/2);

        player.getPerspective().set(glu, player);

        engine.render(gl, glut);

        player.update(tick);
        if (!paused)
            engine.update(tick);

        // lighting
			/*
			double center = boardSize*panelSize/2;
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new double[] {0.2, 0.2, 0.2, 0.2}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new double[] {0.3, 0.3, 0.3, 0.3}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new double[] {center, center, 2.0, 0.0}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, new double[] {90}, 0);
			gl.glEnable(GL2.GL_LIGHT0);
			*/

        // hud
        if (showHUD) {
            String[] upperHUD = new String[]{
                    "FPS: " + animator.getLastFPS(),
                    "Speed: " + speed,
                    "Player: " + player.toString(),
                    "Picked: " + player.getPickedDistance() + " - " + player.getPickedObject(),
                    "Aimed: " + player.getAimedDistance() + " - " + player.getAimedObject()
            };

            ArrayList<String> lowerHUD = engine.renderText();

            int line, spacing = 16;
            gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
            textrenderer.beginRendering(gLDrawable.getSurfaceWidth(), gLDrawable.getSurfaceHeight());
            textrenderer.setColor(0.0f, 1.0f, 0.0f, 0.8f);

            line = 0;
            for (String s : upperHUD)
                textrenderer.draw(s, 5, gLDrawable.getSurfaceHeight() - spacing * (++line));

            line = lowerHUD.size() + 1;
            for (String s : lowerHUD)
                textrenderer.draw(s, 5, spacing * (--line));


            textrenderer.endRendering();
        }
        engine.textQueueClear();

        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {}

    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60, 4.0/3.0, 0.01, 10);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void dispose(GLAutoDrawable drawable) {}

    public static long getFrame() {
        return frame;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        this.speedSecond = speed/1000;
    }

    public static void renderLineQuad(GL2 gl, GLUT glut, Point3[] vertices) {
        renderQuad(gl, glut, vertices, GL2.GL_LINE, QUAD_LINE_MAP, null);
    }

    public static void renderLineQuad(GL2 gl, GLUT glut, Point3[] vertices, RGB color) {
        renderQuad(gl, glut, vertices, GL2.GL_LINE, QUAD_LINE_MAP, new RGB[] {color, color, color, color});
    }

    public static void renderLineQuad(GL2 gl, GLUT glut, Point3[] vertices, RGB[] face_colors) {
        renderQuad(gl, glut, vertices, GL2.GL_LINE, QUAD_LINE_MAP, face_colors);
    }

    public static void renderFillQuad(GL2 gl, GLUT glut, Point3[] vertices) {
        renderQuad(gl, glut, vertices, GL2.GL_FILL, QUAD_FILL_MAP, null);
    }

    public static void renderFillQuad(GL2 gl, GLUT glut, Point3[] vertices, RGB color) {
        renderQuad(gl, glut, vertices, GL2.GL_FILL, QUAD_FILL_MAP, new RGB[] {color, color, color, color, color, color});
    }

    public static void renderFillQuad(GL2 gl, GLUT glut, Point3[] vertices, RGB[] face_colors) {
        renderQuad(gl, glut, vertices, GL2.GL_FILL, QUAD_FILL_MAP, face_colors);
    }

    public static void renderQuad(GL2 gl, GLUT glut, Point3[] vertices, int mode, int[][] quad_map, RGB[] face_colors) {
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, mode);
        gl.glBegin(GL2.GL_QUADS);

        int face = 0;
        for (int[] face_map : quad_map) {
            if (face_colors != null)
                face_colors[face++].setForGL(gl);

            for (int index : face_map)
                gl.glVertex3d(vertices[index].getX(), vertices[index].getY(), vertices[index].getZ());
        }
        gl.glEnd();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_H: {
                showHUD = !showHUD;
                break;
            }
            case KeyEvent.VK_O: {
                paused = !paused;
                break;
            }
            case KeyEvent.VK_L: {
                if (paused)
                    engine.update(0.016);
                break;
            }
            case KeyEvent.VK_V: {
                player.nextPerspective();
                break;
            }
            case KeyEvent.VK_B: {
                ObjectBounding.VISIBLE = !ObjectBounding.VISIBLE;
                OctreeBounding.VISIBLE = !OctreeBounding.VISIBLE;
                BroadPhase.VISIBLE = !BroadPhase.VISIBLE;
                ObjectContact.VISIBLE = !ObjectContact.VISIBLE;
                break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
