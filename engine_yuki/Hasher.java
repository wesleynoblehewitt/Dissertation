package engine_yuki;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Broad phaser for the engine. Breaks the screen down into a 1D array of segments.
 * Objects added to the hasher are stored in the segments their location corresponds to. Objects can occupy multiple segments.
 * @author Wesley
 *
 */
public class Hasher {

	int screenWidth = Constants.screenWidth;
	int screenHeight = Constants.screenHeight;
	int cellSize = Constants.cellSize;
	int size;
	HashMap<Integer, ArrayList<MyObject>> grid; 
	
	//TODO Cull duplicates during broadphase likely necessary
	public Hasher(){
		int cols = screenWidth / cellSize;
		int rows = screenHeight / cellSize;
		int size = cols * rows;
		this.size = size;
		grid = new HashMap<Integer, ArrayList<MyObject>>(size);
		for(int i = 0; i < size; i++){
			
			 grid.put(i, new ArrayList<MyObject>() );
		}
	}
	
	// Clear the hasher of all objects
	public void clear(){
		for(int i = 0; i < size; i++){
			 grid.get(i).clear();
		}
	}
	
	// Add an object to the hasher, it is added to every segment that its bounding box occupies
	public void add(MyObject A){
		
		ArrayList<Integer> IDs = getIDs(A);
		for(Integer ID : IDs){
			if(ID < size){
				grid.get(ID).add(A);
			}
		}
	}
	
	// We check each corner of a bounding box for the shape and record the grid segments they are in
	// Note that this method is inefficient for large shapes which should be avoided.
	private ArrayList<Integer> getIDs(MyObject A){
		
		ArrayList<Integer> ObjectIDs = new ArrayList<Integer>();
		
		// Retrieve shapes radius / max extent
		float radius = A.getRadius();
		
		// Calculate min and max vectors for the shape
		Vectors position = A.getPosition();
		// Top left vector
		Vectors min = new Vectors(position.X() - radius, position.Y() - radius);
		// Bottom right vector
		Vectors max = new Vectors(position.X() + radius, position.Y() + radius);
	
		Vectors range = max.minus(min);
		
		float width = screenWidth / cellSize;
		
		// Number of comparisons usually small
		// Record the segments that the object is in, does a 2 dimensional cycle to account for objects larger than 1 grid segment
		int jumpi = 1;
		int jumpj = 1;
		if(range.x > cellSize){
			jumpi = cellSize/2;
		} 
		if(range.y > cellSize){
			jumpj = cellSize/2;
		}
		for(int i = 0; i < range.x; i += jumpi){
			for(int j = 0; j < range.y; j += jumpj){
				float x = min.x + i;
				float y = min.y + j;
				if(x > screenWidth){
					x = screenWidth;
				} else if(x < 0){
					x = 0;
				}
				if(y > screenHeight){
					y = screenHeight;
				} else if(y < 0){
					y = 0;
				}
				detectLocation(new Vectors(x, y), width, ObjectIDs);
			}
		}
		return ObjectIDs;
	}
	
	// Calculate the grid segment the given vector is in and add to the list
	private void detectLocation(Vectors loc, float width, ArrayList<Integer> list){
		// Floor returns the largest double that is less than or equal to the given double and equal to an integer
		int segment = (int) ((Math.floor(loc.X() / cellSize)) + (Math.floor(loc.Y() /cellSize)) * width);
		
		if(!list.contains(segment)){
			list.add(segment);
		}
	}
	
	// Find all objects that share grid segments with the given object.
	public ArrayList<MyObject> getNearby(MyObject A){
		ArrayList<MyObject> nearby = new ArrayList<MyObject>();
		ArrayList<Integer> IDs = getIDs(A);

		for(Integer ID : IDs){
			if(ID < size){
				ArrayList<MyObject> found = grid.get(ID);

				for(MyObject obj : found ){
					
						if(!nearby.contains(obj) && !obj.equals(A) ){
							
							nearby.add(obj);

						}
				
			}
			}
		}
		return nearby;
	}
}
