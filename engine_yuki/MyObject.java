package engine_yuki;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/** 
 * Basic class for an object, extends into all types of objects in the engine
 * Depending on instantiation an object will differentiate into either a circle or a polygon
 * @author Wesley
 *
 */
public class MyObject {
	
	// If an object has any sub-objects they are stored in this general array, this allows for easy updating and interaction with these objects.
	// All sub-objects share the same position as the master object
	protected ArrayList<MyObject> components = new ArrayList<MyObject>();
	
	// Linear components of velocity
	protected Vectors position;
	protected Vectors velocity = new Vectors(0, 0);
	
	// Angular components of velocity
	protected float orientation = 0f; // Radians
	protected float angularVelocity =  0f;
	float torque = 0f; // Rotational force
	
	protected BoundingShape boundingShape;
	
	// Force is essentially acceleration in vector form
	protected Vectors force = new Vectors(0, 0);

	MassData massData;
	Material material;

	int missingCount = 0;
	protected float xExtent = 0;
	protected float yExtent= 0;
	
	// Instantiates the object as a circle 
	public MyObject(Vectors position, Material material, float radius){
		this.position = position;
		
		//We use 0 to represent infinite mass, this if statement is to prevent 1/0 errors
		this.material = material;
		
		boundingShape = new BoundingCircle(radius);
		massData = boundingShape.calculateMass(material.getDensity());
		xExtent = radius;
		yExtent = radius;
	}
	
	public MyObject(Vectors position, Material material, float radius, String texture){
		this(position, material, radius);
		if(texture != null){
		setTexture(texture);
		}
	}
	// Instantiates the object as a polygon
	public MyObject(Vectors position, Material material, Vectors[] points){
		this.position = position;
		
		//We use 0 to represent infinite mass, this if statement is to prevent 1/0 errors
		this.material = material;
		Matrix2x2 u = new Matrix2x2(0);
		orientation = u.getRads();
		boundingShape = new BoundingPolygon(u);
		((BoundingPolygon) boundingShape).set(points);
		massData = boundingShape.calculateMass(material.getDensity());
		xExtent = boundingShape.getXExtent();
		yExtent = boundingShape.getYExtent();
	}

	public MyObject(Vectors position, Material material, Vectors[] points, String texture){
		this(position, material, points);
		setTexture(texture);
	}
	
	public MyObject(Vectors position, Material material, float xExtent, float yExtent){
		this.position = position;
		
		//We use 0 to represent infinite mass, this if statement is to prevent 1/0 errors
		this.material = material;
		Matrix2x2 u = new Matrix2x2(0);
		orientation = u.getRads();
		boundingShape = new BoundingPolygon(u);
		((BoundingPolygon) boundingShape).setSquare(position, xExtent, yExtent);
		massData = boundingShape.calculateMass(material.getDensity());
		this.xExtent = xExtent;
		this.yExtent = yExtent;
	}
	
	public MyObject(Vectors position, Material material, float xExtent, float yExtent, String texture){
		this(position, material, xExtent, yExtent);
		setTexture(texture);
	}
	
	// Update function, called once per engine cycle. Updates the position of the object based on the forces acting on it.
	public void update(float dt){
		// Don't update if an infinite mass object
		if(massData.getInv_Mass() == 0.0f){
			return;
		}
		// Velocity along x-axis
		float x = velocity.X();
		// Velocity along y-axis
		float y = velocity.Y();

		float Fx = force.X();
		float Fy = force.Y();
		// v = v + acc * time
		// acc = force / mass
		// v = v + (1 / mass * force) * time 
		x += (massData.getInv_Mass() * Fx) * dt;
		// v = v + (acc + grav) * time 
		// Gravity is only applied to velocity along y-axis
		y += (Fy * massData.getInv_Mass() + (Constants.gravity.Y())) * dt;

		velocity.setX(x);
		velocity.setY(y);
		// Angular vel += torque * (1 / inertia) * time
		angularVelocity = angularVelocity + torque * massData.getInv_Inertia() * dt; 
		// Apply set percent drag to slow down angular velocity
		angularVelocity -= angularVelocity * Constants.drag;
		
	}

	// Updates the position of the object in relation to time
	public void updatePosition(float dt){
		
		// Don't update if an infinite mass object
		if(massData.getInv_Mass() == 0.0f){
			return;
		}
		position = position.plus(Vectors.timesFloat(velocity, dt));
		 
		orientation = orientation + angularVelocity * dt;
		setOrientation(orientation);
		
		for(MyObject obj: components){
			obj.setPosition(position);
			obj.setOrientation(orientation);
		}
	
	}

	// Applies an impulse to the object that influences its velocity.
	public void updateVelocity(Vectors impulse, Vectors contactVector){
		velocity = velocity.plus(Vectors.timesFloat(impulse, massData.getInv_Mass()));
		float ang = Maths.CrossProduct(contactVector, impulse);

		angularVelocity += massData.getInv_Inertia() * ang;
		// Various caps on angular velocity 

		if(angularVelocity > 10){
			angularVelocity = 10;
		} else if(angularVelocity < -10){
			angularVelocity = -10;
		}
	//	System.out.println(angularVelocity);
	}
	
	/**
	 * Override this method if object has a collision specific reaction
	 * @param collidee
	 */
	public void collision(MyObject collidee){
	}
	
	public void setOrientation(float radians){
		boundingShape.setOrientation(radians);
		orientation = boundingShape.getOri();
	}

	// Translates the object by newX, newY.
	public void updatePosition(float newX, float newY){
		position.translate(newX, newY);
		for(MyObject obj: components){
			obj.setPosition(position);
		}
	}
	
	// Updates the objects position to be at centre.
	public void updatePosition(Vectors centre){
		this.position = centre;
		for(MyObject obj: components){
			obj.setPosition(position);
		}
	}

	// Getters and Setters

	public void setVelocity(float x, float y){
		velocity.setX(x);
		velocity.setY(y);
	}
	
	public void setForce(float x, float y){
		force.setX(x);
		force.setY(y);
	}
	
	public void setForce(Vectors force){
		this.force = force;
	}
	
	public void applyForce(float x, float y){
		force.translate(x, y);
	}
	
	public void setTorque(float t){
		torque = t;
	}
	
	public void render(Graphics g){
		boundingShape.render(g, position, getColor());
	}
	
	public void render(Graphics g, boolean paused){
		
	}
	
	public Vectors getPosition(){
		return position;
	}
	
	public  Matrix2x2 getOrientation(){
		return boundingShape.getOrientation();
	}
	
	public float getRadius(){
		return boundingShape.getRadius();
	}
	
	public float getXExtent(){
		return xExtent;
	}
	
	public float getYExtent(){
		return yExtent;
	}
	
	public float getStaticFriction(){
		return material.getStaticFriction();
	}
	
	public float getDynamicFriction(){
		return material.getDynamicFriction();
	}
	
	public float getRestitution(){
		return material.getRestitution();
	}

	public Vectors getVelocity(){
		return velocity;
	}
	
	public float getAngularVelocity(){
		return angularVelocity;
	}
	
	public float getMass(){
		return massData.getMass();
	}
	
	public float getInv_Mass(){
		return massData.getInv_Mass();
	}

	public float getInertia(){
		return massData.getInertia();
	}
	
	public float getInv_Inertia(){
		return massData.getInv_Inertia();
	}
	
	public BoundingShape getBoundingShape(){
		return boundingShape;
	}
	
	public int getSize(){
		return boundingShape.getSize();
	}
	
	public Color getColor(){
		return material.getColor();
	}
	
	public ArrayList<Vectors> getNormals(){
		return boundingShape.getNormals();
	}
	
	public ArrayList<Vectors> getPoints(){
		return boundingShape.getPoints();
	}

	public Material getMaterial(){
		return material;
	}
	
	public boolean intangible(){
		return false;
	}
	
	public ArrayList<MyObject> getComponents(){
		return components;
	}
	
	// Special position updater, used for component objects
	public void setPosition(Vectors position){
		this.position = position;
	}
	
	public boolean contains(MyObject obj){
		return(components.contains(obj));
	}
	
	public int missing(){
		missingCount++;
		return missingCount;
	}
	
	public void found(){
		missingCount = 0;
	}
	
	public void setTexture(String texture){
		boundingShape.setTexture(texture);
	}
}
