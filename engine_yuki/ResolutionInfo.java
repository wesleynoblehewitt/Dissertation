package engine_yuki;

public class ResolutionInfo {

	float scalar;
	Vectors RadiusA;
	Vectors RadiusB;
	float invMassSum;

	public ResolutionInfo(float scalar, Vectors RadiusA, Vectors RadiusB, float invMassSum){
		this.scalar = scalar;
		this.RadiusA = RadiusA;
		this.RadiusB = RadiusB;
		this.invMassSum = invMassSum;
	}
	
	public float getScalar() {
		return scalar;
	}

	public Vectors getRadiusA() {
		return RadiusA;
	}

	public Vectors getRadiusB() {
		return RadiusB;
	}

	public float getInvMassSum() {
		return invMassSum;
	}
}
