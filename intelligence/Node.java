package intelligence;


public class Node implements Comparable<Node> {

	private WayPoint node;
	private Node cameFrom;
	
	//Distance from source along optimal path
	private float g;
	//Estimate of distance from current node to the goal
	private float h;
	
	
	public Node(WayPoint node, Node cameFrom, float g, float h){
		this.node = node;
		this.cameFrom = cameFrom;
		this.g = g;
		this.h = h;
	}
	
	public Node(WayPoint node, float g, float h){
		this.node = node;
		cameFrom = null;
		this.g = g;
		this.h = h;
	}
	
	public void setFrom(Node from){
		cameFrom = from;
	}
	
	public void setG(float g){
		this.g = g;
	}
	
	public void setH(float h){
		this.g = h;
	}
	
	public float getG(){
		return g;
	}
	
	public float getF(){
		return g + h;
	}
	
	public WayPoint getWayPoint(){
		return node;
	}
	
	public Node getPrevious(){
		return cameFrom;
	}
	
	@Override
	public int compareTo(Node n) {
		if(this.getF() < n.getF()){
			return -1;
		} else if(this.getF() > n.getF()){
			return 1;
		} else {
			return 0;
		}
		
	}
}