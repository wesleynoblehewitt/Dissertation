package engine_yuki;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


/**
 * Every object has a bounding shape that is used to calculate if the object has collided with another
 * @author Wesley
 *
 */
public abstract class BoundingShape {
	int size = 0;
	Image texture = null;

	@Override
	public abstract BoundingShape clone();

	public abstract void setOrientation(float rads);
	
	public abstract float getOri();
	
	public abstract void render(Graphics g, Vectors position, Color color);
	
	public abstract MassData calculateMass(float density);
	
	public abstract float getRadius();
	
	public abstract float getXExtent();
	
	public abstract float getYExtent();
	
	public abstract ArrayList<Vectors> getNormals();
	
	public abstract ArrayList<Vectors> getPoints();
	
	public int getSize(){
		return size;
	}

	public void setTexture(String texture){
		try {
			this.texture = new Image(texture);
		} catch (SlickException e) {
			// If fails to load image then a null value is set and rendering defaults to a basic polygon drawing
			this.texture = null;
			System.out.println("Slick Exception while loading");
		} catch (RuntimeException e){
			System.out.println("Failed to load texture at "+ texture);
			this.texture = null;
		} 
	}
	
	public abstract Matrix2x2 getOrientation();

	public Image getTexture(){
		return texture;
	}
}