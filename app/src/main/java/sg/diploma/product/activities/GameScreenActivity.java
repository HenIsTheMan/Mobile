package sg.diploma.product.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import sg.diploma.product.R;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.entities.EntityGameBG;
import sg.diploma.product.entity.entities.EntityGamePlayerChar;
import sg.diploma.product.entity.entities.EntityPlat;
import sg.diploma.product.entity.entities.EntityTextOnScreen;
import sg.diploma.product.game.GameView;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.math.Pseudorand;
import sg.diploma.product.math.Vector2;
import sg.diploma.product.state.IState;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

public final class GameScreenActivity extends Activity implements IState{
    public GameScreenActivity(){
        gameBG = null;
        gamePlayerChar = null;
        startPlat = null;
        textOnScreen = null;

        spawnThreshold = 0.0f;
        score = -1;

        fingerDownPos = null;
        fingerUpPos = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Instance = this;
        setContentView(new GameView(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
        return true;
    }

    @Override
    public void onBackPressed(){
        //Do nth
    }

    @Override
    public String GetName(){
        return "GameScreen";
    }

    @Override
    public void OnEnter(SurfaceView _view){
        //* Create game BG
        /*gameBG = EntityGameBG.Create(
            "Special_gameBG",
            R.drawable.game_background
        );

        gameBG.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        gameBG.attribs.pos.y = DeviceManager.screenHeightF * 0.5f;
        final float scaleFactor = DeviceManager.screenHeightF / (ResourceManager.Instance.GetBitmap(R.drawable.game_background, Bitmap.Config.RGB_565).getHeight() * 0.5f);
        gameBG.attribs.scale.x = scaleFactor;
        gameBG.attribs.scale.y = scaleFactor;
        gameBG.SetSpriteAnimXScale(scaleFactor);
        gameBG.SetSpriteAnimYScale(scaleFactor);*/
        //*/

        //* Create text on screen
        textOnScreen = EntityTextOnScreen.Create("Special_gameTextOnScreen", _view.getContext().getAssets(), "fonts/grobold.ttf");
        textOnScreen.attribs.pos.x = 30.0f;
        textOnScreen.attribs.pos.y = 80.0f;
        textOnScreen.SetStrokeWidth(300.0f);
        textOnScreen.SetTextSize(55.0f);
        //*/

        startPlat = EntityPlat.Create("startPlat");
        startPlat.attribs.scale.x = DeviceManager.screenWidthF;
        startPlat.attribs.scale.y = DeviceManager.screenHeightF * 0.03f;
        startPlat.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        startPlat.attribs.pos.y = DeviceManager.screenHeightF - startPlat.attribs.scale.y * 0.5f;
        startPlat.attribs.boxColliderPos.x = startPlat.attribs.pos.x;
        startPlat.attribs.boxColliderPos.y = startPlat.attribs.pos.y;
        startPlat.attribs.boxColliderScale.x = startPlat.attribs.scale.x;
        startPlat.attribs.boxColliderScale.y = startPlat.attribs.scale.y;

        gamePlayerChar = EntityGamePlayerChar.Create(
            "Special_gamePlayerChar",
            R.drawable.player_char
        );

        final float playerCharWidth = (float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f;
        final float playerCharHeight = (float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getHeight() * 0.2f;

        gamePlayerChar.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        gamePlayerChar.attribs.pos.y = DeviceManager.screenHeightF - startPlat.attribs.scale.y - playerCharHeight * 1.2f * 0.5f;

        gamePlayerChar.attribs.boxColliderScale.x = playerCharWidth * 1.2f;
        gamePlayerChar.attribs.boxColliderScale.y = playerCharHeight * 1.2f;
        gamePlayerChar.SetSpriteAnimXScale(1.2f);
        gamePlayerChar.SetSpriteAnimYScale(1.2f);

        spawnThreshold = gamePlayerChar.attribs.boxColliderPos.y + gamePlayerChar.attribs.boxColliderScale.y;
    }

    @Override
    public void OnExit(){
        Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas){
        EntityManager.Instance.SpecialRender(_canvas, "Special_gamePlayerChar");
    }

    @Override
    public void Update(float _dt) {
        if(textOnScreen != null){
            textOnScreen.SetText("FPS: " + 1.0f / _dt);
        }

        if(gamePlayerChar != null){ //So player does not exit play area
            final float playerCharHalfWidth = ((float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f) * 0.5f;
            if(gamePlayerChar.attribs.pos.x < playerCharHalfWidth){
                gamePlayerChar.attribs.pos.x = playerCharHalfWidth;
                gamePlayerChar.SwitchFacing();
            }
            if(gamePlayerChar.attribs.pos.x > DeviceManager.screenWidthF - playerCharHalfWidth){
                gamePlayerChar.attribs.pos.x = DeviceManager.screenWidthF - playerCharHalfWidth;
                gamePlayerChar.SwitchFacing();
            }
        }

        GenAndDestroyPlats();

        EntityManager.Instance.Update(_dt);

        if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Down.GetVal()) {
            fingerDownPos = new Vector2(TouchManager.Instance.GetXPos(), TouchManager.Instance.GetYPos());
        }
        if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Up.GetVal()) {
            fingerUpPos = new Vector2(TouchManager.Instance.GetXPos(), TouchManager.Instance.GetYPos());
        }

        gamePlayerChar.Jump(fingerDownPos, fingerUpPos);
        if(fingerDownPos != null && fingerUpPos != null){
            fingerDownPos = null;
            fingerUpPos = null;
        }

        EntityManager.Instance.LateUpdate(_dt);
    }

    private void GenAndDestroyPlats(){
        if(gamePlayerChar.attribs.boxColliderPos.y + gamePlayerChar.attribs.boxColliderScale.y <= spawnThreshold){
            spawnThreshold -= 700.0f;
            EntityPlat plat = EntityPlat.Create("plat_" + spawnThreshold);
            plat.attribs.scale.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.15f, 0.25f);
            plat.attribs.scale.y = DeviceManager.screenHeightF * Pseudorand.PseudorandFloatMinMax(0.03f, 0.07f);
            plat.attribs.pos.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.2f, 0.8f);
            plat.attribs.pos.y = DeviceManager.screenHeightF - plat.attribs.scale.y * 0.5f + spawnThreshold;
            plat.attribs.boxColliderPos.x = plat.attribs.pos.x;
            plat.attribs.boxColliderPos.y = plat.attribs.pos.y;
            plat.attribs.boxColliderScale.x = plat.attribs.scale.x;
            plat.attribs.boxColliderScale.y = plat.attribs.scale.y;
        }
    }

    private EntityGameBG gameBG;
    private EntityGamePlayerChar gamePlayerChar;
    private EntityPlat startPlat;
    private EntityTextOnScreen textOnScreen;

    private float spawnThreshold;
    private int score;

    private Vector2 fingerDownPos;
    private Vector2 fingerUpPos;

    public static GameScreenActivity Instance;

    static{
        Instance = null;
    }
}