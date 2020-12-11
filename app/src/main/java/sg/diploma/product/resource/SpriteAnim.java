package sg.diploma.product.resource;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public final class SpriteAnim{
	public SpriteAnim(){
		this(null, 0, 0, 0);
	}

	public SpriteAnim(Bitmap _bmp, int _rows, int _cols, int _fps){
		bmp = _bmp;

		rows = _rows;
		cols = _cols;
		width = bmp.getWidth() / _cols;
		height = bmp.getHeight() / _rows;

		currFrame = 0;
		startFrame = 0;
		endFrame = _cols * _rows;

		timePerFrame = 1.0f / (float)_fps;
		timeAcc = 0.0f;
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

	public void Render(Canvas _canvas, int _x, int _y){
		int frameX = currFrame % cols; //??
		int frameY = currFrame / rows; //??
		int srcX = frameX * width;
		int srcY = frameY * height;

		_x -= 0.5f * width;
		_y -= 0.5f * height;

		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(_x, _y, _x + width, _y + height);
		_canvas.drawBitmap(bmp, src, dst, null);
	}

	public void SetAnimationFrames(int _start, int _end){
		timeAcc = 0.0f;
		currFrame = _start;
		startFrame = _start;
		endFrame = _end;
	}

	private Bitmap bmp = null;

	private final int rows;
	private final int cols;
	private final int width;
	private final int height;

	private int currFrame;
	private int startFrame;
	private int endFrame;

	private final float timePerFrame;
	private float timeAcc;
}