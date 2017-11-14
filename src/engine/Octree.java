package engine;

import java.util.Vector;

public class Octree {

	private static Octree octree = null;
	
	private int maxDepth;
	private float size;
	private final Octree parent;
	private static final int[][] dimension_map = {{1,1,1},{1,1,-1},{1,-1,1},{1,-1,-1},{-1,1,1},{-1,1,-1},{-1,-1,1},{-1,-1,-1}};
	private Point3D root;
	private Octree[] children = null;
	private Vector<EngineObject> nodes = new Vector<EngineObject>();
	private BoundingBox bounding;
	
	private Octree(Octree parent, Point3D root, float size, int maxDepth) {
		this.parent = parent;
		this.root = root;
		this.size = size;
		this.maxDepth = maxDepth;
		this.bounding = new BoundingBox(root, size, size, size, size, size, size);
	}
	
	public static Octree get() {
		if (octree == null) octree = new Octree(null, Config.ROOT, Config.OCTREE_MAX_DIMENSION_SIZE, Config.OCTREE_MAX_DEPTH);
		return octree;
	}
		
	public void update() {
		if (!nodes.isEmpty() && children == null && maxDepth > 0) {
			children = new Octree[dimension_map.length];
			for (int i = 0; i < dimension_map.length; i++) {
				Point3D childRoot = root.clone();
				childRoot.move(size*dimension_map[i][0]/2, size*dimension_map[i][1]/2, size*dimension_map[i][2]/2);
				children[i] = new Octree(this, childRoot.clone(), size/2, maxDepth-1);
			}
		}
		
		for (EngineObject node : nodes) {
			Bounding nodeBounding = node.getBounding();
			if (!bounding.encloses(nodeBounding)) {
				removeNode(node);
				parent.addNode(node);
			} else if (children != null) {
				for (int i = 0; i < children.length; i++) {
					if (children[i].getBounding().encloses(nodeBounding)) {
						removeNode(node);
						children[i].addNode(node);
						break;
					}
				}
			}
		}
		if (children != null)
			for (int i = 0; i < children.length; i++) {
				children[i].update();
			}
	}
	
	public Octree getParent() {
		return parent;
	}
	
	public Octree[] getChildren() {
		return children;
	}
	
	public boolean addNode(EngineObject n) {
		for (EngineObject node : nodes) {
			if (n == node) return false;
		}
		nodes.add(n);
		return true;
	}

	public boolean removeNode(EngineObject n) {
		if (nodes.contains(n) && nodes.remove(n)) return true;
		return false;
	}

	public Vector<EngineObject> getNodes() {
		return nodes;
	}

	public Vector<EngineObject> getNodesRecursive() {
		Vector<EngineObject> rec = new Vector<EngineObject>();
		if (children == null) 
			return rec;
		for (int i = 0; i < children.length; i++) {
			Vector<EngineObject> childRec = children[i].getNodesRecursive();
			for (EngineObject node : childRec) {
				rec.add(node);
			}
		}
		return rec;
	}
	
	public Bounding getBounding() {
		return bounding;
	}
}
