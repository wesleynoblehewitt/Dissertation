package engine_yuki;

/**
 * Package used to return values used during polygon collision.
 * @author Wesley
 *
 */
public class axisPackage {

	int bestIndex;
	float bestDistance;
	
	public axisPackage(int bestIndex, float bestDistance){
		this.bestIndex = bestIndex;
		this.bestDistance = bestDistance;
	}
	
	public int getIndex(){
		return bestIndex;
	}
	
	public float getDistance(){
		return bestDistance;
	}
	
}
