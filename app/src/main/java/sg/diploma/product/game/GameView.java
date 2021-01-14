package sg.diploma.product.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import sg.diploma.product.device.UpdateThread;

@SuppressLint("ViewConstructor")
public final class GameView extends SurfaceView{
    public GameView(final Context context, final int color){
        super(context);
        updateThread = new UpdateThread(this, color);
        InternalInit();
    }

    public GameView(final Context context, final int ID, final long timeAddPerFrame, final long delay){
        super(context);
        updateThread = new UpdateThread(this, ID, timeAddPerFrame);
        updateThread.SetDelay(delay);
        InternalInit();
    }

    private void InternalInit(){
        final SurfaceHolder surfaceHolder = getHolder(); //Holds content
        if(surfaceHolder != null){
            surfaceHolder.addCallback(new SurfaceHolder.Callback(){
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder){
                    if(!updateThread.GetIsRunning()){
                        updateThread.Init();
                    }

                    if(!updateThread.isAlive()){
                        updateThread.start();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){
                    //Nothing to type here becos it will be handled by the thread
                    //Can be used to modify the size of the view
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder){
                    updateThread.Terminate();
                }
            });
        }
    }

    private final UpdateThread updateThread;
}