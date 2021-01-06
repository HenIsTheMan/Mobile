package sg.diploma.product.device;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Objects;

import sg.diploma.product.audio.AudioManager;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.load_and_save.SharedPrefsManager;
import sg.diploma.product.state.StateManager;

public final class UpdateThread extends Thread{ //Need dedicated thread to run Surfaceview's update method
    public UpdateThread(){
        this(null);
    }

    public UpdateThread(SurfaceView view){
        isRunning = false;
        surfaceHolder = view.getHolder();

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
            
            StateManager.Instance.Update(deltaTime);

            ///Render
            if(!Objects.equals(StateManager.Instance.GetCurrentStateName(), "")){
                Canvas canvas = surfaceHolder.lockCanvas(null);
                if(canvas != null){
                    synchronized(surfaceHolder){ //Sync to draw
                        canvas.drawColor(Color.BLACK);
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

    public final void Init(){
        isRunning = true;
    }

    public final void Terminate(){
        isRunning = false;
    }

    private boolean isRunning;
    private final SurfaceHolder surfaceHolder;

    static final long targetFPS;

    static{
        targetFPS = 60;
    }
}