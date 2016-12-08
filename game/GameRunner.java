package game;

import objects.Player;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Scene;
import engine_yuki.Vectors;

/**
 * Game runner acts as a testing area for the game.
 * @author Wesley
 *
 */
public class GameRunner extends BasicGame {

	Boolean run = true;
	
	float dt = Constants.dt;

	Scene scene = new Scene(new Player(new Vectors(200, 370)), dt);
	long currentTime;
	float accumulator = 0;

	// Time in seconds
	long frameStart = System.nanoTime();
	
	static TestChamber testChamber;
	
	public GameRunner(String title) {
		super(title);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameRunner game = new GameRunner("Project Snowstorm");
		
		
		@SuppressWarnings("unused")
		float accumulator = 0;

		// Time in seconds
		@SuppressWarnings("unused")
		long frameStart = System.nanoTime();
		
		
		try {
			testChamber = new TestChamber(game);
	//		scene = new Scene(testChamber.getWidth(), testChamber.getHeight());
			testChamber.start();
		} catch (SlickException e) {
			
			e.printStackTrace();
		}

	}
	
	public void add(MyObject A){
		scene.addObject(A);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawString("Test Chamber 1", 250, 50);
		scene.renderGame(g, gc.isPaused());
	}


	@Override
	public void init(GameContainer gc) throws SlickException {	
		Vectors[] points = new Vectors[4];
		Vectors centre = new Vectors(300, 400);
		points[0] = new Vectors(- 200, - 20);
		points[1] = new Vectors(- 200, + 20);
		points[2] = new Vectors(+ 200, - 20);
		points[3] = new Vectors(+ 200, + 20);	
		MyObject base = new MyObject(centre, Material.Solid, points);

		Vectors[] points2 = new Vectors[4];
		Vectors centre2 = new Vectors(200, 100);
		points2[0] = new Vectors(- 10, - 10);
		points2[1] = new Vectors(- 10, + 10);
		points2[2] = new Vectors(+ 10, - 10);
		points2[3] = new Vectors(+ 10, + 10);
		MyObject poly = new MyObject(centre2, Material.Wood, points2);
		poly.setOrientation((float) Math.PI / 4);
		
		MyObject circle = new MyObject(new Vectors(215, 130), Material.Wood, 10);
		MyObject circle2 = new MyObject(new Vectors(210, 340), Material.Solid, 15);
		
		add(base);	// circle within polygon collision buggy, 
		add(poly); // resolved
		add(circle);  // resolved for falling circle
		add(circle2); // resolved
		
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {
		long currentTime;
		currentTime = System.nanoTime();
		// Record time elapsed since last frame
		double change = (double)(currentTime - frameStart)/ 1000000000;
		accumulator += change;
		frameStart = currentTime;
		
		// Clamp accumulator to an arbitrary value
		// This prevents a spiral of death if the physics cannot be computed fast enough to match the dt
		if(accumulator > 0.2f){
			accumulator = 0.2f;
		}
		while(accumulator > dt){
			scene.Step();
			accumulator -= dt;
		}
	}
	
}
