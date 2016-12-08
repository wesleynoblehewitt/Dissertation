package engine_yuki;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Rectangular bounding shape.
 * This bounding shape is never used as a concrete hit box, and instead as a child object for detecting minor collisions.
 * @author Wesley
 *
 */
public class BoundingBox extends BoundingShape{

	Matrix2x2 u;
	// Lower bound of x and y, top left
	Vectors lower;
	// Upper bound of x and y, bottom right
	Vectors upper;

	
	public BoundingBox(Vectors lower, Vectors upper){
		this.lower = lower;
		this.upper = upper;
	}
	
	public void setlower(Vectors lower){
		this.lower = lower;
	}
	
	public void setUpper(Vectors upper){
		this.upper = upper;
	}
	
	public Vectors getLower(){
		return lower;
	}
	
	public Vectors getUpper(){
		return upper;
	}
	
	public void updatePosition(Vectors lower, Vectors upper) {
		this.lower = lower;
		this.upper = upper;
		
	}

	@Override
	public BoundingShape clone() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOrientation(Matrix2x2 u){
		this.u = u;
	}
	
	@Override
	public void setOrientation(float rads) {
		u = new Matrix2x2(rads);
		
	}

	@Override
	public void render(Graphics g, Vectors position, Color color) {
	}

	@Override
	public MassData calculateMass(float density) {

		return new MassData(0, 0);
	}

	@Override
	public float getOri() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Matrix2x2 getOrientation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getRadius() {
		float x = upper.X() - lower.X();
		float y = lower.Y() - upper.Y();
		return Math.max(x, y);
	}

	@Override
	public ArrayList<Vectors> getNormals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Vectors> getPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getXExtent() {
		return upper.X() - lower.X();
	}

	@Override
	public float getYExtent() {
		return lower.Y() - upper.Y();
	}

}
