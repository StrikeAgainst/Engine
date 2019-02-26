package engine.collision;

import java.util.ArrayList;
import java.util.Iterator;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import core.Point3;
import core.Vector3;
import engine.Config;
import engine.RigidObject;
import engine.PhysicalObject;
import engine.collision.bounding.OctreeBounding;

public class Octree {

	private static Octree octree = null;
	private static final float[][] octant_map = {{1,1,1},{1,1,-1},{1,-1,1},{1,-1,-1},{-1,1,1},{-1,1,-1},{-1,-1,1},{-1,-1,-1}};
	
	private int depth;
	private float size, halfSize, quarterSize;
	private final Octree parent;
	private Point3 root;
	private Octree[] octants = null;
	private ArrayList<RigidObject> objects = new ArrayList<>();
	private OctreeBounding bounding;
	private boolean changed = false;
	
	private Octree(Octree parent, Point3 root, float size, int depth) {
		this.parent = parent;
		this.root = root;
		this.size = size;
		this.halfSize = size/2;
		this.quarterSize = size/4;
		this.depth = depth;
		this.bounding = new OctreeBounding(root, size);
	}
	
	public static Octree get() {
		if (octree == null)
			octree = new Octree(null, Config.ROOT, (float) Math.pow(2, Config.OCTREE_MAX_DIMENSION_EXP), 0);
		return octree;
	}
	
	public void render(GL2 gl, GLUT glut) {
		bounding.render(gl, glut);
		if (octants != null)
			for (Octree octant : octants)
				octant.render(gl,  glut);
	}

	// updateDepth all objects in octree structure
	public ArrayList<RigidObject> update(boolean force) {
		changed = false;
		ArrayList<RigidObject> queue = new ArrayList<>();
		RigidObject obj;

		// get objects ascending from sub branches
		if (octants != null)
			for (Octree octant : octants)
				queue.addAll(octant.update(force));

		// check if ascending objects ...
		for (Iterator<RigidObject> it = queue.iterator(); it.hasNext();) {
			obj = it.next();
			// ... stay in this node/descend into different sub branches
			if (insert(obj) != null) {
				changed = true;
				it.remove();
			} else if (emptyRecursive())
                octants = null;
			// ... keep ascending
		}

		// check if objects from this node ...
		for (Iterator<RigidObject> it = objects.iterator(); it.hasNext();) {
			obj = it.next();
			if (obj.internalsUpdated() || force) {
				// ... ascend to parent node
				if (!encloses(obj)) {
					changed = true;
					it.remove();
                    if (emptyRecursive())
                        octants = null;
					queue.add(obj);
				// ... descend to sub branches
				} else if (octants != null)
					for (Octree octant : octants)
						if (octant.insert(obj) != null) {
							changed = true;
							it.remove();
							break;
						}
				// ... stay
			}
		}

		// return objects ascending to parent node
		// if this already is the root node, objects are moving outside of the octree
		return queue;
	}
	
	public ArrayList<Contact> detectContacts() {
		return detectContacts(new ArrayList<>());
	}

	// check all contacts inside octree structure
	public ArrayList<Contact> detectContacts(ArrayList<RigidObject> parentObjects) {
		ArrayList<Contact> contacts = new ArrayList<>();

		//if (changed) System.out.println(toString());

		// check if any objects of this node collide with each other
		for (RigidObject obj1 : objects)
			for (RigidObject obj2 : objects) {
				if (obj1 == obj2)
					break; // instead of continue; prevents duplicate checks
				contacts.addAll(obj1.contactsWith(obj2));
			}

		// check if any objects of this node collide with any objects from parent nodes
		for (RigidObject obj1 : objects)
			for (RigidObject obj2 : parentObjects)
				contacts.addAll(obj1.contactsWith(obj2));

		parentObjects.addAll(objects);
		
		if (octants != null)
            for (Octree octant : octants)
				contacts.addAll(octant.detectContacts(new ArrayList<>(parentObjects)));

		return contacts;
	}

	// check all collisions for a given object inside octree structure
	public ArrayList<Contact> detectContacts(PhysicalObject pobj) {
		ArrayList<Contact> contacts = new ArrayList<>();
		Contact contact;
		
		for (RigidObject obj : objects) {
			if (obj == pobj)
				continue;
			contacts.addAll(pobj.contactsWith(obj));
		}
		
		if (octants != null)
            for (Octree octant : octants)
				contacts.addAll(octant.detectContacts(pobj));
		
		return contacts;
	}
	
	public boolean createOctants() {
		if (depth >= Config.OCTREE_MAX_DEPTH || size <= 1)
			return false;

		octants = new Octree[octant_map.length];

		for (int i = 0; i < octants.length; i++) {
			Point3 octantRoot = root.offset((new Vector3(octant_map[i])).scaled(quarterSize));
			octants[i] = new Octree(this, octantRoot, halfSize, depth+1);
		}
		return true;
	}
	
	public Octree insert(RigidObject obj) {
		if (!encloses(obj))
			return null;
		
		if (objects.contains(obj))
			return this;

		if (octants != null || createOctants())
            for (Octree octant : octants) {
                Octree container = octant.insert(obj);
				if (container != null)
					return container;
			}

		objects.add(obj);
		return this;
	}

	public Octree remove(RigidObject obj) {
		if (objects.contains(obj) && objects.remove(obj)){
            if (emptyRecursive())
                octants = null;
			return this;
		}
		
		if (octants != null)
            for (Octree octant : octants) {
				Octree container = octant.remove(obj);
				if (container != null)
					return container;
			}
		return null;
	}
	
	public boolean contains(RigidObject obj) {
		return objects.contains(obj);
	}
	
	public Octree getContainer(RigidObject obj) {
		if (objects.contains(obj))
			return this;
		
		if (octants != null)
            for (Octree octant : octants) {
				Octree container = octant.getContainer(obj);
				if (container != null)
					return container;
			}
		return null;
	}
	
	public boolean empty() {
		return objects.isEmpty();
	}
	
	public boolean emptyRecursive() {
		if (!objects.isEmpty())
			return false;
		
		if (octants != null)
            for (Octree octant : octants)
				if (!octant.emptyRecursive())
					return false;
		
		return true;
	}
	
	public ArrayList<RigidObject> getObjects() {
		return objects;
	}

	public ArrayList<RigidObject> getObjectsRecursive() {
		ArrayList<RigidObject> rec = new ArrayList<>(objects);
		
		if (octants != null)
            for (Octree octant : octants)
				rec.addAll(octant.getObjectsRecursive());
		
		return rec;
	}
	
	public boolean encloses(RigidObject obj) {
		return bounding.encloses(obj.getBounding().getBroadPhase());
	}
	
	public OctreeBounding getBounding() {
		return bounding;
	}
	
	public String getMapping() {
		return (parent != null?parent.getMapping()+"-":"")+"["+root.getAttributesString()+"]";
	}
	
	public String toString() {
		return "Octree:[depth: "+depth+", mapping: "+getMapping()+", size: "+ objects.size()+", objects:"+ objects.toString()+"]";
	}
}
