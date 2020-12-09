package sg.diploma.product.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import sg.diploma.product.MainMenu;
import sg.diploma.product.R;
import sg.diploma.product.touch.TouchTypes;

public final class SplashPage extends Activity {
    public SplashPage(){
        _active = true;
        _splashTime = 5000;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_page);
        // Never import R, not the right way to solve error
        // Errors like: Typo, image not found, place in the wrong place, syntax

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
}
