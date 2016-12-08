package objects;

import java.util.ArrayList;

import engine_yuki.Material;
import engine_yuki.Maths;
import engine_yuki.MyObject;
import engine_yuki.Tuple;
import engine_yuki.Vectors;

//note: a lot of for loops in this, may cause slow down. keep an eye on it

/**
 * A special vision class. When given a list of objects and a location it will identify any objects that are visible from the given local.
 * Call the method see(position, dir, objectList) where dir is the direction that the viewer is facing (1 for right, -1 for left)
 * @author Wesley
 *
 */
public class Vision {

	public ArrayList<MyObject> see(Vectors position, int dir,  ArrayList<MyObject> objects, float visionDistance, float visionHeight){
		ArrayList<rayInfo> Mayseen = new ArrayList<rayInfo>();
		ArrayList<MyObject> seen = new ArrayList<MyObject>();
		
		/*
		 detect if position is in vision range and unobstructed
		 	fire a ray? rays? if ray makes contact then record object, destroy ray after, no duplicates 
		 		multiples rays - across arc? how many? 1 per degree?
		 			arc +- 45 degrees from centre = 90 rays 
		 			issue - objects not sorted by distance
		 				sort by euclidean distance?
		 				non sorted method? destroy rays after certain distance not collision, repeat with destroy on new list - issue, still not sorted ya mug
		 				sorted method? prune array based on distance + direction, quicksort new array, perform ray tracing - possibility
		 					sorted method. sorting no more than 30 odd objects, reasonabily computationally light
		 	ray collision - line vs polygon/circle, generate equation of line (m + c + 2 vectors).
		 		line vs box? simplified, may cause errors in sight (miss small objects / catch large obejcts)
		 		
		 
		 */
		
		// Prune objects from the list, objects that are behind, too far ahead of, too far above/below the viewer are removed.
		// This is done to shorten the array significantly and make the sorting faster.
		float x = position.X();
		float y = position.Y();
		
		ArrayList<MyObject> locs = new ArrayList<MyObject>();
		
		// If objects are outside the vision bounds then they are removed from the list
		
		if(dir == 1){
			for(int i= 0; i < objects.size(); i++){
				MyObject obj = objects.get(i);
				float Ox = obj.getPosition().X();
				float Oy = obj.getPosition().Y();
				float radius = obj.getRadius();
				
				if(Ox + radius <= x || Ox - radius > x + visionDistance || Oy + radius < y - visionHeight || Oy - radius > y + visionHeight){
					locs.add(objects.get(i));
				}
			}

		} else {
			for(int i= 0; i < objects.size(); i++){
				MyObject obj = objects.get(i);
				float Ox = obj.getPosition().X();
				float Oy = obj.getPosition().Y();
				float radius = obj.getRadius();

				if(Ox - radius >= x || Ox + radius < x - visionDistance || Oy + radius < y - visionHeight || Oy - radius > y + visionHeight){
					locs.add(objects.get(i));
				}
			}
		}
		
		for(MyObject obj: locs){
			objects.remove(obj);
		}

		// Sort the array by euclidean distance from the viewer
		objects = Maths.Sort(position, objects);
		
		
		// Generate a bounding box and edges for each remaining object, these boxes are used for the ray tracing
		for(MyObject obj: objects){
			if(obj.getMaterial() != Material.Transparent && obj.getMaterial() != Material.TransparentM){
				float xExtent = obj.getXExtent();
				float yExtent = obj.getYExtent();
				Vectors pos = obj.getPosition();
				// Calculate min and max vectors for the shape
				// Top left vector
				Vectors min = new Vectors(pos.X() - xExtent, pos.Y() - yExtent);
				// Bottom right vector
				Vectors max = new Vectors(pos.X() + xExtent, pos.Y() + yExtent);
				Mayseen.add(new rayInfo(min, max, obj));
			}
		}
		
		// find Y for ray vector
		// tan(0) = opposite / adjacent - we need to calculate opposite
		// Position.Y() + (tan(i) * visionDistance)
		for(int i = -45; i < 46; i++){
			float Y = y;
			double radians = Math.toRadians(i);
			Y += Math.tan(radians) * (visionDistance);
			Tuple line;
			
			if(dir > 0){
				line = new Tuple(position, new Vectors(x + visionDistance, Y));	
			} else {
				line = new Tuple(position, new Vectors(x - visionDistance, Y));
			}

			Boolean hit = false;
			// For each object in vision range
			for(int j = 0; j < Mayseen.size(); j++){
				rayInfo ray = Mayseen.get(j);
				// If ones of the bounding box edges intersects the ray then record + delete ray
				for(Tuple boxLine : ray.getEdgePoints()){
					if(Maths.lineVSline(line, boxLine)){ //if line vs boxLine collision
						
						
						MyObject obj = ray.getObject();
						if(!seen.contains(obj)){
							seen.add(obj);
						}
						hit = true;
						break;
					}
				}
				// If the ray has hit an object then it is destroyed
				if(hit){
					break;
				}	
			}

		}

		return seen;
	}

	private class rayInfo{
		private MyObject object;
		private ArrayList<Tuple> edgePoints = new ArrayList<Tuple>();
		
		public rayInfo(Vectors lower, Vectors upper,  MyObject object){
			this.object = object;
			calcEdges(lower, upper);
		}
	
		private void calcEdges(Vectors lower, Vectors upper){
		
			Vectors v1 = new Vectors(lower.X(), upper.Y()); // bottom left
			// v2 = lower, top left
			Vectors v3 = new Vectors(upper.X(), upper.Y()); // top right
			// v4 = upper, bottom right
			
			
			edgePoints.add(new Tuple(v1, lower));
			edgePoints.add(new Tuple(v1, upper));
			edgePoints.add(new Tuple(lower, v3));
			edgePoints.add(new Tuple(v3, upper));
			
		}
		
		public MyObject getObject() {
			return object;
		}

		public ArrayList<Tuple> getEdgePoints() {
			return edgePoints;
		}
	}
}