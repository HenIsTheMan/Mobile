package sg.diploma.product.entity.entities;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.graphics.Color;

public final class EntityTextOnScreen extends EntityAbstract{
	public EntityTextOnScreen(final AssetManager assets, final String fPath){
		super();

		FPS = 0.0f;

		color = Color.white;
		paint = new Paint();
		myfont = Typeface.createFromAsset(assets, fPath);
	}

	@Override
	public void Update(final float dt){
		FPS = 1.0f / dt;
	}

	@Override
	public void Render(final Canvas canvas){
		paint.setARGB((int)(color.a * 255.0f), (int)(color.r * 255.0f), (int)(color.g * 255.0f), (int)(color.b * 255.0f));
		paint.setStrokeWidth(300);
		paint.setStyle(Paint.Style.FILL);
		paint.setTypeface(myfont);
		paint.setTextSize(55);
		canvas.drawText("FPS: " + FPS, 30.0f, 80.0f, paint);
	}

	public static EntityTextOnScreen Create(final String key, final AssetManager assets, final String fPath){
		EntityTextOnScreen result = new EntityTextOnScreen(assets, fPath);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetColor(final Color color){
		this.color = color;
	}

	private float FPS;

	private Color color;
	private final Paint paint;
	private final Typeface myfont;
}