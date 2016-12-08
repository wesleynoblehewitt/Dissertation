package game;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import objects.AI;
import objects.Animations;
import objects.CreatureCommand;
import objects.Goal;
import objects.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import engine_yuki.Constants;
import engine_yuki.MyObject;
import engine_yuki.Scene;
import engine_yuki.Vectors;

public abstract class Level extends BasicGameState{
	int ID;
	
	Image background = null;
	Boolean run = true;
	float dt = Constants.dt;
	Vectors playerLoc = new Vectors(40, 550);
	Scene scene = new Scene(new Player(playerLoc), dt);
	long currentTime;
	float accumulator = 0;
	
	
	Animations animations = null;
	// Time in seconds
	long frameStart = System.nanoTime();
	
	static TestChamber testChamber;
	Random random = new Random();
	Timer attackTimer = new Timer();
	Boolean attackCoolDown = false;

	String pauseMsg = "Paused";
	
	GameContainer gc;
	StateBasedGame game;
	
	protected abstract void populateScene();
	protected abstract void getBG();
	protected abstract void won();
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		animations = new Animations();
		populateScene();
		scene.getPlayer().ini(animations);
		this.gc = gc;
		this.game = game;
		getBG();
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		if(background != null){
			background.draw(0, 0, Constants.screenWidth,Constants.screenHeight);
		}
		g.setColor(Color.black);
		g.drawString("Beta Chamber", 50, 100);
		
		if(gc.isPaused()){
			g.drawString(pauseMsg, 50, 130);
		}
			scene.renderGame(g, gc.isPaused());
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame arg1, int arg2) throws SlickException {
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
					pauseMsg = "You Died, middle-click to restart";
					gc.pause();
				}
			}
			
			if(scene.won()){
				pauseMsg = "You made it!";
				gc.pause();
				won();
			}
		}
		
	}

	
	@Override
	public int getID() {
		return ID;
	}
	
	public void add(MyObject A){
		if(A instanceof AI){
			((AI) A).animations(animations);
			scene.addAI((AI) A);
			
			return;
		}
		if(A instanceof Goal){
			scene.setGoal((Goal) A);
		}
		scene.addObject(A);
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount){
		if(button == Constants.MiddleClick){
			reset();
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
			attackTimer.schedule(new AttTT(), 400);
		}
		
		if(!moving){
			scene.Command(CreatureCommand.still);
			
		}
	}
	
	public void reset(){
		gc.resume();
    	scene.clear();
    	populateScene();
    	Player player = new Player(playerLoc);
		player.ini(animations);
		scene.setPlayer(player);
	}
	
	@Override
	public void keyReleased(int key, char c) {
	    switch(key) {
	    case Input.KEY_ESCAPE:
	    	game.enterState(0, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
	    	reset();
	    	break;
	    case Input.KEY_Q:
	    	scene.getPlayer().roll();
	    	break;
	    case Input.KEY_P:
	    	gc.setPaused(!gc.isPaused());
	    default:
	        break;
	    }
	}
	
	// attack cooldown timer task
		protected class AttTT extends TimerTask{
			@Override
			public void run() {
				attackCoolDown = false;		
			}		
		}
}
