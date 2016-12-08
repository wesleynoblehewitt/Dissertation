package engine_yuki;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

/**
 * Polygonial bounding shape, has a Matrix2x2 that represents its orientation and the set of points and normals that represent it
 * @author Wesley
 *
 */
public class BoundingPolygon extends BoundingShape {

	Matrix2x2 u;
	
	
	ArrayList<Vectors> points = new ArrayList<Vectors>();
	ArrayList<Vectors> normals = new ArrayList<Vectors>();
	
	public BoundingPolygon(Matrix2x2 u){
		this.u = u;
	}

	
	// Find the support point of the polygon (Vertex farthest along a given direction).
	public Vectors getSupport(Vectors dir){
		float bestProjection = -Float.MAX_VALUE;
		Vectors bestVertex = null;
		for(int i = 0; i < size; i++){
			Vectors v = points.get(i);
			float projection = Vectors.DOT(v, dir);

			if(projection > bestProjection){
				bestProjection = projection;
				bestVertex = v;
			}
		}

		return bestVertex;
	}
	
	@Override
	public BoundingPolygon clone() {
		BoundingPolygon poly = new BoundingPolygon(u);
		poly.setPoints(points);
		poly.setNormals(normals);
		return poly;
	}

	public void setSquare(Vectors position, float xExtent, float yExtent){
		Vectors[] vertices = new Vectors[4];
		vertices[0] = position.minus(xExtent, yExtent);
		vertices[1] = position.minus(xExtent, -yExtent);
		vertices[2] = position.plus(xExtent, yExtent);
		vertices[3] = position.plus(xExtent, -yExtent);
		set(vertices);
	}
	
	// Secondary initiation function, generates the list of points and normals
	public void set(Vectors[] vertices){
		size = vertices.length;

		// Ensure polygon has at least 3 sides and is not too large
		assert (size > 2 && size <= Constants.maxVerticesCount);
		size = Math.min(size, Constants.maxVerticesCount);
		
		// Find the rightmost point
		int rightMost = 0;
		float highestXCoord = vertices[0].X();
		for(int i = 0; i < size; i++){
			float x = vertices[i].X();
			if(x > highestXCoord){
				rightMost = i;
				highestXCoord = x;
			}

			// If equal X values then take point with most negative Y value
			else if(x == highestXCoord){
				if(vertices[i].Y() < vertices[rightMost].Y()){
					rightMost = i;
				}
			}
		}
	
		int[] hull = new int[Constants.maxVerticesCount];
		int count = 0;
		int hullIndex = rightMost;
		
		Boolean done = false;
		
		// Find the next indexs that wrap around the hull
		while(!done){
			
		hull[count] = hullIndex;
		
		
		int nextHullIndex = 0;
		
		for(int i = 0; i < size; i++){
			// skip if same coordinates
			if(nextHullIndex == hullIndex){
				nextHullIndex = i;
				continue;
			}
			
			// We record each counter clockwise third
			Vectors e1 = vertices[nextHullIndex].minus(vertices[hull[count]]);
			Vectors e2 = vertices[i].minus(vertices[hull[count]]);
			float crossProd = Maths.CrossProduct(e1, e2);
			
			
			if(crossProd < 0.0f){
				nextHullIndex = i;
			}
			if(crossProd == 0.0f && e2.LengthSqr() > e1.LengthSqr()){
				nextHullIndex = i;
			}
			
		}
			count++;
			hullIndex = nextHullIndex;

		
		
		if(nextHullIndex == rightMost){
			size = count;
			done = true;
		}
		
		}
		
		// Transfer vertices into shape
		for(int i = 0; i < size; i++){
			points.add(i, vertices[hull[i]]);
		}
		
		// Compute normals
		for(int i = 0; i < size; i++){
			int j;
			if(i + 1 >= size){
				j = 0;
			} else {
				j = i + 1;
			}
			
			Vectors face = points.get(j).minus(points.get(i));
			assert(face.LengthSqr() > Constants.epsilon * Constants.epsilon);

			Vectors v = new Vectors(face.Y(), -face.X());
			v.normalize();

			normals.add(i, v);
			
		}
	}
	
	// Generates mass data for the shape based off its density and volume
	@Override
	public MassData calculateMass(float density) {
		// Centroid = geometric centre of shape
		Vectors centroid = new Vectors(0, 0);
		float area = 0;
		float inertia = 0;
		float third = 1.0f/3.0f;



		for(int i = 0; i < points.size(); i++){

			Vectors p1 = points.get(i);
			int j;
			if(i + 1 >= points.size()){
				j = 0;
			} else {
				j = i + 1;
			}

			// Calculate triangle of each vertices pair against (0, 0) to find shape area
			Vectors p2 = points.get(j);	
			float D = Maths.CrossProduct(p1, p2);
			float triangularArea = 0.5f * D;

			area += triangularArea;

			Vectors v = (p1.plus(p2));
			v = Vectors.timesFloat(v, triangularArea * third);
			// centroid += triangularArea * third * (p1 + p2)
			centroid.translate(v);

			float intX = p1.X() * p1.X() + p2.X() * p1.X() + p2.X() * p2.X();
			float intY = p1.Y() * p1.Y() + p2.Y() * p1.Y() + p2.Y() * p2.Y();
			inertia += (0.25f * third * D) * (intX + intY);

		}

		centroid = Vectors.timesFloat(centroid, 1.0f/area);


		// We make centroid (0,0) in polygon space by translating the vertice to it
		for(int i = 0; i < points.size(); i++){
			points.set(i, points.get(i).minus(centroid));
		}

		float mass = density * area;
		inertia *= density;
		return new MassData(mass, inertia);

	}

	@Override
	public void setOrientation(float radians) {
		u.set(radians);

	}

	public void setPoints(ArrayList<Vectors> points){
		this.points = points;
		size = points.size();
	}

	public void setNormals(ArrayList<Vectors> normals){
		this.normals = normals;
	}

	@Override
	public int getSize(){
		return size;
	}

	@Override
	public ArrayList<Vectors> getPoints(){
		return points;
	}

	@Override
	public ArrayList<Vectors> getNormals(){
		return normals;
	}

	@Override
	public Matrix2x2 getOrientation(){
		return u;
	}
	
	@Override
	public float getOri(){
		return u.getRads();
	}
	
	@Override
	public float getRadius(){
		float max = -Float.MAX_VALUE;
		
		// Find max extents of the polygon
		for(int i = 0; i < size; i++){
			
			Vectors p = points.get(i);
			float x = p.X();
			float y = p.Y();
			max = Math.max(max, x);
			max = Math.max(max, y);	
		}

		return max;
	}

	@Override
	public void render(Graphics g, Vectors position, Color color){
		Polygon poly = new Polygon();;
		for(int i = 0; i < points.size(); i++){
			Vectors pos = u.multiply(points.get(i));
			pos = pos.plus(position);
			poly.addPoint(pos.X(), pos.Y());
		}
		if(texture != null){
			g.texture(poly, texture);
			
		} else {
			g.setColor(color);
			g.fill(poly);
		}
	}


	@Override
	public float getXExtent() {
		float max = -Float.MAX_VALUE;

		// Find max extents of the polygon
		for(int i = 0; i < size; i++){

			Vectors p = points.get(i);
			float x = p.X();
			max = Math.max(max, x);
		}
		return max;
	}


	@Override
	public float getYExtent() {
		float max = -Float.MAX_VALUE;

		// Find max extents of the polygon
		for(int i = 0; i < size; i++){

			Vectors p = points.get(i);
			float y = p.Y();
			max = Math.max(max, y);
		}
		return max;
	}
}
