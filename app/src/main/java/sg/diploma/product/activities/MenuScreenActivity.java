package sg.diploma.product.activities;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.DisplayMetrics;
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

import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;

import static android.os.SystemClock.elapsedRealtime;

public final class MenuScreenActivity extends Activity implements OnClickListener, IState{
    public MenuScreenActivity(){
        startButton = null;
        settingsButton = null;
        exitButton = null;
        myShape = null;
        font = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StateManager.Instance.AddState(new MenuScreenActivity());
        StateManager.Instance.AddState(new GameScreenActivity());

        setContentView(R.layout.menu_screen_layout);

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

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        startButton.getLayoutParams().width = (int)(300.0f * buttonFactor);
        startButton.getLayoutParams().height = (int)(300.0f * buttonFactor);
        startButton.setTranslationX((float)displayMetrics.widthPixels * 0.2f - (float)startButton.getLayoutParams().width * 0.5f);
        startButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
        settingsButton.getLayoutParams().width = (int)(300.0f * buttonFactor);
        settingsButton.getLayoutParams().height = (int)(300.0f * buttonFactor);
        settingsButton.setTranslationX((float)displayMetrics.widthPixels * 0.5f - (float)settingsButton.getLayoutParams().width * 0.5f);
        settingsButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        exitButton.getLayoutParams().width = (int)(300.0f * buttonFactor);
        exitButton.getLayoutParams().height = (int)(300.0f * buttonFactor);
        exitButton.setTranslationX((float)displayMetrics.widthPixels * 0.8f - (float)exitButton.getLayoutParams().width * 0.5f);
        exitButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

        font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");
        //btn_start.setTypeface(font);
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
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void Render(Canvas _canvas) {
        // 3) Render () --> E.g: Entitymanager, Instance, Canvas
    }

    @Override
    public void OnEnter(SurfaceView _view) {
        // 2) OnEnter() --> Using the surfaceview e.g: Renderbackground
    }

    @Override
    public void OnExit() {
    }

    @Override
    public void Update(float _dt) {
        // 4) Update () --> FPS, timer.. dt
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

    private Button startButton;
    private Button settingsButton;
    private Button exitButton;
    private ImageView myShape;
    private Typeface font;

    //*/
}