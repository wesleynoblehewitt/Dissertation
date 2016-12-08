package objects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public abstract class Destructible extends MyObject {
	public Destructible(Vectors position, Material material, float radius, String texture) {
		super(position, material, radius, texture);
	}

	public Destructible(Vectors position, Material material, float xExtent, float yExtent,  String texture) {
		super(position, material, xExtent, yExtent,  texture);
	}
	
	public Destructible(Vectors position, Material material, Vectors[] dimensions, String texture) {
		super(position, material, dimensions, texture);
	}
	int damage = 50;
	boolean collided = false;
	
	@Override
	public abstract void collision(MyObject collidee);
	

	public boolean collided(){
		return collided;
	}
	
	
	public abstract Animation Explosion();
	
	@Override
	public void render(Graphics g){
		Image texture = boundingShape.getTexture();
		if(texture != null){
			g.setColor(Color.white);
		g.drawImage(texture, position.X() - xExtent, position.Y() - yExtent);
		} else {
			boundingShape.render(g, position, Color.white);
		}

	}

}
