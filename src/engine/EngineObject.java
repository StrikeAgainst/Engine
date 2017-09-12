package engine;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class EngineObject{

	protected float x, y, z, vx = 0, vy = 0, vz = 0, ya = 0, za = 60;
	private float front, back, left, right, top, bottom;
	protected boolean visible = true, showBounds = false;
	protected BoundingBox bounds;
	
	public EngineObject(float xPos, float yPos, float zPos, float front, float back, float left, float right, float top, float bottom) {
		this.x = xPos;
		this.y = yPos;
		this.z = zPos;
		this.front = front;
		this.back = back;
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		ObjectContainer.get().add(this);
	}
	
	protected abstract void draw(GL2 gl, GLUT glut);
	
	public void drawObject(GL2 gl, GLUT glut) {
		if (visible) {
			draw(gl, glut);
			if (showBounds) drawBounds(gl, glut);
		}
	}
	
	public void drawBounds(GL2 gl, GLUT glut) {
		if (showBounds) {
			gl.glColor3f(1f,0.55f,0f);
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
			gl.glBegin(GL2.GL_QUAD_STRIP);
			gl.glVertex3f(x+front, y+left, z+top);
			gl.glVertex3f(x+front, y+left, z+bottom);
			gl.glVertex3f(x+front, y+right, z+top);
			gl.glVertex3f(x+front, y+right, z+bottom);
			gl.glVertex3f(x+back, y+right, z+top);
			gl.glVertex3f(x+back, y+right, z+bottom);
			gl.glVertex3f(x+back, y+left, z+top);
			gl.glVertex3f(x+back, y+left, z+bottom);
			gl.glVertex3f(x+front, y+left, z+top);
			gl.glVertex3f(x+front, y+left, z+bottom);
			gl.glEnd();
		}
	};
	
	public boolean inside(float ox, float oy, float oz) {
		return (ox < x+front && ox > x+back && oy < y+left && oy > y+right && oz < z+top && oz > z+bottom);
	}
	
	public boolean crossing(EngineObject o) {
		if (inside(x+front, y+left, z+top)) return true;
		if (inside(x+front, y+left, z+bottom)) return true;
		if (inside(x+front, y+right, z+top)) return true;
		if (inside(x+front, y+right, z+bottom)) return true;
		if (inside(x+back, y+left, z+top)) return true;
		if (inside(x+back, y+left, z+bottom)) return true;
		if (inside(x+back, y+right, z+top)) return true;
		if (inside(x+back, y+right, z+bottom)) return true;
		return false;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	public float getVX() {
		return vx;
	}
	
	public void setVX(float vx) {
		this.vx = vx;
	}
	
	public float getVY() {
		return vy;
	}
	
	public void setVY(float vy) {
		this.vy = vy;
	}
	
	public float getVZ() {
		return vz;
	}
	
	public void setVZ(float vz) {
		this.vz = vz;
	}
	
	public float getYAngle() {
		return ya;
	}
	
	public void setYAngle(float ya) {
		this.ya = ya;
	}
	
	public float getZAngle() {
		return za; 
	}
	
	public void setZAngle(float za) {
		this.za = za;
	}
	
	public float getFrontBound() {
		return front;
	}
	
	public float getBackBound() {
		return back;
	}
	
	public float getLeftBound() {
		return left;
	}
	
	public float getRightBound() {
		return right;
	}
	
	public float getTopBound() {
		return top;
	}
	
	public float getBottomBound() {
		return bottom;
	}
	
	public void setVisible() {
		visible = true;
	}
	
	public void setInvisible() {
		visible = false;
	}
	
	public String toString() {
		return "[x="+x+", y="+y+", z="+z+", vx="+vx+", vy="+vy+", vz="+vz+", ya="+ya+", za="+za+", z="+z+"]";
	}
}
