package intelligence;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

/** 
 * Call static function search with a world map graph, starting way-point and ending way-point
 * Returns a list of way-points to follow
 * @author Wesley
 *
 */
public class AstarSearch {

	
	/**
	 * What to do?
	 * 
	 * 1. Representation of world
	 * world map? navigation graph? waypoint? navigation mesh?
	 * mesh unnecessary
	 * waypoint seems suitable
	 * graph unnecessary
	 * world map seems suitable
	 * 
	 * world map or way point
	 * world map will result in more realistic movement
	 * way point more hard code based, easier to implement, faster
	 * 
	 * waypoint method: hard code waypoint linked list. each waypoint links to all its neighbours.
	 * way points will be placed above each object centre, also contains information on area dimensions to allow more detailed navigation
	 * 
	 * structure: list of waypoints, list of edges? implement linked list structure of waypoints? graph system?
	 * graph system - waypoints treated as nodes. - good choice
	 * waypoints and edges - some unncessary computation and memory reading
	 * linked list - effective but complicated. similar to graph system
	 * 
	 *  graph uses list system
	 * 
	 * set up nodes, edges etc.
	 * 
	 * so far - Navigation graph consisting of a list of nodes and a list of edges
	 * next - have A* Search iterate over objects in graph performing a search based off a starting node and linking via edges
	 */

	public static ArrayList<WayPoint> search(NavigationGraph graph, WayPoint first, WayPoint last){
		ArrayList<Node> openSet = new ArrayList<Node>();
		ArrayList<Node> closedSet = new ArrayList<Node>();
		PriorityQueue<Node> queue = new PriorityQueue<Node>(20, new NodeComparator());
		
		Node start = new Node(first, 0, graph.Cost(first, last));
		openSet.add(graph.indexOf(first), start);
		queue.add(start);
		
		Node goal = null;
		
		while(openSet.size() > 0){
			Node current = queue.poll();
			WayPoint curPoint = current.getWayPoint();
			openSet.remove(current);
			if(curPoint.compareTo(last) == 0){
				// Reached goal
				goal = current;
				break;
			} else {
				closedSet.add(graph.indexOf(curPoint), current);
				ArrayList<WayPoint> neighbours = graph.getNeighbours(curPoint);
				for(WayPoint neighbour: neighbours){
					// for each neighbour evaluate route
					int loc = graph.indexOf(neighbour);
					Node visited = null;
					if(loc >= 0){
						visited = closedSet.get(loc);
					}
					// if not fully explored
					if(visited == null){
						float g = current.getG() + graph.Cost(curPoint, neighbour);
						Node n = openSet.get(loc);
						if(n == null){
							// New node for open set
							n = new Node(neighbour, current, g, graph.Cost(neighbour, last));
							openSet.add(loc, n);
							queue.add(n);
						} else if(g < n.getG()){
							// if a better route has been found
							n.setFrom(current);
							n.setG(g);
							n.setH(graph.Cost(neighbour, last));
						}
					}
				}	
			}
		}
		
		// If the goal has been reached then we reconstruct the path, else null
		if(goal != null){
			// Use a stack to ensure the starter node is first in the route (starting from goal moving backwards)
			Stack<WayPoint> stack = new Stack<WayPoint>();
			ArrayList<WayPoint> route = new ArrayList<WayPoint>();
			stack.push(goal.getWayPoint());
			
			
			Node parent = goal.getPrevious();
			// Iterate back through parent nodes and add to stack
			while(parent != null){
				stack.push(parent.getWayPoint());
				parent = parent.getPrevious();
			}
			
			// pop stack to list
			while(stack.size() > 0){
				route.add(stack.pop());
			}
				
			return route;
		} 
		return null;
		
	}
	
}
