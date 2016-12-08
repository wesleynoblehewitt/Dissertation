package engine_yuki;

import java.util.ArrayList;


// REDUNDANT


/** 
 * Performs broad phase comparisons to identify whether 2 objects may be colliding
 * First identifies what type of object collision it is dealing with then does a basic collision check
 * Treats polygonial collisions as boxes to gain a simple estimation
 * @author Wesley
 *
 */
public class BroadPhaser {

	// Takes 2 objects and checks if they appear to be colliding returns the result.
	public Boolean collide(MyObject A, MyObject B){
		CollisionType type = CollisionType.identify(A.getBoundingShape(), B.getBoundingShape());
		Boolean result = false;
		
		switch(type){
		case polygonVSpolygon: 	
			BoundingBox boxA = generateBox(A);
			BoundingBox boxB = generateBox(B);
			result = boxVSbox(boxA, boxB);
			break;
		// Treat the circle as a box and generate a boundingbox for it based off its radius, perform boxVSbox detection
		case polygonVScircle:
			BoundingBox box = generateBox(A);
			BoundingBox box2;
			float radius = B.getRadius();
			Vectors centre = B.getPosition();
			
			Vectors lower = new Vectors(centre.X() - radius, centre.Y() - radius);
			Vectors upper = new Vectors(centre.X() + radius, centre.Y() + radius);
			box2 = new BoundingBox(lower, upper);
			box2.setOrientation(0);
			result = boxVSbox(box, box2);
			break;
		case circleVSpolygon:
			radius = A.getRadius();
			centre = A.getPosition();
			
			lower = new Vectors(centre.X() - radius, centre.Y() - radius);
			upper = new Vectors(centre.X() + radius, centre.Y() + radius);
			box = generateBox(B);
			box2 = new BoundingBox(lower, upper);
			box2.setOrientation(0);
			result = boxVSbox(box2, box);
			break;
		case circleVScircle:
			result = circleVScircle(A,  B);
			break;
		}
		
		return result;
		
	}
	
	// Collision detection for 2 boxes, this is used for polygon comparisons. Shapes are treated as boxes to allow a simple collision estimation.
	private Boolean boxVSbox(BoundingBox A, BoundingBox B){
		if(A.getUpper().X() < B.getLower().X() || A.getLower().X() > B.getUpper().X()){
			return false;
		}
		if(A.getUpper().Y() < B.getLower().Y() || A.getLower().Y() > B.getUpper().Y()){
			return false;
		}
		
		return true;
	}

	// Collision detection for 2 circles
	private Boolean circleVScircle(MyObject A, MyObject B){
		float r = A.getRadius() + B.getRadius();
		r *= r;
		Vectors Ap = A.getPosition();
		Vectors Bp = B.getPosition();	
		return r > Maths.Square(Ap.X() - Bp.X()) + Maths.Square(Ap.Y() - Bp.Y());
	    
	}
	
	// Generates a boundingbox that can contain a given polygon, used to simplify collision estimation
	private BoundingBox generateBox(MyObject A){
		float maxX = -Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		
		// Find max and min points on the polygon
		ArrayList<Vectors> points = A.getPoints();
		// Get polygon centre and convert to world space
		Vectors centre = A.getPosition();
		// find min and max coordinates of the polygon.
		for(int i = 0; i < A.getSize(); i++){
			
			Vectors p = points.get(i);
			float x = p.X();
			float y = p.Y();
			minX = Math.min(minX, x);
			minY = Math.min(minY, y);
			maxX = Math.max(maxX, x);
			maxY = Math.max(maxY, y);	
		}
		Vectors lower = new Vectors(centre.X() + minX, centre.Y() + minY);
		Vectors upper = new Vectors(1.05f *centre.X() +  maxX,1.05f *( centre.Y() + maxY));

		BoundingBox box = new BoundingBox(lower, upper);
		box.setOrientation(A.getOrientation());
		return box;
	}
	
}
