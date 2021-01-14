package sg.diploma.product.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Movie;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import sg.diploma.product.device.RenderThread;
import sg.diploma.product.device.UpdateThread;

@SuppressLint("ViewConstructor")
public final class GameView extends SurfaceView{
    public GameView(final Context context, final int color){
        super(context);

        movie = null;

        updateThread = new UpdateThread(this);
        renderThread = new RenderThread(this, color);

        InternalInit();
    }

    public GameView(final Context context, final int ID, final long timeAddPerFrame, final long delay){
        super(context);

        movie = Movie.decodeStream(getContext().getResources().openRawResource(ID));

        updateThread = new UpdateThread(this, movie, timeAddPerFrame);
        updateThread.SetDelay(delay);
        renderThread = new RenderThread(this, movie);

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
                    if(!renderThread.GetIsRunning()){
                        renderThread.Init();
                    }
                    if(!updateThread.isAlive()){
                        updateThread.start();
                    }
                    if(!renderThread.isAlive()){
                        renderThread.start();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){
                    //Nth to type here becos it will be handled by the thread
                    //Can be used to modify the size of the view
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder){
                    updateThread.Terminate();
                    renderThread.Terminate();
                }
            });
        }
    }

    public float GetRenderDt(){
        return renderThread.GetDt();
    }

    private final Movie movie;

    private final UpdateThread updateThread;
    private final RenderThread renderThread;
}