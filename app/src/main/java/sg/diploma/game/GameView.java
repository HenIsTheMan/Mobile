package sg.diploma.game;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import sg.diploma.game.thread.UpdateThread;

// Created by TanSiewLan2020
// GameView is the SurfaceView

public class GameView extends SurfaceView {
    // Surfaceview has a holder to be used to hold the content.
    private SurfaceHolder holder = null;

    //Thread to be known for its existence
    private UpdateThread updateThread = new UpdateThread(this);

    public GameView(Context _context)
    {
        super(_context);
        holder = getHolder();

        if (holder != null)
        {
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    //Setup some stuff to indicate whether thread is running and initialized
                    if (!updateThread.GetIsRunning())
                        updateThread.Init();

                    if (!updateThread.isAlive())
                        updateThread.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    // Nothing to type here cos it will be handle by the thread
                    // Can be used to modify the size of the view.
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    // Done then thread should not run too.
                    updateThread.Terminate();
                }
            });
        }

    }

    //Update thread -- updates all the managers eg: Entitymanager and StateManager
    //
    //
    //Inside run method StateManager.Instance.Start("Default"); ///edit!
    //
    //// In this code, my state name is "Default"
    //// Yours, " Level 1"
    //
    //Surfaceview in a state will have a canvas
    //so that u can render the canvas
    //// Fill the background color to reset
    //  canvas.drawColor(Color.BLACK);
    // locked canvas, sync canvas then u can draw.
    //
    // After draw, you have to unlock the canvas
    //
    // to draw on canvas, u need the thread to run.
}

