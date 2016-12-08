package objects;

import org.newdawn.slick.Animation;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class KnockBackAura extends Destructible{

	boolean active = true;
	int KBforce = 800;
	
	public KnockBackAura(Vectors position, String texture) {
		super(position, Material.Sense, Constants.AuraDimensionsBig, texture);
		damage = 0;
		
	}

	@Override
	public void collision(MyObject collidee) {
		if(active){
			if(!(collidee instanceof Destructible)){
				if(collidee instanceof Creature){
					((Creature) collidee).immune(500);
				}
				
				collidee.setForce(-1 * KBforce * Constants.knockBackScale, 0);
				collidee.update(Constants.dt);
				
			} 	
		}
	}
	public void active(boolean active){
		this.active = active;
	}
	
	@Override
	public boolean intangible(){
		return true;
	}

	@Override
	public Animation Explosion() {
		return null;
	}
}
