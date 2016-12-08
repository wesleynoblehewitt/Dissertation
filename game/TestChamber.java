package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import engine_yuki.Constants;

public class TestChamber extends AppGameContainer{

	GameRunner gameRunner;
	AIChamber aiChamber;
	BetaChamber betaChamber;
	GameChamber gameChamber;
	
	
	public TestChamber(GameRunner game) throws SlickException {
		super(game);	
		gameRunner = game;
		aiChamber = null;
		betaChamber = null;
		gameChamber = null;
		setDisplayMode(Constants.screenWidth, Constants.screenHeight, false);
	}


	public TestChamber(AIChamber game) throws SlickException {
		super(game);
		aiChamber = game;
		gameRunner = null;
		betaChamber = null;
		gameChamber = null;
		setDisplayMode(Constants.screenWidth, Constants.screenHeight, false);
	}
	
	public TestChamber(BetaChamber game) throws SlickException {
		super(game);
		betaChamber = game;
		gameRunner = null;
		aiChamber = null;
		gameChamber = null;
		setDisplayMode(Constants.screenWidth, Constants.screenHeight, false);
	}

	public TestChamber(GameChamber game) throws SlickException {
		super(game);
		betaChamber = null;
		gameRunner = null;
		aiChamber = null;
		gameChamber = game;
		setDisplayMode(Constants.screenWidth, Constants.screenHeight, false);
	}
}
