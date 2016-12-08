package engine_yuki;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Ellipse;


/**
 * Circular bounding shape
 * @author Wesley
 *
 */
public class BoundingCircle extends BoundingShape {

	float radius;
	
	public BoundingCircle(float radius){
		this.radius = radius;
		size = 0;
	}
	public void setRadius(float radius){
		this.radius = radius;
	}

	@Override
	public float getRadius(){
		return radius;
	}
	
	@Override
	public BoundingCircle clone() {
		return new BoundingCircle(radius);
	}
	
	// Does nothing
	@Override
	public void setOrientation(float rads) {
		
	}
	
	@Override
	public float getOri(){
		return 0.0f;
	}

	@Override
	public void render(Graphics g, Vectors position, Color color) {
		if(texture != null){
			Ellipse oval = new Ellipse(position.X(), position.Y(), radius, radius);
			g.texture(oval, texture);
		} else {
			g.setColor(color);
			g.fillOval(position.X() - radius, position.Y() - radius, 2 * radius, 2 * radius);
		}
		
	}
	
	@Override
	public MassData calculateMass(float density) {
		float mass = (float) Math.PI * radius * radius * density;
		float inertia = mass * radius * radius;
		return new MassData(mass, inertia);
	}
	@Override
	public Matrix2x2 getOrientation() {
		return null;
	}
	@Override
	public ArrayList<Vectors> getNormals() {
		return null;
	}
	@Override
	public ArrayList<Vectors> getPoints() {
		return null;
	}
	@Override
	public float getXExtent() {
		return radius;
	}
	@Override
	public float getYExtent() {
		return radius;
	}
	
}
