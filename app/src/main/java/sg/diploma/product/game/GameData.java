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

	public final void ResetVars(){
		//gameBG = null;
		gamePlayerChar = null;
		pauseButton = null;
		startPlat = null;
		textOnScreenFPS = null;
		textOnScreenScore = null;

		playerTravelledY = 0.0f;
		totalYOffset = 0.0f;
		score = -1;
		platIndex = 0;

		fingerDownPos = null;
		fingerUpPos = null;
	}

	/*private void SpawnPlat(){
		EntityPlat plat = EntityPlat.Create("plat_" + ++platIndex, gamePlayerChar);
		plat.SetMyIndex(platIndex);
		plat.attribs.scale.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.2f, 0.6f);
		plat.attribs.scale.y = DeviceManager.screenHeightF * Pseudorand.PseudorandFloatMinMax(0.03f, 0.07f);
		plat.attribs.pos.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.2f, 0.8f);
		plat.attribs.pos.y = gamePlayerChar.attribs.boxColliderPos.y - DeviceManager.screenHeight + plat.attribs.scale.y * 0.5f;
		plat.attribs.boxColliderPos.x = plat.attribs.pos.x;
		plat.attribs.boxColliderPos.y = plat.attribs.pos.y;
		plat.attribs.boxColliderScale.x = plat.attribs.scale.x;
		plat.attribs.boxColliderScale.y = plat.attribs.scale.y;
	}*/

	@Override
	public final void OnEvent(EventAbstract event){
		switch(event.GetID()){
			/*case SpawnPlat:
				SpawnPlat();
				break;*/
			case AddScore:
				score += ((EventAddScore)event).GetScoreAdd();
				break;
		}
	}

	//private EntityGameBG gameBG;
	public static EntityGamePlayerChar gamePlayerChar;
	public static EntityPauseButton pauseButton;
	public static EntityPlat startPlat;
	public static EntityTextOnScreen textOnScreenFPS;
	public static EntityTextOnScreen textOnScreenScore;

	public static float playerTravelledY;
	public static float totalYOffset;
	public static int score;
	public static int platIndex;

	public static Vector2 fingerDownPos;
	public static Vector2 fingerUpPos;

	public static GameData globalInstance;

	static{
		//gameBG = null;
		gamePlayerChar = null;
		pauseButton = null;
		startPlat = null;
		textOnScreenFPS = null;
		textOnScreenScore = null;

		playerTravelledY = 0.0f;
		totalYOffset = 0.0f;
		score = -1;
		platIndex = 0;

		fingerDownPos = null;
		fingerUpPos = null;

		globalInstance = new GameData();
	}
}
