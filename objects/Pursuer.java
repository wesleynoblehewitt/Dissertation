package objects;

import intelligence.NavigationGraph;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Pursuer extends AI {

	
	public Pursuer(Vectors position, NavigationGraph graph) {
		super(position, graph);
		aggressive = true;
	}

	@Override
	public void decide(){
		Aura aura = (Aura) components.get(1);
		
		// decision tree -- react to sight, then aura
		// sight - if player seen then move to attack
		// aura - if player sensed then turn to see player
		// if nothing then patrol (return to original position?)
		
		
		// If an object has been seen then AI is facing in the same direction, no need for specifics
		for(MyObject obj: seen){
			if(obj instanceof Player){
				if(obj.getPosition().X() > position.X() + 25 || obj.getPosition().X() < position.X() - 25){
					pathFind(obj.getPosition());
					aura.reset();
					return;
				} else {
					if(!aggressive){
						Command(CreatureCommand.jump);
					}
					currentState = CreatureState.still;
					attack((Player) obj);
					return;
				}
			}

		}
		
		if(aura.collided()){
			// aura only ever stores one value that is always a player object
			Player target = (Player) aura.collidee.get(0);
			pathFind(target.getPosition());
			if(target.getPosition().X() < position.X()){
				
				direction = -1;
				aura.reset();
				
			} else if(target.getPosition().X() > position.X()){
				direction = 1;
				aura.reset();
				
			}
			return;
		}
			currentState = CreatureState.patrol;
	}


	public void setPatrolDistance(int distance){
		patrolDistance = distance;
	}

	public void setDirection(int i) {
		if(i == 1 || i == -1){
			direction = i;
		}		
	}
}
