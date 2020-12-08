package sg.diploma.game.thread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import sg.diploma.game.EntityManager;
import sg.diploma.game.GameSystem;
import sg.diploma.game.GameView;
import sg.diploma.game.StateManager;

public final class UpdateThread extends Thread{ //Need dedicated thread to run Surfaceview's update method
    public UpdateThread(){
        this(null);
    }

    public UpdateThread(GameView view){
        isRunning = false;
        holder = view.getHolder();

        ///Init managers (if any)
        StateManager.Instance.Init(view);
        EntityManager.Instance.Init(view);
        GameSystem.Instance.Init(view);
    }

    static final long targetFPS;

    static{
        targetFPS = 60;
    }

    private boolean isRunning;
    private SurfaceHolder holder;

    public boolean GetIsRunning(){
        return isRunning;
    }

    public void Init(){
        isRunning = true;
    }

    public void Terminate(){
        isRunning = false;
    }

    @Override
    public void run(){
        long framePerSecond = 1000 / targetFPS;
        long startTime = 0;

        long prevTime = System.nanoTime();
        StateManager.Instance.Start("MainGame");  // To edit to whichever state to start with.

        while(isRunning && StateManager.Instance.GetCurrentState() != "INVALID"){ //Main loop
            startTime = System.currentTimeMillis();
            final long currTime = System.nanoTime();
            final float deltaTime = ((currTime - prevTime) / 1000000000.0f);
            prevTime = currTime;

            StateManager.Instance.Update(deltaTime);

            ///Render
            Canvas canvas = holder.lockCanvas(null);
            if(canvas != null){
                synchronized (holder){ //Able to render
                    canvas.drawColor(Color.BLACK);
                    StateManager.Instance.Render(canvas);
                }
                holder.unlockCanvasAndPost(canvas);
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
}


