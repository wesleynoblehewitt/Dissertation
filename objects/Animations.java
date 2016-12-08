package objects;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/**
 * Class for loading animations. Each method is called with the creatures id and orientation
 * The parameters are used to determine what animation should be loaded
 * @author Wesley
 *
 */
public class Animations {
	
	public static Animation characterIdle = null; 
	public static Animation enemy1StillR = null;
	public static Animation TurretGunR = null;
	public static Animation trollinIdleR = null;
	public static Animation trollinAuraStillR = null;
	public static Animation characterIdle2 = null;
	public static Animation enemy1StillL = null;
	public static Animation TurretGunL = null;
	public static Animation trollinIdle = null;
	public static Animation trollinAuraStillL = null;
	public static Animation characterWalkR = null;
	public static Animation enemy1ShuffleR = null;
	public static Animation characterWalkL = null;
	public static Animation enemy1ShuffleL = null;
	public static Animation playerJumpR = null;
	public static Animation playerJumpL = null;
	public static Animation skelebumJumpR = null;
	public static Animation skelebumJumpL = null;
	public static Animation trollinJumpR = null;
	public static Animation trollinJumpL = null;
	public static Animation playerJumpAttR = null;
	public static Animation playerJumpAttL = null;
	public static Animation skelebumJumpAttR = null;
	public static Animation skelebumJumpAttL = null;
	public static Animation attackR = null;
	public static Animation attackL = null;
	public static Animation skelebumAttackR = null;
	public static Animation skelebumAttackL = null;
	public static Animation turretAttackR = null;
	public static Animation turretAttackL = null;
	public static Animation trollinIceThrow = null;
	public static Animation trollinIceShot = null;
	public static Animation characterKBR = null;
	public static Animation characterKBL = null;
	public static Animation skelebumKBR = null;
	public static Animation skelebumKBL = null;
	public static Animation trollinHitR = null;
	public static Animation trollinHitL = null;
	public static Animation rollR = null;
	public static Animation rollL = null;
	
	public Animations(){
		try {
			characterIdle = new Animation(new SpriteSheet("Images/characterIdle.png", 20, 20), 600);
			enemy1StillR = new Animation(new SpriteSheet("Images/enemy1StillR.png", 20, 20), 600);
			TurretGunR = new Animation(new SpriteSheet("Images/TurretGunR.png", 20, 10), 100);
			trollinIdleR = new Animation(new SpriteSheet("Images/trollinIdleR.png", 60, 60), 600);
			trollinAuraStillR = new Animation(new SpriteSheet("Images/trollinAuraStillR.png", 100, 90), 100);
			characterIdle2 = new Animation(new SpriteSheet("Images/characterIdle2.png", 20, 20), 600);
			enemy1StillL = new Animation(new SpriteSheet("Images/enemy1StillL.png", 20, 20), 600);
			TurretGunL = new Animation(new SpriteSheet("Images/TurretGunL.png", 20, 10), 100);
			trollinIdle = new Animation(new SpriteSheet("Images/trollinIdle.png", 60, 60), 600);
			trollinAuraStillL = new Animation(new SpriteSheet("Images/trollinAuraStillL.png", 100, 90), 100);
			characterWalkR = new Animation(new SpriteSheet("Images/characterWalkRight.png", 20, 20), 100);
			enemy1ShuffleR = new Animation(new SpriteSheet("Images/enemy1ShuffleR.png", 20, 20), 200);
			characterWalkL = new Animation(new SpriteSheet("Images/characterWalkLeft.png", 20, 20), 100);
			enemy1ShuffleL = new Animation(new SpriteSheet("Images/enemy1Shuffle.png", 20, 20), 200);
			playerJumpR =  new Animation(new SpriteSheet("Images/jumpRight.png", 20, 25), 100);
			playerJumpL = new Animation(new SpriteSheet("Images/jumpLeft.png", 20, 25), 100);
			skelebumJumpR = new Animation(new SpriteSheet("Images/skelebumJumpR.png", 20, 20), 100);
			skelebumJumpL = new Animation(new SpriteSheet("Images/skelebumJumpL.png", 20, 20), 100);
			trollinJumpR = new Animation(new SpriteSheet("Images/trollinJumpR.png", 60, 80), 200);
			trollinJumpL = new Animation(new SpriteSheet("Images/trollinJumpL.png", 60, 80), 200);
			playerJumpAttR = new Animation(new SpriteSheet("Images/JumpAtt1.png", 25, 20), 70);
			playerJumpAttL = new Animation(new SpriteSheet("Images/JumpAtt2.png", 25, 20), 70);
			skelebumJumpAttR = new Animation(new SpriteSheet("Images/skelebumJumpAttackR.png", 20, 20), 100);
			skelebumJumpAttL = new Animation(new SpriteSheet("Images/skelebumJumpAttackL.png", 20, 20), 100);
			attackR =  new Animation(new SpriteSheet("Images/attackRight.png", 25, 20), 70);
			attackL =  new Animation(new SpriteSheet("Images/attackLeft.png", 25, 20), 70);
			skelebumAttackR = new Animation(new SpriteSheet("Images/skelebumAttackR.png", 20, 20), 200);
			skelebumAttackL = new Animation(new SpriteSheet("Images/skelebumAttackL.png", 20, 20), 200);
			turretAttackR = new Animation(new SpriteSheet("Images/TurretGunFireR.png", 20, 10), 50);
			turretAttackL = new Animation(new SpriteSheet("Images/TurretGunFire.png", 20, 10), 50);
			trollinIceThrow  = new Animation(new SpriteSheet("Images/trollinIceThrow.png", 100, 90), 50);
			trollinIceShot = new Animation(new SpriteSheet("Images/trollinIceShot.png", 100, 90), 100);
			characterKBR = new Animation(new SpriteSheet("Images/characterKBR.png", 20, 20), 100);
			characterKBL = new Animation(new SpriteSheet("Images/characterKBL.png", 20, 20), 100);
			skelebumKBR = new Animation(new SpriteSheet("Images/skelebumKBR.png", 20, 20), 50);
			skelebumKBL = new Animation(new SpriteSheet("Images/skelebumKBL.png", 20, 20), 50);
			trollinHitR = new Animation(new SpriteSheet("Images/trollinHitR.png", 70, 70), 200);
			trollinHitL = new Animation(new SpriteSheet("Images/trollinHitL.png", 70, 70), 200);
			rollR = new Animation(new SpriteSheet("Images/rollR.png", 20, 20), 50);
			rollL = new Animation(new SpriteSheet("Images/rollL.png", 20, 20), 50);
			System.out.println("successful load");
		} catch (SlickException e) {
			System.out.println("failed to load all animations");
		} catch (RuntimeException e){
			System.out.println("failed to find all animations");
		}
	}
	
	public Animation Still(int id, boolean right){
			if(right){
				switch(id){
				case 0: return characterIdle;
				case 1: return enemy1StillR;
				case 2: return TurretGunR;
				case 3: return trollinIdleR;
				case 4: return trollinAuraStillR;
				}
			} else {
				switch(id){
				case 0: return characterIdle2;
				case 1: return enemy1StillL;
				case 2: return TurretGunL;
				case 3: return trollinIdle;
				case 4: return trollinAuraStillL;
				}
			}
			return null;	
	}
		
	public Animation PlayerWalk(int id, boolean right){
			if(right){
				switch(id){
				case 0: return characterWalkR;
				case 1: return enemy1ShuffleR;
				}
			} else {
				switch(id){
				case 0: return characterWalkL;
				case 1: return enemy1ShuffleL;
				}
			}
			return null;
	}
	
	// Only use a small part of the jump animation
	public Animation Jump(int id, boolean right){
			Animation ani = null;
			if(right){
				switch(id){
				case 0: ani =  playerJumpR;
				break;
				case 1: ani = skelebumJumpR;
				break;
				case 3: ani = trollinJumpR;
				break;
				}
			} else {
				switch(id){
				case 0: ani = playerJumpL;
				break;
				case 1: ani = skelebumJumpL;
				break;
				case 3: ani = trollinJumpL;
				break;
				}
			}
			if(ani != null){
				ani.restart();
				ani.setLooping(false);
				ani.stopAt(2);
			}
			return ani;
	}
	
	public Animation Attack(int id, boolean right, boolean jump){
			Animation ani = null;
			if(right){
				if(jump){
					switch(id){
					case 0: ani = playerJumpAttR;
					break;
					case 1: ani = skelebumJumpAttR;
					break;
					}
				} else {
					switch(id){
					case 0: ani =  attackR;
					break;
					case 1: ani = skelebumAttackR;
					break;
					case 2: ani = turretAttackR;
					break;
					}
				}
			} else {
				if(jump){
					switch(id){
					case 0: ani = playerJumpAttL;
					break;
					case 1: ani = skelebumJumpAttL;
					break;
					}
				} else {
					switch(id){
					case 0: ani =  attackL;
					break;
					case 1: ani = skelebumAttackL;
					break;
					case 2: ani = turretAttackL;
					break;
					case 3: ani = trollinIceThrow;
					break;
					case 4: ani = trollinIceShot;
					break;
					}
				}
			}
			if(ani != null){
				ani.restart();
				ani.setLooping(false);
			}
			return ani;
	}
	
	public Animation Falling(int id, boolean right){
			Animation ani = null;
			if(right){
				switch(id){
				case 0: ani =  playerJumpR;
				ani.setLooping(false);
				ani.stopAt(2);
				ani.setCurrentFrame(2);
				break;
				
				case 1: ani = skelebumJumpR;
				ani.setLooping(false);
				ani.setCurrentFrame(2);
				break;
				}
			} else {
				switch(id){
				case 0:ani = playerJumpL;
				ani.setLooping(false);
				ani.stopAt(2);
				ani.setCurrentFrame(2);
				break;
				
				case 1: ani = skelebumJumpL;
				ani.setLooping(false);
				ani.setCurrentFrame(2);
				break;
				}
			}
			if(ani != null){
				ani.setLooping(false);
				ani.stopAt(2);
				ani.setCurrentFrame(2);
			}
			return ani;
	}

	public static Animation Death(int id, boolean right){
		Animation ani = null;
		try{
			if(right){
				switch(id){
				case 0:
					ani = new Animation(new SpriteSheet("Images/dieright.png", 25, 20), 150);
					break;
				case 1:
					ani = new Animation(new SpriteSheet("Images/skelebumDeathR.png", 20, 20), 150);
					break;
				case 2:
					ani = new Animation(new SpriteSheet("Images/explosion.png", 5, 5), 150);
					break;
				}
			} else {
				switch(id){
				case 0:
					ani = new Animation(new SpriteSheet("Images/dieleft.png", 25, 20), 150);
					break;
				case 1:
					ani = new Animation(new SpriteSheet("Images/skelebumDeathL.png", 20, 20), 150);
					break;
				case 2:
					ani = new Animation(new SpriteSheet("Images/explosion.png", 5, 5), 150);
					break;
				case 3:
					ani = new Animation(new SpriteSheet("Images/trollinDeath.png", 70, 70), 150);
					break;
				}
			}
			ani.setLooping(false);
			return ani;	
		} catch (SlickException e){
			return null;
		} catch (RuntimeException e){
			return null;
		}
	}

	public Animation KnockBack(int id, boolean right){
		Animation ani = null;
			if(right){
				switch(id){
				case 0: ani = characterKBR;
				break;
				case 1: ani = skelebumKBR;
				break;
				case 2 : return null;
				case 3: ani = trollinHitR;
				break;
				}
			} else {
				switch(id){
				case 0: ani = characterKBL;
				break;
				case 1: ani = skelebumKBL;
				break;
				case 2: return null;
				case 3: ani = trollinHitL;
				break;
				}
			}
			ani.restart();
			ani.setLooping(false);
			return ani;
	}

	
	public Animation Roll(boolean right){
		Animation ani = null;
			if(right){
				ani = rollR;
				
			} else {
				ani = rollL;
			}
			ani.restart();
			ani.setLooping(false);	
			return ani;
	}

}
