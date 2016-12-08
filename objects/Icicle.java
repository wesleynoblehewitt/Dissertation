package objects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Icicle extends Destructible {

	
	public Icicle(Vectors position) {
		super(position, Material.Ice, Constants.baseWidth, Constants.baseHeightL, "Images/Icicle.png");
		damage = 100;
		velocity.set(0, 50);
	}
	
	@Override
	public void collision(MyObject collidee){
		if(collidee instanceof Creature){
			if(((Creature) collidee).immune()){
				return;
			}
			((Creature) collidee).attackedS(damage);
		} 

		if(collidee.getMaterial() != Material.Sense){
			collided = true;
		}
	}

	@Override
	public Animation Explosion() {
		Animation ani = null;
		try {
			ani = new Animation(new SpriteSheet("Images/icicleExplosion.png", 40, 60), 150);
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
