package sg.diploma.product.entity.entities;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.graphics.Color;

public final class EntityTextOnScreen extends EntityAbstract{
	public EntityTextOnScreen(final AssetManager assets, final String fPath){
		super();

		FPS = 0.0f;

		color = Color.white;
		paint = new Paint();
		myfont = Typeface.createFromAsset(assets, fPath);

		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.UI;
		attribs.type = EntityTypes.EntityType.TextOnScreen;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		strokeWidth = 100.0f;
		textSize = 55.0f;
	}

	@Override
	public void Update(final float dt){
		FPS = 1.0f / dt;
	}

	@Override
	public void Render(final Canvas canvas){
		paint.setARGB((int)(color.a * 255.0f), (int)(color.r * 255.0f), (int)(color.g * 255.0f), (int)(color.b * 255.0f));
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Paint.Style.FILL);
		paint.setTypeface(myfont);
		paint.setTextSize(textSize);
		canvas.drawText("FPS: " + FPS, attribs.pos.x, attribs.pos.y, paint);
	}

	public static EntityTextOnScreen Create(final String key, final AssetManager assets, final String fPath){
		EntityTextOnScreen result = new EntityTextOnScreen(assets, fPath);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetColor(final Color color){
		this.color = color;
	}

	public void SetStrokeWidth(final float strokeWidth){
		this.strokeWidth = strokeWidth;

	}

	public void SetTextSize(final float textSize){
		this.textSize = textSize;
	}

	private float FPS;

	private Color color;
	private final Paint paint;
	private final Typeface myfont;

	private float strokeWidth;
	private float textSize;
}