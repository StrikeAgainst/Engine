package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
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

	public void render(GL2 gl, GLUT glut) {
		for (RigidObject o : objects)
			o.render(gl, glut);
	}

	public void update(double tick) {
		for (RigidObject o : objects)
			o.update(tick);
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
		octree.clear();
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
