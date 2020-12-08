package sg.diploma.product.game;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import sg.diploma.product.thread.UpdateThread;

public class GameView extends SurfaceView {
    public GameView(final Context context){
        super(context);
        updateThread = new UpdateThread(this);
        holder = getHolder();

        if(holder != null){
            holder.addCallback(new SurfaceHolder.Callback(){
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if(!updateThread.GetIsRunning()){
                        updateThread.Init();
                    }

                    if(!updateThread.isAlive()){
                        updateThread.start();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    //Nothing to type here becos it will be handled by the thread
                    //Can be used to modify the size of the view
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    updateThread.Terminate();
                }
            });
        }
    }

    private UpdateThread updateThread;
    private SurfaceHolder holder; //Holds content
}