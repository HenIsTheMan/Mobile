package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.audio.AudioTypes;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.device.UpdateThread;
import sg.diploma.product.entity.EntityConstraint;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.entities.EntityBall;
import sg.diploma.product.entity.entities.EntityMenuPlayerChar;
import sg.diploma.product.entity.entities.EntityTextOnScreen;
import sg.diploma.product.graphics.Color;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

public final class MenuScreenActivity extends Activity implements OnClickListener, IState, SensorEventListener{
    public MenuScreenActivity(){
        isFingerOffScreenBefore = true;
        shldStartMoving = false;

        startButton = null;
        optionsButton = null;
        exitButton = null;

        ball = null;
        menuPlayerChar = null;
        textOnScreen = null;

        playIcon = null;
        gearsIcon = null;
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

        SurfaceView menuSurfaceView = findViewById(R.id.menuSurfaceView);
        updateThread = new UpdateThread(menuSurfaceView);
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
        sensorManager.registerListener(this, sensorManager.getSensorList(TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);

        AudioManager.Instance.LoadAudioVolData();
        AudioManager.Instance.PlayAudio(R.raw.theme, AudioTypes.AudioType.Music);
        InitOthers();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    @Override
    public void onSensorChanged(SensorEvent SenseEvent){
        if(ball != null){
            Log.e("Me", "Here");
            ball.SetVals(SenseEvent.values);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
        return true;
    }

    @Override
    public void onClick(View v){
        AudioManager.Instance.PlayAudio(R.raw.button_press, AudioTypes.AudioType.Sound);
        if(v == startButton){
            EntityManager.Instance.SendAllEntitiesForRemoval();
            StateManager.Instance.ChangeState("GameScreen");

            startActivity(new Intent(this, GameScreenActivity.class));
            return;
        }
        if(v == optionsButton){
            EntityManager.Instance.SendAllEntitiesForRemoval();
            AudioManager.Instance.SaveAudioVolData();
            StateManager.Instance.ChangeState("OptionsScreen");

            startActivity(new Intent(this, OptionsScreenActivity.class));
            return;
        }
        if(v == exitButton){
            finishAndRemoveTask();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed(){
        finishAndRemoveTask();
        System.exit(0);
    }

    @Override
    public void Render(Canvas _canvas){
        _canvas.drawColor(0xFF333333);
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
        ball.attribs.scale.x = ball.attribs.scale.y = 400.0f;
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

        menuPlayerChar.attribs.yMin = new EntityConstraint();
        menuPlayerChar.attribs.yMin.val = (DeviceManager.screenHeightF * 0.35f + DeviceManager.screenWidthF * 0.25f) * 1.3f;
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
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

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
        startButton.setOnClickListener(this);
        startButton.getLayoutParams().width = buttonSize;
        startButton.getLayoutParams().height = buttonSize;
        startButton.setTranslationX(DeviceManager.screenWidthF * 0.2f - (float)buttonSize * 0.5f);
        startButton.setTranslationY(DeviceManager.screenHeightF * 0.4f);

        optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(this);
        optionsButton.getLayoutParams().width = buttonSize;
        optionsButton.getLayoutParams().height = buttonSize;
        optionsButton.setTranslationX(DeviceManager.screenWidthF * 0.5f - (float)buttonSize * 0.5f);
        optionsButton.setTranslationY(DeviceManager.screenHeightF * 0.4f);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        exitButton.getLayoutParams().width = buttonSize;
        exitButton.getLayoutParams().height = buttonSize;
        exitButton.setTranslationX(DeviceManager.screenWidthF * 0.8f - (float)buttonSize * 0.5f);
        exitButton.setTranslationY(DeviceManager.screenHeightF * 0.4f);

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

        leaveIcon = findViewById(R.id.leaveIcon);
        leaveIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        leaveIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
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

    private Button startButton;
    private Button optionsButton;
    private Button exitButton;

    private static EntityBall ball;
    private EntityMenuPlayerChar menuPlayerChar;
    private EntityTextOnScreen textOnScreen;

    private ImageView playIcon;
    private ImageView gearsIcon;
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