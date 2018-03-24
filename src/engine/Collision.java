package engine;

import world.Vector3D;

public class Collision {
	
	private PhysicalObject thisNode;
	private EngineObject otherNode;
	private Vector3D normal;
	private float timeFrame;
	
	public Collision(PhysicalObject thisNode, EngineObject otherNode, Vector3D normal, float timeFrame) {
		this.thisNode = thisNode;
		this.otherNode = otherNode;
		this.normal = normal;
		this.timeFrame = timeFrame;
	}
	
	public PhysicalObject getThisNode() {
		return thisNode;
	}	
	
	public EngineObject getOtherNode() {
		return otherNode;
	}	
	
	public float getTimeFrame() {
		return timeFrame;
	}	
	
	public Vector3D getNormal() {
		return normal;
	}	
	
	public void resolve() {
		Vector3D thisMovement = thisNode.getNextMovement();
		float tmx = thisMovement.getX(), tmy = thisMovement.getY(), tmz = thisMovement.getZ();
		float x_in, y_in, z_in, x_out, y_out, z_out;
		/*if (otherNode instanceof PhysicalObject) {
			Vector3D otherMovement = ((PhysicalObject) otherNode).getNextMovement();
		    if (mx > 0) {
		    	x_in = b2.x - (b1.x + b1.w);
		    	x_out = (b2.x + b2.w) - b1.x;
		    } else {
		    	x_in = (b2.x + b2.w) - b1.x;
		    	x_out = b2.x - (b1.x + b1.w);
		    }
		} else {
			
		}*/
	}
	
	public String toString() {
		return "Collision:["+thisNode.toString()+", "+otherNode.toString()+"]";
	}
}
