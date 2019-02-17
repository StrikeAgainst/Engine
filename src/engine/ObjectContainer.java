package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import engine.collision.Collision;
import engine.collision.CollisionContainer;
import engine.collision.Octree;

public class ObjectContainer extends Observable implements Iterable<RigidObject> {
	
	private static ObjectContainer container = null;
	private Octree octree = Octree.get();
	private ArrayList<RigidObject> objects = new ArrayList<>();
	
	private ObjectContainer() {}
	
	public static ObjectContainer get() {
		if (container == null)
			container = new ObjectContainer();
		return container;
	}

	public void renderAll(GL2 gl, GLUT glut) {
		renderObjects(gl, glut);
		renderOctree(gl, glut);
	}

	public void renderObjects(GL2 gl, GLUT glut) {
		for (RigidObject o : objects)
			o.render(gl, glut);
	}

	public void renderOctree(GL2 gl, GLUT glut) {
		octree.render(gl, glut);
	}

	public void updateAll(float tick) {
		updateObjects(tick);
		updateOctree(false);
	}

	public void updateObjects(float tick) {
		for (RigidObject o : objects) {
			o.update(tick);
		}
	}

	public void updateOctree(boolean force) {
		CollisionContainer.get().clear();

		ArrayList<RigidObject> outsiders = octree.update(force);
		for (RigidObject outsider : outsiders) {
			if (outsider instanceof PlayableObject && ((PlayableObject) outsider).getPlayer() != null) {
				((PlayableObject) outsider).getPlayer().reset();
				octree.insert(outsider);
			} else {
				remove(outsider);
				outsider.destroy();
			}
		}

		ArrayList<Collision> collisions = octree.detectCollisions();
		CollisionContainer.get().add(collisions);
	}
	
	public void add(RigidObject o) {
		if (!objects.contains(o)) {
			objects.add(o);
			o.updateInternals();
			if (o.getBounding() != null)
				octree.insert(o);
		}
	}

	public void add(ArrayList<RigidObject> os) {
		for (RigidObject o : os)
			add(o);
	}
	
	public void remove(RigidObject o) {
		if (objects.contains(o)) {
			octree.remove(o);
			objects.remove(o);
		}
	}

	public RigidObject getByID(int id) {
		for (RigidObject o : objects)
			if (o.getID() == id)
				return o;

		return null;
	}
	
	public void clear() {
		objects.clear();
	}
	
	public boolean contains(RigidObject o){
		return objects.contains(o);
	}
	
	public int size() {
		return objects.size();
	}

	public Iterator<RigidObject> iterator() {
		return objects.iterator();
	}

	public String toString() {
		return objects.toString();
	}

	public String[] toStringArray() {
		int i = 0;
		String[] s = new String[objects.size()];
		for (RigidObject o : objects) {
			s[i++] = o.toString();
		}
		return s;
	}
}
