package sg.diploma.product.device;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Objects;

import sg.diploma.product.state.StateManager;

public final class RenderThread extends Thread{ //Need dedicated thread to run Surfaceview's Render method
	public RenderThread(final SurfaceView view, final int color){
		this.view = view;
		isRunning = false;
		surfaceHolder = this.view.getHolder();
		limitFPS = false;
		targetFPS = 60;

		useGifBG = false;
		this.color = color;
	}

	public RenderThread(final SurfaceView view, final Movie movie){
		this.view = view;
		isRunning = false;
		surfaceHolder = this.view.getHolder();
		limitFPS = false;
		targetFPS = 60;

		useGifBG = true;
		this.movie = movie;
		this.view.setLayerType(View.LAYER_TYPE_NONE, null);
		this.view.setWillNotDraw(false);
	}

	@Override
	public void run(){
		while(isRunning){
			if(!Objects.equals(StateManager.Instance.GetCurrentStateName(), "")){
				Canvas canvas = surfaceHolder.lockCanvas(null); //Origin is top left
				if(canvas != null){
					synchronized(surfaceHolder){ //Sync to draw
						if(useGifBG){
							final float viewWidthF = (float)view.getWidth();
							final float viewHeightF = (float)view.getHeight();
							final float movieWidthF = (float)movie.width();
							final float movieHeightF = (float)movie.height();
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

			final long framePerSecond = 1000 / targetFPS;
			final long startTime = System.currentTimeMillis();

			//* Limit frame rate
			if(limitFPS){
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
			//*/
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

	public void SetLimitFPS(final boolean limitFPS){
		this.limitFPS = limitFPS;
	}

	public void SetTargetFPS(final long targetFPS){
		this.targetFPS = targetFPS;
	}

	public void SetColor(final int color){
		this.color = color;
	}

	private boolean isRunning;
	private final SurfaceHolder surfaceHolder;
	private final SurfaceView view;
	private boolean limitFPS;
	private long targetFPS;

	private int color;

	private final boolean useGifBG;
	private Movie movie;
}