package engine;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.collision.*;
import engine.force.ForceRegistry;
import main.Scenario;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Engine implements KeyListener {

    private static ArrayList<String> renderTextQueue = new ArrayList<>();

    private ObjectContainer objects = ObjectContainer.get();
    private ContactContainer contacts = ContactContainer.get();
    private Octree octree = Octree.get();
    private ForceRegistry forces = ForceRegistry.get();

    private Scenario scenario;
    private Player player;

    public Engine(Scenario scenario, Player player) {
        this.scenario = scenario;
        this.player = player;
    }

    public void render(GL2 gl, GLUT glut) {
        objects.render(gl, glut);
        octree.render(gl, glut);

        if (scenario.getGround() != null)
            scenario.getGround().render(gl, glut);
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
        octree.update(true);
    }

    public void update(float tick) {
        queueText(contacts.toStringArray());
        contacts.clear();

        forces.updateForces(tick);
        player.update(tick);
        objects.update(tick);

        ArrayList<RigidObject> outsiders = octree.update(false);
        for (RigidObject outsider : outsiders) {
            if (outsider instanceof PlayableObject && ((PlayableObject) outsider).getPlayer() != null) {
                ((PlayableObject) outsider).getPlayer().reset();
                octree.insert(outsider);
            } else {
                objects.remove(outsider);
                outsider.destroy();
            }
        }

        ArrayList<Contact> contactList = octree.detectContacts();

        if (scenario.getGround() != null)
            for (RigidObject o : objects)
                contactList.addAll(o.contactsWith(scenario.getGround()));

        contacts.add(contactList);

        ArrayList<ObjectContact> ocs = new ArrayList<>();
        for (Contact c : contacts.getAll())
            if (c instanceof ObjectContact)
                ocs.add((ObjectContact) c);

        ContactResolver.resolveAll(ocs, tick);
    }

    public Player getPlayer() {
        return player;
    }

    public void reset() {
        forces.clear();
        objects.clear();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
