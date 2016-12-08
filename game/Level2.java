package game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import engine_yuki.MyObject;

public class Level2 extends Level{

	public Level2(){
		ID = 2;
	}

	@Override
	protected void getBG(){
		try {
			background = new Image(MapCollection.backgroundL2);
		} catch (SlickException e) {
			System.out.println("Failed to load background for level 2");
		} catch (RuntimeException e){
			System.out.println("Failed to find background for level 2");
		}
	}

	
	@Override
	protected void populateScene() {
		ArrayList<MyObject> map = MapCollection.betaWorld2();
		for(MyObject obj: map){
			add(obj);
		}
	}

	@Override
	protected void won() {
		game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
    	reset();
	}
	
}
