package objects;

import java.util.ArrayList;

import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Sense extends MyObject {

	Boolean collided = false;
	ArrayList<MyObject> collidee = new ArrayList<MyObject>();
	
	public Sense(Vectors position, Vectors[] points) {
		super(position, Material.Sense, points);
	}
	
	public Sense(Vectors position, float radius){
		super(position, Material.Sense, radius);
	}

	public boolean collided(){
		return collided;
	}
	
	public void reset(){
		collided = false;
		collidee.clear();
	}
	
	@Override
	public boolean intangible(){
		return true;
	}
	
	public ArrayList<MyObject> collidee(){
		return collidee;
	}
	
}
