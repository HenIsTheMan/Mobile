package sg.diploma.product.entity.entities;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityManager;

public final class EntityTextOnScreen extends EntityAbstract{
	public EntityTextOnScreen(final AssetManager assets){
		super();

		FPS = 0.0f;
		frameCount = 0;
		lastTime = 0;
		lastFrameTime = 0;

		paint = new Paint();
		myfont = Typeface.createFromAsset(assets, "fonts/grobold.ttf");
	}

	@Override
	public void Update(final float dt){
		final long currentTime = System.currentTimeMillis();
		lastTime = currentTime;

		if(currentTime - lastFrameTime > 1000){
			FPS = (++frameCount * 1000.f) / (currentTime - lastFrameTime);
			lastFrameTime = currentTime;
			frameCount = 0;
		}

	}

	@Override
	public void Render(final Canvas canvas){
		paint.setARGB(255, 0,0,0);
		paint.setStrokeWidth(200); // Stroke width is just the thickness of the appearance of the text
		paint.setTypeface(myfont); // using the type of font we defined
		paint.setTextSize(70);
		canvas.drawText("FPS: " + FPS, 30, 80, paint);
		// drawText(String text, float x, float y, Paint paint)
		// Draw the text, with origin at (x,y), using the specified paint.

		/*Paint paint = new Paint();
        paint.setARGB(255, 255,0 ,0);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE); // FILL_AND_STROKE
        _canvas.drawRect(ScreenWidth/20 + 5, ScreenHeight/25 - 5, 4 * ScreenWidth/20, 2 * ScreenHeight/25, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        _canvas.drawRect(ScreenWidth/20 + 8, ScreenHeight/25, ScreenWidth/20 + value, 2 * ScreenHeight/25 - 5, paint);*/
	}

	public static EntityTextOnScreen Create(final String key, final AssetManager assets){
		EntityTextOnScreen result = new EntityTextOnScreen(assets);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	float FPS;
	int frameCount;
	long lastTime;
	long lastFrameTime;

	Paint paint;
	Typeface myfont;
}