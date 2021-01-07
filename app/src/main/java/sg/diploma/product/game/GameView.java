package sg.diploma.product.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStream;

import sg.diploma.product.R;
import sg.diploma.product.device.UpdateThread;

public final class GameView extends SurfaceView{
    public GameView(final Context context){
        super(context);

        setWillNotDraw(false);
        final InputStream is = context.getResources().openRawResource(R.raw.game_background);
        movie = Movie.decodeStream(is);
        movieStart = 0;

        updateThread = new UpdateThread(this);
        SurfaceHolder surfaceHolder = getHolder(); //Holds content

        if(surfaceHolder != null){
            surfaceHolder.addCallback(new SurfaceHolder.Callback(){
                @Override
                public final void surfaceCreated(SurfaceHolder surfaceHolder){
                    if(!updateThread.GetIsRunning()){
                        updateThread.Init();
                    }

                    if(!updateThread.isAlive()){
                        updateThread.start();
                    }
                }

                @Override
                public final void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){
                    //Nothing to type here becos it will be handled by the thread
                    //Can be used to modify the size of the view
                }

                @Override
                public final void surfaceDestroyed(SurfaceHolder surfaceHolder){
                    updateThread.Terminate();
                }
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        final long now = android.os.SystemClock.uptimeMillis();
        if(movieStart == 0){
            movieStart = now;
        }

        final int relTime = (int)((now - movieStart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas, (float)getWidth() * 0.5f, (float)getHeight() * 0.5f);
        invalidate();
    }

    private final UpdateThread updateThread;
    private final Movie movie;
    private long movieStart;
}