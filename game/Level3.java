package game;

import java.util.ArrayList;

import objects.YetiBoss;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import engine_yuki.MyObject;
import engine_yuki.Vectors;

public class Level3 extends Level{
	
	boolean spawned = false;
	
	public Level3(){
		ID = 3;
	}
	
	@Override
	protected void populateScene() {
		ArrayList<MyObject> map = MapCollection.betaWorld3();
		for(MyObject obj: map){
			add(obj);
		}
	}
	@Override
	public void update(GameContainer gc, StateBasedGame arg1, int arg2) throws SlickException {
		super.update(gc, arg1, arg2);
		if(!gc.isPaused()){
			if(!spawned){
				if(scene.getPlayer().getPosition().X() > 300){
					spawned = true;
					System.out.println("spawned");
					add(new YetiBoss(new Vectors(810, 550)));
				}
			}

			if(defeated()){
				pauseMsg = "Victory!";
				gc.pause();
				//Timer timer = new Timer();
				//timer.schedule(new winTimer(), 5000);
				won();
			}
		}
	}

	@Override
	protected void getBG() {
		try {
			background = new Image(MapCollection.backgroundL3);
		} catch (SlickException e) {
			System.out.println("Failed to load background for level 2");
		} catch (RuntimeException e){
			System.out.println("Failed to find background for level 2");
		}
	}

	public boolean defeated(){
		if(scene.getAI().size() == 0 && spawned){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void won() {

	}
	
	@Override
	public void reset(){
		spawned = false;
		super.reset();
	}
	
//	private class winTimer extends TimerTask{
//
//		@Override
//		public void run() {
//			game.enterState(0, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
//			reset();
//		}
//		
//	}
}
