package objects;

import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Legs extends Sense{

	float height;
	
	public Legs(Vectors position, float height, Vectors[] dimensions) {
		super(position, dimensions);
		this.height = height;
	}

	@Override
	public void setPosition(Vectors position){
		this.position = new Vectors(position.X(), position.Y() + height);
	}
	
	@Override
	public void collision(MyObject collidee) {
			collided = true;
			this.collidee.add(collidee);
	}
	
}
