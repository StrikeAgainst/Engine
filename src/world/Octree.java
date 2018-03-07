package world;

import java.util.ArrayList;
import java.util.Iterator;

import engine.Config;
import engine.EngineObject;
import engine.Intersection;

public class Octree {

	private static Octree octree = null;
	private static final int[][] dimension_map = {{1,1,1},{1,1,-1},{1,-1,1},{1,-1,-1},{-1,1,1},{-1,1,-1},{-1,-1,1},{-1,-1,-1}};
	
	private int depth, size;
	private final Octree parent;
	private Point3D root;
	private Octree[] octants = null;
	private ArrayList<EngineObject> nodes = new ArrayList<EngineObject>();
	private OctreeBounding bounding;
	
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
		
	public ArrayList<EngineObject> update() {
		ArrayList<EngineObject> queue = new ArrayList<EngineObject>();
		EngineObject node;
		
		if (octants != null)
			for (int i = 0; i < octants.length; i++)
				queue.addAll(octants[i].update());

		for (Iterator<EngineObject> it = queue.iterator(); it.hasNext();) {
			node = it.next();
			if (encloses(node)) {
				insertNode(node);
				it.remove();
			}		
		}
				
		for (Iterator<EngineObject> it = nodes.iterator(); it.hasNext();) {
			node = it.next();
			if (node.isMoving()) {
				if (!encloses(node)){
					queue.add(node);
					it.remove();
				} else if (octants != null)
					for (int i = 0; i < octants.length; i++) 
						if (octants[i].encloses(node)) {
							octants[i].insertNode(node);
							it.remove();
							break;
						}
			}
		}
		return queue;
	}
	
	public ArrayList<Intersection> detectCollisions() {
		return detectCollisions(new ArrayList<EngineObject>());
	}
	
	public ArrayList<Intersection> detectCollisions(ArrayList<EngineObject> parentNodes) {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		Vector3D normal = null;
		int count1 = 0, count2 = 0;

		//if (nodes.size() > 0) System.out.println(toString());
		
		for (EngineObject node1 : nodes) {
			node1.resetIntersections();
			count2 = 0;
			for (EngineObject node2 : nodes) {
				if (count2 >= count1)
					break;
				normal = node1.intersects(node2);
				if (normal != null) {
					Intersection intersection = new Intersection(node1, node2, normal);
					intersections.add(intersection);
					node1.addIntersection(intersection);
					node2.addIntersection(intersection.reverse());
				}
				count2++;
			}
			count1++;
		}

		for (EngineObject node1 : nodes)
			for (EngineObject node2 : parentNodes) {
				normal = node1.intersects(node2);
				if (normal != null) {
					Intersection intersection = new Intersection(node1, node2, normal);
					intersections.add(intersection);
					node1.addIntersection(intersection);
					node2.addIntersection(intersection.reverse());
				}
			}

		parentNodes.addAll(nodes);
		
		if (octants != null)
			for (int i = 0; i < octants.length; i++)
				intersections.addAll(octants[i].detectCollisions(parentNodes));
		
		return intersections;
	}
	
	public Octree getParent() {
		return parent;
	}
	
	public Octree[] getOctants() {
		return octants;
	}
	
	public boolean createOctants() {
		if (depth < Config.OCTREE_MAX_DEPTH && size > 1) {
			int halfsize = size/2;
			octants = new Octree[dimension_map.length];
			
			for (int i = 0; i < octants.length; i++) {
				Point3D childRoot = root.clone();
				childRoot.move(dimension_map[i][0]*halfsize, dimension_map[i][1]*halfsize, dimension_map[i][2]*halfsize);
				octants[i] = new Octree(this, childRoot.clone(), halfsize, depth+1);
			}
			return true;
		}
		return false;
	}
	
	public boolean deleteOctants() {
		if (emptyRecursive()) {
			octants = null;
			return true;
		}
		return false;
	}
	
	public Octree insertNode(EngineObject node) {
		if (nodes.contains(node))
			return this;

		if (octants != null || createOctants())		
			for (int i = 0; i < octants.length; i++) 
				if (octants[i].encloses(node))
					return octants[i].insertNode(node);
		
		nodes.add(node);
		return this;
	}

	public Octree removeNode(EngineObject node) {
		if (nodes.contains(node) && nodes.remove(node)){
			deleteOctants();
			return this;
		}
		
		if (octants != null) 
			for (int i = 0; i < octants.length; i++) {
				Octree container = octants[i].removeNode(node);
				if (container instanceof Octree)
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
			for (int i = 0; i < octants.length; i++) {
				Octree container = octants[i].getNodeContainer(node);
				if (container instanceof Octree)
					return container;
			}
		return null;
	}
	
	public boolean empty() {
		return nodes.isEmpty();
	}
	
	public boolean emptyRecursive() {
		if (nodes.isEmpty())
			return true;
		
		if (octants != null) 
			for (int i = 0; i < octants.length; i++) 
				if (octants[i].emptyRecursive())
					return true;
		return false;
	}
	
	public ArrayList<EngineObject> getNodes() {
		return nodes;
	}

	public ArrayList<EngineObject> getNodesRecursive() {
		ArrayList<EngineObject> rec = new ArrayList<EngineObject>(nodes);
		
		if (octants != null) 
			for (int i = 0; i < octants.length; i++)
				rec.addAll(octants[i].getNodesRecursive());
		
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
		return "Octree: [depth: "+depth+", mapping: "+getMapping()+", size: "+nodes.size()+", nodes:"+nodes.toString()+"]";
	}
}
