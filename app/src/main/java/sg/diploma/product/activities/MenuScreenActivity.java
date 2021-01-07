package sg.diploma.product.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
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

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.device.DeviceManager;
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
        isFingerOffScreenBefore = true;
        shldStartMoving = false;

        startButtonDownAnimSet = null;
        startButtonUpAnimSet = null;
        optionsButtonDownAnimSet = null;
        optionsButtonUpAnimSet = null;
        shopButtonDownAnimSet = null;
        shopButtonUpAnimSet = null;
        exitButtonDownAnimSet = null;
        exitButtonUpAnimSet = null;

        startButton = null;
        optionsButton = null;
        shopButton = null;
        exitButton = null;

        ball = null;
        menuPlayerChar = null;
        textOnScreen = null;

        playIcon = null;
        gearsIcon = null;
        shoppingCartIcon = null;
        leaveIcon = null;
        myShape = null;

        updateThread = null;

        sensorManager = null;

        gameTitleBossText = null;
        gameTitleGirlText = null;

        font = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_screen_layout);

        Publisher.AddListener(ListenerFlagsWrapper.ListenerFlags.MenuScreenActivity.GetVal(), this);

        SurfaceView menuSurfaceView = findViewById(R.id.menuSurfaceView);
        updateThread = new UpdateThread(menuSurfaceView, R.raw.space, 8);
        updateThread.SetDelay(10);
        SurfaceHolder surfaceHolder = menuSurfaceView.getHolder();

        if(surfaceHolder != null){
            surfaceHolder.addCallback(new SurfaceHolder.Callback(){
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder){
                    if(!updateThread.GetIsRunning()){
                        updateThread.Init();
                    }

                    if(!updateThread.isAlive()){
                        updateThread.start();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder){
                    updateThread.Terminate();
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
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Publisher.RemoveListener(ListenerFlagsWrapper.ListenerFlags.MenuScreenActivity.GetVal());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    @Override
    public void onSensorChanged(SensorEvent SenseEvent){
        if(ball != null){
            ball.SetVals(SenseEvent.values);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        if(view == startButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startButton.startAnimation(startButtonDownAnimSet);
                    return true;
                case MotionEvent.ACTION_UP:
                    startButton.startAnimation(startButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    EntityManager.Instance.SendAllEntitiesForRemoval();
                    StateManager.Instance.ChangeState("GameScreen");

                    startActivity(new Intent(this, GameScreenActivity.class));
                    //finish();

                    return true;
            }
            return false;
        }
        if(view == optionsButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    optionsButton.startAnimation(optionsButtonDownAnimSet);
                    return true;
                case MotionEvent.ACTION_UP:
                    optionsButton.startAnimation(optionsButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    EntityManager.Instance.SendAllEntitiesForRemoval();
                    AudioManager.Instance.SaveAudioVolData();
                    StateManager.Instance.ChangeState("OptionsScreen");

                    startActivity(new Intent(this, OptionsScreenActivity.class));
                    finish();

                    return true;
            }
            return false;
        }
        if(view == shopButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    shopButton.startAnimation(shopButtonDownAnimSet);
                    return true;
                case MotionEvent.ACTION_UP:
                    shopButton.startAnimation(shopButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    EntityManager.Instance.SendAllEntitiesForRemoval();
                    StateManager.Instance.ChangeState("ShopScreen");

                    startActivity(new Intent(this, ShopScreenActivity.class));
                    finish();

                    return true;
            }
            return false;
        }
        if(view == exitButton){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    exitButton.startAnimation(exitButtonDownAnimSet);
                    return true;
                case MotionEvent.ACTION_UP:
                    exitButton.startAnimation(exitButtonUpAnimSet);
                    AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);

                    finishAndRemoveTask();
                    System.exit(0);

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
    public void OnEnter(SurfaceView _view){
        //* Create ball
        ball = EntityBall.Create(
            "ball"
        );

        ball.attribs.pos.x = DeviceManager.screenWidthF * 0.5f;
        ball.attribs.pos.y = DeviceManager.screenHeightF * 0.9f;
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

        //* Create text on screen
        textOnScreen = EntityTextOnScreen.Create("menuTextOnScreen", _view.getContext().getAssets(), "fonts/grobold.ttf");
        textOnScreen.attribs.pos.x = 30.0f * 0.5f;
        textOnScreen.attribs.pos.y = DeviceManager.screenHeightF - 30.0f;
        textOnScreen.SetColor(Color.green);
        textOnScreen.SetStrokeWidth(100.0f);
        textOnScreen.SetTextSize(30.0f);
        //*/
    }

    @Override
    public void OnExit(){
    }

    @Override
    public void Update(float _dt){
        if(textOnScreen != null){
            textOnScreen.SetText("FPS: " + 1.0f / _dt);
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
    public String GetName(){
        return "MenuScreen";
    }

    @Override
    public void OnEvent(EventAbstract event){
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

        optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnTouchListener(this);
        optionsButton.getLayoutParams().width = buttonSize;
        optionsButton.getLayoutParams().height = buttonSize;
        optionsButton.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)buttonSize * 0.5f);
        optionsButton.setTranslationY(DeviceManager.screenHeightF * 0.45f - (float)buttonSize * 0.5f);

        optionsButtonDownAnimSet = new AnimationSet(true);
        optionsButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.ABSOLUTE, optionsButton.getTranslationX() + buttonSize * 0.5f,
                Animation.ABSOLUTE, optionsButton.getTranslationY() + buttonSize * 0.5f));
        optionsButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        optionsButtonDownAnimSet.setDuration(400);
        optionsButtonDownAnimSet.setFillEnabled(true);
        optionsButtonDownAnimSet.setFillAfter(true);
        optionsButtonDownAnimSet.setInterpolator(this, R.anim.my_anticipate_interpolator);

        optionsButtonUpAnimSet = new AnimationSet(true);
        optionsButtonUpAnimSet.addAnimation(new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.ABSOLUTE, optionsButton.getTranslationX() + buttonSize * 0.5f,
                Animation.ABSOLUTE, optionsButton.getTranslationY() + buttonSize * 0.5f));
        optionsButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
        optionsButtonUpAnimSet.setDuration(400);
        optionsButtonUpAnimSet.setFillEnabled(true);
        optionsButtonUpAnimSet.setFillAfter(true);
        optionsButtonUpAnimSet.setInterpolator(this, R.anim.my_overshoot_interpolator);

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

        final float exitButtonSize = (float)buttonSize * 0.7f;
        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnTouchListener(this);
        exitButton.getLayoutParams().width = exitButton.getLayoutParams().height = (int)exitButtonSize;
        final float exitButttonTranslateX = DeviceManager.screenWidthF * 0.85f - exitButtonSize * 0.5f;
        exitButton.setTranslationX(exitButttonTranslateX);
        exitButton.setTranslationY(DeviceManager.screenHeightF - (DeviceManager.screenWidthF - exitButttonTranslateX));

        exitButtonDownAnimSet = new AnimationSet(true);
        exitButtonDownAnimSet.addAnimation(new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.ABSOLUTE, exitButton.getTranslationX() + exitButtonSize * 0.5f,
                Animation.ABSOLUTE, exitButton.getTranslationY() + exitButtonSize * 0.5f));
        exitButtonDownAnimSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        exitButtonDownAnimSet.setDuration(400);
        exitButtonDownAnimSet.setFillEnabled(true);
        exitButtonDownAnimSet.setFillAfter(true);
        exitButtonDownAnimSet.setInterpolator(this, R.anim.my_anticipate_interpolator);

        exitButtonUpAnimSet = new AnimationSet(true);
        exitButtonUpAnimSet.addAnimation(new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f,
                Animation.ABSOLUTE, exitButton.getTranslationX() + exitButtonSize * 0.5f,
                Animation.ABSOLUTE, exitButton.getTranslationY() + exitButtonSize * 0.5f));
        exitButtonUpAnimSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));
        exitButtonUpAnimSet.setDuration(400);
        exitButtonUpAnimSet.setFillEnabled(true);
        exitButtonUpAnimSet.setFillAfter(true);
        exitButtonUpAnimSet.setInterpolator(this, R.anim.my_overshoot_interpolator);

        playIcon = findViewById(R.id.playIcon);
        playIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        playIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        playIcon.setTranslationX(startButton.getTranslationX()
                + (startButton.getLayoutParams().width
                - playIcon.getLayoutParams().width) * 0.5f);
        playIcon.setTranslationY(startButton.getTranslationY()
                + (startButton.getLayoutParams().height
                - playIcon.getLayoutParams().height) * 0.5f);

        gearsIcon = findViewById(R.id.gearsIcon);
        gearsIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        gearsIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        gearsIcon.setTranslationX(optionsButton.getTranslationX()
                + (optionsButton.getLayoutParams().width
                - gearsIcon.getLayoutParams().width) * 0.5f);
        gearsIcon.setTranslationY(optionsButton.getTranslationY()
                + (optionsButton.getLayoutParams().height
                - gearsIcon.getLayoutParams().height) * 0.5f);

        shoppingCartIcon = findViewById(R.id.shoppingCartIcon);
        shoppingCartIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        shoppingCartIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        shoppingCartIcon.setTranslationX(shopButton.getTranslationX()
                + (shopButton.getLayoutParams().width
                - shoppingCartIcon.getLayoutParams().width) * 0.5f);
        shoppingCartIcon.setTranslationY(shopButton.getTranslationY()
                + (shopButton.getLayoutParams().height
                - shoppingCartIcon.getLayoutParams().height) * 0.5f);

        leaveIcon = findViewById(R.id.leaveIcon);
        leaveIcon.getLayoutParams().width = (int)(exitButtonSize * 0.65f);
        leaveIcon.getLayoutParams().height = (int)(exitButtonSize * 0.65f);
        leaveIcon.setTranslationX(exitButton.getTranslationX()
                + (exitButton.getLayoutParams().width
                - leaveIcon.getLayoutParams().width) * 0.5f);
        leaveIcon.setTranslationY(exitButton.getTranslationY()
                + (exitButton.getLayoutParams().height
                - leaveIcon.getLayoutParams().height) * 0.5f);

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
    }

    private boolean isFingerOffScreenBefore;
    private boolean shldStartMoving;

    private AnimationSet startButtonDownAnimSet;
    private AnimationSet startButtonUpAnimSet;
    private AnimationSet optionsButtonDownAnimSet;
    private AnimationSet optionsButtonUpAnimSet;
    private AnimationSet shopButtonDownAnimSet;
    private AnimationSet shopButtonUpAnimSet;
    private AnimationSet exitButtonDownAnimSet;
    private AnimationSet exitButtonUpAnimSet;

    private Button startButton;
    private Button optionsButton;
    private Button shopButton;
    private Button exitButton;

    private static EntityBall ball;
    private EntityMenuPlayerChar menuPlayerChar;
    private EntityTextOnScreen textOnScreen;

    private ImageView playIcon;
    private ImageView gearsIcon;
    private ImageView shoppingCartIcon;
    private ImageView leaveIcon;
    private ImageView myShape;

    private UpdateThread updateThread;

    private SensorManager sensorManager;

    private TextView gameTitleBossText;
    private TextView gameTitleGirlText;

    private Typeface font;

    private static boolean showMyShape;

    static{
        showMyShape = true;
    }
}