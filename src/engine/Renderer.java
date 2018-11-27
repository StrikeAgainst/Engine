package engine;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import world.Perspective;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Renderer extends GLCanvas implements GLEventListener, KeyListener {

    private GL2 gl;
    private GLU glu;
    private GLUT glut;
    private TextRenderer textrenderer = new TextRenderer(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    private Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
    private Engine engine;
    private Scenario scenario;

    private long lastTick;
    private GLAnimatorControl animator;

    public Renderer(GLCapabilities cap, Engine engine, Scenario scenario) {
        super(cap);
        glu = new GLU();
        glut = new GLUT();
        this.engine = engine;
        this.scenario = scenario;

        this.addGLEventListener(this);
        this.addKeyListener(engine);
        this.addKeyListener(scenario.getPlayer());
        this.addMouseMotionListener(scenario.getPlayer());
    }

    public void init(GLAutoDrawable gLDrawable) {
        gl = gLDrawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);
        //gl.glEnable(GL2.GL_LIGHTING);
        //gl.glEnable(GL2.GL_COLOR_MATERIAL);
        //gl.glEnable(GL2.GL_NORMALIZE);
        //gl.glShadeModel(GL2.GL_SMOOTH);

        animator = gLDrawable.getAnimator();
        lastTick = System.currentTimeMillis();
    }

    public void display(GLAutoDrawable gLDrawable) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // frame duration
        long newTick = System.currentTimeMillis();
        float tick = (float)(newTick-lastTick)/1000;
        lastTick = newTick;

        // mouse
        if (scenario.getPlayer().isMouseOn())
            this.setCursor(blankCursor);
        else
            this.setCursor(Cursor.getDefaultCursor());
        scenario.getPlayer().setMouseCenter(this.getLocationOnScreen().getX()+this.getSize().getWidth()/2, this.getLocationOnScreen().getY()+this.getSize().getHeight()/2);

        if (scenario.getPlayer().getPlayerObject() == null)
            Perspective.spectator.setCamera(glu, scenario.getPlayer());
        else
            scenario.getPerspective().setCamera(glu, scenario.getPlayer());

        engine.render(gl, glut);
        engine.tick(tick);

        // lighting
			/*
			float center = boardSize*panelSize/2;
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] {0.2f, 0.2f, 0.2f, 0.2f}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {0.3f, 0.3f, 0.3f, 0.3f}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {center, center, 2.0f, 0.0f}, 0);
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, new float[] {90}, 0);
			gl.glEnable(GL2.GL_LIGHT0);
			*/

        // hud
        int line = 0, spacing = 16;
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
        textrenderer.beginRendering(gLDrawable.getSurfaceWidth(), gLDrawable.getSurfaceHeight());
        textrenderer.setColor(0.0f, 1.0f, 0.0f, 0.8f);
        textrenderer.draw("FPS: "+animator.getLastFPS(), 5, gLDrawable.getSurfaceHeight()-spacing*(++line));
        textrenderer.draw("Player: "+scenario.getPlayer().toString(), 5, gLDrawable.getSurfaceHeight()-spacing*(++line));
        textrenderer.endRendering();

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
                scenario.nextPerspective();
                break;
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
