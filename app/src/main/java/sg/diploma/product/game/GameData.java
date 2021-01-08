package sg.diploma.product.game;

import sg.diploma.product.entity.entities.EntityGamePlayerChar;
import sg.diploma.product.entity.entities.EntityPauseButton;
import sg.diploma.product.entity.entities.EntityPlat;
import sg.diploma.product.entity.entities.EntityTextOnScreen;
import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.IListener;
import sg.diploma.product.event.events.EventAddScore;
import sg.diploma.product.math.Vector2;

public final class GameData implements IListener{ //Singleton
	private GameData(){
	}

	public void ResetVars(){
		gamePlayerChar = null;
		pauseButton = null;
		startPlat = null;
		textOnScreenFPS = null;
		textOnScreenScore = null;

		playerTravelledY = 0.0f;
		totalYOffset = 0.0f;
		score = -1;

		fingerDownPos = null;
		fingerUpPos = null;
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
	public static EntityTextOnScreen textOnScreenFPS;
	public static EntityTextOnScreen textOnScreenScore;

	public static float playerTravelledY;
	public static float totalYOffset;
	public static int score;

	public static Vector2 fingerDownPos;
	public static Vector2 fingerUpPos;

	public static GameData globalInstance;

	static{
		gamePlayerChar = null;
		pauseButton = null;
		startPlat = null;
		textOnScreenFPS = null;
		textOnScreenScore = null;

		playerTravelledY = 0.0f;
		totalYOffset = 0.0f;
		score = -1;

		fingerDownPos = null;
		fingerUpPos = null;

		globalInstance = new GameData();
	}
}
