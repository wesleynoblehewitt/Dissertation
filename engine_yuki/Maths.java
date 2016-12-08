package engine_yuki;

import java.util.ArrayList;

/**
 * Collection of mathematics methods  that are used throughout the physics engine.
 * The methods are an extension of the Math class used in Java.
 * @author Wesley
 *
 */
public class Maths {

	static ArrayList<MyObject> sortingList;

	// Find the lower of two restitution values.
	public static float minRes(float resA, float resB){
		if(resA < resB){
			return resA;
		} else {
			return resB;
		}
	}

	// Compares 2 floats to see if they are equal within the tolerance of epsilon
	public static Boolean Equal(float a, float b){
		return (Math.abs(a - b) <= Constants.epsilon);
	}

	// Squares a float
	public static float Square(float a){
		return (a * a);
	}

	// Find the cross product of two vectors
	public static float CrossProduct(Vectors A, Vectors B){
		return A.X() * B.Y() - A.Y() * B.X();
	}

	// Find the cross product of a vector and float
	public static Vectors CrossProduct(Vectors A, float S){
		return new Vectors(S * A.Y(), -S * A.X());
	}

	// Find the cross product of a float and vector
	public static Vectors CrossProduct(float S, Vectors B){
		return new Vectors(-S * B.Y(), S * B.X());
	}

	// Perform a pythagorean solve (A^2 + B^2 = C^2)
	public static float PythagoreanSolve(float staticA, float staticB){

		float C =  staticA * staticA + staticB * staticB;
		C = (float) Math.sqrt(C);
		return C;
	}
	// Find the axis of least penetration between two polygons 
	public static axisPackage FindAxisLeastPenetration(BoundingPolygon A, BoundingPolygon B, Vectors Apos, Vectors Bpos){
		float bestDistance = -Float.MAX_VALUE;
		int bestIndex = 0;
		ArrayList<Vectors> normals = A.getNormals();
		ArrayList<Vectors> points = A.getPoints();
		Matrix2x2 Au = A.getOrientation();
		Matrix2x2 Bu = B.getOrientation();
		Bu = Bu.transpose();

		// Normal, Support point, Vertex
		Vectors n, s, v;

		// Dot product
		float d;

		for(int i = 0; i < points.size(); i++){
			n = normals.get(i);

			// We transform a face normal from A into B's model space
			Vectors n2 = Au.multiply(n);
			n = Bu.multiply(n2);

			s = B.getSupport(Vectors.inverse(n));

			v = points.get(i);
			v = Au.multiply(v).plus(Apos);
			v = v.minus(Bpos);
			v = Bu.multiply(v);


			d = Vectors.DOT(n, s.minus(v) );

			if(d > bestDistance){
				bestDistance = d;
				bestIndex = i;
			}
		}


		return new axisPackage(bestIndex, bestDistance);
	}

	public static Vectors[] FindIncidentFace(BoundingPolygon refPoly, BoundingPolygon incPoly, int referenceIndex, Vectors incPos){	
		Vectors[] v = new Vectors[2];
		Vectors refNormal = refPoly.getNormals().get(referenceIndex);

		// Calculate the reference normal in the incident polygon's frame of reference.
		refNormal = refPoly.getOrientation().multiply(refNormal); // Convert to the world view first.
		Matrix2x2 u = incPoly.getOrientation();
		refNormal = u.transpose().multiply(refNormal); // Convert the world view to the incident's view.

		int incidentFace = 0;
		float minDOT = Float.MAX_VALUE;
		float dot;
		ArrayList<Vectors> incNormals = incPoly.getNormals();

		// We find the most anti-normal face on the incident polygon
		for(int i = 0; i < incPoly.getSize(); i++){
			dot = Vectors.DOT(refNormal, incNormals.get(i));

			if(dot < minDOT){
				minDOT = dot;
				incidentFace = i;
			}
		}

		// Assign values for v

		ArrayList<Vectors> incPoints = incPoly.getPoints();

		// v[0] = u * incPoints[incidentFace] + position
		v[0] = u.multiply(incPoints.get(incidentFace)).plus(incPos); 

		if(incidentFace + 1 >= incPoints.size()){
			incidentFace = 0;
		} else {
			incidentFace += 1;
		}

		// v[1] = u * incPoints[incidentFace + 1] + position
		v[1] = u.multiply(incPoints.get(incidentFace)).plus(incPos);

		return v;
	}

	// Sort the given list by euclidean distance from the given vector
	public static ArrayList<MyObject> Sort(Vectors position, ArrayList<MyObject> list){
		sortingList = list;
		quickSort(position, 0, list.size()-1);

		return sortingList;
	}

	// Sorts a list by euclidean distance
	private static void quickSort(Vectors position, int lower, int higher){
		if(sortingList.size() > 0){
			int p = (higher + lower) / 2;
			int low = lower;
			int high = higher;

			float pDist = euclideanDist(position, (sortingList.get(p).getPosition()));

			while(low < high){
				while(euclideanDist(position, (sortingList.get(low).getPosition())) < pDist){
					low++; 
				}

				while(euclideanDist(position, sortingList.get(high).getPosition()) > pDist){
					high--;
				}

				if(low <= high){
					MyObject temp = sortingList.get(high);
					sortingList.set(high, sortingList.get(low));
					sortingList.set(low, temp);
					low++;
					high--;
				}
			}

			if(lower < high){
				quickSort(position, lower, high);
			}
			if(low < higher){
				quickSort(position, low, higher);
			}
		}

	}

	public static float euclideanDist(Vectors position, Vectors object){
		float x = (float) Math.sqrt(Square(position.X() - object.X()) + Square(position.Y() - object.Y()));
		return x;
	}

	public static float manhattanDist(Vectors A, Vectors B){
		return Math.abs(A.X() - B.X()) + Math.abs(A.Y() - B.Y());
	}

	public static boolean lineVSline(Tuple LineA, Tuple LineB){

		Vectors L1A = LineA.getA();
		Vectors L1B = LineA.getB();
		Vectors L2A = LineB.getA();
		Vectors L2B = LineB.getB();

		float denom = ((L1B.x - L1A.x) * (L2B.y - L2A.y)) - ((L1B.y - L1A.y) * (L2B.x - L2A.x));
		float numer1 = ((L1A.y - L2A.y) * (L2B.x - L2A.x)) - ((L1A.x - L2A.x) * (L2B.y - L2A.y));
		float numer2 = ((L1A.y - L2A.y) * (L1B.x - L1A.x)) - ((L1A.x - L2A.x) * (L1B.y - L1A.y));

		if( denom == 0){
			return (numer1 == 0 && numer2 == 0);
		}

		float r = numer1 / denom;
		float s = numer2 / denom;

		return ((r >= 0 && r <= 1) && (s >= 0 && s <= 1));
	}

	public static boolean boxVSbox(BoundingBox A, BoundingBox B){
		if(A.getUpper().X() < B.getLower().X() || A.getLower().X() > B.getUpper().X()) return false;
		if(A.getUpper().Y() < B.getLower().Y() || A.getLower().Y() > B.getUpper().Y()) return false;
		return true;
	}
	
	public static boolean withinRange(float x, float left, float right){
		if(left < x && right > x){
			return true;
		}
		return false;
	}
}
