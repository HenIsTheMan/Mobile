package sg.diploma.product;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.content.Intent;

// Now LevelSelection is also a state
public class LevelSelection extends Activity implements OnClickListener, IState{

    // Define button
    private Button btn_back;
    private Button btn_easy;
    private Button btn_medium;
    private Button btn_hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.level_selection);
        // Never import R, not the right way to solve error
        // Errors like: Typo, image not found, place in the wrong place, syntax

        // Define button as an object
        // Which name: R = resource registry, look for id - btn_start
        btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_easy = (Button)findViewById(R.id.btn_easy);
        btn_easy.setOnClickListener(this);
        btn_medium = (Button)findViewById(R.id.btn_medium);
        btn_medium.setOnClickListener(this);
        btn_hard = (Button)findViewById(R.id.btn_hard);
        btn_hard.setOnClickListener(this);

        StateManager.Instance.AddState(new LevelSelection());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_back) // View v = click on the btn_start
        {
            // intent --> to set to another class which another page or screen that we are launching.
            intent.setClass(this, MainMenu.class);
            //StateManager.Instance.ChangeState(("Default")); // Default is like a loading page
        } else if(v == btn_easy){
            intent.setClass(this, GamePage.class);
        } else if(v == btn_medium){
            intent.setClass(this, GamePage.class);
        } else if(v == btn_hard){
            intent.setClass(this, GamePage.class);
        }
        // Transit from Main Menu to Splash Page
        startActivity(intent);
    }

    // Below are the method in a statebase interface
    @Override
    public void Render(Canvas _canvas) {
    }

    @Override
    public void OnEnter(SurfaceView _view) {
    }

    @Override
    public void OnExit() {
    }

    @Override
    public void Update(float _dt) {
    }

    @Override
    public String GetName() {
        return "LevelSelection"; // Statename
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
}