package sg.diploma.product.activities;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Build;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.os.Bundle;
import android.content.Intent;

import android.graphics.Typeface;
import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;

public final class MenuScreenActivity extends Activity implements OnClickListener, IState{
    private Button btn_start;
    private Button btn_exit;
    private Typeface font;

    public MenuScreenActivity(){
        btn_start = null;
        btn_exit = null;
        font = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_screen_layout);

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        btn_exit = findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);

        font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");
        btn_start.setTypeface(font);
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent();

        if(v == btn_start){
            intent.setClass(this, GameScreenActivity.class);
            StateManager.Instance.ChangeState(("GameScreen"));
            startActivity(intent);
        } else if(v == btn_exit){
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
    //*/
}