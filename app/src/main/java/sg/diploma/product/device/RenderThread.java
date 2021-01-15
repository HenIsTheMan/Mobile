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
		super();
		setName("RenderThread_193541T");

		this.view = view;
		isRunning = false;
		surfaceHolder = this.view.getHolder();
		limitFPS = false;
		targetFPS = 60;

		dt = 0.0f;
		this.color = color;

		useGifBG = false;
	}

	public RenderThread(final SurfaceView view, final Movie movie){
		super();
		setName("RenderThread_193541T");

		this.view = view;
		isRunning = false;
		surfaceHolder = this.view.getHolder();
		limitFPS = false;
		targetFPS = 60;

		dt = 0.0f;

		useGifBG = true;
		this.movie = movie;
		this.view.setLayerType(View.LAYER_TYPE_NONE, null);
		this.view.setWillNotDraw(false);
	}

	@Override
	public void run(){
		long startTime;
		long prevTime = System.nanoTime();

		while(isRunning){
			final long currTime = System.nanoTime();
			dt = ((currTime - prevTime) / 1000000000.0f);
			prevTime = currTime;

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

						synchronized(StateManager.Instance){
							StateManager.Instance.Render(canvas);
						}
					}
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}

			//* Limit frame rate
			startTime = System.currentTimeMillis();
			if(limitFPS){
				final long framePerSecond = 1000 / targetFPS;
				try{
					final long sleepTime = framePerSecond - (System.currentTimeMillis() - startTime);

					if(sleepTime > 0){
						sleep(sleepTime);
					}
				} catch(final InterruptedException e){
					e.printStackTrace();
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

	public void Begin(){
		isRunning = true;
		if(!isAlive()){
			start();
		}
	}

	public void Terminate(){
		isRunning = false;
		if(isAlive()){
			try{
				join();
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public float GetDt(){
		return dt;
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

	private float dt;
	private int color;

	private final boolean useGifBG;
	private Movie movie;
}