package game;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import objects.AI;
import objects.CreatureCommand;
import objects.Player;
import objects.Pursuer;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import engine_yuki.Constants;
import engine_yuki.Material;
import engine_yuki.MyObject;
import engine_yuki.Scene;
import engine_yuki.Vectors;

public class AIChamber extends BasicGame{

	Boolean run = true;
	float dt = Constants.dt;
	Scene scene = new Scene(new Player(new Vectors(100, 370)), dt);
	long currentTime;
	float accumulator = 0;

	// Time in seconds
	long frameStart = System.nanoTime();
	
	static TestChamber testChamber;
	Random random = new Random();
	Timer attackTimer = new Timer();
	Boolean attackCoolDown = false;
	
	
	public AIChamber(String title) {
		super(title);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AIChamber game = new AIChamber("Project Snowstorm");
		
		try {
			testChamber = new TestChamber(game);
			testChamber.start();
		} catch (SlickException e) {
			
			e.printStackTrace();
		}

	}
	
	public void add(MyObject A){
		scene.addObject(A);
	}
	
	private void populateScene(){
		
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

		
		add(base);
		add(base2);
		add(triangle);
		add(square);
		
		AI pursuer = new Pursuer(new Vectors(200, 375), MapCollection.alphaWorld());
		scene.addAI(pursuer);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawString("Test Chamber 2", 250, 50);
		scene.renderGame(g, gc.isPaused());
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		populateScene();
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {
		if(!testChamber.isPaused()){
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
		
		if(scene.getPlayer().getHealth() <= 0){
		//	System.out.println("pause");
		//	testChamber.pause();
		}
		}
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
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
			int dist = random.nextInt(20) + 30;
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
			if(testChamber.isPaused()){
				scene.setPlayer(new Player(new Vectors(100, 370)));
				testChamber.resume();
			}	
		}
	}

	public void processInput(){
			Input input = testChamber.getInput();
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
				attackTimer.schedule(new AttTT(), 1000);
			}
			
			if(!moving){
				scene.Command(CreatureCommand.still);
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
