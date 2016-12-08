package objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.Vectors;

/**
 * The main player object, this object is controlled by the user and is the primary method of interaction with the game world
 * The player object is considered a box
 * @author Wesley
 *
 */
public class Player extends Creature{

	float maxHealth = health;
	
	public Player(Vectors position) {
		super(position, Material.Creature, Constants.CreatureDimensionsMedium);
	}

	public void ini(Animations ani){
		health = 500;
		maxHealth = health;
		damage = 80;
		armor = 20;
		magicRes = 16;
		id = 0;
		attackRate = 210;
		movebonus = Constants.playerBonus;
		animations = ani;
		if(animations != null){
			currentAnimation = animations.Still(id, true);
		}
	}

	public void roll(){
		if(!immunity){
			timer.schedule(new ImmunityTimer(), 1000);
			immunity = true;
			velocity.set(direction * 50, 0);
			if(animations != null){
				currentAnimation = animations.Roll(direction == 1);
			}
		}
	}

	@Override
	public void Reset() {
	}

	@Override
	protected void moveTowards() {
	}

	@Override
	protected void Patrol() {
	}

	@Override
	public void render(Graphics g, boolean paused){
		g.setColor(Color.red);
		g.fillRect(20.0f, 10.0f, 300.0f, 20.0f);
		g.setColor(Color.green);
		g.fillRect(20.0f, 10.0f, (health / maxHealth) * 300.0f, 20.0f);
		if(extraAnimation != null){
			if(direction == -1){
				g.drawAnimation(extraAnimation, position.X() - xExtent * 1.6f, position.Y() - yExtent);
			} else {
				g.drawAnimation(extraAnimation, position.X() - xExtent * 0.8f, position.Y() - yExtent);
			}

			if(extraAnimation.isStopped()){
				extraAnimation = null;
			}
			return;
		} else {
			if(currentAnimation == null){
				boundingShape.render(g, position, getColor());
			} else {
				if(paused){
					currentAnimation.stop();
				} else {
					currentAnimation.start();
				}
				if(direction == -1){
					g.drawAnimation(currentAnimation, position.X() - xExtent * 1.6f, position.Y()- yExtent);
				} else {
					g.drawAnimation(currentAnimation, position.X() - xExtent * 0.8f, position.Y()- yExtent);
				}
			}
		}

	}
}
