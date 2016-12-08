package engine_yuki;

/**
 * 2x2 matrix that is used to represent orientation of a polygon.
 * @author Wesley
 *
 */
public class Matrix2x2 {

	float XX, XY;
	float YX, YY;
	
	Vectors XCol;
	Vectors YCol;
	
	
	public Matrix2x2(Vectors x, Vectors y){
		XCol = x;
		YCol = y;
		XX = x.X();
		XY = x.Y();
		YX = y.X();
		YY = y.Y();
	}
	
	public Matrix2x2(float XX, float XY, float YX, float YY){
		this.XX = XX;
		this.XY = XY;
		this.YX = YX;
		this.YY = YY;
	}
	
	public Matrix2x2(double rads){
		double c = Math.cos(rads);
		double s = Math.sin(rads);
		
		// round values to 0 when very small. Due to java math errors this is necessary to aquire 0 values when using cos/sin
		if(c < Constants.epsilon && c > -Constants.epsilon){
			c = 0;
		}
		if(s < Constants.epsilon && s > -Constants.epsilon){
			s = 0;
		}
		XX = (float) c;
		if(s == 0){
			XY = (float) s;
		} else {
			XY = (float) -s;
		}
		YX = (float) s;
		YY = (float) c;
		XCol = new Vectors(XX, XY);
		YCol = new Vectors(YX, YY);
	}

	// Rotates a vector 
	public Vectors rotate(Vectors r){
		return new Vectors(XX * r.X() + XY * r.Y(), YX * r.X() + YY * r.Y());
	}
	
	// Multiply two matrices together
	public Matrix2x2 multiply(Matrix2x2 mat22){
		float m00, m01, m10, m11;
		m00 = XX * mat22.XX() + XY * mat22.YX();
		m01 = XX * mat22.XY() + XY * mat22.YY();
		m10 = YX * mat22.XX() + YY * mat22.YX();
		m11 = YX * mat22.XY() + YY * mat22.YY();
		return new Matrix2x2(m00, m01, m10, m11);
	}
	
	// Multiply a vector by the matrix
	public Vectors multiply(Vectors vec){
		return new Vectors(XX * vec.X() + XY * vec.Y(), YX * vec.X() + YY * vec.Y());
	}
	
	// Transposing turns all the columns of a matrix to rows and vice versa.
	public Matrix2x2 transpose(){
		return new Matrix2x2(XX, YX, XY, YY);
	}
	
	// Same as instantiation function, used to update the matrix
	public void set(float radians){
		double c = Math.cos(radians);
		double s = Math.sin(radians);
		
		XX = (float) c;
		if(s == 0){
			XY = (float) s;
		} else {
			XY = (float) -s;
		}
		YX = (float) s;
		YY = (float) c;
		
		XCol = new Vectors(XX, XY);
		YCol = new Vectors(YX, YY);
	
	}
	
	public float XX(){
		return XX;
	}
	
	public float XY(){
		return XY;
	}
	
	public float YX(){
		return YX;
	}
	
	public float YY(){
		return YY;
	}
	
	public Vectors getXCol(){
		return XCol;
	}
	
	public Vectors getYCol(){
		return YCol;
	}
	
	public float getRads(){
		return (float) Math.acos(XX);
	}
}
