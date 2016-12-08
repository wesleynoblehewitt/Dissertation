package engine_yuki;

/**
 * Collection of constant values that are used in the physics engine.
 * @author Wesley
 *
 */
public class Constants {

	/**
	 * Physics Constants
	 */
	
	// Used in positional correction
	// The percent by which penetration is reduced during a collision, if above the slop value
	final static float CorrectionPercent = (float) 0.6;
	// The threshold distance which objects are allowed to penetrate during collision without being adjusted
	final static float slop = (float) 0.05;
	
	// Frames per second cap
	final static float fps = 60;
	// Delta time, the frequency at which the physics engine updates
	public final static float dt = 1/fps;
	
	final static int gravityScale = 5;
	public final static Vectors gravity = new Vectors(0, 10.0f * gravityScale);
	final static float drag = 0.01f;
	
	final static float epsilon = 0.0001f;
	
	final static float relative_bias = 0.95f;
	final static float absolute_bias = 0.01f;
	
	// used for calculating approximations between vectors 
	public final static float leniance = 1;
	
	public final static int maxVerticesCount = 8;

	/**
	 * Chamber settings
	 */
	public final static int screenWidth = 1000;
	public final static int screenHeight = 600;
	public final static int cellSize = 40;

	public final static int LeftClick = 0;
	public final static int MiddleClick = 2;
	public final static int RightClick = 1;

	
	/**
	 * Map Constants
	 */
	
	// Standard distance of the waypoint above its parent object
	public final static float WayPointHeight = 5;
	
	// Solid object x/y extents
	public final static float baseWidth = 10;
	public final static float baseWidthW = 20;
	public final static float baseHeightL = 20;
	public final static float baseHeight = 10;

	
	
	/**
	 * Object Constants
	 */
	public final static int knockBackScale = 10000;
	
	public final static int jumpAcc = -50;
	public final static int moveSpeed = 20;
	public final static int playerBonus = 20;
	// v^2 = u^2 + 2as, s = v^2 - u^2 / 2a
	public final static float jumpHeight =  -Maths.Square(Constants.jumpAcc) / (2 * Constants.gravity.Y());
	// dist = v * t
	// v = u + at, t = v - u / a = 0 - -50 / 50 = 1; t = 2;
	public final static float jumpDist = moveSpeed * 2;
	
	public final static int missingGrace = 40;
	
	public final static float attackRangeMx = 5;
	public final static float attackRangeMy = 3;
	
	public final static float visionDistance = 80;
	public final static float visionHeight = 40;
	
	public final static float turretVisionDistance = 200;
	public final static float turretVisionHeight = 100;
	
	public final static float bossVisionDistance = 500;
	
	public final static int ShotSize = 3;
	public final static int iceBallSize = 8;
	public final static int shotSpeed = 120;
	
	public final static Vectors[] AuraDimensions = {new Vectors(40, 20), new Vectors(40, -20), new Vectors(-40, 20), new Vectors(-40, -20)};
	public final static Vectors[] AuraDimensionsBig = {new Vectors(50, 40), new Vectors(50, -60), new Vectors(-50, 40), new Vectors(-50, -60)};
	
	public final static Vectors[] CreatureLegsMediumDimensions = {new Vectors(-5, 0), new Vectors(5, 0), new Vectors(-5, 10), new Vectors(5, 10)};
	public final static Vectors[] CreatureLegsLargeDimensions = {new Vectors(-10, 0), new Vectors(10, 0), new Vectors(-10, 30), new Vectors(10, 30)};
	public final static float CreatureHeightMedium = 6;
	public final static float CreatureHeightLarge = 16;
	public final static Vectors[] CreatureDimensionsMedium = {new Vectors(10, 10), new Vectors(10, -10), new Vectors(-10, 10), new Vectors(-10, -10)};
	public final static Vectors[] CreatureDimensionsLarge = {new Vectors (20, 30), new Vectors(20, -30), new Vectors(-20, 30), new Vectors(-20, -30)};
}
