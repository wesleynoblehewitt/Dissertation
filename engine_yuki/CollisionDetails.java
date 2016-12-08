package engine_yuki;

import java.util.ArrayList;

/**
 * A class that acts as a small package, storing information on a collision.
 * @author Wesley
 *
 */
public class CollisionDetails {
	
	MyObject A = null;
	MyObject B = null;
	// penetration depth
	float PD = 0.0f;
	// collision normal
	Vectors CN = null;
	
	// Used in polygon collisions
	int contactCount = 0;
	ArrayList<Vectors> contacts = new ArrayList<Vectors>();
	
	public CollisionDetails(MyObject A, MyObject B, float PD, Vectors CN){
		this.A = A;
		this.B = B;
		this.PD = PD;
		this.CN = CN;

	}
	
	public CollisionDetails(MyObject A, MyObject B){
		this.A = A;	
		this.B = B;
	}
	
	public void setA(MyObject A){
		this.A = A;
	}
	
	public void setB(MyObject B){
		this.B = B;
	}
	
	public void updateObjects(MyObject A, MyObject B){
		this.A = A;
		this.B = B;
	}
	
	// Set the penetration depth
	public void setPD(float PD){
		this.PD = PD;
	}
	
	// Set the collision normal
	public void setCN(Vectors CN){
		this.CN = CN;
	}
	
	// Set the contact count value
	public void setCC(int contactCount){
		this.contactCount = contactCount;
	}
	
	public void newContact(int i, Vectors v){
		contacts.add(i, v);
	}
	
	public void setNull(){
		A = null;
		B = null;
		PD = 0.0f;
		CN = null;
		contactCount = 0;
	}
	
	public MyObject getA(){
		return A;
	}
	
	public MyObject getB(){
		return B;
	}
	
	// Return penetration depth
	public float getPD(){
		return PD;
	}
	
	// Return collisionNormal
	public Vectors getCN(){
		return CN;
	}
	
	// Return contact count
	public int getCC(){
		return contactCount;
	}
	
	public Vectors getContact(int i){
		return contacts.get(i);
	}
	
	public ArrayList<Vectors> getContacts(){
		return contacts;
	}
	
	// Compares this collision detail to another to see if they contain the same objects.
	@Override
	public boolean equals(Object o){
		if(o instanceof CollisionDetails){
			if(((CollisionDetails) o).getA().equals(B) && ((CollisionDetails) o).getB().equals(A)){
				return true;
			} else if(((CollisionDetails) o).getA().equals(A) && ((CollisionDetails) o).getB().equals(B)){
				return true;
			}
		}
		return false;
	}
	
//	public boolean valid(){
//		if(A.intangible() || B.intangible()){
//			return false;
//		}
//		
//		if(A instanceof Creature){
//			return ((Creature) A).member(B);
//		}
//		
//		if(B instanceof Creature){
//			return ((Creature) B).member(A);
//		}
//	}
}
