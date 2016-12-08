package engine_yuki;

import java.util.ArrayList;

import objects.AI;
import objects.Animations;
import objects.Creature;
import objects.CreatureCommand;
import objects.Destructible;
import objects.Goal;
import objects.Player;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Scene handles the updating of the physics. Call Step() to perform a cycle of updating, 
 * performs broadphasing, narrowphasing and vector updating.
 * @author Wesley
 *
 */
public class Scene {

	private float dt;
	private Hasher spaceHash = new Hasher();
	private CollisionResolver CR = new CollisionResolver();
	
	Player player;
	Goal goal = null;
	
	ArrayList<MyObject> AIs = new ArrayList<MyObject>();
	
	// Array containing all the objects in the physics engine
	ArrayList<MyObject> objects = new ArrayList<MyObject>();

	
	// List of collisions collected from broad phasing, each details contains object information and there location in the object array.
	ArrayList<CollisionDetails> collisions = new ArrayList<CollisionDetails>();
	
	ArrayList<aniTuple> deaths = new ArrayList<aniTuple>();
	
	int[][] map;
	
	public Scene(Player player, float dt){
		this.player = player;
		this.dt = dt;

	}

	public Scene(float dt){
		this.dt = dt;
		player = null;
	}
	
	// Updates the velocities and positions of all objects within the physics engine
	public void Step(){
		
		// AIs on 0 health die
		ArrayList<MyObject> toDelete = new ArrayList<MyObject>();
		for(MyObject obj: AIs){
			if(((Creature) obj).getHealth() <= 0){
				toDelete.add(obj);
				int d = ((Creature) obj).getDeathSize();
				aniTuple at = null;
				if(d > 0){
					at = new aniTuple(Animations.Death(((Creature) obj).getID(), ((Creature) obj).getDirection() == 1), obj.getPosition(), obj.getXExtent(), obj.getYExtent(), d, d);
				} else { 
					at = new aniTuple(Animations.Death(((Creature) obj).getID(), ((Creature) obj).getDirection() == 1), obj.getPosition(), obj.getXExtent(), obj.getYExtent());
				}
				if(at.ani != null){
					deaths.add(at);
				}
			}
		}
		
		for(MyObject obj: toDelete){
			AIs.remove(obj);
		}
		toDelete.clear();
		// Perform broadphasing
		BroadPhase();

		// Update the velocities of all objects in the scene
		for(MyObject obj: objects){
			obj.update(dt);
		}
		
		ArrayList<MyObject> allObjs = new ArrayList<MyObject>();
		if(player != null){
			allObjs.add(player);
		}
		allObjs.addAll(AIs);
		allObjs.addAll(objects);
		
		for(MyObject obj: AIs){
			((AI) obj).run(allObjs);
			obj.update(dt);
			((AI) obj).falling();
		}
		
		if(player != null){
			player.update(dt);
			player.falling();
		}
		

		// Perform collision resolution
		NarrowPhase();	

		// Update the position of all objects in the scene, also recalculates velocities 
		for(MyObject obj: objects){
			obj.updatePosition(dt);
			if(obj.getPosition().Y() > Constants.screenHeight){
				if(obj.missing() > Constants.missingGrace){
					toDelete.add(obj);
					System.out.println("An object fell out of the world, better watch those ledges");
				}
			}
			if(obj instanceof Destructible){
				if(((Destructible) obj).collided() && !toDelete.contains(obj)){
					aniTuple ani = new aniTuple(((Destructible) obj).Explosion(), obj.getPosition(), obj.getXExtent(), obj.getYExtent(), obj.getYExtent() * 2, obj.getYExtent()*2);
					if(ani.ani != null){
						deaths.add(ani);
					}
					toDelete.add(obj);
				}
			}
		}

		
		for(MyObject obj: AIs){
			obj.updatePosition(dt);
			if(obj.getPosition().Y() > Constants.screenHeight){
				if(obj.missing() > Constants.missingGrace){
					toDelete.add(obj);
					System.out.println("an AI fell to its death");
				}
			}
		}
		for(MyObject obj: toDelete){
			objects.remove(obj);
			AIs.remove(obj);
		}
		toDelete.clear();
		
		if(player != null){
			player.updatePosition(dt);
			if(player.getPosition().Y() > Constants.screenHeight){
				if(player.missing() > Constants.missingGrace){
					player.setHealth(0);
					System.out.println("You fell to your death");
				}
			}
		}
		
		// Perform positional correction on all objects that collided
		for(int i = 0; i < collisions.size(); i++){
			if(collisions.get(i) != null){
				CorrectPositions(collisions.get(i));
			}
		}
		
		// Set acting forces to 0
		for(MyObject obj: objects){
			obj.setForce(0, 0);
			obj.setTorque(0);
		}

		
		for(MyObject obj: AIs){
			obj.setForce(0, 0);
			obj.setTorque(0);

			MyObject[] shot = ((AI) obj).getShot();
			if(shot != null){
				for(int i = 0; i < shot.length; i++){
					if(shot[i] != null){
						objects.add(shot[i]);
					}
				}
			}
		}
		
		if(player != null){
			player.setForce(0, 0);
			player.setTorque(0);
		}
	}
	
	private void BPIterate(ArrayList<MyObject> list){
		for(MyObject obj: list){
			ArrayList<MyObject> nearby = spaceHash.getNearby(obj);
			for(MyObject objB : nearby){
				if(obj.getMaterial() == Material.Solid && objB.getMaterial() == Material.Solid){
					
				} else {
					if(player != null){
						if(player.immune()){
							if(obj instanceof Player && (objB instanceof Creature || objB instanceof Destructible)){
								break;
							} else if(objB instanceof Player && (obj instanceof Creature || obj instanceof Destructible)){
								break;
							}
						}
					}
					CollisionDetails d = new CollisionDetails(obj, objB);
					if(!collisions.contains(d)){
						collisions.add(d);
					}
				}
			}
		}
	}
	
	// Broad phasing algorithm, cycles through the objects in the engine to find objects that may be colliding with one another.
	private void BroadPhase(){
		collisions.clear();
		spaceHash.clear();

		// Update the spatial hasher
		for(MyObject obj : objects){
			spaceHash.add(obj);
		}

		for(MyObject obj : AIs){
			spaceHash.add(obj);
			for(MyObject comp : obj.getComponents()){
				spaceHash.add(comp);
			}
		}
		if(player != null){
			spaceHash.add(player);
			for(MyObject comp: player.getComponents()){
				spaceHash.add(comp);
				
			}
		}
		
		if(goal != null){
			spaceHash.add(goal);
		}
			
			
		// Retrieve lists of possible collisions and add to the collisions list
		BPIterate(objects);
		BPIterate(AIs);
		for(MyObject ai: AIs){
			ArrayList<MyObject> comp = ai.getComponents();
			BPIterate(comp);
		}
			
		if(goal != null && player != null){
			ArrayList<MyObject> nearby = spaceHash.getNearby(goal);
			for(MyObject obj : nearby){
				CollisionDetails d = new CollisionDetails(player, obj);
				if(!collisions.contains(d)){
					collisions.add(d);
				}
			}
		}
		
		if(player != null){
			ArrayList<MyObject> nearby = spaceHash.getNearby(player);
				for(MyObject obj : nearby){
					if(Maths.Equal(obj.getInv_Mass(), 0) || player.immune()){
						
					} else {
						
						CollisionDetails d = new CollisionDetails(player, obj);
						if(!collisions.contains(d)){
							collisions.add(d);
						}
					}
				}
			}
		}

	// Takes the results from broad phasing and applies accurate collision detection and resolution to them.
	private void NarrowPhase(){

		CollisionDetails details ;
		for(int i = 0; i < collisions.size(); i++){
			details = collisions.get(i);
			CollisionType type = CollisionType.identify(details.getA().getBoundingShape(), details.getB().getBoundingShape());

			switch(type){
			case polygonVSpolygon: 	
				details = CR.PolygonVsPolygon(details);
				break;
			case polygonVScircle:
				details = CR.PolygonVsCircle(details);
				break;
			case circleVSpolygon:
				details = CR.CircleVsPolygon(details);
				break;
			case circleVScircle:
				details = CR.CircleVsCircle(details);
				break;
			}

			if(details != null){
			
				MyObject A = details.getA();
				MyObject B = details.getB();

				if(A.contains(B) || B.contains(A)){
					collisions.set(i, null);
				} else {	
					A.collision(B);
					B.collision(A);
					
					if(A.intangible() || B.intangible()){
						collisions.set(i, null);
					} else { 
					
						details = Resolve(details);
						collisions.set(i, details);
					}
				}
			} else {
				collisions.set(i, null);
			}
		}
	}

	private CollisionDetails Resolve(CollisionDetails currentCollision){
		MyObject A = currentCollision.getA();
		MyObject B = currentCollision.getB();
		
		int contacts = currentCollision.getCC();
		ArrayList<Vectors> points = currentCollision.getContacts();
		Vectors collisionNormal = currentCollision.getCN();
		
		float elasticity;
		elasticity= Maths.minRes(A.getRestitution(), B.getRestitution());
		
		// elasticity calculation
		for(int i = 0; i < contacts; i++){
			Vectors radiusA = points.get(i).minus(A.getPosition());
			Vectors radiusB = points.get(i).minus(B.getPosition());
			
			Vectors relativeVel = (B.getVelocity().plus(Maths.CrossProduct(B.getAngularVelocity(), radiusB)))
									.minus(A.getVelocity().minus(Maths.CrossProduct(A.getAngularVelocity(), radiusA)));
			
			if(relativeVel.LengthSqr() < (Vectors.timesFloat(Constants.gravity, dt)).LengthSqr() + Constants.epsilon){
				elasticity = 0.0f;
			}
		}
		
		
		// Collision Resolution
		Vectors AVel = A.getVelocity();
		Vectors BVel = B.getVelocity();
		float Aangvel = A.getAngularVelocity();
		float Bangvel = B.getAngularVelocity();
		ResolutionInfo[] impulses = new ResolutionInfo[contacts];
		
		for(int i = 0; i < contacts; i++){
			// Calculate radii from centre of mass to contact point. We use shape centre as COM to simplify calculations
			Vectors radiusA = points.get(i).minus(A.getPosition());
			Vectors radiusB = points.get(i).minus(B.getPosition());

			// Find relative velocity
			Vectors relativeVel = BVel.plus(Maths.CrossProduct(Bangvel, radiusB))
					.minus(AVel.minus(Maths.CrossProduct(Aangvel, radiusA)));

			// Velocity along collision normal
			float contactVel = Vectors.DOT(relativeVel, collisionNormal);		

			// If objects already moving apart then ignore the collision
			if(contactVel > 0){
				return currentCollision;
			}

			float raCrossN = Maths.CrossProduct(radiusA, collisionNormal);
			float rbCrossN = Maths.CrossProduct(radiusB, collisionNormal);

			// Calculate denominator of impulse calculation
			// invMassSum = (1/massA + 1/massB + (raCrossN)^2 * 1/inertiaA + (rbCrossN)^2 * 1/inertiaB
			float invMassSum = A.getInv_Mass() + B.getInv_Mass();
			// Apply orientational mass
			invMassSum += Maths.Square(raCrossN) * A.getInv_Inertia() + Maths.Square(rbCrossN) * B.getInv_Inertia();

			// Calculate impulse scalar 
			float scalar = -(1.0f + elasticity) * contactVel;
			scalar /= invMassSum;
			scalar /= contacts;
			impulses[i] = new ResolutionInfo(scalar, radiusA, radiusB, invMassSum);
		}

		for(int i = 0; i < contacts; i++){
			ResolutionInfo info = impulses[i];
			
			Vectors impulse = Vectors.timesFloat(collisionNormal, info.getScalar());
			Vectors radiusA = info.getRadiusA();
			Vectors radiusB = info.getRadiusB();
			
			A.updateVelocity(Vectors.inverse(impulse), radiusA);;
			B.updateVelocity(impulse, radiusB);

			// Recalculate relative velocity
		
			if(!(A instanceof Creature) && !(B instanceof Creature)){
				Vectors relativeVel = B.getVelocity().plus(Maths.CrossProduct(B.getAngularVelocity(), radiusB))
						.minus(A.getVelocity().minus(Maths.CrossProduct(A.getAngularVelocity(), radiusA)));
				Vectors t = relativeVel.minus(Vectors.timesFloat(collisionNormal, Vectors.DOT(relativeVel, collisionNormal)));
				
				t.normalize();
	
				// Find impulse tangent magnitude
				float jt = -Vectors.DOT(relativeVel, t);
				jt /= info.getInvMassSum();
				jt /= contacts;
				
				// Ignore friction if value is very small
				if(Maths.Equal(jt, 0.0f)){
					return currentCollision;
				}
				
				// We clamp the friction magnitude by identifying the type of friction that affects it
				Vectors tangentImpulse;
				if(Math.abs(jt) < info.getScalar() * Maths.PythagoreanSolve(A.getStaticFriction(), B.getStaticFriction()) ){
					tangentImpulse = Vectors.timesFloat(t, jt);
				} else {
					tangentImpulse = Vectors.timesFloat(t,-info.getScalar() * Maths.PythagoreanSolve(A.getDynamicFriction(), B.getDynamicFriction()));
				}
				A.updateVelocity(Vectors.inverse(tangentImpulse), radiusA);
				B.updateVelocity(tangentImpulse, radiusB);
			}
		}
		currentCollision.updateObjects(A, B);
		return currentCollision;
	}

	// Updates the position of the objects once the impulse resolution has been completed. Objects positions are only updated if penetration depth
	// is above a set threshold, this prevents object jittering.
	private void CorrectPositions(CollisionDetails currentCollision){
		
		MyObject A = currentCollision.getA();
		MyObject B = currentCollision.getB();
		
		Vectors CN = currentCollision.getCN();
		float PD = currentCollision.getPD();
		
		// We only perform the correction if the penetration depth is above the slop value. This prevents object jittering. 
		// find the correction distance
		float inv_mass = A.getInv_Mass() + B.getInv_Mass();
		float correctionX;
		float correctionY;
		PD = PD - Constants.slop;
		PD = Math.max(PD, 0);
		correctionX = PD / inv_mass * Constants.CorrectionPercent * CN.X() ;
		correctionY = PD / inv_mass * Constants.CorrectionPercent * CN.Y() ;
		
		// update the positions of the objects based off their mass
		inv_mass = A.getInv_Mass();
		Vectors position = A.getPosition();
		position = position.minus(new Vectors(inv_mass * correctionX, inv_mass * correctionY));
		A.updatePosition(position);
		
		inv_mass = B.getInv_Mass();
		position = B.getPosition();	
		position = position.plus(new Vectors(inv_mass * correctionX, inv_mass * correctionY));
		B.updatePosition(position);	
	}
	
	// Method for rendering the game's objects.
	public void renderGame(Graphics g, boolean paused){
		for(MyObject obj: objects){
			
			if(obj.getMaterial() != Material.Solid){
				g.setColor(obj.getColor());
			} else {
				g.setColor(Color.lightGray);
			}
			
				obj.render(g);
		    // calculate an interpolated transform for rendering
		}
		
		for(MyObject obj: AIs){
			g.setColor(obj.getColor());
			obj.render(g, paused);
		}
		
		if(player != null){
			g.setColor(player.getColor());
			player.render(g, paused);
		}
		
		renderDeaths(g);
	}
	
	public void renderDeaths(Graphics g){
		ArrayList<aniTuple> toDelete = new ArrayList<aniTuple>();
		for(int i = 0; i < deaths.size(); i++){
			aniTuple at = deaths.get(i);
			if(at.ani.isStopped()){
				toDelete.add(at);
			} else {
				//g.drawAnimation(at.ani, at.pos.X() - at.xE, at.pos.Y() - at.yE);
				if(at.width > 0){
				at.ani.draw(at.pos.X() - at.xE*2, at.pos.Y() - at.yE, at.width, at.height);
				} else {
					at.ani.draw(at.pos.X() - at.xE, at.pos.Y() - at.yE);
				}
			}
		}
		for(aniTuple a: toDelete){
			deaths.remove(a);
		}
	}
	
	public void updateMap(int[][] map){
		this.map = map;
	}
	
	public void addObject(MyObject A){
		objects.add(A);
	}

	
	public void addAI(AI A){
		AIs.add(A);
	}
	
	public ArrayList<MyObject> getAIs(){
		return AIs;
	}
	
	// Commands the player with a new action
	public void Command(CreatureCommand command){
		if(player != null){
			if(command == CreatureCommand.attack){
				player.attack(AIs);
			}
			player.Command(command);
		}
	}
	
	// Commands the player to follow its current state
	public void Command(){
		player.command();
	}
	
	public Player getPlayer(){
		return player;
	}
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public void setGoal(Goal goal){
		this.goal = goal;
	}
	// Remove an object from the scene
	public void removeObject(MyObject A){
		objects.remove(A);
	}
	
	// Remove an object by index location from the scene
	public void removeObject(int A){
		objects.remove(A);
	}
	
	public void clear(){
		objects.clear();
		collisions.clear();
		AIs.clear();
	}
	
	public ArrayList<MyObject> getObjects(){
		return objects;
	}

	public ArrayList<MyObject> getAI(){
		return AIs;
	}
	
	public boolean won(){
		if(goal != null){
			return goal.won();
		} else {
			return false;
		}
	}
	
	public void playerDied(){
		player.setAnimation(Animations.Death(player.getID(), player.getDirection() == 1));
	}
	
	public class aniTuple{
		public Vectors pos;
		public Animation ani;
		public float xE;
		public float yE;
		public float width = 0;
		public float height = 0;
		public aniTuple(Animation ani, Vectors pos, float xE, float yE){
			this.pos = pos;
			this.ani = ani;
			this.xE = xE;
			this.yE = yE;
		}
		public aniTuple(Animation ani, Vectors pos, float xE, float yE, float width, float height){
			this.pos = pos;
			this.ani = ani;
			this.xE = xE;
			this.yE = yE;
			this.width = width;
			this.height = height;
		}
	}
}

