package sg.diploma.product.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.device.RenderThread;
import sg.diploma.product.device.UpdateThread;
import sg.diploma.product.dialog_frags.MenuDialogFrag;
import sg.diploma.product.entity.EntityConstraint;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.entities.EntityBall;
import sg.diploma.product.entity.entities.EntityMenuPlayerChar;
import sg.diploma.product.entity.entities.EntityTextOnScreen;
import sg.diploma.product.event.EventAbstract;
import sg.diploma.product.event.IListener;
import sg.diploma.product.event.ListenerFlagsWrapper;
import sg.diploma.product.event.Publisher;
import sg.diploma.product.graphics.Color;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

public final class MenuScreenActivity
    extends FragmentActivity
    implements View.OnTouchListener, IState, SensorEventListener, IListener{

    public MenuScreenActivity(){
        calibrated = false;
        isFingerOffScreenBefore = true;
        shldStartMoving = false;

        startButtonDownAnimSet = null;
        startButtonUpAnimSet = null;
        rankingsButtonDownAnimSet = null;
        rankingsButtonUpAnimSet = null;
        shopButtonDownAnimSet = null;
        shopButtonUpAnimSet = null;
        optionsButtonDownAnimSet = null;
        optionsButtonUpAnimSet = null;
        exitButtonDownAnimSet = null;
        exitButtonUpAnimSet = null;

        startButton = null;
        rankingsButton = null;
        shopButton = null;
        optionsButton = null;
        exitButton = null;

        ball = null;
        menuPlayerChar = null;
        textOnScreenUpdateFPS = null;
        textOnScreenRenderFPS = null;

        playIcon = null;
        podiumIcon = null;
        shoppingCartIcon = null;
        gearsIcon = null;
        leaveIcon = null;
        myShape = null;

        sensorManager = null;

        gameTitleBossText = null;
        gameTitleGirlText = null;

        font = null;

        callbackManager = null;

        movie = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen_layout);

        Publisher.AddListener(ListenerFlagsWrapper.ListenerFlags.MenuScreenActivity.GetVal(), this);

        final SurfaceView menuSurfaceView = findViewById(R.id.menuSurfaceView);
        movie = Movie.decodeStream(menuSurfaceView.getContext().getResources().openRawResource(R.raw.space));

        updateThread = new UpdateThread(menuSurfaceView, movie, 80);
        updateThread.SetDelay(60);
        renderThread = new RenderThread(menuSurfaceView, movie);

        final SurfaceHolder surfaceHolder = menuSurfaceView.getHolder();

        if(surfaceHolder != null){
            surfaceHolder.addCallback(new SurfaceHolder.Callback(){
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder){
                    if(!updateThread.GetIsRunning()){
                        updateThread.Begin();
                    }
                    if(!renderThread.GetIsRunning()){
                        renderThread.Begin();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder){
                    if(updateThread.GetIsRunning()){
                        updateThread.Terminate();
                    }
                    if(renderThread.GetIsRunning()){
                        renderThread.Terminate();
                    }
                }
            });
        }

        sensorManager = (SensorManager)menuSurfaceView.getContext().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_FASTEST
        );

        AudioManager.Instance.LoadAudioVolData();
        AudioManager.Instance.PlayAudio(R.raw.theme, AudioTypes.AudioType.Music);
        InitOthers();
        InitFB();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Publisher.RemoveListener(ListenerFlagsWrapper.ListenerFlags.MenuScreenActivity.GetVal());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    @Override
    public void onSensorChanged(SensorEvent SenseEvent){
        if(ball != null){
            if(!calibrated){
                SenseEvent.values[0] = 0.0f;
                SenseEvent.values[1] = 0.0f;
                SenseEvent.values[2] = 0.0f;
                calibrated = true;
            }
            ball.SetVals(SenseEvent.values);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event){
        TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        if(view == startButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startButton.clearAnimation();
                    startButton.startAnimation(startButtonDownAnimSet);

                    return true;
                case MotionEvent.ACTION_UP:
                    startButton.clearAnimation();
                    startButton.startAnimation(startButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    EntityManager.Instance.SendAllEntitiesForRemoval();
                    StateManager.Instance.ChangeState("GameScreen");

                    finish();
                    startActivity(new Intent(this, GameScreenActivity.class));

                    return true;
            }
            return false;
        }
        if(view == rankingsButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    rankingsButton.clearAnimation();
                    rankingsButton.startAnimation(rankingsButtonDownAnimSet);

                    return true;
                case MotionEvent.ACTION_UP:
                    rankingsButton.clearAnimation();
                    rankingsButton.startAnimation(rankingsButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    EntityManager.Instance.SendAllEntitiesForRemoval();
                    StateManager.Instance.ChangeState("RankingsScreen");

                    finish();
                    startActivity(new Intent(this, RankingsScreenActivity.class));

                    return true;
            }
            return false;
        }
        if(view == shopButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    shopButton.clearAnimation();
                    shopButton.startAnimation(shopButtonDownAnimSet);

                    return true;
                case MotionEvent.ACTION_UP:
                    shopButton.clearAnimation();
                    shopButton.startAnimation(shopButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    EntityManager.Instance.SendAllEntitiesForRemoval();
                    StateManager.Instance.ChangeState("ShopScreen");

                    finish();
                    startActivity(new Intent(this, ShopScreenActivity.class));

                    return true;
            }
            return false;
        }
        if(view == optionsButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    optionsButton.clearAnimation();
                    optionsButton.startAnimation(optionsButtonDownAnimSet);

                    return true;
                case MotionEvent.ACTION_UP:
                    optionsButton.clearAnimation();
                    optionsButton.startAnimation(optionsButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    EntityManager.Instance.SendAllEntitiesForRemoval();
                    AudioManager.Instance.SaveAudioVolData();
                    StateManager.Instance.ChangeState("OptionsScreen");

                    finish();
                    startActivity(new Intent(this, OptionsScreenActivity.class));

                    return true;
            }
            return false;
        }
        if(view == exitButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    exitButton.clearAnimation();
                    exitButton.startAnimation(exitButtonDownAnimSet);

                    return true;
                case MotionEvent.ACTION_UP:
                    exitButton.clearAnimation();
                    exitButton.startAnimation(exitButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    return true;
            }
            return false;
        }

        return false;
    }

    @Override
    public void onBackPressed(){
        if(MenuDialogFrag.isShown){
            return;
        }

        MenuDialogFrag dialogFrag = new MenuDialogFrag();
        dialogFrag.show(getSupportFragmentManager(), "MenuDialogFrag");
    }

    @Override
    public void Render(Canvas _canvas){
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void OnEnter(@NonNull SurfaceView _view){
        //* Create ball
        ball = EntityBall.Create(
            "ball"
        );

        ball.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        ball.attribs.pos.y = DeviceManager.screenHeightF * 0.8f;
        ball.attribs.scale.x = ball.attribs.scale.y = 80.0f;
        final float ballHalfSize = ball.attribs.scale.x * 0.5f;

        ball.attribs.xMin = new EntityConstraint();
        ball.attribs.xMax = new EntityConstraint();
        ball.attribs.yMin = new EntityConstraint();
        ball.attribs.yMax = new EntityConstraint();
        ball.attribs.xMin.val = ballHalfSize;
        ball.attribs.xMax.val = DeviceManager.screenWidthF - ballHalfSize;
        ball.attribs.yMin.val = ballHalfSize;
        ball.attribs.yMax.val = DeviceManager.screenHeightF - ballHalfSize;
        //*/

        //* Create menu player char
        menuPlayerChar = EntityMenuPlayerChar.Create(
                "menuPlayerChar",
                R.drawable.player_char
        );

        menuPlayerChar.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        menuPlayerChar.attribs.pos.y = DeviceManager.screenHeightF * 3.0f * 0.225f;

        menuPlayerChar.attribs.scale.x = menuPlayerChar.attribs.scale.y = 1.8f;
        menuPlayerChar.SetSpriteAnimXScale(1.8f);
        menuPlayerChar.SetSpriteAnimYScale(1.8f);

        menuPlayerChar.attribs.xMin = new EntityConstraint();
        menuPlayerChar.attribs.xMin.val = menuPlayerChar.GetWidth() * menuPlayerChar.attribs.scale.x * 0.25f;
        menuPlayerChar.attribs.xMax = new EntityConstraint();
        menuPlayerChar.attribs.xMax.val = DeviceManager.screenWidthF - menuPlayerChar.GetWidth() * menuPlayerChar.attribs.scale.x * 0.25f;
        menuPlayerChar.attribs.yMin = new EntityConstraint();
        menuPlayerChar.attribs.yMin.val = (DeviceManager.screenHeightF * 0.35f + DeviceManager.screenWidthF * 0.25f) * 1.3f;
        menuPlayerChar.attribs.yMax = new EntityConstraint();
        menuPlayerChar.attribs.yMax.val = DeviceManager.screenHeightF - menuPlayerChar.GetHeight() * menuPlayerChar.attribs.scale.y * 0.5f;
        //*/

        final float textSize = DeviceManager.screenWidthF * 0.015f;
        final Color textColor = new Color(1.0f, 0.0f, 1.0f, 1.0f);

        textOnScreenUpdateFPS = EntityTextOnScreen.Create("menuTextOnScreenUpdateFPS", _view.getContext().getAssets(), "fonts/grobold.ttf");
        textOnScreenUpdateFPS.attribs.pos.x = DeviceManager.screenWidthF * 0.03f;
        textOnScreenUpdateFPS.attribs.pos.y = DeviceManager.screenHeightF - textOnScreenUpdateFPS.attribs.pos.x * 5.5f;
        textOnScreenUpdateFPS.SetColor(textColor);
        textOnScreenUpdateFPS.SetStrokeWidth(90.0f);
        textOnScreenUpdateFPS.SetTextSize(textSize);

        textOnScreenRenderFPS = EntityTextOnScreen.Create("menuTextOnScreenRenderFPS", _view.getContext().getAssets(), "fonts/grobold.ttf");
        textOnScreenRenderFPS.attribs.pos.x = DeviceManager.screenWidthF * 0.03f;
        textOnScreenRenderFPS.attribs.pos.y = DeviceManager.screenHeightF - textOnScreenRenderFPS.attribs.pos.x * 2.0f;
        textOnScreenRenderFPS.SetColor(textColor);
        textOnScreenRenderFPS.SetStrokeWidth(90.0f);
        textOnScreenRenderFPS.SetTextSize(textSize);
    }

    @Override
    public void Update(float _dt){
        if(textOnScreenUpdateFPS != null){
            textOnScreenUpdateFPS.SetText("UpdateFPS: " + 1.0f / _dt);
        }
        if(textOnScreenRenderFPS != null && renderThread != null){
            textOnScreenRenderFPS.SetText("RenderFPS: " + 1.0f / renderThread.GetDt());
        }

        if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Down.GetVal()) {
            if(isFingerOffScreenBefore){
                shldStartMoving = true;
                isFingerOffScreenBefore = false;
            }
        } else if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Up.GetVal()) {
            isFingerOffScreenBefore = true;
        }

        if(shldStartMoving){
            menuPlayerChar.StartMoving(TouchManager.Instance.GetXPos(), TouchManager.Instance.GetYPos());
            shldStartMoving = false;
        }

        EntityManager.Instance.Update(_dt);
    }

    @Override
    public void OnExit(){
    }

    @Override
    public String GetName(){
        return "MenuScreen";
    }

    @Override
    public void OnEvent(@NonNull EventAbstract event){
        switch(event.GetID()){
            case EndProg:
                finishAndRemoveTask();
                System.exit(0);
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitOthers(){
        if(showMyShape){
            myShape = findViewById(R.id.myShape);
            myShape.getLayoutParams().width = (int)(DeviceManager.screenWidthF * 1.2f);
            myShape.getLayoutParams().height = (int)(DeviceManager.screenHeightF * 1.2f);

            Animation myShapeAnim = AnimationUtils.loadAnimation(this, R.anim.my_shape_anim);
            myShapeAnim.setStartOffset(799);
            myShapeAnim.setAnimationListener(new Animation.AnimationListener(){
                @Override
                public void onAnimationStart(Animation anim){
                }

                @Override
                public void onAnimationRepeat(Animation anim){
                }

                @Override
                public void onAnimationEnd(Animation anim){
                    myShape.setVisibility(View.GONE);
                }
            });
            myShape.startAnimation(myShapeAnim);

            showMyShape = false;
        }

        final float buttonFactor = DeviceManager.screenWidthF * 0.25f / 300.0f;
        final int buttonSize = (int)(300.0f * buttonFactor);

        startButton = findViewById(R.id.startButton);
        startButton.setOnTouchListener(this);
        startButton.getLayoutParams().width = buttonSize;
        startButton.getLayoutParams().height = buttonSize;
        startButton.setTranslationX(DeviceManager.screenWidthF * 0.2f - (float)buttonSize * 0.5f);
        startButton.setTranslationY(DeviceManager.screenHeightF * 0.45f - (float)buttonSize * 0.5f);

        startButtonDownAnimSet = new AnimationSet(true);
        startButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.ABSOLUTE, startButton.getTranslationX() + buttonSize * 0.5f,
                Animation.ABSOLUTE, startButton.getTranslationY() + buttonSize * 0.5f));
        startButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        startButtonDownAnimSet.setDuration(400);
        startButtonDownAnimSet.setFillEnabled(true);
        startButtonDownAnimSet.setFillAfter(true);
        startButtonDownAnimSet.setInterpolator(this, R.anim.my_anticipate_interpolator);

        startButtonUpAnimSet = new AnimationSet(true);
        startButtonUpAnimSet.addAnimation(new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.ABSOLUTE, startButton.getTranslationX() + buttonSize * 0.5f,
                Animation.ABSOLUTE, startButton.getTranslationY() + buttonSize * 0.5f));
        startButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
        startButtonUpAnimSet.setDuration(400);
        startButtonUpAnimSet.setFillEnabled(true);
        startButtonUpAnimSet.setFillAfter(true);
        startButtonUpAnimSet.setInterpolator(this, R.anim.my_overshoot_interpolator);

        rankingsButton = findViewById(R.id.rankingsButton);
        rankingsButton.setOnTouchListener(this);
        rankingsButton.getLayoutParams().width = buttonSize;
        rankingsButton.getLayoutParams().height = buttonSize;
        rankingsButton.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)buttonSize * 0.5f);
        rankingsButton.setTranslationY(DeviceManager.screenHeightF * 0.45f - (float)buttonSize * 0.5f);

        rankingsButtonDownAnimSet = new AnimationSet(true);
        rankingsButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.ABSOLUTE, rankingsButton.getTranslationX() + buttonSize * 0.5f,
                Animation.ABSOLUTE, rankingsButton.getTranslationY() + buttonSize * 0.5f));
        rankingsButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        rankingsButtonDownAnimSet.setDuration(400);
        rankingsButtonDownAnimSet.setFillEnabled(true);
        rankingsButtonDownAnimSet.setFillAfter(true);
        rankingsButtonDownAnimSet.setInterpolator(this, R.anim.my_anticipate_interpolator);

        rankingsButtonUpAnimSet = new AnimationSet(true);
        rankingsButtonUpAnimSet.addAnimation(new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.ABSOLUTE, rankingsButton.getTranslationX() + buttonSize * 0.5f,
                Animation.ABSOLUTE, rankingsButton.getTranslationY() + buttonSize * 0.5f));
        rankingsButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
        rankingsButtonUpAnimSet.setDuration(400);
        rankingsButtonUpAnimSet.setFillEnabled(true);
        rankingsButtonUpAnimSet.setFillAfter(true);
        rankingsButtonUpAnimSet.setInterpolator(this, R.anim.my_overshoot_interpolator);

        shopButton = findViewById(R.id.shopButton);
        shopButton.setOnTouchListener(this);
        shopButton.getLayoutParams().width = buttonSize;
        shopButton.getLayoutParams().height = buttonSize;
        shopButton.setTranslationX(DeviceManager.screenWidthF * 0.8f - (float)buttonSize * 0.5f);
        shopButton.setTranslationY(DeviceManager.screenHeightF * 0.45f - (float)buttonSize * 0.5f);

        shopButtonDownAnimSet = new AnimationSet(true);
        shopButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.ABSOLUTE, shopButton.getTranslationX() + buttonSize * 0.5f,
                Animation.ABSOLUTE, shopButton.getTranslationY() + buttonSize * 0.5f));
        shopButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        shopButtonDownAnimSet.setDuration(400);
        shopButtonDownAnimSet.setFillEnabled(true);
        shopButtonDownAnimSet.setFillAfter(true);
        shopButtonDownAnimSet.setInterpolator(this, R.anim.my_anticipate_interpolator);

        shopButtonUpAnimSet = new AnimationSet(true);
        shopButtonUpAnimSet.addAnimation(new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.ABSOLUTE, shopButton.getTranslationX() + buttonSize * 0.5f,
                Animation.ABSOLUTE, shopButton.getTranslationY() + buttonSize * 0.5f));
        shopButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
        shopButtonUpAnimSet.setDuration(400);
        shopButtonUpAnimSet.setFillEnabled(true);
        shopButtonUpAnimSet.setFillAfter(true);
        shopButtonUpAnimSet.setInterpolator(this, R.anim.my_overshoot_interpolator);

        final float otherButtonSize = (float)buttonSize * 0.7f;
        final float exitButttonTranslateX = DeviceManager.screenWidthF * 0.87f - otherButtonSize * 0.5f;
        final float otherButtonTranslateY = DeviceManager.screenHeightF - (DeviceManager.screenWidthF - exitButttonTranslateX);
        final float otherButtonSpacing = DeviceManager.screenWidthF - exitButttonTranslateX;

        optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnTouchListener(this);
        optionsButton.getLayoutParams().width = optionsButton.getLayoutParams().height = (int)otherButtonSize;
        optionsButton.setTranslationX(exitButttonTranslateX - otherButtonSpacing);
        optionsButton.setTranslationY(otherButtonTranslateY);

        optionsButtonDownAnimSet = new AnimationSet(true);
        optionsButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.ABSOLUTE, optionsButton.getTranslationX() + otherButtonSize * 0.5f,
                Animation.ABSOLUTE, optionsButton.getTranslationY() + otherButtonSize * 0.5f));
        optionsButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        optionsButtonDownAnimSet.setDuration(400);
        optionsButtonDownAnimSet.setFillEnabled(true);
        optionsButtonDownAnimSet.setFillAfter(true);
        optionsButtonDownAnimSet.setInterpolator(this, R.anim.my_anticipate_interpolator);

        optionsButtonUpAnimSet = new AnimationSet(true);
        optionsButtonUpAnimSet.addAnimation(new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.ABSOLUTE, optionsButton.getTranslationX() + otherButtonSize * 0.5f,
                Animation.ABSOLUTE, optionsButton.getTranslationY() + otherButtonSize * 0.5f));
        optionsButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
        optionsButtonUpAnimSet.setDuration(400);
        optionsButtonUpAnimSet.setFillEnabled(true);
        optionsButtonUpAnimSet.setFillAfter(true);
        optionsButtonUpAnimSet.setInterpolator(this, R.anim.my_overshoot_interpolator);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnTouchListener(this);
        exitButton.getLayoutParams().width = exitButton.getLayoutParams().height = (int)otherButtonSize;
        exitButton.setTranslationX(exitButttonTranslateX);
        exitButton.setTranslationY(otherButtonTranslateY);

        exitButtonDownAnimSet = new AnimationSet(true);
        exitButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.ABSOLUTE, exitButton.getTranslationX() + otherButtonSize * 0.5f,
                Animation.ABSOLUTE, exitButton.getTranslationY() + otherButtonSize * 0.5f));
        exitButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        exitButtonDownAnimSet.setDuration(400);
        exitButtonDownAnimSet.setFillEnabled(true);
        exitButtonDownAnimSet.setFillAfter(true);
        exitButtonDownAnimSet.setInterpolator(this, R.anim.my_anticipate_interpolator);

        exitButtonUpAnimSet = new AnimationSet(true);
        exitButtonUpAnimSet.addAnimation(new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.ABSOLUTE, exitButton.getTranslationX() + otherButtonSize * 0.5f,
                Animation.ABSOLUTE, exitButton.getTranslationY() + otherButtonSize * 0.5f));
        exitButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
        exitButtonUpAnimSet.setDuration(400);
        exitButtonUpAnimSet.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation anim){
            }

            @Override
            public void onAnimationRepeat(Animation anim){
            }

            @Override
            public void onAnimationEnd(Animation anim){
                finishAndRemoveTask();
                System.exit(0);
            }
        });
        exitButtonUpAnimSet.setInterpolator(this, R.anim.my_overshoot_interpolator);

        //* Init icons
        playIcon = findViewById(R.id.playIcon);
        playIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        playIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        playIcon.setTranslationX(startButton.getTranslationX()
                + (startButton.getLayoutParams().width
                - playIcon.getLayoutParams().width) * 0.5f);
        playIcon.setTranslationY(startButton.getTranslationY()
                + (startButton.getLayoutParams().height
                - playIcon.getLayoutParams().height) * 0.5f);

        podiumIcon = findViewById(R.id.podiumIcon);
        podiumIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        podiumIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        podiumIcon.setTranslationX(rankingsButton.getTranslationX()
                + (rankingsButton.getLayoutParams().width
                - podiumIcon.getLayoutParams().width) * 0.5f);
        podiumIcon.setTranslationY(rankingsButton.getTranslationY()
                + (rankingsButton.getLayoutParams().height
                - podiumIcon.getLayoutParams().height) * 0.5f);

        shoppingCartIcon = findViewById(R.id.shoppingCartIcon);
        shoppingCartIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        shoppingCartIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        shoppingCartIcon.setTranslationX(shopButton.getTranslationX()
                + (shopButton.getLayoutParams().width
                - shoppingCartIcon.getLayoutParams().width) * 0.5f);
        shoppingCartIcon.setTranslationY(shopButton.getTranslationY()
                + (shopButton.getLayoutParams().height
                - shoppingCartIcon.getLayoutParams().height) * 0.5f);

        gearsIcon = findViewById(R.id.gearsIcon);
        gearsIcon.getLayoutParams().width = (int)(otherButtonSize * 0.65f);
        gearsIcon.getLayoutParams().height = (int)(otherButtonSize * 0.65f);
        gearsIcon.setTranslationX(optionsButton.getTranslationX()
                + (optionsButton.getLayoutParams().width
                - gearsIcon.getLayoutParams().width) * 0.5f);
        gearsIcon.setTranslationY(optionsButton.getTranslationY()
                + (optionsButton.getLayoutParams().height
                - gearsIcon.getLayoutParams().height) * 0.5f);

        leaveIcon = findViewById(R.id.leaveIcon);
        leaveIcon.getLayoutParams().width = (int)(otherButtonSize * 0.65f);
        leaveIcon.getLayoutParams().height = (int)(otherButtonSize * 0.65f);
        leaveIcon.setTranslationX(exitButton.getTranslationX()
                + (exitButton.getLayoutParams().width
                - leaveIcon.getLayoutParams().width) * 0.5f);
        leaveIcon.setTranslationY(exitButton.getTranslationY()
                + (exitButton.getLayoutParams().height
                - leaveIcon.getLayoutParams().height) * 0.5f);
        //*/

        //* Init texts
        font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");

        gameTitleBossText = findViewById(R.id.gameTitleBossText);
        gameTitleBossText.setTypeface(font);
        gameTitleBossText.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                DeviceManager.screenWidthF * 0.2f / DeviceManager.scaledDensity);
        gameTitleBossText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        gameTitleBossText.setTranslationX(DeviceManager.screenWidthF * 0.5f
                - (float)gameTitleBossText.getMeasuredWidth() * 0.5f);
        gameTitleBossText.setTranslationY(DeviceManager.screenHeightF * 0.05f);

        gameTitleGirlText = findViewById(R.id.gameTitleGirlText);
        gameTitleGirlText.setTypeface(font);
        gameTitleGirlText.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                DeviceManager.screenWidthF * 0.18f / DeviceManager.scaledDensity);
        gameTitleGirlText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        gameTitleGirlText.setTranslationX(DeviceManager.screenWidthF * 0.5f
                - (float)gameTitleGirlText.getMeasuredWidth() * 0.5f);
        gameTitleGirlText.setTranslationY(DeviceManager.screenHeightF * 0.17f);
        //*/
    }

    private void InitFB(){
        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.getLayoutParams().width = (int)(DeviceManager.screenWidthF * 0.8f);
        loginButton.setTranslationX(DeviceManager.screenWidthF * 0.5f - loginButton.getLayoutParams().width * 0.5f);
        loginButton.setTranslationY(DeviceManager.screenHeightF * 0.3f - loginButton.getLayoutParams().height * 0.5f);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult){
            }

            @Override
            public void onCancel(){
            }

            @Override
            public void onError(FacebookException exception){
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean calibrated;
    private boolean isFingerOffScreenBefore;
    private boolean shldStartMoving;

    private AnimationSet startButtonDownAnimSet;
    private AnimationSet startButtonUpAnimSet;
    private AnimationSet rankingsButtonDownAnimSet;
    private AnimationSet rankingsButtonUpAnimSet;
    private AnimationSet shopButtonDownAnimSet;
    private AnimationSet shopButtonUpAnimSet;
    private AnimationSet optionsButtonDownAnimSet;
    private AnimationSet optionsButtonUpAnimSet;
    private AnimationSet exitButtonDownAnimSet;
    private AnimationSet exitButtonUpAnimSet;

    private Button startButton;
    private Button rankingsButton;
    private Button shopButton;
    private Button optionsButton;
    private Button exitButton;

    private static EntityBall ball;
    private EntityMenuPlayerChar menuPlayerChar;
    private EntityTextOnScreen textOnScreenUpdateFPS;
    private EntityTextOnScreen textOnScreenRenderFPS;

    private ImageView playIcon;
    private ImageView podiumIcon;
    private ImageView shoppingCartIcon;
    private ImageView gearsIcon;
    private ImageView leaveIcon;
    private ImageView myShape;

    private SensorManager sensorManager;

    private TextView gameTitleBossText;
    private TextView gameTitleGirlText;

    private Typeface font;

    private CallbackManager callbackManager;

    private Movie movie;

    private static UpdateThread updateThread;
    private static RenderThread renderThread;

    private static boolean showMyShape;

    static{
        updateThread = null;
        renderThread = null;

        showMyShape = true;
    }
}