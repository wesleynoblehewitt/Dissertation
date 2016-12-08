package objects;

import intelligence.AstarSearch;
import intelligence.NavigationGraph;
import intelligence.WayPoint;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import engine_yuki.BoundingBox;
import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.Maths;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

/**
 * The master class for AI in the game. All specilised AI branch out from this but still rely on the same base structure.
 * @author Wesley
 *
 */
public abstract class AI extends Creature {

	final Vectors spawn;
	
	int attackRate = 2500;
	float visionDistance = Constants.visionDistance;
	float visionHeight = Constants.visionHeight;
	Vision vision = new Vision();
	ArrayList<MyObject> seen;
	float patrolDistance = 10;
	float currentDist = new Random().nextInt(20) - 10;
	float patrolRate = 0.1f;
	
	NavigationGraph graph;
	ArrayList<WayPoint> route;
	WayPoint currentGoal = null;
	WayPoint currentPoint = null;
	int routeAttempts = 0;
	Vectors target = null;
	boolean reset = false;
	boolean aggressive = true;
	int gapCount = 0;
	
	
	Boolean override = false;
 
	Boolean attackCoolDown = false;
	
	public AI(Vectors position, NavigationGraph graph) {
		super(position, Material.Creature, Constants.CreatureDimensionsMedium);
		components.add(1, new Aura(position));
		spawn = position;
		this.graph = graph;
		
	}
	
	public AI(Vectors position, NavigationGraph graph, Vectors[] dimensions){
		super(position, Material.Creature, dimensions);
		components.add(1, new Aura(position));
		spawn = position;
		this.graph = graph;
	}
	
	// When running the AI takes a list of all the objects active in the scene, these are used for vision and decision making
	public void run(ArrayList<MyObject> allObjects){
		// The object list is pruned to include just visible objects, the list is cloned to prevent this affecting later uses of the variable
		ArrayList<MyObject> clone = new ArrayList<MyObject>(allObjects);
		clone.remove(this);
		seen = vision.see(position, direction, clone, visionDistance, visionHeight);
		
		// The AI decides what its state is based off the previous update.
		decide();
		// Execute the current state
		command();
	}
	
	
	/**
	 * When a target is located the AI will perform an A* Search to find a route to the target, it will then follow this route during its move commands
	 * @param target
	 */
	protected void pathFind(Vectors target){
		if(graph != null){
		
		WayPoint currentPoint = graph.inGraph(position);
		WayPoint goal = graph.inGraph(target);

		// if not already moving towards, or at the target position then perform pathfinding
		if(goal.compareTo(currentGoal) != 0 && goal.compareTo(currentPoint) != 0 && !route.contains(goal)){
			if(currentPoint == null || goal == null){
				route = null;
				currentGoal = null;
				this.target = null;
				routeAttempts++;
				unreachableGoal();
				return;
			} else {
				// A* search
				route = AstarSearch.search(graph, currentPoint, goal);
				if(route != null){
					currentGoal = route.get(0);
					routeAttempts = 0;
				} else {
					route = null;
					currentGoal = null;
					this.target = null;
					routeAttempts++;
					unreachableGoal();
				}
			}
		}
		
		this.target = target;
		currentState = CreatureState.moveTowards;
		} else {
			System.out.println("null graph");
		}
	}
	
	/**
	 * The main decision method for the AI, varies depending on the AI but generally will take a list of sensed objects and 
	 * based on what is seen choose an appropriate action e.g. moving towards a player 	
	 * @param seen
	 */
	public abstract void decide();

	private void jumpDecide(){
		// If an object infront of to be jumped over then jump. 
		Vectors estimateTravel = Vectors.timesFloat(velocity, Constants.dt);
		
		Vectors pos = position.plus(estimateTravel);
		BoundingBox myBox = new BoundingBox(new Vectors(pos.X() - xExtent, pos.Y() - yExtent),
				new Vectors(pos.X() + xExtent, pos.Y() + yExtent));
		// lowest point on the AI
		float lowMe = position.Y() + yExtent;
		ArrayList<MyObject> floor = new ArrayList<MyObject>();
		MyObject floorInfront = null;
		
		// find time for v = 0	
		for(MyObject obj: seen){
			
			// compare against seen items, if there is a collision and the object is jumpable, then jump
			float theirX = obj.getXExtent();
			float theirY = obj.getYExtent();
			BoundingBox theirBox = new BoundingBox(new Vectors(obj.getPosition().X() - theirX, obj.getPosition().Y() - theirY),
					new Vectors(obj.getPosition().X() + theirX, obj.getPosition().Y() + theirY)); 
			
			float hiY = obj.getPosition().Y() - theirY;
			if(Maths.boxVSbox(myBox, theirBox)){
				// highest point on the seen object

				// Due to minor object overlap, apply small leniance value to comparison
				if(hiY < lowMe - Constants.leniance && hiY > lowMe + Constants.jumpHeight){
					Command(CreatureCommand.still);
					Command(CreatureCommand.jump);
					return;
				}

				if(hiY >= lowMe - Constants.leniance){
					floorInfront = obj;
					gapCount = 0;
				}
			}

			if(hiY >= lowMe - Constants.leniance){
				floor.add(obj);
			}
		}

		// If there is a gap infront of the AI
		if(floorInfront == null){
			gapCount++;
			if(gapCount > 3){
				// Check if flooring within jumping distance, if so then opt to jump
				float jumpDist = Constants.jumpDist;

				switch(direction){
				case 1:
					for(MyObject obj: floor){
						pos = obj.getPosition();
						if(pos.X() < position.plus(jumpDist, 0).X()){
							Command(CreatureCommand.jump);
							Command(CreatureCommand.moveRight);
							override = true;
							return;
						}
					}
					//currentState = CreatureState.stop;
					break;
				case -1:
					for(MyObject obj: floor){
						pos = obj.getPosition();
						if(pos.X() > position.minus(jumpDist, 0).X()){
							Command(CreatureCommand.jump);
							Command(CreatureCommand.moveLeft);
							override = true;
							return;
						}
					}
					//		currentState = CreatureState.stop;
					break;
				}
			}
			gapCount = 0;
		}
	}
	
	/**
	 * The final part of movement, when the route is complete then the AI closes in on the final target location
	 */
	public void move() {
		
		// The AI looks infront of itself, if there is an object infront of it then it reacts accordingly
		jumpDecide();
		if(!override){
			//System.out.println("ha");
			if(target.X()  < position.X()){
				direction = -1; 
				Command(CreatureCommand.moveLeft);
			} else if(target.X()  > position.X()) {
				direction = 1;
				Command(CreatureCommand.moveRight);
			} else {
				Command(CreatureCommand.still);
			}
			return;
		}
		override = false;
	//		Command(CreatureCommand.still);
	}
	
	
	public void attack(Player target){

		if(aggressive && !attackCoolDown){
			
			ArrayList<MyObject> player = new ArrayList<MyObject>();
			player.add(target);
			attack(player);
			attackCoolDown = true;
			
			timer.schedule(new AttTT(), attackRate);
		}
	}
	
	/**
	 * Called when the path finding fails to find a route to the given target, the AI will re-attempts to move to that location over a number 
	 * of iterations, if it continues to fail then it attempts to reset its position back to its spawn.
	 */
	public void unreachableGoal() {
		 // reattempt x times then reset <-
			 
			// Pathfinding to the spawn fails then wait in position for further sense commands
			if(currentState == CreatureState.reset){
				currentState = CreatureState.still;
				return;
			}
			
			// If failed to route for 30 frames (0.5 seconds) then reset override
			if(routeAttempts > 30){
				//reset
				routeAttempts = 0;
				Reset();
			}
		}
	
	@Override
	public void Reset(){
		currentState = CreatureState.reset;
		pathFind(spawn);
		command();
	}
	
	@Override
	protected void Patrol(){
		//if can move
		if(reset){
			if(position.X() != spawn.X()){
				if(position.Xaprox(spawn.X())){
					reset = false;
							return;
				}
				Reset();
				currentDist = 0;
				//System.out.println("back");
			} else {
				
				reset = false;
			}
			return;
		}
		if(spawn.X() - patrolDistance *2  > position.X() || spawn.X() + patrolDistance *2 < position.X()){
			reset = true;
			Reset();
			currentDist = 0;
			return;

		}
		
		if(direction > 0){
			if(currentDist < patrolDistance){
				MoveRight();
				currentDist += patrolRate;
			} else {
				MoveLeft();
				currentDist -= patrolRate;
			}
		} else if(currentDist > -patrolDistance){
			MoveLeft();
			currentDist -= patrolRate;
		} else {
			MoveRight();
			currentDist += patrolRate;
		}
		
	}
	
	public void moveForward(){
		if(direction > 0){
			currentState = CreatureState.moveRight;
		} else {
			currentState = CreatureState.moveLeft;
		}
	}
	
	@Override
	protected void moveTowards(){
		if(currentGoal != null){
			if(currentGoal.inPoint(position)){
				int next = route.indexOf(currentGoal) + 1;
				route.remove(next - 1);
				currentPoint = currentGoal;
				try{
					currentGoal = route.get(next);
				} catch (IndexOutOfBoundsException e){
					route = null;
					move();
					return;
				}
				
			}
			
			// face target, move towards
			// is above? jump
			Vectors location = currentGoal.getLocation();
			
			jumpDecide();
			if(location.X()  < position.X()){
				direction = -1; 
				Command(CreatureCommand.moveLeft);
			} else {
				direction = 1;
				Command(CreatureCommand.moveRight);
			}
		} else {
			// If routing is over then move towards the specific target location
			move();
		}
	}
	
	public MyObject[] getShot(){
		return null;
	}
	
	@Override
	public void collision(MyObject collidee){
		if(collidee instanceof Player){
			if(((Player) collidee).immune()){
				return;
			}
			if(collidee.getPosition().X() > position.X() - xExtent/4 && collidee.getPosition().X() < position.X() + xExtent/4){
				((Player) collidee).attackedM(damage);
				int dir;
				if(collidee.getPosition().X() < position.X()){
					dir = -1;
					collidee.getPosition().translate(-5, 0);
				} else {
					dir = 1;
					collidee.getPosition().translate(5, 0);
				}
				
				collidee.setForce(dir * 40 * Constants.knockBackScale, -15 * Constants.knockBackScale);
				collidee.update(Constants.dt);
			}
		} 
	}
	
	
	// attack cooldown timer task
		protected class AttTT extends TimerTask{
			@Override
			public void run() {
				attackCoolDown = false;
			}
			
		}
}
