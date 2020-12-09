package sg.diploma.product.thread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import sg.diploma.product.EntityManager;
import sg.diploma.product.game.GameManager;
import sg.diploma.product.game.GameView;
import sg.diploma.product.StateManager;

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
        GameManager.Instance.Init();
    }

    @Override
    public void run(){
        long framePerSecond = 1000 / targetFPS;
        long startTime = 0;

        long prevTime = System.nanoTime();
        StateManager.Instance.Start("MainMenu"); //Start state

        while(isRunning && StateManager.Instance.GetCurrentState() != "INVALID"){ //Main loop
            startTime = System.currentTimeMillis();
            final long currTime = System.nanoTime();
            final float deltaTime = ((currTime - prevTime) / 1000000000.0f);
            prevTime = currTime;

            StateManager.Instance.Update(deltaTime);

            ///Render
            Canvas canvas = holder.lockCanvas(null);
            if(canvas != null){
                synchronized (holder){ //Sync to draw
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

    public boolean GetIsRunning(){
        return isRunning;
    }

    public void Init(){
        isRunning = true;
    }

    public void Terminate(){
        isRunning = false;
    }

    private boolean isRunning;
    private SurfaceHolder holder;

    static final long targetFPS;

    static{
        targetFPS = 60;
    }
}