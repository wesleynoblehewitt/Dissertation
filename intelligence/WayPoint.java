package intelligence;

import engine_yuki.Vectors;

/**
 * Represents a traversable point in the game world. Used as part of a hard-coded graph that represents an interperation of the world map.
 * @author Wesley
 *
 */
public class WayPoint implements Comparable<WayPoint>{

	// Waypoint location
	Vectors location;

	// area around way point
	float xExtent;
	float yExtent;
	
	
	public WayPoint(Vectors location, float yExtent, float xExtent){
		this.location = location;
		this.xExtent = xExtent;
		this.yExtent = yExtent;
	}
	
	public Vectors getLocation(){
		return location;
	}
	
	public float getXDist(){
		return xExtent;
	}
	
	public float getYDist(){
		return yExtent;
	}

	@Override
	public int compareTo(WayPoint point) {
		if(point == null){
			return -1;
		}
		Vectors loc = point.getLocation();
		
		if(location == loc){
			return 0;
		}
		return -1;
		
	}
	
	public boolean inPoint(Vectors point){
		if(location.X() - xExtent < point.X() || location.X() + xExtent > point.X() ){
			if(location.Y() + 10 > point.Y() || location.Y() - point.Y() < point.Y()){
				return true;
			}
		}
		return false;
	}
}