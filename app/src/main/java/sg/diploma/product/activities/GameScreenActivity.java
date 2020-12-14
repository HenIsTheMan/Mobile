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
import sg.diploma.product.entity.entities.EntityGamePlayerChar;
import sg.diploma.product.entity.entities.EntityPauseButton;
import sg.diploma.product.entity.entities.EntityPlat;
import sg.diploma.product.entity.entities.EntityTextOnScreen;
import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.IListener;
import sg.diploma.product.event.ListenerFlagsWrapper;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.game.GameData;
import sg.diploma.product.game.GameView;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.math.Pseudorand;
import sg.diploma.product.math.Vector2;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

public final class GameScreenActivity extends Activity implements IState, IListener{
    public GameScreenActivity(){
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
        Publisher.AddListener(ListenerFlagsWrapper.ListenerFlags.GameData.GetVal(), GameData.globalInstance);

        //* Create game BG (Removed as super laggy)
        /*GameData.gameBG = EntityGameBG.Create(
            "Special_gameBG",
            R.drawable.game_background
        );

        GameData.gameBG.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        GameData.gameBG.attribs.pos.y = DeviceManager.screenHeightF * 0.5f;
        final float scaleFactor = DeviceManager.screenHeightF / (ResourceManager.Instance.GetBitmap(R.drawable.game_background, Bitmap.Config.RGB_565).getHeight() * 0.5f);
        GameData.gameBG.attribs.scale.x = scaleFactor;
        GameData.gameBG.attribs.scale.y = scaleFactor;
        GameData.gameBG.SetSpriteAnimXScale(scaleFactor);
        GameData.gameBG.SetSpriteAnimYScale(scaleFactor);*/
        //*/

        //* Create text on screen
        final float textSize = DeviceManager.screenWidthF * 0.05f;
        GameData.textOnScreenFPS = EntityTextOnScreen.Create("Special_gameTextOnScreenFPS", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenFPS.attribs.pos.x = textSize * 0.5f;
        GameData.textOnScreenFPS.attribs.pos.y = textSize;
        GameData.textOnScreenFPS.SetStrokeWidth(400.0f);
        GameData.textOnScreenFPS.SetTextSize(textSize);

        GameData.textOnScreenScore = EntityTextOnScreen.Create("Special_gameTextOnScreenScore", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenScore.attribs.pos.x = textSize * 0.5f;
        GameData.textOnScreenScore.attribs.pos.y = textSize * 2.0f;
        GameData.textOnScreenScore.SetStrokeWidth(400.0f);
        GameData.textOnScreenScore.SetTextSize(textSize);
        //*/

        //* Create game player char and start plat
        GameData.gamePlayerChar = EntityGamePlayerChar.Create(
            "Special_gamePlayerChar",
            R.drawable.player_char
        );

        GameData.startPlat = EntityPlat.Create("plat_0", GameData.gamePlayerChar);
        GameData.startPlat.SetMyIndex(GameData.platIndex++);
        GameData.startPlat.attribs.scale.x = DeviceManager.screenWidthF;
        GameData.startPlat.attribs.scale.y = DeviceManager.screenHeightF * 0.03f;
        GameData.startPlat.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        GameData.startPlat.attribs.pos.y = DeviceManager.screenHeightF - GameData.startPlat.attribs.scale.y * 0.5f;
        GameData.startPlat.attribs.boxColliderPos.x = GameData.startPlat.attribs.pos.x;
        GameData.startPlat.attribs.boxColliderPos.y = GameData.startPlat.attribs.pos.y;
        GameData.startPlat.attribs.boxColliderScale.x = GameData.startPlat.attribs.scale.x;
        GameData.startPlat.attribs.boxColliderScale.y = GameData.startPlat.attribs.scale.y;

        for(int i = 0; i < 3; ++i){
            EntityPlat plat = EntityPlat.Create("plat_" + ++GameData.platIndex, GameData.gamePlayerChar);
            plat.SetMyIndex(GameData.platIndex);
            plat.attribs.scale.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.2f, 0.6f);
            plat.attribs.scale.y = DeviceManager.screenHeightF * Pseudorand.PseudorandFloatMinMax(0.03f, 0.07f);
            plat.attribs.pos.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.2f, 0.8f);
            plat.attribs.pos.y = DeviceManager.screenHeightF - 400.0f * (float)(i + 1);
            plat.attribs.boxColliderPos.x = plat.attribs.pos.x;
            plat.attribs.boxColliderPos.y = plat.attribs.pos.y;
            plat.attribs.boxColliderScale.x = plat.attribs.scale.x;
            plat.attribs.boxColliderScale.y = plat.attribs.scale.y;
        }

        final float playerCharWidth = (float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f;
        final float playerCharHeight = (float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getHeight() * 0.2f;

        GameData.gamePlayerChar.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        GameData.gamePlayerChar.attribs.pos.y = DeviceManager.screenHeightF - GameData.startPlat.attribs.scale.y - playerCharHeight * 1.2f * 0.5f;

        GameData.gamePlayerChar.attribs.boxColliderScale.x = playerCharWidth * 1.2f;
        GameData.gamePlayerChar.attribs.boxColliderScale.y = playerCharHeight * 1.2f;
        GameData.gamePlayerChar.SetSpriteAnimXScale(1.2f);
        GameData.gamePlayerChar.SetSpriteAnimYScale(1.2f);
        //*/

        //* Create pause button
        GameData.pauseButton = EntityPauseButton.Create("Special_pauseButton", R.drawable.pause_icon);
        final float buttonSize = DeviceManager.screenWidthF * 0.1f;
        GameData.pauseButton.attribs.scale.x = buttonSize;
        GameData.pauseButton.attribs.scale.y = buttonSize;
        GameData.pauseButton.attribs.pos.x = DeviceManager.screenWidthF - buttonSize;
        GameData.pauseButton.attribs.pos.y = buttonSize;
        //*/
    }

    @Override
    public void OnExit(){
        Publisher.RemoveListener(ListenerFlagsWrapper.ListenerFlags.GameData.GetVal());

        Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas){
        EntityManager.Instance.SpecialRender(_canvas, "Special_gamePlayerChar");
    }

    @Override
    public void Update(float _dt) {
        if(GameData.textOnScreenFPS != null){
            GameData.textOnScreenFPS.SetText("FPS: " + 1.0f / _dt);
        }
        if(GameData.textOnScreenScore != null){
            GameData.textOnScreenScore.SetText("Score: " + GameData.score);
        }

        if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Down.GetVal()) {
            GameData.fingerDownPos = new Vector2(TouchManager.Instance.GetXPos(), TouchManager.Instance.GetYPos());
        }
        if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Up.GetVal()) {
            GameData.fingerUpPos = new Vector2(TouchManager.Instance.GetXPos(), TouchManager.Instance.GetYPos());
        }

        if(GameData.gamePlayerChar != null){
            GameData.gamePlayerChar.Jump(GameData.fingerDownPos, GameData.fingerUpPos);
            if(GameData.fingerDownPos != null && GameData.fingerUpPos != null){
                GameData.fingerDownPos = null;
                GameData.fingerUpPos = null;
            }

            //* So player does not exit play area
            final float playerCharHalfWidth = ((float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f) * 0.5f;
            if(GameData.gamePlayerChar.attribs.pos.x < playerCharHalfWidth){
                GameData.gamePlayerChar.attribs.pos.x = playerCharHalfWidth;
                GameData.gamePlayerChar.SwitchFacing();
            }
            if(GameData.gamePlayerChar.attribs.pos.x > DeviceManager.screenWidthF - playerCharHalfWidth){
                GameData.gamePlayerChar.attribs.pos.x = DeviceManager.screenWidthF - playerCharHalfWidth;
                GameData.gamePlayerChar.SwitchFacing();
            }
            //*/
        }

        EntityManager.Instance.Update(_dt);

        EntityManager.Instance.LateUpdate(_dt);
    }

    @Override
    public void OnEvent(EventAbstract event){
        switch(event.GetID()){
            case EndGame:
                GameData.globalInstance.ResetVars();
                EntityManager.Instance.SendAllEntitiesForRemoval();
                StateManager.Instance.ChangeState("MenuScreen");
                break;
        }
    }

    public static GameScreenActivity Instance;

    static{
        Instance = null;
    }
}