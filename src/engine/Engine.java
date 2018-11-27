package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.collision.Collision;
import engine.particle.Particle;
import world.Octree;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Engine implements KeyListener {

    private Scenario scenario;

    private boolean pause = false;
    private ObjectContainer container = ObjectContainer.get();
    private ArrayList<Collision> collisions;
    private Octree octree = Octree.get();
    private Player player;

    public Engine(Scenario scenario) {
        this.scenario = scenario;
        player = scenario.getPlayer();
    }

    public void render(GL2 gl, GLUT glut) {
        if (scenario.world() == null) {
            for (EngineObject e : container)
                e.drawObject(gl, glut);
            octree.draw(gl, glut);
        } else
            for (Particle p : scenario.world().getParticles())
                p.draw(gl, glut);
    }

    public void tick(float tick) {
        if (!pause) {
            if (scenario.world() == null) {
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
                //for (Collision c : collisions)
                //	c.resolve();

                for (EngineObject e : container) {
                    if (e instanceof PhysicalObject) {
                        ((PhysicalObject) e).move();
                    }
                }
            } else {
                scenario.world().startFrame();
                scenario.world().runPhysics(tick);
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_G: {
                player.toggleGravitational();
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
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
