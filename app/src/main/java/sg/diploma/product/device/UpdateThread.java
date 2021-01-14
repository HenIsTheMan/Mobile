package sg.diploma.product.device;

import android.graphics.Movie;
import android.view.SurfaceView;

import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.currency.CurrencyManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.load_and_save.SharedPrefsManager;
import sg.diploma.product.state.StateManager;

public final class UpdateThread extends Thread{ //Need dedicated thread to run Surfaceview's update method
    public UpdateThread(final SurfaceView view){
        isRunning = false;
        limitFPS = false;
        targetFPS = 60;

        useGifBG = false;

        if(!managersAreInitialized){
            InitManagers(view);
            managersAreInitialized = true;
        }
    }

    public UpdateThread(final SurfaceView view, final Movie movie, final long timeAddPerFrame){
        isRunning = false;
        limitFPS = false;
        targetFPS = 60;

        useGifBG = true;
        this.movie = movie;
        this.timeAddPerFrame = timeAddPerFrame;
        delay = 0;
        nextUpdateTime = 0;
        currMovieTime = 0;

        if(!managersAreInitialized){
            InitManagers(view);
            managersAreInitialized = true;
        }
    }

    private void InitManagers(final SurfaceView view){ //Init managers (if any)
        StateManager.Instance.Init(view);
        EntityManager.Instance.Init(view);
        ResourceManager.Instance.Init(view);
        AudioManager.Instance.Init(view);
        CurrencyManager.Instance.Init(view);
        SharedPrefsManager.Instance.Init(view);
    }

    @Override
    public void run(){
        final long framePerSecond = 1000 / targetFPS;
        long startTime;
        long prevTime = System.nanoTime();

        while(isRunning){
            startTime = System.currentTimeMillis();
            final long currTime = System.nanoTime();
            final float deltaTime = ((currTime - prevTime) / 1000000000.0f);
            prevTime = currTime;

            if(useGifBG){
                final long timeNow = android.os.SystemClock.uptimeMillis();
                if(nextUpdateTime == 0){
                    nextUpdateTime = timeNow + delay;
                } else if(timeNow >= nextUpdateTime){
                    currMovieTime += timeAddPerFrame;
                    nextUpdateTime = timeNow + delay;
                }
                if(currMovieTime >= movie.duration()){
                    currMovieTime = 0;
                }
                movie.setTime(currMovieTime);
            }
            
            StateManager.Instance.Update(deltaTime);

            //* Limit frame rate
            if(limitFPS){
                try{
                    long sleepTime = framePerSecond - (System.currentTimeMillis() - startTime);

                    if(sleepTime > 0){
                        sleep(sleepTime);
                    }
                } catch(final InterruptedException e){
                    isRunning = false;
                    Terminate();
                }
            }
            //*/
        }
    }

    /** @noinspection BooleanMethodIsAlwaysInverted*/
    public boolean GetIsRunning(){
        return isRunning;
    }

    public void Init(){
        isRunning = true;
    }

    public void Terminate(){
        isRunning = false;
    }

    public void SetLimitFPS(final boolean limitFPS){
        this.limitFPS = limitFPS;
    }

    public void SetTargetFPS(final long targetFPS){
        this.targetFPS = targetFPS;
    }

    public void SetDelay(final long delay){
        this.delay = delay;
    }

    private boolean isRunning;
    private boolean limitFPS;
    private long targetFPS;

    private final boolean useGifBG;
    private Movie movie;
    private long timeAddPerFrame;
    private long delay;
    private long nextUpdateTime;
    private int currMovieTime;

    static boolean managersAreInitialized;

    static{
        managersAreInitialized = false;
    }
}