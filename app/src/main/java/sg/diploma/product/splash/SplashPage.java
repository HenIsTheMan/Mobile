package sg.diploma.product.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import android.widget.ImageView;
import android.view.animation.AnimationUtils;

import sg.diploma.product.MainMenu;
import sg.diploma.product.R;
import sg.diploma.product.touch.TouchTypes;

public final class SplashPage extends Activity {
    public SplashPage(){
        _active = true;
        _splashTime = 5000;

        androidLogo0 = null;
        androidLogo1 = null;
        androidLogo2 = null;
        androidLogo3 = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen_layout);
        // Never import R, not the right way to solve error
        // Errors like: Typo, image not found, place in the wrong place, syntax

        androidLogo0 = findViewById(R.id.androidLogo0);
        androidLogo0.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        androidLogo1 = findViewById(R.id.androidLogo1);
        androidLogo1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        androidLogo2 = findViewById(R.id.androidLogo2);
        androidLogo2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        androidLogo3 = findViewById(R.id.androidLogo3);
        androidLogo3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(200);
                        if(_active) {
                            waited += 200;
                        }
                    }
                } catch(InterruptedException e) {
                    //Do nth
                } finally {
                    finish();

                    ///Create new activity based on and intent with CurrentActivity
                    Intent intent = new Intent(SplashPage.this, MainMenu.class);
                    startActivity(intent);
                }
            }
        };
        splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == TouchTypes.TouchType.Down.GetVal()){
            _active = false;
        }
        return true;
    }

    private boolean _active;
    private int _splashTime;

    private ImageView androidLogo0;
    private ImageView androidLogo1;
    private ImageView androidLogo2;
    private ImageView androidLogo3;
}
