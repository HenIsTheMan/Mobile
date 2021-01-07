package sg.diploma.product.device;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Objects;

import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.load_and_save.SharedPrefsManager;
import sg.diploma.product.state.StateManager;

public final class UpdateThread extends Thread{ //Need dedicated thread to run Surfaceview's update method
    public UpdateThread(final SurfaceView view, final int ID, final long timeAddPerFrame){
        this.view = view;
        isRunning = false;
        surfaceHolder = this.view.getHolder();

        useGifBG = true;
        color = 0xFF333333;

        this.view.setLayerType(View.LAYER_TYPE_NONE, null);
        this.view.setWillNotDraw(false);
        movie = Movie.decodeStream(this.view.getContext().getResources().openRawResource(ID));
        this.timeAddPerFrame = timeAddPerFrame;
        delay = 0;
        nextUpdateTime = 0;
        currMovieTime = 0;

        ///Init managers (if any)
        StateManager.Instance.Init(this.view);
        EntityManager.Instance.Init(this.view);
        ResourceManager.Instance.Init(this.view);
        AudioManager.Instance.Init(this.view);
        SharedPrefsManager.Instance.Init(this.view);
    }

    @Override
    public void run(){
        long framePerSecond = 1000 / targetFPS;
        long startTime;

        long prevTime = System.nanoTime();

        while(isRunning){ //Main loop
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
            }
            
            StateManager.Instance.Update(deltaTime);

            ///Render
            if(!Objects.equals(StateManager.Instance.GetCurrentStateName(), "")){
                Canvas canvas = surfaceHolder.lockCanvas(null);
                if(canvas != null){
                    synchronized(surfaceHolder){ //Sync to draw
                        if(useGifBG){
                            final float viewWidthF = (float)view.getWidth();
                            final float viewHeightF = (float)view.getHeight();
                            final float movieWidthF = (float)movie.width();
                            final float movieHeightF = (float)movie.height();
                            movie.setTime(currMovieTime);
                            canvas.scale(viewWidthF / movieWidthF, viewHeightF / movieHeightF);
                            movie.draw(canvas, 0.0f, 0.0f);
                            canvas.scale(movieWidthF / viewWidthF, movieHeightF / viewHeightF);
                        } else{
                            canvas.drawColor(color);
                        }

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

    public boolean GetIsRunning(){
        return isRunning;
    }

    public void Init(){
        isRunning = true;
    }

    public void Terminate(){
        isRunning = false;
    }

    public void SetUseGifBG(final boolean useGifBG){
        this.useGifBG = useGifBG;
    }

    public void SetColor(final int color){
        this.color = color;
    }

    public void SetDelay(final long delay){
        this.delay = delay;
    }

    private boolean isRunning;
    private final SurfaceHolder surfaceHolder;
    private final SurfaceView view;

    private boolean useGifBG;
    private int color;

    private final Movie movie;
    private final long timeAddPerFrame;
    private long delay;
    private long nextUpdateTime;
    private int currMovieTime;

    static final long targetFPS;

    static{
        targetFPS = 60;
    }
}