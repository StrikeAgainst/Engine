package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.collision.CollisionContainer;
import engine.force.ForceRegistry;
import main.Scenario;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Engine implements KeyListener {

    private static ArrayList<String> renderTextQueue = new ArrayList<>();

    private ObjectContainer container = ObjectContainer.get();
    private Scenario scenario;
    private Player player;

    public Engine(Scenario scenario, Player player) {
        this.scenario = scenario;
        this.player = player;
    }

    public void render(GL2 gl, GLUT glut) {
        container.renderAll(gl, glut);
    }

    public static void queueText(String s) {
        renderTextQueue.add(s);
    }

    public static void queueText(String[] s) {
        renderTextQueue.addAll(Arrays.asList(s));
    }

    public static void queueText(ArrayList<String> s) {
        renderTextQueue.addAll(s);
    }

    public ArrayList<String> renderText() {
        ArrayList<String> renderText = new ArrayList<>(renderTextQueue);
        renderTextQueue.clear();
        return renderText;
    }

    public void init() {
        container.updateOctree(true);
    }

    public void update(float tick) {
        queueText(CollisionContainer.get().toStringArray());
        player.update(tick);
        container.updateAll(tick);
    }

    public Player getPlayer() {
        return player;
    }

    public void reset() {
        ForceRegistry.get().clear();
        container.clear();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
