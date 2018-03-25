package engine;

import world.Vector3D;

public class Momentum {
	
	private static boolean GRAVITY = true;
	protected float vx = 0, vy = 0, vz = 0, vya = 0, vza = 0;
	
	public Momentum() {}
	
	public Momentum(float vx, float vy, float vz, float vya, float vza) {
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		this.vya = vya;
		this.vza = vza;
	}

	public Momentum(Momentum m) {
		this.vx = m.getVX();
		this.vy = m.getVY();
		this.vz = m.getVZ();
		this.vya = m.getVYAngle();
		this.vza = m.getVZAngle();
	}
	
	public static void toggleGravity() {
		GRAVITY = !GRAVITY;
	}
		
	public void update(double tick, boolean gravitational) {
		if (GRAVITY && gravitational)
			this.vz += (float)(Physics.g*tick);
	}
	
	public void addMomentum(Momentum momentum) {
		this.vx += momentum.getVX();
		this.vy += momentum.getVY();
		this.vz += momentum.getVZ();
		this.vya += momentum.getVYAngle();
		this.vza += momentum.getVZAngle();
	}
	
	public Vector3D toVector(double tick) {
		return new Vector3D((float)(vx*tick), (float)(vy*tick), (float)(vz*tick));
	}
	
	public void nullify() {
		this.vx = 0;
		this.vy = 0;
		this.vz = 0;
		this.vya = 0;
		this.vza = 0;
	}
	
	public float getVX() {
		return vx;
	}
	
	public void addVX(float vx) {
		this.vx += vx;
	}
	
	public void setVX(float vx) {
		this.vx = vx;
	}
	
	public float getVY() {
		return vy;
	}
	
	public void addVY(float vy) {
		this.vy += vy;
	}
	
	public void setVY(float vy) {
		this.vy = vy;
	}
	
	public float getVZ() {
		return vz;
	}
	
	public void addVZ(float vz) {
		this.vz += vz;
	}
	
	public void setVZ(float vz) {
		this.vz = vz;
	}
	
	public float getVYAngle() {
		return vya;
	}
	
	public void addVYAngle(float vya) {
		this.vya += vya;
	}
	
	public void setVYAngle(float vya) {
		this.vya = vya;
	}
	
	public float getVZAngle() {
		return vza; 
	}
	
	public void addVZAngle(float vza) {
		this.vza += vza;
	}
	
	public void setVZAngle(float vza) {
		this.vza = vza;
	}
	
	public boolean isStill() {
		return (vx == 0 && vy == 0 && vz == 0 && vya == 0 && vza == 0);
	}
	
	public String toString() {
		return "[vx="+vx+", vy="+vy+", vz="+vz+", vya="+vya+", vza="+vza+"]";
	}
}
