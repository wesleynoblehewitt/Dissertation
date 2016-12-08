package objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import engine_yuki.Constants;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Turret extends AI{

	double angle = 0;
	Shot shot = null;
	Image base = null; 
	
	public Turret(Vectors position, int direction) {
		super(position, null);
		health = 10;
		
		this.direction = direction;
		visionDistance = Constants.turretVisionDistance;
		visionHeight = Constants.turretVisionHeight; 
		id = 2;
		
		aggressive = true;
		attackRate = 300;
		deathSize = 25;
		currentState = CreatureState.stop;
		attackCoolDown = false;
		try {
			base = new Image("Images/TurretBase.png");
			if(animations != null){
				currentAnimation = animations.Still(id, direction == 1);
			}
			animationState = AnimationState.Still;
		} catch (SlickException e) {
			e.printStackTrace();
		} catch (RuntimeException e){
			System.out.println("player jump animation not found");
		}
	}

	
	@Override
	public void decide() {
		for(MyObject obj: seen){
			if(obj instanceof Player){
				// If lowest point on player <= highest point on turret
				Vectors playerLoc = obj.getPosition();
				if(playerLoc.Y() + obj.getYExtent() >= position.Y() - yExtent){
					
					// Adjust angle of turret to be pointing at player
					float x = 0;
					float y = playerLoc.Y() - position.Y();;
					if(direction == 1){
						x = playerLoc.X() - position.X();
					} else {
						x = position.X() - playerLoc.X();
					}
					
					orientation = (float) Math.atan(y/-x);
					if(orientation < Math.toRadians(-45)){
						orientation = (float) Math.toRadians(-45);
					} else if(orientation > 0){
						orientation = 0;
					}
					boundingShape.setOrientation(orientation);
					
					currentState = CreatureState.attack;
					if(!attackCoolDown){
						
						attackCoolDown = true;
						timer.schedule(new AttTT(), attackRate);
						// Calculate shot spawn location and velocity based off orientation
						Vectors spawn;
						if(direction == 1){
							 x = position.X() + xExtent + Constants.ShotSize + 1;
							 y = orientation * (x - position.X());
						} else {
							x = position.X() - xExtent - Constants.ShotSize - 1;
							y = orientation * (position.X() - x);
						}
						y = position.Y() - y;
						spawn = new Vectors(x, y);
						Vectors vel = new Vectors((float)(direction * Constants.shotSpeed * Math.cos(orientation)),(float) -(Constants.shotSpeed * Math.sin(orientation)));
						shot = new Shot(spawn, null, vel);
						if(animationState != AnimationState.Attack){
							if(animations != null){
								currentAnimation = animations.Attack(id, direction == 1, false);
							}
							animationState = AnimationState.Attack;
						}
					}
				} else {
					// do fear animation
				}
			} 
		}
		
	}

	@Override
	public void render(Graphics g, boolean paused){
		if(currentAnimation!= null){
			if(paused){
				currentAnimation.stop();
			} else {
				currentAnimation.start();
			}
		
			Image image = currentAnimation.getCurrentFrame();
			image.setRotation((float) Math.toDegrees(orientation));
			currentAnimation.draw(position.X() + (direction * xExtent), position.Y() - yExtent);
			
		
			if(animationState == AnimationState.Attack && currentAnimation.isStopped()){
				if(animations != null){
					currentAnimation = animations.Still(id, direction == 1);
				}
				animationState = AnimationState.Still;
			}
		}
		g.drawImage(base, position.X() + (direction * xExtent / 2), position.Y());
	}

	@Override
	public Destructible[] getShot(){
		Shot s = shot;
		shot = null;
		Destructible[] re = {s};
		return re;
	}

	@Override
	public void updatePosition(float dt){
	}
	@Override
	public void updatePosition(float newX, float newY){
	}
	@Override
	public void updatePosition(Vectors centre){
	}
	@Override
	public void animations(Animations ani){
		animations = ani;
		if(animations != null){
			currentAnimation = animations.Still(id, direction == 1);
		}
	}
}