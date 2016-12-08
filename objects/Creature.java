package objects;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import engine_yuki.BoundingBox;
import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.Maths;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public abstract class Creature extends MyObject{

	// In-game stats
	float health = 100;
	int damage = 40;
	int armor = 10;
	int magicRes = 5;
	
	int id = 1;
	int movebonus = 0;
	
	// Creature states
	CreatureState currentState = CreatureState.still;
	AnimationState animationState = AnimationState.stillR;
	int direction = 1; // The direction the creature is facing, can be either 1 (right) or -1 (left)
	int jumpDir = 1;
	
	boolean airborne = false;
	boolean immunity = false;
	// Used for creatures that have self controlled animation
	boolean aniOverride = false;
	Animations animations = null;
	
	Animation currentAnimation = null;
	Animation extraAnimation = null;
	
	Vectors prevPos = position;
	int count = 0;
	int jumpcount = 0;
	int deathSize = 0;
	Timer timer = new Timer(); 
	
	ArrayList<MyObject> targets = null;
	int attackRate = 1200;
	
	public Creature(Vectors position, Material material, Vectors[] points) {
		super(position, material, points);
		components.add(0, new Legs(new Vectors(position.X(), position.Y() + Constants.CreatureHeightMedium), Constants.CreatureHeightMedium, Constants.CreatureLegsMediumDimensions));
		
	}
	
	@Override
	public void updateVelocity(Vectors impulse, Vectors contactVector){
		super.updateVelocity(impulse, contactVector);
		angularVelocity = 0;
	}
	
	public void Command(CreatureCommand command){
		if(!immunity){
			switch(command){
			case attack:
				break;
			case attackedM:
				break;
			case attackedS:
				break;
			case jump:
				Jump();
				break;
			case moveLeft:	
				MoveLeft();
				break;
			case moveRight:	
				MoveRight();
				break;
			case still:
				Still();
				break;
			case moveForward:
				break;
			default:
				break;
			}
		}
	}
	
	public void command(){
		if(!immunity){
		switch(currentState){
			case moveLeft:
				MoveLeft();
				break;
			case moveRight:
				MoveRight();
				break;
			case still:
				Still();
				break;
			case patrol:
				Patrol();
				break;
			case reset:
				Reset();
				break;
			case moveTowards:
				moveTowards();
			default:
				break;
			}
		}
	}
	
	/**
	 * Scene has attack round, calls to all AI to see if they wish to attack, attack if in range of player and can see them
	 * if creature within attack shape then apply damage to all
	 * After attack check if creature is dead. if dead remove from ai list. if player then end game
	 * @param target
	 * @return
	 */
	public void attack(ArrayList<MyObject> targets){
		if(!immunity){
			boolean check = animationState == AnimationState.jumpR || animationState == AnimationState.jumpL|| animationState == AnimationState.fallingL|| animationState == AnimationState.fallingR ;
			if(animations != null){
			extraAnimation = animations.Attack(id, direction == 1, check);
			}
			// Store targets
			// Process attack calculations once attack animation is executed
			this.targets = targets;
			timer.schedule(new atTimer(), attackRate);
		}
	}
	
	
	public void attackedM(int damage){
		if(!immunity){
			health -= damage - armor;
			velocity.set(0, velocity.Y());
			applyForce(-direction * 25 * Constants.knockBackScale, -15 * Constants.knockBackScale);
			immune(800);
			Animation ani = null;
			if(animations != null){
			ani = animations.KnockBack(id, direction == 1);
			}
			if(ani != null){
				currentAnimation = ani;
			}
			System.out.println(health);
			System.out.println("ouch");
		}
		if(health <= 0){
			if(animations != null){
				currentAnimation = Animations.Death(id, direction == 1);
			} else {
				currentAnimation = null;
			}
		}
		
	}
	
	public void attackedS(int damage){
		if(!immunity){
			health -= damage - magicRes;
			System.out.println("oof");
			System.out.println(health);
			
			if(health <= 0){
				if(animations != null){
				currentAnimation = Animations.Death(id, direction == 1);
				} else {
					currentAnimation = null;
				}
			}
		}
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	// We only update the animation and state if the current state has changed
	protected void MoveRight(){
		direction = 1;
		velocity.setX(Constants.moveSpeed + movebonus);
		currentState = CreatureState.moveRight;
		
		if(animationState != AnimationState.WalkR && !airborne){
			animationState = AnimationState.WalkR;	
			if(animations != null){
			currentAnimation = animations.PlayerWalk(id, true);
			} else {
				currentAnimation = null;
			}
		}
	}
	
	protected void Jump(){
	//	falling();
		if(!airborne){
			velocity.setY(Constants.jumpAcc);
			currentState = CreatureState.jump;
			if(direction == 1){
				animationState = AnimationState.jumpR;
				if(animations != null){
				currentAnimation = animations.Jump(id, true);
				}
			} else {
				animationState = AnimationState.jumpL;
				if(animations != null){
				currentAnimation = animations.Jump(id, false);
				}
			}
			jumpDir = direction;

		}
	}

	protected void MoveLeft(){
		direction = -1;
		velocity.setX(-Constants.moveSpeed - movebonus);
		currentState = CreatureState.moveLeft;
		if(animationState != AnimationState.WalkL && !airborne){
			animationState = AnimationState.WalkL;
			if(animations != null){
			currentAnimation = animations.PlayerWalk(id, false);
			
			}
		}
	}
	
	/**
	 * Used as a special check when jumping to prevent errors in the animations. 
	 * @return
	 */
	private boolean jumpingCheck(){
		if((animationState == AnimationState.jumpR || animationState == AnimationState.jumpL) && !airborne){
			jumpcount++;
			if(jumpcount > 8){
				jumpcount = 0;

				return false;
			}  else {
				return true;
			}
		}
		jumpcount = 0;
		return false;
	}
	
	protected void Still(){
		// Due to being the default behaviour extra restraints are needed to deal with state changes
		
		velocity.setX(0);
		currentState = CreatureState.still;
		if(!jumpingCheck()){

			if(direction == 1){	
				if(animationState != AnimationState.stillR && !airborne){
					animationState = AnimationState.stillR;
					if(animations != null){
						currentAnimation = animations.Still(id, true);
					} else {
						currentAnimation = null;
					}
				} 
			} else {
				if(animationState != AnimationState.stillL && !airborne){
					animationState = AnimationState.stillL;
					if(animations != null){
						currentAnimation = animations.Still(id, false);
					} else {
						currentAnimation = null;
					}
				}
			}
		}
	}

	public abstract void Reset();
	// Only used by AIs, is a filler method that is overridden in children classes
	protected abstract void moveTowards();
	// Only used by AIs, is a filler method that is overridden in children classes
	protected abstract void Patrol();
	
	public void falling(){
		Legs legs = (Legs) components.get(0);

		// If standing on something that is not a sense then increment count and return
		if(legs.collided()){
			ArrayList<MyObject> collidee = legs.collidee();
			for(MyObject col: collidee){
				if(!(col instanceof Sense)){
					count++;
					if(count > 5){
						legs.reset();
						airborne = false;
						count = 0;
					}
					return;
				}
			}
		}
		
		airborne = true;
		legs.reset();
		if(!immunity){
			boolean check = animationState == AnimationState.jumpR || animationState == AnimationState.jumpL|| animationState == AnimationState.fallingL|| animationState == AnimationState.fallingR ;
			if(!check || (check && jumpDir != direction)){
			count++;
			if(count > 5){
				
				currentState = CreatureState.jump;
				if(direction == 1){
					animationState = AnimationState.fallingR;
				} else {
					animationState = AnimationState.fallingL;
				}
				if(!aniOverride && animations != null){
					currentAnimation = animations.Falling(id, direction == 1);
				}
				jumpDir = direction;
				count = 0;
			}
		}
		}
	}
	
	@Override
	public void render(Graphics g, boolean paused){
		if(extraAnimation != null){
			g.drawAnimation(extraAnimation, position.X() - xExtent, position.Y() - yExtent);
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
			g.drawAnimation(currentAnimation, position.X() - xExtent, position.Y() - yExtent);
		}
		}
	//	g.setColor(Color.black);
	//	g.drawString("" + health, position.X(), position.Y() - this.getYExtent() - 20);
	}
	
	public void immune(int i){
		immunity = true;
		timer.schedule(new ImmunityTimer(), i);
	}
	
	public int getDirection(){
		return direction;
	}
	
	public int getHealth(){
		return (int)health;
	}
	
	public int getID(){
		return id;
	}

	public void setAnimation(Animation ani){
		currentAnimation = ani;
	}
	
	public int getDeathSize(){
		return deathSize;
	}
	
	public boolean immune(){
		return immunity;
	}
	
	public void animations(Animations ani){
		animations = ani;
	}
	
	protected class ImmunityTimer extends TimerTask{

		@Override
		public void run() {
			immunity = false;
			Still();
			animationState = AnimationState.roll;
		}
		
	}
	
	protected class atTimer extends TimerTask{

		@Override
		public void run() {
			if(targets != null){
				// generate attack square? if intersection then damage
				// check if within x distance
				Vectors infront;
				BoundingBox box1 = null;
				switch(direction){
				case 1:
					infront = position.plus(new Vectors(xExtent, 0f)); 
					box1 = new BoundingBox(new Vectors(infront.X(), infront.Y() - Constants.attackRangeMy), new Vectors(infront.X() + Constants.attackRangeMx, infront.Y() + Constants.attackRangeMy));
					break;
				case -1:
					infront = position.minus(new Vectors(xExtent, 0f));
					box1 = new BoundingBox(new Vectors(infront.X() - Constants.attackRangeMx, infront.Y() - Constants.attackRangeMy), new Vectors(infront.X(), infront.Y() + Constants.attackRangeMy));
					break;
				}
				for(MyObject target: targets){
					Vectors pos = target.getPosition();
					float xExtent = target.getXExtent();
					float yExtent = target.getYExtent();
					BoundingBox box2 = new BoundingBox(new Vectors(pos.X() - xExtent, pos.Y() - yExtent), new Vectors(pos.X() + xExtent, pos.Y() + yExtent));
					if(Maths.boxVSbox(box1, box2)){
						((Creature) target).attackedM(damage);
						
					}
				
				}
				targets = null;
			}
			
		}
		
	}
}

enum AnimationState {
	WalkL,
	WalkR,
	jumpL,
	jumpR,
	stillL,
	stillR,
	fallingL, 
	fallingR, 
	Still, 
	Attack, 
	roll;
}
