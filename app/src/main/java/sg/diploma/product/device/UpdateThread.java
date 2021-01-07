package sg.diploma.product.device;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.InputStream;
import java.util.Objects;

import sg.diploma.product.R;
import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.load_and_save.SharedPrefsManager;
import sg.diploma.product.state.StateManager;

public final class UpdateThread extends Thread{ //Need dedicated thread to run Surfaceview's update method
    public UpdateThread(){
        this(null);
    }

    public UpdateThread(SurfaceView _view){
        view = _view;
        isRunning = false;
        surfaceHolder = view.getHolder();

        view.setLayerType(View.LAYER_TYPE_NONE, null);
        view.setWillNotDraw(false);
        final InputStream is = view.getContext().getResources().openRawResource(R.raw.game_background);
        movie = Movie.decodeStream(is);
        delay = 0;
        timeAddPerFrame = 5;
        nextUpdateTime = 0;
        currMovieTime = 0;

        ///Init managers (if any)
        StateManager.Instance.Init(view);
        EntityManager.Instance.Init(view);
        ResourceManager.Instance.Init(view);
        AudioManager.Instance.Init(view);
        SharedPrefsManager.Instance.Init(view);
    }

    @Override
    public final void run(){
        long framePerSecond = 1000 / targetFPS;
        long startTime;

        long prevTime = System.nanoTime();

        while(isRunning){ //Main loop
            startTime = System.currentTimeMillis();
            final long currTime = System.nanoTime();
            final float deltaTime = ((currTime - prevTime) / 1000000000.0f);
            prevTime = currTime;

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
            
            StateManager.Instance.Update(deltaTime);

            ///Render
            if(!Objects.equals(StateManager.Instance.GetCurrentStateName(), "")){
                Canvas canvas = surfaceHolder.lockCanvas(null);
                if(canvas != null){
                    synchronized(surfaceHolder){ //Sync to draw
                        canvas.drawColor(Color.BLACK);

                        final float viewWidthF = (float)view.getWidth();
                        final float viewHeightF = (float)view.getHeight();
                        final float movieWidthF = (float)movie.width();
                        final float movieHeightF = (float)movie.height();
                        movie.setTime(currMovieTime);
                        canvas.scale(viewWidthF / movieWidthF, viewHeightF / movieHeightF);
                        movie.draw(canvas, 0.0f, 0.0f);
                        canvas.scale(movieWidthF / viewWidthF, movieHeightF / viewHeightF);

                        StateManager.Instance.Render(canvas);

                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            //Control frame rate
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
    }

    public final boolean GetIsRunning(){
        return isRunning;
    }

    public final void Init(){
        isRunning = true;
    }

    public final void Terminate(){
        isRunning = false;
    }

    private boolean isRunning;
    private final SurfaceHolder surfaceHolder;
    private final SurfaceView view;

    private final Movie movie;
    private final long delay;
    private final long timeAddPerFrame;
    private long nextUpdateTime;
    private int currMovieTime;

    static final long targetFPS;

    static{
        targetFPS = 60;
    }
}