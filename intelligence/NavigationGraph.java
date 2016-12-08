package intelligence;

import java.util.ArrayList;

import engine_yuki.Maths;
import engine_yuki.Vectors;

public class NavigationGraph {

	ArrayList<WayPoint> nodes = new ArrayList<WayPoint>();
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	public void add(WayPoint A){
		nodes.add(A);
	}
	
	public void addEdge(WayPoint A, WayPoint B){
		if(nodes.contains(A) && nodes.contains(B)){
			float cost = Cost(A, B);
			Edge e1 = new Edge(A, B, cost);
			Edge e2 = new Edge(B, A, cost);
			edges.add(e1);
			edges.add(e2);
		}
	}
		
	public int indexOf(WayPoint point){
		return nodes.indexOf(point);
	}
	
	public int indexOf(Edge edge){
		return edges.indexOf(edge);
	}
	
	/**
	 * Adjust this to change how the the heuristic value h is calculated
	 * @param A
	 * @param B
	 * @return
	 */
	public float Cost(WayPoint A, WayPoint B){
		return Maths.manhattanDist(A.getLocation(), B.getLocation());
	}
	
	public ArrayList<WayPoint> getNeighbours(WayPoint point){
		ArrayList<WayPoint> neighbours = new ArrayList<WayPoint>();
		for(Edge edge: edges){
			if(edge.linked(point)){
				neighbours.add(edge.getTo());
			}
		}
		return neighbours;		
	}
	
	public WayPoint inGraph(Vectors point){
		for(WayPoint wayPoint: nodes){
			if(wayPoint.inPoint(point)){
				return wayPoint;
			}
		}
		
		return null;
	}
	
}
