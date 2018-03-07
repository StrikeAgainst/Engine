package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import world.Octree;

public class ObjectContainer extends Observable implements Iterable<EngineObject> {
	
	private static ObjectContainer container = null;
	private Octree octree = Octree.get();
	private ArrayList<EngineObject> objects = new ArrayList<EngineObject>();
	
	private ObjectContainer() {}
	
	public static ObjectContainer get() {
		if (container == null) container = new ObjectContainer();
		return container;
	}
	
	public void drawAll(GL2 gl, GLUT glut) {
		for (EngineObject object : objects) {
			object.drawObject(gl, glut);
		}
	}
	
	public boolean add(EngineObject e) {
		for (EngineObject object : objects) {
			if (e == object) return false;
		}
		objects.add(e.getID(), e);
		if (e.getBounding() != null)
			octree.insertNode(e);
		return true;
	}
	
	public boolean remove(EngineObject e) {
		return (objects.contains(e) && objects.remove(e) && octree.removeNode(e) instanceof Octree);
	}
	
	public void clear() {
		objects.clear();
	}
	
	public EngineObject get(int id) {
		return objects.get(id);
	}
	
	public boolean contains(EngineObject e){
		return objects.contains(e);
	}
	
	public int size() {
		return objects.size();
	}

	public Iterator<EngineObject> iterator() {
		return objects.iterator();
	}
	
	public void addObserver(Observer o) {
		this.addObserver(o);
	}
	
	public String getContentString() {
		String s = "";
		int c = 0;
		for (EngineObject object : objects) {
			s += "Object "+c+": "+object.toString()+", ";
			c++;
		}
		return s;
	}

}
