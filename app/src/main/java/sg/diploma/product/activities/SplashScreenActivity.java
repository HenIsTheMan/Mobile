package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;

import sg.diploma.product.R;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchTypes;

public final class SplashScreenActivity extends Activity{
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
        moveTaskToBack(true);
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
        AnimationSet androidLogoAnimSet0 = new AnimationSet(true);
        androidLogoAnimSet0.addAnimation(AnimationUtils.loadAnimation(this, R.anim.android_logo_anim));
        androidLogoAnimSet0.addAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_with_top_left_pivot));
        AnimationSet androidLogoAnimSet1 = new AnimationSet(true);
        androidLogoAnimSet1.addAnimation(AnimationUtils.loadAnimation(this, R.anim.android_logo_anim));
        androidLogoAnimSet1.addAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_with_top_right_pivot));
        AnimationSet androidLogoAnimSet2 = new AnimationSet(true);
        androidLogoAnimSet2.addAnimation(AnimationUtils.loadAnimation(this, R.anim.android_logo_anim));
        androidLogoAnimSet2.addAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_with_bottom_left_pivot));
        AnimationSet androidLogoAnimSet3 = new AnimationSet(true);
        androidLogoAnimSet3.addAnimation(AnimationUtils.loadAnimation(this, R.anim.android_logo_anim));
        androidLogoAnimSet3.addAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_with_bottom_right_pivot));

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

        Thread splashThread = new Thread(){
            @Override
            public void run(){
            try{
                sleep(_splashTime);
            } catch(InterruptedException e){
                e.printStackTrace();
            } finally{
                finishAffinity();

                startActivity(new Intent(SplashScreenActivity.this, MenuScreenActivity.class));
            }
            }
        };

        Thread splashControlThread = new Thread(){
            @Override
            public void run(){
            for(;;){
                if(!_active){
                    splashThread.interrupt();
                    break;
                }
            }
            }
        };

        splashThread.start();
        splashControlThread.start();
    }

    private boolean _active;
    private final int _splashTime;

    private ImageView androidLogo0;
    private ImageView androidLogo1;
    private ImageView androidLogo2;
    private ImageView androidLogo3;
    private ImageView androidStudioLogo;
    private ImageView androidStudioText;
}