package objects;


import engine_yuki.Constants;
import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Aura extends Sense {


	
	public Aura(Vectors position){
		super(position, Constants.AuraDimensions);
	}

	public Aura(Vectors position, Vectors[] dimensions){
		super(position, dimensions);
	}
	
	@Override
	public void collision(MyObject collidee) {
		if(collidee instanceof Player){
			collided = true;
			this.collidee.add(0, collidee);
		}
		
		
	}
	
}
