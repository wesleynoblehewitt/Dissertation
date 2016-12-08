package engine_yuki;

/**
 * Class that stores 2 integers that represents a location in a space
 * Contains various functions for mathematics on the vector
 * @author Wesley
 *
 */
public class Vectors {

	float x;
	float y;
	
	public Vectors(float x, float y){
		if(x == -0){
			x = 0;
		}
		if(y == -0){
			y = 0;
		}
		
		this.x = x;
		this.y = y;
	}
	
	// translates the vector towards the given vector b.
	public void translate(Vectors b){
		this.x += b.X();
		this.y += b.Y();
	}
	
	// translate the vector x component towards the given x.
	public void translateX(float x){
		this.x += x;
	}
	
	// translate the vector y component towards the given y.
	public void translateY(float y){
		this.y += y;
	}
	
	// translate the vector towards the given x and y.
	public void translate(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	// Checks if the given value is approxiametly equal to x
	public Boolean Xaprox(float x){
		return(this.x > x - Constants.leniance/2 && this.x < x + Constants.leniance/2);
	}
	
	public boolean Yaprox(float y){
		return(this.y > y - Constants.leniance && this.y < y + Constants.leniance);
	}
	
	// Subtracts 2 vectors and returns a new vector containing the result.
	public Vectors minus(Vectors b){
		float newX = x - b.X();
		float newY = y - b.Y();
		return new Vectors(newX, newY);
	}
	
	public Vectors minus(float x, float y){
		float newX = this.x - x;
		float newY = this.y - y;
		return new Vectors(newX, newY);
	}
	
	// Add 2 vectors and return a new vector containing the result.
	public Vectors plus(Vectors b){
		float newX = x + b.X();
		float newY = y + b.Y();
		return new Vectors(newX, newY);
	}
	
	
	public Vectors plus(float x, float y){
		float newX = this.x + x;
		float newY = this.y + y;
		return new Vectors(newX, newY);
	}
	
	// Returns the dot product of two vectors.
	public float dot(Vectors b){
		return (x * b.X()) + (y * b.Y());
	}
	
	// Alternative implementation of dot product.
	public static float DOT(Vectors a, Vectors b){
		return (a.X() * b.X()) + (a.Y() * b.Y());
	}
	
	// Translate the vector by a given scalar value.
	public static Vectors timesFloat(Vectors vec, float f){
		Vectors result = vec.copy();
		result.x *= f;
		result.y *= f;
		return result;
	}
	
	public static Vectors divideFloat(Vectors vec, float f){
		Vectors result = vec.copy();
		if(f == 0){
			result.x = 0;
			result.y = 0;
		} else {
			result.x /= f;
			result.y /= f;
		}
		return result;
	}
	// Produce a clone of the vector.
	public Vectors copy(){
		Vectors copy = new Vectors(x, y);
		return copy;
	}
	
	public float LengthSqr(){
		return x * x + y * y;
	}
	
	public static float distSqr(Vectors a, Vectors b){
		Vectors c = a.minus(b);
		return Vectors.DOT(c, c);
	}
	
	
	// Normalizes the vector
	public void normalize(){
		
		float length = (float) Math.sqrt((x * x) + (y * y));
		if(length > Constants.epsilon){
			if(x != 0){
				x = x / length;
			}
			
			if(y != 0){
				y = y / length;
			}
		}
	}
	
	public static Vectors inverse(Vectors v){
		Vectors Iv = v.copy();
		float x = Iv.X();
		float y = Iv.Y();
		if(x != 0){
			x = -x;
		}
		if(y != 0){
			y = -y;
		}
		Iv.setX(x);
		Iv.setY(y);
		
		return Iv;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public float X(){
		return x;
	}
	
	public float Y(){
		return y;
	}
}
