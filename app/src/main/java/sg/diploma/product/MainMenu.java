package sg.diploma.product;

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

import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;

public class MainMenu extends Activity implements OnClickListener, IState{
    private Button btn_start;
    private Button btn_exit;

    private Typeface font;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");

        setContentView(R.layout.menu_screen_layout);
        // Never import R, not the right way to solve error
        // Errors like: Typo, image not found, place in the wrong place, syntax

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        btn_exit = findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);

        btn_start.setTypeface(font);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        if(v == btn_start){
            intent.setClass(this, GamePage.class);
            StateManager.Instance.ChangeState(("MainGame"));

            startActivity(intent);
        }
        else if(v == btn_exit)
        {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
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
    public String GetName() {
        return "MainMenu";
    }

    // Part of the activity life cycle
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //*/
}