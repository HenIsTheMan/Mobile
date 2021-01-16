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

import java.util.ArrayList;

import sg.diploma.product.R;
import sg.diploma.product.background.BackgroundManager;
import sg.diploma.product.background.BackgroundStatuses;
import sg.diploma.product.currency.CurrencyManager;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.easing.EaseInBack;
import sg.diploma.product.easing.EaseInBounce;
import sg.diploma.product.easing.EaseInCirc;
import sg.diploma.product.easing.EaseInCubic;
import sg.diploma.product.easing.EaseInElastic;
import sg.diploma.product.easing.EaseInExpo;
import sg.diploma.product.easing.EaseInOutBack;
import sg.diploma.product.easing.EaseInOutBounce;
import sg.diploma.product.easing.EaseInOutCirc;
import sg.diploma.product.easing.EaseInOutCubic;
import sg.diploma.product.easing.EaseInOutElastic;
import sg.diploma.product.easing.EaseInOutExpo;
import sg.diploma.product.easing.EaseInOutQuad;
import sg.diploma.product.easing.EaseInOutQuart;
import sg.diploma.product.easing.EaseInOutQuint;
import sg.diploma.product.easing.EaseInOutSine;
import sg.diploma.product.easing.EaseInQuad;
import sg.diploma.product.easing.EaseInQuart;
import sg.diploma.product.easing.EaseInQuint;
import sg.diploma.product.easing.EaseInSine;
import sg.diploma.product.easing.EaseOutBack;
import sg.diploma.product.easing.EaseOutBounce;
import sg.diploma.product.easing.EaseOutCirc;
import sg.diploma.product.easing.EaseOutCubic;
import sg.diploma.product.easing.EaseOutElastic;
import sg.diploma.product.easing.EaseOutExpo;
import sg.diploma.product.easing.EaseOutQuad;
import sg.diploma.product.easing.EaseOutQuart;
import sg.diploma.product.easing.EaseOutQuint;
import sg.diploma.product.easing.EaseOutSine;
import sg.diploma.product.easing.Easing;
import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.ParticleSystem;
import sg.diploma.product.entity.entities.EntityCoin;
import sg.diploma.product.entity.entities.EntityEnemy;
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
        canSpawnEnemy = false;

        lastTriggerPosY = 0.0f;
        lastTriggerScaleY = 1.0f;
        jumpMag = 0.0f;

        coinIndex = 0;
        enemyIndex = 0;
        platIndex = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Instance = this;
        Integer indexBG = null;

        BackgroundManager.Instance.LoadBackgroundData(Instance, "Backgrounds.ser");
        final ArrayList<BackgroundStatuses.BackgroundStatus> backgrounds = BackgroundManager.Instance.GetBackgrounds();
        final int backgroundsSize = backgrounds.size();
        for(int i = 0; i < backgroundsSize; ++i){
            if(backgrounds.get(i) == BackgroundStatuses.BackgroundStatus.Equipped){
                indexBG = i;
                break;
            }
        }

        if(indexBG == null){
            view = new GameView(this, 0xFF000000);
        } else{
            switch(indexBG){
                case 0:
                    view = new GameView(this, BackgroundManager.Instance.rawIDs[0], 80, 100);
                    break;
                case 1:
                    view = new GameView(this, BackgroundManager.Instance.rawIDs[1], 80, 100);
                    break;
                case 2:
                    view = new GameView(this, BackgroundManager.Instance.rawIDs[2], 80, 100);
                    break;
                case 3:
                    view = new GameView(this, BackgroundManager.Instance.rawIDs[3], 80, 100);
                    break;
                case 4:
                    view = new GameView(this, BackgroundManager.Instance.rawIDs[4], 80, 100);
                    break;
                case 5:
                    view = new GameView(this, BackgroundManager.Instance.rawIDs[5], 80, 100);
                    break;
                case 6:
                    view = new GameView(this, BackgroundManager.Instance.rawIDs[6], 80, 100);
                    break;
                case 7:
                    view = new GameView(this, BackgroundManager.Instance.rawIDs[7], 80, 100);
                    break;
            }
        }
        setContentView(view);

        CurrencyManager.Instance.LoadCurrencyData();

        assert view != null;
        vibrator = (Vibrator)view.getContext().getSystemService(VIBRATOR_SERVICE);

        Publisher.AddListener(ListenerFlagsWrapper.ListenerFlags.GameScreenActivity.GetVal(), this);
    }

    @Override
    protected void onStop(){
        super.onStop();
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
        particleSystem.Init(999, R.drawable.smoke_particle);

        final float textSize = DeviceManager.screenWidthF * 0.015f;
        final float realTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, DeviceManager.displayMetrics);

        GameData.textOnScreenScore = EntityTextOnScreen.Create("Special_gameTextOnScreenScore", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenScore.SetStrokeWidth(400.0f);
        GameData.textOnScreenScore.SetTextSize(textSize);
        GameData.textOnScreenScore.attribs.pos.x = DeviceManager.screenWidthF * 0.05f - realTextSize * 0.5f;
        GameData.textOnScreenScore.attribs.pos.y = DeviceManager.screenHeightF * 0.05f - realTextSize * 0.5f;

        GameData.textOnScreenCoins = EntityTextOnScreen.Create("Special_gameTextOnScreenCoins", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenCoins.SetStrokeWidth(400.0f);
        GameData.textOnScreenCoins.SetTextSize(textSize);
        GameData.textOnScreenCoins.attribs.pos.x = DeviceManager.screenWidthF * 0.05f - realTextSize * 0.5f;
        GameData.textOnScreenCoins.attribs.pos.y = DeviceManager.screenHeightF * 0.1f - realTextSize * 0.5f;

        GameData.textOnScreenUpdateFPS = EntityTextOnScreen.Create("Special_gameTextOnScreenUpdateFPS", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenUpdateFPS.SetStrokeWidth(400.0f);
        GameData.textOnScreenUpdateFPS.SetTextSize(textSize);
        GameData.textOnScreenUpdateFPS.attribs.pos.x = DeviceManager.screenWidthF * 0.05f - realTextSize * 0.5f;
        GameData.textOnScreenUpdateFPS.attribs.pos.y = DeviceManager.screenHeightF * 0.15f - realTextSize * 0.5f;

        GameData.textOnScreenRenderFPS = EntityTextOnScreen.Create("Special_gameTextOnScreenRenderFPS", _view.getContext().getAssets(), "fonts/grobold.ttf");
        GameData.textOnScreenRenderFPS.SetStrokeWidth(400.0f);
        GameData.textOnScreenRenderFPS.SetTextSize(textSize);
        GameData.textOnScreenRenderFPS.attribs.pos.x = DeviceManager.screenWidthF * 0.05f - realTextSize * 0.5f;
        GameData.textOnScreenRenderFPS.attribs.pos.y = DeviceManager.screenHeightF * 0.2f - realTextSize * 0.5f;

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
        GameData.startPlat.attribs.colliderPos.x = GameData.startPlat.attribs.pos.x;
        GameData.startPlat.attribs.colliderPos.y = GameData.startPlat.attribs.pos.y;
        GameData.startPlat.attribs.colliderScale.x = GameData.startPlat.attribs.scale.x;
        GameData.startPlat.attribs.colliderScale.y = GameData.startPlat.attribs.scale.y;
        GameData.startPlat.SetSteppedOnColor(new Color(1.0f, 0.0f, 1.0f, 0.7f));

        final float playerCharWidth = (float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getWidth() / 9.f * 0.5f;
        final float playerCharHeight = (float)ResourceManager.Instance.GetBitmap(R.drawable.player_char, Bitmap.Config.RGB_565).getHeight() * 0.2f;

        GameData.gamePlayerChar.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        GameData.gamePlayerChar.attribs.pos.y = DeviceManager.screenHeightF - GameData.startPlat.attribs.scale.y - playerCharHeight * 1.2f * 0.5f;

        GameData.gamePlayerChar.attribs.colliderScale.x = playerCharWidth * 1.2f;
        GameData.gamePlayerChar.attribs.colliderScale.y = playerCharHeight * 1.2f;
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
        EntityManager.Instance.cam.SetVelY(-DeviceManager.screenHeightF * 0.1f);
    }

    @Override
    public void Update(float _dt) {
        if(GameData.textOnScreenScore != null){
            GameData.textOnScreenScore.SetText("Score: " + GameData.score);
        }
        if(GameData.textOnScreenCoins != null){
            GameData.textOnScreenCoins.SetText("Coin(s): " + GameData.collectedCoins);
        }
        if(GameData.textOnScreenUpdateFPS != null){
            GameData.textOnScreenUpdateFPS.SetText("UpdateFPS: " + 1.0f / _dt);
        }
        if(GameData.textOnScreenRenderFPS != null && view != null){
            GameData.textOnScreenRenderFPS.SetText("RenderFPS: " + 1.0f / ((GameView)view).GetRenderDt());
        }

        particleSystem.Update(_dt);

        EntityManager.Instance.Update(_dt);

        if(GameData.gamePlayerChar != null){
            final int motionEventAction = TouchManager.Instance.GetMotionEventAction();
            if(motionEventAction == TouchTypes.TouchType.Down.GetVal()){
                jumpMag = -DeviceManager.screenHeightF * 0.95f;
            } else if(motionEventAction == TouchTypes.TouchType.Up.GetVal() && jumpMag < 0.0f){
                GameData.gamePlayerChar.Jump(jumpMag);
                jumpMag = 0.0f;
            }

            EntityManager.Instance.cam.SetPosX(GameData.gamePlayerChar.attribs.pos.x - DeviceManager.screenWidthF * 0.5f);
            GenLvl();
        }

        EntityManager.Instance.LateUpdate(_dt);
    }

    @Override
    public void OnExit(){
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

    private void GenLvl(){
        if(lastTriggerPosY + lastTriggerScaleY * 0.5f >= EntityManager.Instance.cam.GetPos().y){
            final float offset = (float)Pseudorand.PseudorandIntMinMax(380, 400);
            final float posY = lastTriggerPosY - offset;
            final float scaleY = DeviceManager.screenHeightF * Pseudorand.PseudorandFloatMinMax(0.02f, 0.03f);

            if(Pseudorand.PseudorandIntMinMax(1, 5) != 1){
                final EntityPlat plat = EntityPlat.Create("plat_" + ++platIndex);
                plat.SetMyIndex(platIndex);

                if(Pseudorand.PseudorandIntMinMax(1, 5) == 1){
                    if(canSpawnEnemy){
                        plat.attribs.pos.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.4f, 0.6f);
                        plat.attribs.pos.y = posY;
                        plat.attribs.scale.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(1.0f, 1.2f);
                        plat.attribs.scale.y = scaleY;
                        ConfigCollider(plat);

                        SpawnEnemy(plat);

                        canSpawnEnemy = false;
                    } else{
                        plat.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
                        plat.attribs.pos.y = posY;
                        plat.attribs.scale.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.4f, 0.6f);
                        plat.attribs.scale.y = scaleY;
                        ConfigCollider(plat);

                        plat.SetXOffsetMag(plat.attribs.scale.x * 0.2f);
                        plat.SetXOffsetSpd(3.0f);
                        plat.SetEasing(easingTypes[Pseudorand.PseudorandIntMinMax(0, easingTypes.length - 1)]);
                        canSpawnEnemy = true;
                    }
                } else{
                    plat.attribs.pos.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.3f, 0.7f);
                    plat.attribs.pos.y = posY;
                    plat.attribs.scale.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.25f, 0.5f);
                    plat.attribs.scale.y = scaleY;
                    ConfigCollider(plat);

                    if(Pseudorand.PseudorandIntMinMax(1, 5) == 1){
                        SpawnCoin(plat);
                    }

                    canSpawnEnemy = true;
                }
            } else{
                for(int i = 0; i < 2; ++i){
                    final EntityPlat plat = EntityPlat.Create("plat_" + ++platIndex);
                    plat.SetMyIndex(platIndex);

                    plat.attribs.pos.x = DeviceManager.screenWidthF * ((i & 1) == 1
                        ? Pseudorand.PseudorandFloatMinMax(0.0f, 0.2f)
                        : Pseudorand.PseudorandFloatMinMax(0.8f, 1.0f));
                    plat.attribs.pos.y = posY;
                    plat.attribs.scale.x = DeviceManager.screenWidthF * Pseudorand.PseudorandFloatMinMax(0.3f, 0.55f);
                    plat.attribs.scale.y = scaleY;
                    ConfigCollider(plat);

                    if(Pseudorand.PseudorandIntMinMax(1, 10) == 1){
                        SpawnCoin(plat);
                    }
                }

                canSpawnEnemy = true;
            }

            lastTriggerPosY = posY;
            lastTriggerScaleY = scaleY;
        }
    }

    private void SpawnCoin(final EntityPlat plat){
        final EntityCoin coin = EntityCoin.Create("coin_" + ++coinIndex, R.drawable.coin);
        coin.SetMyIndex(coinIndex);

        coin.attribs.scale.x = coin.attribs.scale.y = DeviceManager.screenWidthF * 0.1f;
        coin.attribs.pos.x = plat.attribs.pos.x;
        coin.attribs.pos.y = plat.attribs.pos.y - (plat.attribs.scale.y + coin.attribs.scale.y) * 0.5f - DeviceManager.screenHeightF * 0.04f;

        coin.SetYOffsetSpd(5.0f);
        coin.SetYOffsetMag(DeviceManager.screenHeightF * 0.04f * 2.0f);
        ConfigCollider(coin);
    }

    private void SpawnEnemy(final EntityPlat plat){
        final EntityEnemy enemy = EntityEnemy.Create("enemy_" + ++enemyIndex, R.drawable.enemy);
        enemy.SetMyIndex(enemyIndex);

        enemy.attribs.scale.x = enemy.attribs.scale.y = DeviceManager.screenWidthF * 0.1f;
        enemy.attribs.pos.x = plat.attribs.pos.x + plat.attribs.scale.x * Pseudorand.PseudorandFloatMinMax(-0.4f, 0.4f);
        enemy.attribs.pos.y = plat.attribs.pos.y - (plat.attribs.scale.y + enemy.attribs.scale.y) * 0.5f - DeviceManager.screenHeightF * 0.005f;

        ConfigCollider(enemy);
    }

    private void ConfigCollider(final EntityAbstract entity){
        entity.attribs.colliderPos.x = entity.attribs.pos.x;
        entity.attribs.colliderPos.y = entity.attribs.pos.y;
        entity.attribs.colliderScale.x = entity.attribs.scale.x;
        entity.attribs.colliderScale.y = entity.attribs.scale.y;
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

                finish();
                startActivity(new Intent(this, GameOverScreenActivity.class));

                break;
            case SendForDeactivation:
                particleSystem.AddParticleToDeactivate(((EventSendForDeactivation)event).GetParticle());
                break;
        }
    }

    private boolean canSpawnEnemy;

    private float lastTriggerPosY;
    private float lastTriggerScaleY;
    private float jumpMag;

    private int coinIndex;
    private int enemyIndex;
    private int platIndex;

    private static View view;

    private static Vibrator vibrator;
    public static GameScreenActivity Instance;
    private static final ParticleSystem particleSystem;

    private static final Easing[] easingTypes;

    static{
        view = null;

        vibrator = null;
        Instance = null;
        particleSystem = new ParticleSystem();

        easingTypes = new Easing[]{
            EaseInBack.globalObj,
            EaseInBounce.globalObj,
            EaseInCirc.globalObj,
            EaseInCubic.globalObj,
            EaseInElastic.globalObj,
            EaseInExpo.globalObj,
            EaseInOutBack.globalObj,
            EaseInOutBounce.globalObj,
            EaseInOutCirc.globalObj,
            EaseInOutCubic.globalObj,
            EaseInOutElastic.globalObj,
            EaseInOutExpo.globalObj,
            EaseInOutQuad.globalObj,
            EaseInOutQuart.globalObj,
            EaseInOutQuint.globalObj,
            EaseInOutSine.globalObj,
            EaseInQuad.globalObj,
            EaseInQuart.globalObj,
            EaseInQuint.globalObj,
            EaseInSine.globalObj,
            EaseOutBack.globalObj,
            EaseOutBounce.globalObj,
            EaseOutCirc.globalObj,
            EaseOutCubic.globalObj,
            EaseOutElastic.globalObj,
            EaseOutExpo.globalObj,
            EaseOutQuad.globalObj,
            EaseOutQuart.globalObj,
            EaseOutQuint.globalObj,
            EaseOutSine.globalObj,
        };
    }
}