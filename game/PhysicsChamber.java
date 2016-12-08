package game;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Scene;
import engine_yuki.Vectors;

/**
 * Testing environment
 * Left click to spawn circle
 * Right click to spawn polygon
 * @author Wesley
 *
 */
public class PhysicsChamber extends BasicGameState {

	static int ID = 4;
	
	Boolean run = true;
	float dt = Constants.dt;
	Scene scene = new Scene(dt);
	long currentTime;
	float accumulator = 0;

	StateBasedGame game;
	
	// Time in seconds
	long frameStart = System.nanoTime();
	
	GameContainer gc;
	Random random = new Random();
	
	public void add(MyObject A){
		scene.addObject(A);
	}
	
	public void populateScene(){
		Vectors[] points = new Vectors[4];
		Vectors centre = new Vectors(300, 400);
		points[0] = new Vectors(- 200, - 20);
		points[1] = new Vectors(- 200, + 20);
		points[2] = new Vectors(+ 200, - 20);
		points[3] = new Vectors(+ 200, + 20);	
		MyObject base = new MyObject(centre, Material.Solid, points);

		points = new Vectors[3];
		centre = new Vectors(350, 380);
		points[0] = new Vectors(-40, 0);
		points[1] = new Vectors(-40, -30);
		points[2] = new Vectors(30, 0);
		MyObject triangle = new MyObject(centre, Material.Solid, points);
		
		
		points = new Vectors[4];
		centre = new Vectors(300, 100);
		points[0] = new Vectors(- 200, - 20);
		points[1] = new Vectors(- 200, + 20);
		points[2] = new Vectors(+ 200, - 20);
		points[3] = new Vectors(+ 200, + 20);	
		MyObject base2 = new MyObject(centre, Material.Solid, points);
		
		points = new Vectors[5];
		centre = new Vectors(80, 415);
		points[0] = new Vectors(- 70, - 20);
		points[1] = new Vectors(- 70, + 20);
		points[2] = new Vectors(+ 20, - 20);
		points[3] = new Vectors(+ 20, + 20);
		points[4] = new Vectors(-25, - 40);
		MyObject square = new MyObject(centre, Material.Solid, points);
		square.setOrientation((float) (Math.PI/6));
		
		points = new Vectors[4];
		centre = new Vectors(350, 250);
		points[0] = new Vectors(- 20, - 20);
		points[1] = new Vectors(- 20, + 20);
		points[2] = new Vectors(+ 20, - 20);
		points[3] = new Vectors(+ 20, + 20);
		MyObject square2 = new MyObject(centre, Material.Solid, points);
		square2.setOrientation((float) (Math.PI/4));
		
		MyObject circle2 = new MyObject(new Vectors(230, 300), Material.Solid, 15);
		
		add(base);
		add(base2);
		add(triangle);
		add(circle2); 
		add(square);
		add(square2);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		int w = (Constants.screenWidth / 2 )+ 20;
		g.drawString("Physics Chamber", w, 50);
		g.drawString("Left Click to spawn a circle", w, 100);
		g.drawString("Right Click to spawn a polygon", w, 120);
		g.drawString("Middle Click to reset the chamber", w, 140);
		g.drawString("Press Escape to exit the chamber", w, 160);
		scene.renderGame(g, gc.isPaused());
	}

	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		populateScene();
		this.game = game;
		this.gc = gc;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int i) throws SlickException {
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
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
		System.out.println("clicked");
		// Add random circle on left click
		if(button == Constants.LeftClick){
			
			System.out.println("left click");
			int radius = random.nextInt(10) + 5;
			int mat = random.nextInt(4) + 1;
			MyObject co = new MyObject(new Vectors(x, y), Material.getMaterialByID(mat) ,radius);
			add(co);
		}
		
		// Add random polygon on right click
		if(button == Constants.RightClick){
			System.out.println("right click");
			int contacts = random.nextInt(Constants.maxVerticesCount - 2) + 3;
			
			Vectors[] points = new Vectors[contacts];
			int dist = random.nextInt(20) + 10;
			for(int i = 0; i < contacts; i++){
				int Vx = random.nextInt(2 * dist) - dist;
				int Vy = random.nextInt(2 * dist) - dist;
				if(Vx <10 &&  Vx> -10){
					Vx = 20;
				}
				if(Vy < 10 && Vy> -10){
					Vy = 20;
				}
				points[i] = new Vectors(Vx, Vy);
			}
			
			int mat = random.nextInt(4) + 1;
			MyObject po = new MyObject(new Vectors(x, y), Material.getMaterialByID(mat), points);
			float pi = (float) (Math.PI - (random.nextFloat() * 2 * Math.PI));
			po.setOrientation(pi);
			add(po);
		}
		
		if(button == Constants.MiddleClick){
			scene.clear();
			populateScene();
		}
	}
	@Override
	public void keyReleased(int key, char c) {
	    switch(key) {
	    case Input.KEY_ESCAPE:
	    	game.enterState(Menu.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
	    	scene.clear();
	    	populateScene();
	    	break;
	    default:
	        break;
	    }
	}

	@Override
	public int getID() {
		return ID;
	}
}