package world;

import java.util.ArrayList;
import java.util.Iterator;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import engine.Config;
import engine.EngineObject;
import engine.PhysicalObject;
import engine.Collision;

public class Octree {

	private static Octree octree = null;
	public static boolean SHOW_BOUNDING = false;
	private static final int[][] dimension_map = {{1,1,1},{1,1,-1},{1,-1,1},{1,-1,-1},{-1,1,1},{-1,1,-1},{-1,-1,1},{-1,-1,-1}};
	
	private int depth, size;
	private final Octree parent;
	private Point3D root;
	private Octree[] octants = null;
	private ArrayList<EngineObject> nodes = new ArrayList<>();
	private OctreeBounding bounding;
	private boolean changed = false;
	
	private Octree(Octree parent, Point3D root, int size, int depth) {
		this.parent = parent;
		this.root = root;
		this.size = size;
		this.depth = depth;
		this.bounding = new OctreeBounding(root, size);
	}
	
	public static Octree get() {
		if (octree == null)
			octree = new Octree(null, Config.ROOT, (int) Math.pow(2, Config.OCTREE_MAX_DIMENSION_EXP), 0);
		return octree;
	}
	
	public static void toggleShowBounding() {
		Octree.SHOW_BOUNDING = !Octree.SHOW_BOUNDING;
	}
	
	public void draw(GL2 gl, GLUT glut) {
		if (SHOW_BOUNDING) {
			bounding.draw(gl,  glut, changed);
			if (octants != null)
				for (Octree octant : octants)
					octant.draw(gl,  glut);
		}
	}
	
	public ArrayList<EngineObject> update() {
		changed = false;
		ArrayList<EngineObject> queue = new ArrayList<>();
		EngineObject node;
		
		if (octants != null)
			for (Octree octant : octants)
				queue.addAll(octant.update());

		for (Iterator<EngineObject> it = queue.iterator(); it.hasNext();) {
			node = it.next();
			Octree octant = insertNode(node);
			if (octant != null) {
				changed = true;
				it.remove();
			} else if (emptyRecursive())
                octants = null;
		}
		
		for (Iterator<EngineObject> it = nodes.iterator(); it.hasNext();) {
			node = it.next();
			if (node.isMoving()) {
				if (!encloses(node)) {
					changed = true;
					it.remove();
                    if (emptyRecursive())
                        octants = null;
					queue.add(node);
				} else if (octants != null)
					for (Octree octant : octants)
						if (octant.insertNode(node) != null) {
							changed = true;
							it.remove();
							break;
						}
			}
		}

		return queue;
	}
	
	public ArrayList<Collision> detectCollisions() {
		return detectCollisions(new ArrayList<>());
	}
	
	public ArrayList<Collision> detectCollisions(ArrayList<EngineObject> parentNodes) {
		ArrayList<Collision> collisions = new ArrayList<>();
		Collision collision;

		//if (changed) System.out.println(toString());
		
		for (EngineObject node1 : nodes)
			for (EngineObject node2 : nodes) {
				if (node1 == node2)
					break;
				collision = node1.collides(node2);
				if (collision != null) 
					collisions.add(collision);
			}

		for (EngineObject node1 : nodes)
			for (EngineObject node2 : parentNodes) {
				collision = node1.collides(node2);
				if (collision != null) 
					collisions.add(collision);
			}

		parentNodes.addAll(nodes);
		
		if (octants != null)
            for (Octree octant : octants)
				collisions.addAll(octant.detectCollisions(new ArrayList<>(parentNodes)));
		
		return collisions;
	}
	
	public ArrayList<Collision> detectCollisions(PhysicalObject pnode) {
		ArrayList<Collision> collisions = new ArrayList<>();
		Collision collision;
		
		for (EngineObject node : nodes) {
			collision = pnode.collides(node);
			if (collision != null)
				collisions.add(collision);
		}
		
		if (octants != null)
            for (Octree octant : octants)
				collisions.addAll(octant.detectCollisions(pnode));
		
		return collisions;
	}
	
	public Octree getParent() {
		return parent;
	}
	
	public Octree[] getOctants() {
		return octants;
	}
	
	public boolean createOctants() {
		if (depth >= Config.OCTREE_MAX_DEPTH || size <= 1)
			return false;
			
		int halfsize = size/2;
		octants = new Octree[dimension_map.length];
		
		for (int i = 0; i < octants.length; i++) {
			Point3D octantRoot = new Point3D(root);
			octantRoot.move(dimension_map[i][0]*halfsize, dimension_map[i][1]*halfsize, dimension_map[i][2]*halfsize);
			octants[i] = new Octree(this, octantRoot, halfsize, depth+1);
		}
		return true;
	}
	
	public Octree insertNode(EngineObject node) {
		if (!encloses(node))
			return null;
		
		if (nodes.contains(node))
			return this;

		if (octants != null || createOctants())
            for (Octree octant : octants) {
                Octree container = octant.insertNode(node);
				if (container != null)
					return container;
			}
		
		nodes.add(node);
		return this;
	}

	public Octree removeNode(EngineObject node) {
		if (nodes.contains(node) && nodes.remove(node)){
            if (emptyRecursive())
                octants = null;
			return this;
		}
		
		if (octants != null)
            for (Octree octant : octants) {
				Octree container = octant.removeNode(node);
				if (container != null)
					return container;
			}
		return null;
	}
	
	public boolean contains(EngineObject node) {
		return nodes.contains(node);
	}
	
	public Octree getNodeContainer(EngineObject node) {
		if (nodes.contains(node))
			return this;
		
		if (octants != null)
            for (Octree octant : octants) {
				Octree container = octant.getNodeContainer(node);
				if (container != null)
					return container;
			}
		return null;
	}
	
	public boolean empty() {
		return nodes.isEmpty();
	}
	
	public boolean emptyRecursive() {
		if (!nodes.isEmpty())
			return false;
		
		if (octants != null)
            for (Octree octant : octants)
				if (!octant.emptyRecursive())
					return false;
		
		return true;
	}
	
	public ArrayList<EngineObject> getNodes() {
		return nodes;
	}

	public ArrayList<EngineObject> getNodesRecursive() {
		ArrayList<EngineObject> rec = new ArrayList<>(nodes);
		
		if (octants != null)
            for (Octree octant : octants)
				rec.addAll(octant.getNodesRecursive());
		
		return rec;
	}
	
	public boolean encloses(EngineObject node) {
		return bounding.encloses(node.getBounding());
	}
	
	public OctreeBounding getBounding() {
		return bounding;
	}
	
	public String getMapping() {
		return (parent != null?parent.getMapping()+"-":"")+root.toStringShort();
	}
	
	public String toString() {
		return "Octree:[depth: "+depth+", mapping: "+getMapping()+", size: "+nodes.size()+", nodes:"+nodes.toString()+"]";
	}
}
