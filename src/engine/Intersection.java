package engine;

import world.Vector3D;

public class Intersection {
	
	private EngineObject thisNode, otherNode;
	private Vector3D normal;
	
	public Intersection(EngineObject thisNode, EngineObject otherNode, Vector3D normal) {
		this.thisNode = thisNode;
		this.otherNode = otherNode;
		this.normal = normal;
	}
	
	public Vector3D getNormal() {
		return normal;
	}	
	
	public EngineObject getThisNode() {
		return thisNode;
	}	
	
	public EngineObject getOtherNode() {
		return otherNode;
	}	
	
	public Intersection reverse() {
		return new Intersection(otherNode, thisNode, normal.reverseClone());
	}
}
