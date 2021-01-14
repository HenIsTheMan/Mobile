package sg.diploma.product.game;

import sg.diploma.product.entity.entities.EntityGamePlayerChar;
import sg.diploma.product.entity.entities.EntityPauseButton;
import sg.diploma.product.entity.entities.EntityPlat;
import sg.diploma.product.entity.entities.EntityTextOnScreen;
import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.IListener;
import sg.diploma.product.event.events.EventAddScore;

public final class GameData implements IListener{ //Singleton
	private GameData(){
	}

	public void ResetVars(){
		gamePlayerChar = null;
		pauseButton = null;
		startPlat = null;
		textOnScreenUpdateFPS = null;
		textOnScreenRenderFPS = null;
		textOnScreenScore = null;
		textOnScreenCoins = null;

		collectedCoins = 0;
		score = -1;
	}

	@Override
	public void OnEvent(EventAbstract event){
		switch(event.GetID()){
			case AddScore:
				score += ((EventAddScore)event).GetScoreAdd();
				break;
		}
	}

	public static EntityGamePlayerChar gamePlayerChar;
	public static EntityPauseButton pauseButton;
	public static EntityPlat startPlat;
	public static EntityTextOnScreen textOnScreenUpdateFPS;
	public static EntityTextOnScreen textOnScreenRenderFPS;
	public static EntityTextOnScreen textOnScreenScore;
	public static EntityTextOnScreen textOnScreenCoins;

	public static int collectedCoins;
	public static int score;

	public static GameData globalInstance;

	static{
		gamePlayerChar = null;
		pauseButton = null;
		startPlat = null;
		textOnScreenUpdateFPS = null;
		textOnScreenRenderFPS = null;
		textOnScreenScore = null;
		textOnScreenCoins = null;

		collectedCoins = 0;
		score = -1;

		globalInstance = new GameData();
	}
}
