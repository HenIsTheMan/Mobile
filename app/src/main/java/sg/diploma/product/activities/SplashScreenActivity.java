package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import sg.diploma.product.R;
import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchTypes;

public final class SplashScreenActivity extends Activity{
    public SplashScreenActivity(){
        isRunning = true;
        splashThread = null;

        androidLogo0 = null;
        androidLogo1 = null;
        androidLogo2 = null;
        androidLogo3 = null;
        androidStudioLogo = null;
        androidStudioText = null;
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event){
        if(event.getAction() == TouchTypes.TouchType.Down.GetVal()){
            isRunning = false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        StateManager.Instance.AddState(new MenuScreenActivity());
        StateManager.Instance.AddState(new GameScreenActivity());
        StateManager.Instance.AddState(new OptionsScreenActivity());
        StateManager.Instance.AddState(new ShopScreenActivity());
        StateManager.Instance.AddState(new RankingsScreenActivity());
        StateManager.Instance.AddState(new GameOverScreenActivity());

        AnimationSet androidLogoAnimSet0 = new AnimationSet(true);
        androidLogoAnimSet0.addAnimation(AnimationUtils.loadAnimation(this, R.anim.android_logo_anim));
        androidLogoAnimSet0.addAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_with_top_left_pivot));
        androidLogoAnimSet0.setFillEnabled(true);
        androidLogoAnimSet0.setFillAfter(true);

        AnimationSet androidLogoAnimSet1 = new AnimationSet(true);
        androidLogoAnimSet1.addAnimation(AnimationUtils.loadAnimation(this, R.anim.android_logo_anim));
        androidLogoAnimSet1.addAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_with_top_right_pivot));
        androidLogoAnimSet1.setFillEnabled(true);
        androidLogoAnimSet1.setFillAfter(true);

        AnimationSet androidLogoAnimSet2 = new AnimationSet(true);
        androidLogoAnimSet2.addAnimation(AnimationUtils.loadAnimation(this, R.anim.android_logo_anim));
        androidLogoAnimSet2.addAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_with_bottom_left_pivot));
        androidLogoAnimSet2.setFillEnabled(true);
        androidLogoAnimSet2.setFillAfter(true);

        AnimationSet androidLogoAnimSet3 = new AnimationSet(true);
        androidLogoAnimSet3.addAnimation(AnimationUtils.loadAnimation(this, R.anim.android_logo_anim));
        androidLogoAnimSet3.addAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_with_bottom_right_pivot));
        androidLogoAnimSet3.setFillEnabled(true);
        androidLogoAnimSet3.setFillAfter(true);

        final float factor0 = DeviceManager.screenWidthF * 0.25f / 254.0f;

        androidLogo0 = findViewById(R.id.androidLogo0);
        androidLogo0.startAnimation(androidLogoAnimSet0);
        androidLogo0.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo0.getLayoutParams().height = (int)(284.0f * factor0);

        androidLogo1 = findViewById(R.id.androidLogo1);
        androidLogoAnimSet1.setStartOffset(200);
        androidLogo1.startAnimation(androidLogoAnimSet1);
        androidLogo1.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo1.getLayoutParams().height = (int)(284.0f * factor0);

        androidLogo2 = findViewById(R.id.androidLogo2);
        androidLogoAnimSet2.setStartOffset(400);
        androidLogo2.startAnimation(androidLogoAnimSet2);
        androidLogo2.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo2.getLayoutParams().height = (int)(284.0f * factor0);

        androidLogo3 = findViewById(R.id.androidLogo3);
        androidLogoAnimSet3.setStartOffset(600);
        androidLogo3.startAnimation(androidLogoAnimSet3);
        androidLogo3.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo3.getLayoutParams().height = (int)(284.0f * factor0);

        final float factor1 = DeviceManager.screenWidthF / 513.0f;
        androidStudioLogo = findViewById(R.id.androidStudioLogo);
        androidStudioLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.android_studio_logo_anim));
        androidStudioLogo.getLayoutParams().width = (int)(513.0f * factor1);
        androidStudioLogo.getLayoutParams().height = (int)(512.0f * factor1);

        final float factor2 = DeviceManager.screenWidthF / 850.0f;
        androidStudioText = findViewById(R.id.androidStudioText);
        androidStudioText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.android_studio_text_anim));
        androidStudioText.getLayoutParams().width = (int)(850.0f * factor2);
        androidStudioText.getLayoutParams().height = (int)(90.0f * factor2);

        splashThread = new Thread(){
            @Override
            public void run(){
                long prevTime = System.nanoTime();
                float elapsedTime = 0.0f;
                
                while(isRunning){
                    final long currTime = System.nanoTime();

                    final float dt = ((currTime - prevTime) / 1000000000.0f);
                    elapsedTime += dt;
                    if(elapsedTime >= 7.0f){
                        break;
                    }

                    prevTime = currTime;
                }

                finishAffinity();
                StateManager.Instance.ChangeState("MenuScreen");
                startActivity(new Intent(SplashScreenActivity.this, MenuScreenActivity.class));
            }
        };

        splashThread.setName("splashThread");
        splashThread.start();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
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
        try{
            splashThread.join();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private volatile boolean isRunning;
    private Thread splashThread;

    private ImageView androidLogo0;
    private ImageView androidLogo1;
    private ImageView androidLogo2;
    private ImageView androidLogo3;
    private ImageView androidStudioLogo;
    private ImageView androidStudioText;
}