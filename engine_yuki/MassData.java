package engine_yuki;

/**
 * Storage class, contains data on an objects mass and inertia
 * @author Wesley
 *
 */
public class MassData {

	float mass;
	float inv_mass;
	
	// Rotational mass 
	float inertia;
	float inv_inertia;
	
	public MassData(float mass, float inertia){
		this.mass = mass;
		this.inertia = inertia;
		
		if(mass == 0){
			inv_mass = 0;
		} else {
			inv_mass = 1/mass;
		}
		
		if(inertia == 0){
			inv_inertia = 0;
		} else {
			inv_inertia = 1/inertia;
		}
		
	}
	
	public void setMass(float mass){
		this.mass = mass;

		if(mass == 0){
			inv_mass = 0;
		} else {
			inv_mass = 1/mass;
		}
	}
	
	public void setInertia(float inertia){
		this.inertia = inertia;
		if(inertia == 0){
			inv_inertia = 0;
		} else {
			inv_inertia = 1/inertia;
		}
	}
	
	public float getMass(){
		return mass;
	}
	
	public float getInv_Mass(){
		return inv_mass;
	}
	
	public float getInertia(){
		return inertia;
	}
	
	public float getInv_Inertia(){
		return inv_inertia;
	}
	
}
