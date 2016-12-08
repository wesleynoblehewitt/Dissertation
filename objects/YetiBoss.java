package objects;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.Maths;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

/**
 * Special Yeti boss
 * @author Wesley
 *
 */
public class YetiBoss extends AI {
	
	
	Phase phase = Phase.first;
	boolean jumping = false;
	boolean wait = false;
	int jC = 0;
	int range = 300;
	
	ArrayList<MyObject> ices = new ArrayList<MyObject>();
	int shotCount = 0;
	boolean shotCD = false;
	int KBA;
	int i = 0;
	int k = 0;
	
	boolean stunned = false;
	boolean aniWait = false;
	
	Timer icicles = new Timer();
	Random random = new Random();
	
	int aniXOff = 0;
	int aniYOff = 0;
	
	public YetiBoss(Vectors position) {
		super(position, null, Constants.CreatureDimensionsLarge);
		aggressive = false;
		id = 3;
		health = 600;
		damage = 60;
		armor = 20;
		magicRes = 0;
		direction = -1;
		visionDistance = Constants.bossVisionDistance;
		components.add(0, new Legs(new Vectors(position.X(), position.Y() + Constants.CreatureHeightLarge)
		, Constants.CreatureHeightLarge, Constants.CreatureLegsLargeDimensions));
		components.add(1, new Aura(position, Constants.AuraDimensionsBig));
		deathSize = 70;
		aniOverride = true;
		Animation ani;
		try {
			ani = new Animation(new SpriteSheet("Images/trollinSpawnL.png", 60, 80), 100);
			ani.setLooping(false);
			aniXOff = 10;
			aniYOff = 20;
		} catch (SlickException e) {
			System.out.println("Error loading boss spawn");
			ani = null;
		} catch (RuntimeException e){
			System.out.println("Error finding boss spawn");
			ani = null;
		}
		currentAnimation = ani;
	}
	
	@Override
	public void decide() {
		boolean located = false;
		Player player = null;
		// Turn to face player
		Aura aura = (Aura) components.get(1);
		if(aura.collided()){
			// aura only ever stores one value that is always a player object
			Player tar = (Player) aura.collidee.get(0);
			player = tar;
			if(tar.getPosition().X() < position.X()){
				
				direction = -1;
				aura.reset();
				
			} else if(tar.getPosition().X() > position.X()){
				direction = 1;
				aura.reset();
				
			}
			located = true;
		}
	
		for(MyObject obj: seen){
			if(obj instanceof Player){
				located = true;
				player = (Player) obj;
			}
		}
		if(!located){
			direction = direction * -1;
			
			return;
			
		}
		
		if(located){
			switch(phase){
			case first:
				phase1(player.getPosition());
				break;
			case ftrans:
				phase1_5();
				break;
			case second:
				phase2(player.getPosition());
				break;
			case strans:
				phase2_5(player);
				break;
			case third:
				phase3(player.getPosition());
				break;
			case dead:
				dead();
				break;
			}
		}	
	}

	private Vectors calcVel(int direction, Vectors dist){
		float xSpeed = direction * 400;
		float t = dist.X()/ xSpeed; 
		float ySpeed = (dist.Y() / t) + (0.5f * Constants.gravity.Y() * t);
		return new Vectors(xSpeed, ySpeed);
	}
	
	private void jumpAni(){
		aniXOff = 10;
		aniYOff = 20;
		if(animations != null){
			currentAnimation = animations.Jump(id, direction == 1);
		}
	}
	
	private void stillAni(){
		if(phase == Phase.first){
			aniXOff = 10;
			aniYOff = 0;
			if(animations != null){
				currentAnimation = animations.Still(id, direction == 1);
			}
		} else {
			aniXOff = 30;
			aniYOff = 30;
			if(animations != null){
				currentAnimation = animations.Still(4, direction == 1);
			}
		}
	}

	private void hitAni(){
		if(animations != null){
			currentAnimation = animations.KnockBack(id, direction == 1);
		}
		aniXOff = 10;
		aniYOff = 10;
		aniWait = true;
	}
	
	private void attackAni(){
		if(phase == Phase.second){
			aniXOff = 30;
			aniYOff = 30;
			if(animations != null){
				currentAnimation = animations.Attack(id, false, false);
			}
		} else {
			aniXOff = 30;
			aniYOff = 30;
			if(animations != null){
				currentAnimation = animations.Attack(4, false, false);
			}
			currentAnimation.setLooping(true);
		}
	}
	
	private void vulnerable(boolean stunned){
		try {
			if(stunned){
				currentAnimation = new Animation(new SpriteSheet("Images/trollinTired.png", 60, 60), 400);
				aniXOff = 10;
				aniYOff = 0;
			} else {
				currentAnimation = new Animation(new SpriteSheet("Images/trollinTiredS.png", 100, 90), 400);
				aniXOff = 30;
				aniYOff = 30;
			}
		} catch (SlickException e) {
			System.out.println("boss tired animation error");
		} catch (RuntimeException e){
			System.out.println("boss tired animation not found");
		}
	}
	
	private void phase1(Vectors location){
		if(aniWait && currentAnimation.isStopped()){
			stillAni();
			aniWait = false;
		}
		
		
		float dist = (direction * location.X()) - (direction * position.X());
		if(!wait && dist < range && !airborne && !aniWait){
			target = location;
			// Jump at players location
			wait = true;
			jumping = true;
			// calculate required vel to reach location, set vel to that
			// set jumping	
			int time = 4;
			float xSpeed = direction * (dist/time) * 1.5f;
			float ySpeed = -(Constants.gravity.Y() * (time/2)) / 1.5f;
			velocity.set(xSpeed, ySpeed);
			position.translate(0, -2);
			jC = 0;
			jumpAni();
			return;
		}
		if(jumping && !airborne){
			jC++;
			if(jC > 8){
				jumping = false;
				timer.schedule(new WaitTimer(), 3800);
				velocity.set(0, 0);
				jC = 0;
				spawnIcicles();
				stillAni();
			} 
		}
	
	}
	
	private void phase1_5(){
		if(!aniWait){
			if(!airborne){
				if(Maths.withinRange(spawn.X(), (position.X( )- xExtent), (position.X() + xExtent))){
					velocity.setX(0);
					phase = Phase.second;
					System.out.println("phase 2");
					KnockBackAura KB = new KnockBackAura(spawn, null);
					components.add(KB);	
					KBA = components.indexOf(KB);
					icicles.schedule(new IcicleTimer(), 300, 1000);
					wait = false;
					timer.cancel();
					timer = new Timer();
					stillAni();
					direction = -1;
					return;
				} else {
					float dist = spawn.X() - position.X();
					float dir = dist/dist;
					int time = 4;
					float xSpeed = dir * (dist/time) * 1.45f;
					float ySpeed = -(Constants.gravity.Y() * (time/2)) / 1.5f;
					velocity.set(xSpeed, ySpeed);
					position.translate(0, -2);
				}
			}
			if(!wait){
				velocity.setX(0);
				phase = Phase.second;
				System.out.println("phase 2");
				KnockBackAura KB = new KnockBackAura(spawn, null);
				components.add(KB);	
				KBA = components.indexOf(KB);
				icicles.schedule(new IcicleTimer(), 300, 1000);
				wait = false;
				timer.cancel();
				timer = new Timer();
				position = spawn;
				stillAni();
				direction = -1;
			}
		} else {
			if(currentAnimation != null){
				if (currentAnimation.isStopped()){
					aniWait = false;
				}
			}
		}
		
	}
	
	private void phase2(Vectors location){
		position.setX(spawn.X());
		velocity.setX(0);
		if(!wait){
			if(shotCount < 3){
				shotCount++;
				attackAni();
				// generate shot
				Vectors pos = new Vectors(position.X() + direction*(xExtent + Constants.iceBallSize + 2), position.Y());
				Vectors dist = new Vectors(0, 0);
				dist.setX((direction * location.X()) - (direction * pos.X()));
				dist.setY((pos.Y() - location.Y()));
				Vectors vel = calcVel(direction, dist);
				Shot shot = new Shot(pos, Constants.iceBallSize, null, vel);
				ices.add(shot);
				timer.schedule(new WaitTimer(), 1500);
				wait = true;
			} else { 
				wait = true;
				shotCount = 0;
				stunned = true;
				timer.schedule(new VulnerableTimer(), 5000);
				((KnockBackAura) components.get(KBA)).active(false);
				vulnerable(stunned);
			}
		} else {
			if(currentAnimation != null){
				if (currentAnimation.isStopped()){
					stillAni();
				}
			}
		}
	}
	
	private void phase2_5(Player player){
		ices.add(new MyObject(new Vectors(442, 567), Material.Transparent, Constants.baseWidth, Constants.baseHeight * 1.3f));
		//ices.add(new MyObject(new Vectors(668, 56), Material.TransparentM, Constants.baseWidth, Constants.baseHeight * 1.4f));
		ices.add(new MyObject(new Vectors(297, 35), Material.TransparentM, Constants.baseWidth, Constants.baseHeightL));
		phase = Phase.third;
		try {
			currentAnimation = new Animation(new SpriteSheet("Images/trollinTrans2.png", 60, 80), 100);
			aniXOff = 10;
			aniYOff = 20;
			currentAnimation.setLooping(false);

		} catch (SlickException e) {
			System.out.println("boss transition animation error");
		} catch (RuntimeException e){
			System.out.println("boss transition animation not found");
		}
		
		((KnockBackAura) components.get(KBA)).active(true);
		
		player.setForce(direction * 800 * Constants.knockBackScale, 0);
		player.update(Constants.dt);
		
		System.out.println("phase 3");
		
		timer.cancel();
		timer = new Timer();
		wait = true;
		stunned = true;
		timer.schedule(new VulnerableTimer(), 1700);
	}
	
	private void phase3(Vectors location){
		position.setX(spawn.X());
		velocity.setX(0);
		// stuns after 10 seconds
		if(!wait){
		timer.schedule(new StunnedTimer(), 10000);
		wait = true;
		attackAni();
		}
		if(!stunned){
			if(!shotCD){
				shotCD = true;
				timer.schedule(new ShotTimer(), 500);
				
				Vectors pos = new Vectors(position.X() + direction*(xExtent + Constants.iceBallSize + 2), position.Y());
				Vectors dist = new Vectors(0, 0);
				dist.setX((direction * location.X()) - (direction * pos.X()));
				dist.setY((pos.Y() - location.Y()));
				Vectors vel = calcVel(direction, dist);
				Shot shot = new Shot(pos, Constants.iceBallSize, null, vel);
				ices.add(shot);
			}
		} else {
			if(currentAnimation != null){
				if (currentAnimation.isStopped()){
					vulnerable(stunned);
				}
			}
		}
	}
	
	private void dead(){
		if(currentAnimation.isStopped()){
			health = 0;
		}
	}
	
	private void spawnIcicles(){
		if(phase == Phase.first){
			ices.add(new Icicle(new Vectors(391, 162)));
			ices.add(new Icicle(new Vectors(512, 102)));
			ices.add(new Icicle(new Vectors(594, 142)));
			ices.add(new Icicle(new Vectors(874, 122)));
			return;
		} 
		
		 int j = random.nextInt(4);
		 while(j == i){
			 j = random.nextInt(4);
		 }
		  i = j;
		 chooseIcicle(Phase.second);
		 
		 if(phase == Phase.third){
			 int l = random.nextInt(3);
			 while(l == k){
				 l = random.nextInt(3);
			 }
			  k = l;
			  chooseIcicle(Phase.third);
		 }
	}

	private void chooseIcicle(Phase phase){
		if(phase == Phase.second){
		 switch(i){
		 case 0: ices.add(new Icicle(new Vectors(391, 161)));
		 break;
		 case 1: ices.add(new Icicle(new Vectors(512, 101)));
		 break;
		 case 2: ices.add(new Icicle(new Vectors(594, 141)));
		 break;
		 case 3: ices.add(new Icicle(new Vectors(874, 121)));
		 break;
		 }
		}
		if(phase == Phase.third){
			switch(k){
			 case 0: ices.add(new Icicle(new Vectors(644, 60)));
			 break;
			 case 1: ices.add(new Icicle(new Vectors(753, 60)));
			 break;
			 case 2: ices.add(new Icicle(new Vectors(552, 60)));
			 break;

			 }
		}
	}
	
	@Override
	public void attackedM(int damage){
		if(phase == Phase.second){
			health -= damage - armor;	
			System.out.println("oof");
			System.out.println(health);
			((KnockBackAura) components.get(KBA)).active(true);
			stunned = false;
			vulnerable(!stunned);
			if(health <= 150){
				
				System.out.println("phase change 2");
				phase = Phase.strans;
			}
		}
		if(phase == Phase.third){
			health -= damage - armor;	
			System.out.println("oof");
			System.out.println(health);
			((KnockBackAura) components.get(KBA)).active(true);
			stunned = true;
			vulnerable(!stunned);
			if(health <= 0){
				health = 0;
				wait = true;
				stunned = true;
				//currentAnimation = Animations.Death(id, direction == 1);
				aniXOff = 10;
				aniYOff = 10;
				phase = Phase.dead;
			}
		}
		
	}
	
	@Override
	public void attackedS(int damage){
		if(phase == Phase.first){
			health -= damage - magicRes;
			System.out.println("rawr");
			System.out.println(health);

			hitAni();

			if(health <= 300){
				aniWait = true;
				phase = Phase.ftrans;
				System.out.println("phase change 1");
				wait = true;
				timer.cancel();
				timer = new Timer();
				timer.schedule(new WaitTimer(), 10000);
			}
		}
		
		if(phase == Phase.third){
			timer.schedule(new VulnerableTimer(), 10000);
			((KnockBackAura) components.get(KBA)).active(false);
			stunned = true;
			System.out.println("argghh");
			hitAni();
		}
	}
	
	@Override
	public MyObject[] getShot(){
		MyObject[] temp = ices.toArray(new MyObject[ices.size()]);
		ices.clear();
		return temp;
	}
	
	@Override
	public void command(){
		
	}
	
	@Override
	public void render(Graphics g, boolean paused){
		if(currentAnimation == null){
			boundingShape.render(g, position, getColor());
		} else {	
			if(paused){
				currentAnimation.stop();
			} else {
				currentAnimation.start();
			}
			g.drawAnimation(currentAnimation, position.X() - xExtent - aniXOff, position.Y()- yExtent - aniYOff);
		}
	}
	private class WaitTimer extends TimerTask{
		@Override
		public void run() {
			wait = false;
		}	
	}

	private class VulnerableTimer extends TimerTask{

		@Override
		public void run() {
			wait = false;
			((KnockBackAura) components.get(KBA)).active(true);
			stunned = false;
			stillAni();
		}
		
	}
	
	private class StunnedTimer extends TimerTask{

		@Override
		public void run() {
			ices.add(new Icicle(new Vectors(spawn.X(), 60)));
		}		
	}
	
	private class ShotTimer extends TimerTask{

		@Override
		public void run() {
			shotCD = false;
		}
		
	}
	
	private class IcicleTimer extends TimerTask{

		@Override
		public void run() {
			spawnIcicles();
		}
		
		
	}
	
}
enum Phase {
	first, 
	ftrans, 
	second, 
	strans, 
	third, 
	dead;
}