package engine_yuki;

import java.util.ArrayList;

/**
 * Class that takes details on a collision and computes a resolution
 * Returns the updated object information within a collisionDetails package
 * @author Wesley
 *
 */
public class CollisionResolver {
	Vectors[] incidentFace;
	CollisionDetails currentCollision;
	
	// Calculate the collision for 2 bounding polygons and performs impulse resolution
	public CollisionDetails PolygonVsPolygon(CollisionDetails details){
		BoundingPolygon A = (BoundingPolygon) details.getA().getBoundingShape();
		BoundingPolygon B = (BoundingPolygon) details.getB().getBoundingShape();
		
		// If there is no separating face (distance >= 0) then there is not a collision
		axisPackage first = Maths.FindAxisLeastPenetration(A, B, details.getA().getPosition(), details.getB().getPosition());
		if(first.getDistance() >= 0.0f){
			return null;
		}
		
		axisPackage second = Maths.FindAxisLeastPenetration(B, A, details.getB().getPosition(), details.getA().getPosition());
		if(second.getDistance() >= 0.0f){
			return null;
		}
		
		// Identify which polygon contains the reference face
		
		int referenceIndex;
		Boolean flip;
		BoundingPolygon refPoly; // Reference polygon
		BoundingPolygon incPoly; // Incident polygon
		Vectors refPos;
		Vectors incPos;
		
		float penA = first.getDistance();
		float penB = second.getDistance();
		
		// If penetration A > Penetration B. Apply bias to minimise floating point errors.
		if(penA >= penB * Constants.relative_bias + penA * Constants.absolute_bias){
			refPoly = A;
			refPos = details.getA().getPosition();
			incPoly = B;
			incPos = details.getB().getPosition();
			referenceIndex = first.getIndex();
			flip = false;
		} else {
			refPoly = B;
			refPos = details.getB().getPosition();
			incPoly = A;
			incPos = details.getA().getPosition();
			referenceIndex = second.getIndex();
			flip = true;
		}
		
		incidentFace = new Vectors[2];
		incidentFace = Maths.FindIncidentFace(refPoly, incPoly, referenceIndex, incPos);
		
		// Create reference face vertices
		ArrayList<Vectors> refPoints = refPoly.getPoints();
		Vectors v1 = refPoints.get(referenceIndex);
		if(referenceIndex + 1 >= refPoints.size()){
			referenceIndex = 0;
		} else {
			referenceIndex += 1;
		}
		Vectors v2 = refPoints.get(referenceIndex);

		// Transform the vertices to the world space
		Matrix2x2 u = refPoly.getOrientation(); // Retrieve the reference polygon's orientation matrix, used for the transformation

		// v = u * v + position
		v1 = u.multiply(v1).plus(refPos);
		v2 = u.multiply(v2).plus(refPos);

		// Calculate the reference face side normal
		Vectors sideNormal = v2.minus(v1);
		sideNormal.normalize();

		// Orthognalize the side normal, this will be the collision normal
		Vectors refFaceNormal = new Vectors(sideNormal.Y(), -sideNormal.X());
		
		// ax + by = c
		// c = distance from origin
		float referenceC = Vectors.DOT(refFaceNormal, v1);
		
		float negSide = -Vectors.DOT(sideNormal, v1);
		float posSide = Vectors.DOT(sideNormal, v2);
		
		// Perform clipping
		// Collision coming this far may have been caused by floating point errors, clipping checks this
		if (clip(Vectors.inverse(sideNormal), negSide) < 2){
			return null;
		}
		
		if (clip(sideNormal, posSide) < 2){
			return null;
		}
		
		
		// Perform flip if necessary
		if(flip){
			details.setCN(Vectors.inverse(refFaceNormal));
		} else {
			details.setCN(refFaceNormal);
		}
		
		// Calculate penetration depth
		int cp = 0; // Clipped points behind reference face
		float separation = Vectors.DOT(refFaceNormal, incidentFace[0]) - referenceC;
		if(separation <= 0.0f){
			details.newContact(cp, incidentFace[0]);	
			details.setPD(-separation);
			cp++;
		} else {
			details.setPD(0);
		}
		
		separation = Vectors.DOT(refFaceNormal, incidentFace[1]) - referenceC;
		if(separation <= 0.0f){
			details.newContact(cp, incidentFace[1]);
			float pen = details.getPD() - separation;
			cp++;
			pen /= cp;
			details.setPD(pen);
		
		}

		details.setCC(cp);

		return details;
	}

	public CollisionDetails PolygonVsCircle(CollisionDetails details){
		// flip A and B for calculation
		MyObject A = details.getA();
		MyObject B = details.getB();
		details.updateObjects(B, A);
		details = CircleVsPolygon(details);
		if(details != null){
			// Inverse collision normal
			Vectors CN = details.getCN();

			CN = Vectors.inverse(CN);

			details.setCN(CN);

			// Put A and B in correct order again
			A = details.getA();
			B = details.getB();
			details.updateObjects(B, A);
		}
		return details;
	}
	
	// Calculate the collision for a bounding circle and bounding polygon and performs impulse resolution
	public CollisionDetails CircleVsPolygon(CollisionDetails details){
		BoundingCircle A = (BoundingCircle) details.getA().getBoundingShape();
		BoundingPolygon B = (BoundingPolygon) details.getB().getBoundingShape();
		
		// used to avoid repeated getter function calls
		Vectors Apos = details.getA().getPosition();
		Vectors Bpos = details.getB().getPosition();
		
		float radius = A.getRadius();
		
		// Transform centre to polygon model space
		Matrix2x2 u = B.getOrientation(); // Orientation matrix of polygon B
		// centre = u * (A.position() - B.position())
		Vectors centre = Apos;
		centre = centre.minus(Bpos);
		centre = u.transpose().multiply(centre);	
		
		// Find edge with min penetration
		float separation = -Float.MAX_VALUE;
		int faceNormal = 0;
		
		int n = B.getSize();
		ArrayList<Vectors> normals = B.getNormals();
		ArrayList<Vectors> points = B.getPoints();
		
		// Find edge with the minimum penetration
		for(int i = 0; i < n; i++){
			float s = Vectors.DOT(normals.get(i), centre.minus(points.get(i)));
			//No collision
			if(s > radius){
				return null;
			} else {
				if(s > separation){
					
					separation = s;
					faceNormal = i;	
				}
			}
		}
		// Get face vertices
		Vectors v1 = points.get(faceNormal);
		int i2; 
		if(faceNormal + 1 < n){
			i2 = faceNormal + 1;
		} else {
			i2 = 0;
		}
		Vectors v2 = points.get(i2);
		
		// if circle centre is within polygon then rejoice, we can perform a simple calculation
		if(separation < Constants.epsilon){
			details.setCC(1);
			Vectors normal = u.multiply(normals.get(faceNormal)); 
			details.setCN(Vectors.inverse(normal));
			normal = Vectors.timesFloat(normal, radius).plus(Apos);
			details.newContact(0, normal);
			details.setPD(radius);
			
		} else {
		// Determine which voronoi region the circle edge is within
			
			float dot1 = Vectors.DOT(centre.minus(v1), v2.minus(v1));
			float dot2 = Vectors.DOT(centre.minus(v2), v1.minus(v2));
			details.setPD(radius - separation);
			details.setCC(1);
			Vectors CN;
			
			// Closest to vertices 1
			if(dot1 <= 0.0f){
				if(Vectors.distSqr(centre, v1) > radius * radius){
					return null;
				}
				
				CN = v1.minus(centre);
				CN = u.multiply(CN);
				CN.normalize();;
				details.setCN(CN);
				// v1 = u * v1 + B.position()
				v1 = u.multiply(v1).plus(Bpos);
				details.newContact(0, v1);
				
			// Closest to vertices 2
			} else if(dot2 <= 0.0f){
				if(Vectors.distSqr(centre, v2) > radius * radius){
					return null;
				}

				CN = v2.minus(centre);
				CN = u.multiply(CN);
				CN.normalize();
				details.setCN(CN);
				// v2 = u * v2 + B.position()
				v2 = u.multiply(v2).plus(Bpos);
				details.newContact(0, v2);		
				
			// Closest to face
			} else {
				CN = normals.get(faceNormal);
				
				if(Vectors.DOT(centre.minus(v1), CN) > radius){
					return null;
				}
				CN = u.multiply(CN);

				CN = Vectors.inverse(CN);
				
				details.setCN(CN);
				details.newContact(0, Vectors.timesFloat(CN, radius).plus(Apos));	
			}		
		}
		return details;
	}
	
	// Calculates the collision for 2 bounding circle and performs impulse resolution.
	public CollisionDetails CircleVsCircle(CollisionDetails details){
		BoundingCircle A = (BoundingCircle) details.getA().getBoundingShape();
		BoundingCircle B = (BoundingCircle) details.getB().getBoundingShape();
		
		// Vector form A to B = B position - A position
		Vectors n = details.getB().getPosition().minus(details.getA().getPosition());
		float r = A.getRadius() + B.getRadius();
		 
		float nLengthSquared = n.LengthSqr();
		// Circles haven't collided, return nothing
		if(nLengthSquared > r * r){
			return null;
		}
		
		details.setCC(1);
		
		// Circles have collided, compute collision normal and penetration depth
		float distance = (float) Math.sqrt(nLengthSquared);
		
		// If circles are not on top of one another
		if(distance != 0){
			details.setPD(r - distance);
			Vectors CN = Vectors.divideFloat(n, distance);
			details.setCN(CN);
			details.newContact(0, Vectors.timesFloat(CN, r).plus(details.getA().getPosition()));
			return details;
		} else {
			// Circles are on top of one another, collision normal is set to a default value
			details.setPD(A.getRadius());
			details.setCN(new Vectors(1, 0));
			details.newContact(0, details.getA().getPosition());
			return details;
		}
	}
	
	private int clip(Vectors normal, float c){
		int sp = 0;
		Vectors[] out = { incidentFace[0], incidentFace[1]};
		
		// We retrieve the distance from each end point to the normal
		float d1 = Vectors.DOT(normal, incidentFace[0]) - c;
		float d2 = Vectors.DOT(normal, incidentFace[1]) - c;
		
		// If the value is negative then we clip them to the plane
		if(d1 <= 0.0f){
			out[sp++] = incidentFace[0];
		}
		if(d2 <= 0.0f){
			out[sp++] = incidentFace[1];
		}
		
		// If the points are on different sides of the plane
		if(d1 * d2 < 0.0f){
			float alpha = d1 / (d1 - d2);
			
			// out[sp] = face[0] + alpha * (face[1] - face[0])
			out[sp] = incidentFace[0].plus(Vectors.timesFloat((incidentFace[1].minus(incidentFace[0])), alpha));
			++sp;
		}
		
		// Assign converted values
		incidentFace[0] = out[0];
		incidentFace[1] = out[1];
		
		// Ensure sp is 0 - 2
		assert(sp != 3);
		
		return sp;
	}	
}