package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import sg.diploma.product.R;
import sg.diploma.product.currency.CurrencyManager;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.ParticleSystem;
import sg.diploma.product.entity.entities.EntityGamePlayerChar;
import sg.diploma.product.entity.entities.EntityPauseButton;
import sg.diploma.product.entity.entities.EntityPlat;
import sg.diploma.product.entity.entities.EntityTextOnScreen;
import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.IListener;
import sg.diploma.product.event.ListenerFlagsWrapper;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.event.events.EventSendForDeactivation;
import sg.diploma.product.game.GameData;
import sg.diploma.product.game.GameView;
import sg.diploma.product.graphics.Color;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.math.Pseudorand;
import sg.diploma.product.math.Vector2;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

public final class GameScreenActivity extends Activity implements IState, IListener{
    public GameScreenActivity(){
        platIndex = 0;
        lastTriggerPosY = 0.0f;
        lastTriggerScaleY = 1.0f;
        jumpMag = 0.0f;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Instance = this;
        View view = new GameView(this);
        setContentView(view);

        CurrencyManager.Instance.LoadCurrencyData();

        vibrator = (Vibrator)view.getContext().getSystemService(VIBRATOR_SERVICE);

        Publisher.AddListener(ListenerFlagsWrapper.ListenerFlags.GameScreenActivity.GetVal(), this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Publisher.RemoveListener(ListenerFlagsWrapper.ListenerFlags.GameScreenActivity.GetVal());
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event){
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
    public void OnEnter(@NonNull SurfaceView _view){
        Publisher.AddListener(ListenerFlagsWrapper.ListenerFlags.GameData.GetVal(), GameData.globalInstance);

        particleSystem.Init(999, R.drawable.smoke_particle);

        //* Create text on screen
        final float textSize = DeviceManager.screenWidthF * 0.015f;
        final float realTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, DeviceManager.displayMetrics);
        GameData.textOnScreenFPS = EntityTextOnScreen.Create("Special_gameTextOnScreenFPS", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenFPS.SetStrokeWidth(400.0f);
        GameData.textOnScreenFPS.SetTextSize(textSize);
        GameData.textOnScreenFPS.attribs.pos.x = DeviceManager.screenWidthF * 0.05f - realTextSize * 0.5f;
        GameData.textOnScreenFPS.attribs.pos.y = DeviceManager.screenHeightF * 0.05f - realTextSize * 0.5f;

        GameData.textOnScreenScore = EntityTextOnScreen.Create("Special_gameTextOnScreenScore", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenScore.SetStrokeWidth(400.0f);
        GameData.textOnScreenScore.SetTextSize(textSize);
        GameData.textOnScreenScore.attribs.pos.x = DeviceManager.screenWidthF * 0.05f - realTextSize * 0.5f;
        GameData.textOnScreenScore.attribs.pos.y = DeviceManager.screenHeightF * 0.1f - realTextSize * 0.5f;

        GameData.textOnScreenCoins = EntityTextOnScreen.Create("Special_gameTextOnScreenCoins", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenCoins.SetStrokeWidth(400.0f);
        GameData.textOnScreenCoins.SetTextSize(textSize);
        GameData.textOnScreenCoins.attribs.pos.x = DeviceManager.screenWidthF * 0.05f - realTextSize * 0.5f;
        GameData.textOnScreenCoins.attribs.pos.y = DeviceManager.screenHeightF * 0.15f - realTextSize * 0.5f;
        //*/

        //* Create game player char and start plat
        GameData.gamePlayerChar = EntityGamePlayerChar.Create(
            "gamePlayerChar",
            R.drawable.player_char,
            particleSystem
        );

        GameData.startPlat = EntityPlat.Create("plat_start");
        GameData.startPlat.attribs.scale.x = DeviceManager.screenWidthF;
        GameData.startPlat.attribs.scale.y = DeviceManager.screenHeightF * 0.03f;
        GameData.startPlat.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        GameData.startPlat.attribs.pos.y = DeviceManager.screenHeightF - GameData.startPlat.attribs.scale.y * 0.5f;
        GameData.startPlat.attribs.boxColliderPos.x = GameData.startPlat.attribs.pos.x;
        GameData.startPlat.attribs.boxColliderPos.y = GameData.startPlat.attribs.pos.y;
        GameData.startPlat.attribs.boxColliderScale.x = GameData.startPlat.attribs.scale.x;
        GameData.startPlat.attribs.boxColliderScale.y = GameData.startPlat.attribs.scale.y;
        GameData.startPlat.SetSteppedOnColor(new Color(1.0f, 0.0f, 1.0f, 0.7f));

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
        GameData.pauseButton = EntityPauseButton.Create("Special_pauseButton", R.drawable.pause_icon_white, R.drawable.pause_icon_yellow);
        final float buttonSize = DeviceManager.screenWidthF * 0.1f;
        GameData.pauseButton.attribs.scale.x = buttonSize;
        GameData.pauseButton.attribs.scale.y = buttonSize;
        GameData.pauseButton.attribs.pos.x = DeviceManager.screenWidthF - buttonSize;
        GameData.pauseButton.attribs.pos.y = buttonSize;
        //*/

        lastTriggerPosY = GameData.startPlat.attribs.pos.y;
        lastTriggerScaleY = GameData.startPlat.attribs.scale.y;
        EntityManager.Instance.cam.SetPosY(GameData.gamePlayerChar.attribs.pos.y - DeviceManager.screenHeightF * 0.5f);
        EntityManager.Instance.cam.SetVelY(-100.0f);
    }

    @Override
    public void OnExit(){
        Publisher.RemoveListener(ListenerFlagsWrapper.ListenerFlags.GameData.GetVal());

        Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas){
        EntityManager.Instance.SpecialRender(_canvas);

        Vector2 camPos = EntityManager.Instance.GetSceneCam().GetPos();
        _canvas.translate(-camPos.x, -camPos.y);
        particleSystem.Render(_canvas);
        _canvas.translate(camPos.x, camPos.y);
    }

    @Override
    public void Update(float _dt) {
        if(GameData.textOnScreenFPS != null){
            GameData.textOnScreenFPS.SetText("FPS   " + 1.0f / _dt);
        }
        if(GameData.textOnScreenScore != null){
            GameData.textOnScreenScore.SetText("Score   " + GameData.score);
        }
        if(GameData.textOnScreenCoins != null){
            GameData.textOnScreenCoins.SetText("Coins   " + GameData.collectedCoins);
        }

        particleSystem.Update(_dt);

        EntityManager.Instance.Update(_dt);

        if(GameData.gamePlayerChar != null){
            final int motionEventAction = TouchManager.Instance.GetMotionEventAction();
            if(motionEventAction == TouchTypes.TouchType.Down.GetVal()){
                jumpMag = -2000.0f; //??
            } else if(motionEventAction == TouchTypes.TouchType.Up.GetVal()){
                GameData.gamePlayerChar.Jump(jumpMag);
                jumpMag = 0.0f;
            }

            EntityManager.Instance.cam.SetPosX(GameData.gamePlayerChar.attribs.pos.x - DeviceManager.screenWidthF * 0.5f);
            SpawnPlats();
        }

        EntityManager.Instance.LateUpdate(_dt);
    }

    private void SpawnPlats(){
        if(lastTriggerPosY + lastTriggerScaleY * 0.5f >= EntityManager.Instance.cam.GetPos().y){
            final float offset = Pseudorand.PseudorandFloatMinMax(380.f, 490.f);
            EntityPlat plat = EntityPlat.Create("plat_" + ++platIndex);
            plat.SetMyIndex(platIndex);

            plat.attribs.scale.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.2f, 0.4f);
            plat.attribs.scale.y = DeviceManager.screenHeightF * Pseudorand.PseudorandFloatMinMax(0.04f, 0.06f);
            plat.attribs.pos.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.2f, 0.8f);
            plat.attribs.pos.y = lastTriggerPosY - offset;

            plat.attribs.boxColliderPos.x = plat.attribs.pos.x;
            plat.attribs.boxColliderPos.y = plat.attribs.pos.y;
            plat.attribs.boxColliderScale.x = plat.attribs.scale.x;
            plat.attribs.boxColliderScale.y = plat.attribs.scale.y;

            lastTriggerPosY = plat.attribs.pos.y;
            lastTriggerScaleY =  plat.attribs.scale.y;
        }
    }

    @Override
    public void OnEvent(@NonNull EventAbstract event){
        switch(event.GetID()){
            case EndGame:
                if(Build.VERSION.SDK_INT >= 26){
                    vibrator.vibrate(VibrationEffect.createOneShot(400, 255));
                } else{
                    final long[] pattern = {0, 400, 50};
                    vibrator.vibrate(pattern, -1);
                }

                EntityManager.Instance.SendAllEntitiesForRemoval();
                StateManager.Instance.ChangeState("GameOverScreen");

                startActivity(new Intent(this, GameOverScreenActivity.class));
                finish();

                break;
            case SendForDeactivation:
                particleSystem.AddParticleToDeactivate(((EventSendForDeactivation)event).GetParticle());
                break;
        }
    }

    private int platIndex;
    private float lastTriggerPosY;
    private float lastTriggerScaleY;
    private float jumpMag;

    private static Vibrator vibrator;
    public static GameScreenActivity Instance;
    private static final ParticleSystem particleSystem;

    static{
        vibrator = null;
        Instance = null;
        particleSystem = new ParticleSystem();
    }
}