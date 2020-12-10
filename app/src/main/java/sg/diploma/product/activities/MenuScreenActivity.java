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
        font = null;
        myShape = null;
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
        final float newWidth = (float)displayMetrics.widthPixels * 1.2f;
        final float newHeight = (float)displayMetrics.heightPixels * 1.2f;
        myShape.getLayoutParams().width = (int)newWidth;
        myShape.getLayoutParams().height = (int)newHeight;

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

/*        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        btn_exit = findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);

        font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");
        btn_start.setTypeface(font);

        androidLogo3 = findViewById(R.id.androidLogo3);
        androidLogoAnim3.setStartOffset(600);
        androidLogo3.startAnimation(androidLogoAnim3);
        androidLogo3.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo3.getLayoutParams().height = (int)(284.0f * factor0);*/
    }

    @Override
    public void onClick(View v){
        /*if(v == btn_start){
            StateManager.Instance.ChangeState("GameScreen");
            startActivity(new Intent(this, GameScreenActivity.class));
        } else if(v == btn_exit){
            finishAndRemoveTask();
            System.exit(0);
        }*/
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
    private Typeface font;
    private ImageView myShape;

    //*/
}