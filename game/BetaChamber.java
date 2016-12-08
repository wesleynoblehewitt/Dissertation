package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import objects.AI;
import objects.CreatureCommand;
import objects.Goal;
import objects.Icicle;
import objects.Player;
import objects.YetiBoss;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Scene;
import engine_yuki.Vectors;

@SuppressWarnings("unused")
public class BetaChamber extends BasicGame{
	
	Image background = null;
	
	Boolean run = true;
	float dt = Constants.dt;
	Vectors playerLoc = new Vectors(40, 550);
	Scene scene = new Scene(new Player(playerLoc), dt);
	long currentTime;
	float accumulator = 0;
	
	// Time in seconds
	long frameStart = System.nanoTime();
	
	Random random = new Random();
	Timer attackTimer = new Timer();
	Boolean attackCoolDown = false;

	String pauseMsg = "Paused";
	
	GameContainer gc;
	
	boolean spawned = false;
	public BetaChamber(String title) {
		super(title);
	}

	private void populateScene(){
		ArrayList<MyObject> map = MapCollection.betaWorld3();
		for(MyObject obj: map){
			add(obj);
		}
	}
	
	// used to draw an overlay on the mouse, used in designing
	
	private void renderMouseShape(Graphics g){
	//	MyObject obj =  new MyObject(new Vectors(mousePosition.X(), mousePosition.Y()), Material.Ice, Constants.baseWidth, Constants.baseHeight * 1.5f);
	//	obj.getBoundingShape().render(g, obj.getPosition(),new Color(1f, 1f, 1f, 0.2f) );
		
//		Circle circ = new Circle(mousePosition.X(), mousePosition.Y(), 5);
//		g.setColor(new Color(1f, 1f, 1f, 0.2f));
//		g.fill(circ);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {	
			
		if(background != null){
			background.draw();
		}
		renderMouseShape(g);
		g.setColor(Color.black);
		g.drawString("Beta Chamber", 50, 200);
		
		if(gc.isPaused()){
			g.drawString(pauseMsg, 50, 250);
		}
		
		scene.renderGame(g, gc.isPaused());
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		populateScene();
		//background = new Image("Images/mountainBG.png");
		scene.getPlayer().ini(null);
		this.gc = gc;
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		if(!gc.isPaused()){
			long currentTime;
			currentTime = System.nanoTime();
			// Record time elapsed since last frame
			double change = (double)(currentTime - frameStart) / 1000000000;
			accumulator += change;
			frameStart = currentTime;
			
			// Clamp accumulator to an arbitrary value
			// This prevents a spiral of death if the physics cannot be computed fast enough to match the dt
			if(accumulator > 0.2f){
				accumulator = 0.2f;
			}
			while(accumulator > dt){	
				scene.Step();
				processInput();
				accumulator -= dt;
				
			}
			if(scene.getPlayer() != null){
				if(scene.getPlayer().getHealth() <= 0){
					System.out.println("You Died");
					scene.playerDied();
					pauseMsg = "You Died, middle-click to retry";
					gc.pause();
				}
			}
			
			if(!spawned){
				if(scene.getPlayer().getPosition().X() > 300){
					spawned = true;
					System.out.println("spawned");
					add(new YetiBoss(new Vectors(810, 550)));
				}
			}
			
			if(won()){
				pauseMsg = "Victory!";
				gc.pause();
			}
		}
	}

	public boolean won(){
		if(scene.getAI().size() == 0 && spawned){
			return true;
		} else {
			return false;
		}
	}
	
	public void add(MyObject A){
		if(A instanceof AI){
			scene.addAI((AI) A);
			return;
		}
		if(A instanceof Goal){
			scene.setGoal((Goal) A);
		}
		scene.addObject(A);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BetaChamber game = new BetaChamber("Project Snowstorm");
		
		try {
			TestChamber testChamber = new TestChamber(game);
			testChamber.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
		// Add a solid square at the location. This is used to design and build the maps quickly and of uniform blocks
		if(button == Constants.LeftClick){
		//	MyObject base = new MyObject(new Vectors(x, y), Material.Ice, Constants.baseWidth, Constants.baseHeight * 1.5f);
			Icicle base = new Icicle(new Vectors(x, y));
			add(base);
//			MyObject wayPoint = new MyObject(new Vectors(x, y), Material.Solid, 5);
//			add(wayPoint);
//			add(new MyObject(new Vectors(x, y), Material.Solid, 5));
			System.out.println("position = " + x + ", " + y);
		}
		
		if(button == Constants.MiddleClick){
			scene.clear();
			populateScene();
			pauseMsg = "Paused";
			Player player = new Player(playerLoc);
			player.ini(null);
			scene.setPlayer(player);
			gc.resume();
			spawned = false;
		}
	}
	
	public void processInput(){
			Input input = gc.getInput();
			Boolean moving = false;
			if(input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)){
				scene.Command(CreatureCommand.moveRight);
				moving = true;
			}
			
			if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)){
				scene.Command(CreatureCommand.moveLeft);
				moving = true;
			}
			
			if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)){
				scene.Command(CreatureCommand.jump);
			}
			
			if(input.isKeyDown(Input.KEY_SPACE) && !attackCoolDown){
				scene.Command(CreatureCommand.attack);
				attackCoolDown = true;
				attackTimer.schedule(new AttTT(), 300);
			}
			
			if(!moving){
				scene.Command(CreatureCommand.still);
				
			}
	}
	
	@Override
	public void keyReleased(int key, char c) {
	    switch(key) {
	    case Input.KEY_Q:
	    	scene.getPlayer().roll();
	    	break;
	    case Input.KEY_P:
	    	gc.setPaused(!gc.isPaused());
	    	break;
	    default:
	        break;
	    }
	}
	// attack cooldown timer task
	private class AttTT extends TimerTask{
		@Override
		public void run() {
			attackCoolDown = false;		
		}		
	}
	
}
