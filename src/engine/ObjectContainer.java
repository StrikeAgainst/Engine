package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import world.Octree;

public class ObjectContainer extends Observable implements Iterable<EngineObject> {
	
	private static ObjectContainer container = null;
	private Octree octree = Octree.get();
	private ArrayList<EngineObject> objects = new ArrayList<>();
	
	private ObjectContainer() {}
	
	public static ObjectContainer get() {
		if (container == null) container = new ObjectContainer();
		return container;
	}
	
	public void add(EngineObject e) {
		if (!objects.contains(e) &&(e.getBounding() == null || octree.insertNode(e) != null))
			objects.add(e);
	}
	
	public void remove(EngineObject e) {
		if (objects.contains(e)) {
			objects.remove(e);
			octree.removeNode(e);
		}
	}
	
	public void clear() {
		objects.clear();
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
}
