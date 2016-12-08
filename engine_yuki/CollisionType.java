package engine_yuki;

/**
 * Takes 2 objects and identifies whether they are circles or polygons.
 * @author Wesley
 *
 */
public enum CollisionType {
	circleVScircle,
	polygonVSpolygon,
	circleVSpolygon, 
	polygonVScircle;


	public static CollisionType identify(BoundingShape A, BoundingShape B){

		if(A instanceof BoundingPolygon){
			if(B instanceof BoundingCircle){	
				return polygonVScircle; 
			}

			return polygonVSpolygon;
		} else {
			if(B instanceof BoundingPolygon){
				return circleVSpolygon;
			}

			return circleVScircle;
		}
	}

}
