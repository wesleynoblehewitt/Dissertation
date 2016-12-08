package objects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Goal extends MyObject {

	boolean won = false;
	Animation ani = null;
	
	public Goal(Vectors position) {
		super(position, Material.Sense, 5);
		try {
			ani = new Animation(new SpriteSheet("Images/GoalShine.png", 10, 15), 150);
		} catch (SlickException e) {
			
		} catch (RuntimeException e){
			
		}
	}
    
	
	@Override
	public void collision(MyObject collidee){
		if(collidee instanceof Player){
			won = true;
		}
	}
	
	public boolean won(){
		return won;
	}
	
	@Override
	public void render(Graphics g){
		
		if(ani != null){
			ani.draw(position.X(), position.Y() - yExtent *2, 10, 15);
		} else {
			boundingShape.render(g, position, getColor());
		}
	}
}
