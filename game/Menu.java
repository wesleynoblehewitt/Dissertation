package game;

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

public class Menu extends BasicGameState {

	static int ID = 0;
	
	StateBasedGame game;
	
	@Override
	public void init(GameContainer arg0, StateBasedGame game) throws SlickException {
		this.game = game;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		 g.setColor(Color.white);
		 
		 int screenWidth = Constants.screenWidth;
		 g.drawString("Beta Game", screenWidth / 2, 50);
		 
		 g.drawString("1. Play Game", screenWidth / 4, 100);
		 g.drawString("2. Physics Chamber", screenWidth / 4, 150);
		 g.drawString("3. Exit", screenWidth / 4, 200);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		
	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void keyReleased(int key, char c) {
	    switch(key) {
	    case Input.KEY_1:
	        game.enterState(1, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
	        break;
	    case Input.KEY_2:
	    	game.enterState(4, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
	        break;
	    case Input.KEY_3:
	    	System.exit(0);
	    	break;
	    case Input.KEY_Z:
	    	 game.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		        break;
	    case Input.KEY_X:
	    	game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
	        break;
	    case Input.KEY_ESCAPE:
	    	System.exit(0);
	    	break;
	    default:
	        break;
	    }
	}
}
