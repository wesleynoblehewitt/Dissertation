package engine_yuki;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Represents a material
 * All objects are made up of a material
 * @author Wesley
 *
 */
public enum Material {

	Solid(0, 0.4f, 0, 0.8f, 0.6f, Color.darkGray),
	Earth(1, 0.8f, 1.6f, 0.4f, 0.31f, Color.red),
	Stone(2, 0.4f, 2.515f, 0.6f, 0.48f, Color.lightGray),
	Ice(3, 0.3f, 0.919f, 0.1f, 0.03f, Color.cyan),
	Wood(4, 1, 0.77f, 0.35f, 0.2f, Color.orange),
	Creature(5, 0.5f, 0.5f, 0, 0, Color.blue),
	Sense(6, 0, 0, 0, 0, Color.white), 
	Shot(7, 0, 0.1f, 0, 0, Color.white),
	Transparent(8, 0.4f, 0, 0.8f, 0.6f, Color.white), 
	TransparentM(9, 0.4f, 2.515f, 0.6f, 0.48f, Color.lightGray);
	
	private final int ID;
	private final float restitution;
	private final float density;
	private final float staticFriction;
	private final float dynamicFriction;
	private Color color;
	private Image texture = null;
	
	private Material(int ID, float restitution, float density, float staticFriction, float dynamicFriction, Color color){
		this.ID = ID;
		this.restitution = restitution;
		this.density = density;
		this.staticFriction = staticFriction;
		this.dynamicFriction = dynamicFriction;
		this.color = color;
	}
	
	private Material(int ID, float restitution, float density, float staticFriction, float dynamicFriction, Color color, String texture){
		this.ID = ID;
		this.restitution = restitution;
		this.density = density;
		this.staticFriction = staticFriction;
		this.dynamicFriction = dynamicFriction;
		this.color = color;
		try {
			this.texture = new Image("Images/grass.png");
		} catch (SlickException e) {
			// If fails to load image then a null value is set and rendering defaults to a basic polygon drawing
			this.texture = null;
			System.out.println("Slick Exception while loading");
		} catch (RuntimeException e){
			System.out.println("Failed to load texture at "+ texture);
			this.texture = null;
		} 
	}

	
	public static Material getMaterialByID(int id){
		switch(id){
		case 0: return Solid;
		case 1: return Earth;
		case 2: return Stone;
		case 3: return Earth;
		case 4: return Wood;
		case 5: return Creature;
		case 6: return Sense;
		case 7: return Shot;
		case 8: return Transparent;
		case 9: return TransparentM;
		default: return null;
		}
		
	}
	
	public int getID(){
		return ID;
	}
	
	public float getRestitution(){
		return restitution;
	}
	
	public float getDensity(){
		return density;
	}
	
	public float getStaticFriction(){
		return staticFriction;
	}
	
	public float getDynamicFriction(){
		return dynamicFriction;
	}
	
	public Color getColor(){
		return color;
	}


	public Image getTexture() {
		return texture;
	}


	public void setTexture(String texture) {
		try {
			this.texture = new Image(texture);
		} catch (SlickException e) {
			// If fails to load image then a null value is set and rendering defaults to a basic polygon drawing
			this.texture = null;
		}
		catch (RuntimeException e){
			this.texture = null;
		}
	}
}
