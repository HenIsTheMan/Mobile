package com.example.projectsetup;

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

// Now Mainmenu is also a state
public class MainMenu extends Activity implements OnClickListener, StateBase {

    // Define button
    private Button btn_start;
    private Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Hide Top Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main_menu);
        // Never import R, not the right way to solve error
        // Errors like: Typo, image not found, place in the wrong place, syntax

        // Define button as an object
        // Which name: R = resource registry, look for id - btn_start
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        btn_exit = (Button)findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);

        StateManager.Instance.AddState(new MainMenu());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_start) // View v = click on the btn_start
        {
            // intent --> to set to another class which another page or screen that we are launching.
            intent.setClass(this, LevelSelection.class);
            //StateManager.Instance.ChangeState(("Default")); // Default is like a loading page

            // Transit from Main Menu to Splash Page
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
        return "MainMenu"; // Statename
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