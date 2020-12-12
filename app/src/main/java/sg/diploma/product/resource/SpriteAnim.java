package sg.diploma.product.resource;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import sg.diploma.product.math.Vector2;

public final class SpriteAnim{
	public SpriteAnim(){
		this(null, 0, 0, 0);
	}

	public SpriteAnim(Bitmap _bmp, int _rows, int _cols, int _fps){
		bmp = _bmp;

		xScale = 1.0f;
		yScale = 1.0f;

		rows = _rows;
		cols = _cols;
		width = bmp.getWidth() / _cols;
		height = bmp.getHeight() / _rows;

		currFrame = 0;
		startFrame = 0;
		endFrame = _cols * _rows;

		timePerFrame = 1.0f / (float)_fps;
		timeAcc = 0.0f;

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
	}

	public void Update(float _dt){
		timeAcc += _dt;
		if(timeAcc > timePerFrame){
			++currFrame;
			if(currFrame >= endFrame){
				currFrame = startFrame;
			}
			timeAcc = 0.0f;
		}
	}

	public void Render(final Canvas _canvas, int _x, int _y){
		int frameX = currFrame % cols;
		int frameY = currFrame / cols;
		int srcX = frameX * width;
		int srcY = frameY * height;

		_x -= 0.5f * width * xScale;
		_y -= 0.5f * height * yScale;

		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(_x, _y, _x + (int)((float)width * xScale), _y + (int)((float)height * yScale));

		_canvas.drawBitmap(bmp, src, dst, paint);
	}

	public void SetFrames(int _start, int _end){
		timeAcc = 0.0f;
		currFrame = _start;
		startFrame = _start;
		endFrame = _end;
	}

	public void GenScaledBitmap(Vector2 scale){
		bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() * (int)scale.x, bmp.getHeight() * (int)scale.y, true);
		width = bmp.getWidth() / cols;
		height = bmp.getHeight() / rows;
	}

	public void SetXScale(final float xScale){
		this.xScale = xScale;
	}

	public void SetYScale(final float yScale){
		this.yScale = yScale;
	}

	private Bitmap bmp;

	private float xScale;
	private float yScale;

	private final int rows;
	private final int cols;
	private int width;
	private int height;

	private int currFrame;
	private int startFrame;
	private int endFrame;

	private final float timePerFrame;
	private float timeAcc;

	private final Paint paint;
}