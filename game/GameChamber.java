package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameChamber extends StateBasedGame {

	static TestChamber container;
	public GameChamber(String name) {
		super(name);
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		  addState(new Menu());
		  addState(new Level1());
		  addState(new Level2());
		  addState(new Level3());
		  addState(new PhysicsChamber());
	}

    public static void main(String[] args) {
//    	System.setProperty("java.library.path", "libs");
//    	System.setProperty("org.lwjgl.librarypath", new File("libs/native/windows").getAbsolutePath());
    	GameChamber game = new GameChamber("game chamber");
    	
    	try {
			container = new TestChamber(game);
		
			container.start();
    	} catch (SlickException e) {
			e.printStackTrace();
		}
    }
}
