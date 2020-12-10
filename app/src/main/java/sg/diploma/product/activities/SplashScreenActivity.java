package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import android.view.SurfaceView;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;

import sg.diploma.product.R;
import sg.diploma.product.state.IState;
import sg.diploma.product.touch.TouchTypes;

public final class SplashScreenActivity extends Activity implements IState{
    public SplashScreenActivity(){
        _active = true;
        _splashTime = 8000;

        androidLogo0 = null;
        androidLogo1 = null;
        androidLogo2 = null;
        androidLogo3 = null;
        androidStudioLogo = null;
        androidStudioText = null;
    }

    @Override
    public void onBackPressed(){
        finishAndRemoveTask();
        System.exit(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == TouchTypes.TouchType.Down.GetVal()){
            _active = false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.splash_screen_layout);

        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        final float factor0 = (float)displayMetrics.widthPixels / 4.0f / 254.0f;
        Animation androidLogoAnim0 = AnimationUtils.loadAnimation(this, R.anim.android_logo_anim);
        Animation androidLogoAnim1 = AnimationUtils.loadAnimation(this, R.anim.android_logo_anim);
        Animation androidLogoAnim2 = AnimationUtils.loadAnimation(this, R.anim.android_logo_anim);
        Animation androidLogoAnim3 = AnimationUtils.loadAnimation(this, R.anim.android_logo_anim);

        androidLogo0 = findViewById(R.id.androidLogo0);
        androidLogo0.startAnimation(androidLogoAnim0);
        androidLogo0.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo0.getLayoutParams().height = (int)(284.0f * factor0);

        androidLogo1 = findViewById(R.id.androidLogo1);
        androidLogoAnim1.setStartOffset(200);
        androidLogo1.startAnimation(androidLogoAnim1);
        androidLogo1.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo1.getLayoutParams().height = (int)(284.0f * factor0);

        androidLogo2 = findViewById(R.id.androidLogo2);
        androidLogoAnim2.setStartOffset(400);
        androidLogo2.startAnimation(androidLogoAnim2);
        androidLogo2.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo2.getLayoutParams().height = (int)(284.0f * factor0);

        androidLogo3 = findViewById(R.id.androidLogo3);
        androidLogoAnim3.setStartOffset(600);
        androidLogo3.startAnimation(androidLogoAnim3);
        androidLogo3.getLayoutParams().width = (int)(254.0f * factor0);
        androidLogo3.getLayoutParams().height = (int)(284.0f * factor0);

        final float factor1 = (float)displayMetrics.widthPixels / 513.0f;
        androidStudioLogo = findViewById(R.id.androidStudioLogo);
        androidStudioLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.android_studio_logo_anim));
        androidStudioLogo.getLayoutParams().width = (int)(513.0f * factor1);
        androidStudioLogo.getLayoutParams().height = (int)(512.0f * factor1);

        final float factor2 = (float)displayMetrics.widthPixels / 850.0f;
        androidStudioText = findViewById(R.id.androidStudioText);
        androidStudioText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.android_studio_text_anim));
        androidStudioText.getLayoutParams().width = (int)(850.0f * factor2);
        androidStudioText.getLayoutParams().height = (int)(90.0f * factor2);

        Thread splashTread = new Thread(){
            @Override
            public void run(){
                try{
                    int waited = 0;
                    while(_active && (waited < _splashTime)){
                        sleep(200);
                        if(_active){
                            waited += 200;
                        }
                    }
                } catch(InterruptedException e){
                    //Do nth
                } finally{
                    finishAffinity();

                    ///Create new activity based on and intent with CurrentActivity
                    Intent intent = new Intent(SplashScreenActivity.this, MenuScreenActivity.class);
                    startActivity(intent);
                }
            }
        };
        splashTread.start();
    }

    @Override
    public void Render(Canvas _canvas){
        //Do nth
    }

    @Override
    public void OnEnter(SurfaceView _view){
        //Do nth
    }

    @Override
    public void OnExit(){
        //Do nth
    }

    @Override
    public void Update(float _dt) {
        //Do nth
    }

    @Override
    public String GetName(){
        return "SplashScreen";
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

    private boolean _active;
    private int _splashTime;

    private ImageView androidLogo0;
    private ImageView androidLogo1;
    private ImageView androidLogo2;
    private ImageView androidLogo3;
    private ImageView androidStudioLogo;
    private ImageView androidStudioText;
}