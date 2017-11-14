package engine;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class ObjectContainer extends Observable implements Iterable<EngineObject> {
	
	private static ObjectContainer container = null;
	private Vector<EngineObject> eos = new Vector<EngineObject>();
	
	private ObjectContainer() {}
	
	public static ObjectContainer get() {
		if (container == null) container = new ObjectContainer();
		return container;
	}
	
	public void drawAll(GL2 gl, GLUT glut) {
		for (EngineObject eo : eos) {
			eo.drawObject(gl, glut);
		}
	}
	
	public boolean add(EngineObject e) {
		for (EngineObject eo : eos) {
			if (e == eo) return false;
		}
		eos.add(e);
		return true;
	}
	
	public boolean remove(EngineObject e) {
		if (eos.contains(e) && eos.remove(e)) return true;
		return false;
	}
	
	public void removeAll() {
		eos.removeAllElements();
	}
	
	public boolean contains(EngineObject e){
		return eos.contains(e);
	}
	
	public int size() {
		return eos.size();
	}

	public Iterator<EngineObject> iterator() {
		return eos.iterator();
	}
	
	public void addObserver(Observer o) {
		this.addObserver(o);
	}
	
	public String getContentString() {
		String s = "";
		int c = 0;
		for (EngineObject eo : eos) {
			s = s+"Object "+c+": "+eo.toString()+", ";
			c++;
		}
		return s;
	}

}
