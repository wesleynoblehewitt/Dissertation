package objects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Shot extends Destructible{

	public Shot(Vectors position, String texture, Vectors velocity) {
		super(position, Material.Shot, Constants.ShotSize, texture);
		damage = 55;
		this.velocity = velocity;
	}

	public Shot(Vectors position, int shotSize, String texture, Vectors velocity) {
		super(position, Material.Shot, shotSize, texture);
		damage = 55;
		this.velocity = velocity;
	}

	
	@Override
	public void collision(MyObject collidee){
		if(collidee instanceof Player){
			if(((Player) collidee).immune()){
				return;
			}
			((Player) collidee).attackedS(damage);
		} 

		if(collidee.getMaterial() != Material.Sense){
			collided = true;
		}
	}

	@Override
	public boolean collided(){
		return collided;
	}

	@Override
	public Animation Explosion() {
		Animation ani = null;
		try {
			ani = new Animation(new SpriteSheet("Images/explosion.png", 5, 5), 50);
			ani.setLooping(false);
			return ani;
		} catch (SlickException e) {
			System.out.println("explosion animaton error");
			return null;
		} catch (RuntimeException e){
			System.out.println("explosion animation not found");
			return null;
		}
	}
}
