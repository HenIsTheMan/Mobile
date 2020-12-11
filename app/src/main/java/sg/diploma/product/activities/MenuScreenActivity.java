package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.os.Bundle;
import android.content.Intent;

import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.entities.EntityPlayerChar;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.thread.UpdateThread;

import static android.os.SystemClock.elapsedRealtime;

public final class MenuScreenActivity extends Activity implements OnClickListener, IState{
    public MenuScreenActivity(){
        startButton = null;
        settingsButton = null;
        exitButton = null;

        playerChar = null;

        playIcon = null;
        gearsIcon = null;
        leaveIcon = null;
        myShape = null;

        menuSurfaceView = null;

        gameTitleBossText = null;
        gameTitleGirlText = null;

        font = null;
    }

    private UpdateThread updateThread;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_screen_layout);

        menuSurfaceView = findViewById(R.id.menuSurfaceView);
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

        InitOthers();
    }

    @Override
    public void onClick(View v){
        if(v == startButton){
            StateManager.Instance.ChangeState("GameScreen");
            startActivity(new Intent(this, GameScreenActivity.class));
            return;
        }
        if(v == settingsButton){
            //
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
    public void Render(Canvas _canvas) {
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void OnEnter(SurfaceView _view) {
        playerChar = EntityPlayerChar.Create();
    }

    @Override
    public void OnExit() {
    }

    @Override
    public void Update(float _dt) {
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
        myShape = findViewById(R.id.myShape);
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        myShape.getLayoutParams().width = (int)((float)displayMetrics.widthPixels * 1.2f);
        myShape.getLayoutParams().height = (int)((float)displayMetrics.heightPixels * 1.2f);

        Animation myShapeAnim = AnimationUtils.loadAnimation(this, R.anim.my_shape_anim);
        myShapeAnim.setStartTime((int)elapsedRealtime() + 1000);
        myShapeAnim.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation anim){
            }
            @Override
            public void onAnimationRepeat(Animation anim){
            }
            @Override
            public void onAnimationEnd(Animation anim) {
                myShape.setVisibility(View.GONE);
            }
        });
        myShape.startAnimation(myShapeAnim);

        final float buttonFactor = (float)displayMetrics.widthPixels / 4.0f / 300.0f;
        final int buttonSize = (int)(300.0f * buttonFactor);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        startButton.getLayoutParams().width = buttonSize;
        startButton.getLayoutParams().height = buttonSize;
        startButton.setTranslationX((float)displayMetrics.widthPixels * 0.2f - (float)buttonSize * 0.5f);
        startButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
        settingsButton.getLayoutParams().width = buttonSize;
        settingsButton.getLayoutParams().height = buttonSize;
        settingsButton.setTranslationX((float)displayMetrics.widthPixels * 0.5f - (float)buttonSize * 0.5f);
        settingsButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        exitButton.getLayoutParams().width = buttonSize;
        exitButton.getLayoutParams().height = buttonSize;
        exitButton.setTranslationX((float)displayMetrics.widthPixels * 0.8f - (float)buttonSize * 0.5f);
        exitButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

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
        gearsIcon.setTranslationX(settingsButton.getTranslationX()
                + (settingsButton.getLayoutParams().width
                - gearsIcon.getLayoutParams().width) * 0.5f);
        gearsIcon.setTranslationY(settingsButton.getTranslationY()
                + (settingsButton.getLayoutParams().height
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
                (float)displayMetrics.widthPixels * 0.2f
                        / displayMetrics.scaledDensity);
        gameTitleBossText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        gameTitleBossText.setTranslationX((float)displayMetrics.widthPixels * 0.5f
                - (float)gameTitleBossText.getMeasuredWidth() * 0.5f);
        gameTitleBossText.setTranslationY((float)displayMetrics.heightPixels * 0.07f);

        gameTitleGirlText = findViewById(R.id.gameTitleGirlText);
        gameTitleGirlText.setTypeface(font);
        gameTitleGirlText.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                (float)displayMetrics.widthPixels * 0.18f
                        / displayMetrics.scaledDensity);
        gameTitleGirlText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        gameTitleGirlText.setTranslationX((float)displayMetrics.widthPixels * 0.5f
                - (float)gameTitleGirlText.getMeasuredWidth() * 0.5f);
        gameTitleGirlText.setTranslationY((float)displayMetrics.heightPixels * 0.2f);
    }

    private Button startButton;
    private Button settingsButton;
    private Button exitButton;

    private EntityPlayerChar playerChar;

    private ImageView playIcon;
    private ImageView gearsIcon;
    private ImageView leaveIcon;
    private ImageView myShape;

    private SurfaceView menuSurfaceView;

    private TextView gameTitleBossText;
    private TextView gameTitleGirlText;

    private Typeface font;
}