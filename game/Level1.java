package game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import engine_yuki.MyObject;

public class Level1 extends Level{
	
	public Level1(){
		ID = 1;
	}
	
	@Override
	protected void getBG(){
		try {
			background = new Image(MapCollection.backgroundL1);
		} catch (SlickException e) {
			System.out.println("Failed to load background for level 1");
		} catch (RuntimeException e){
			System.out.println("Failed to find background for level 1");
		}
	}
	
	@Override
	protected void populateScene(){
		ArrayList<MyObject> map = MapCollection.betaMap();
		for(MyObject obj: map){
			add(obj);
		}
	}

	
	
	@Override
	protected void won() {
		game.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
    	reset();
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		super.render(gc, arg1, g);
		g.setColor(Color.black);
		g.drawString("WASD / Arrows keys to move", 50, 160);
		g.drawString("Q to roll, Space bar to attack", 50, 180);
		g.drawString("P to pause, Escape to exit to menu", 50, 200);
		g.drawString("Middle-click to reset if you mess up", 50, 220);
		g.drawString("Aim for the goal, but use your wits!", 50, 260);
		g.drawString("There are enemies afoot!", 50, 280);
		
		g.drawString("Some blocks are movable, ", 600, 316);
		g.drawString("use them to reach new areas", 600, 336);
		g.drawString("Aim for here", 870, 425);
	}
}