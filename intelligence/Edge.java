package intelligence;

public class Edge {

	WayPoint from;
	WayPoint to;
	// Cost of travelling along this edge
	float cost;
	
	
	Edge(WayPoint from, WayPoint to, float cost){
		this.from = from;
		this.to = to;
		this.cost = cost;
	}
	
	public boolean linked(WayPoint point){
		if(from.compareTo(point) == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public WayPoint getTo(){
		return to;
	}
	
}
