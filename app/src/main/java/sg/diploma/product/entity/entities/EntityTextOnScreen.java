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
	private EntityTextOnScreen(final AssetManager assets, final String fPath){
		super();

		color = Color.white;
		strokeWidth = 100.0f;
		paint = new Paint();

		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.UI;
		attribs.type = EntityTypes.EntityType.TextOnScreen;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.None;

		textSize = 55.0f;
		text = "";

		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.createFromAsset(assets, fPath));
	}

	@Override
	public void Update(final float dt){
	}

	@Override
	public void Render(final Canvas canvas){
		canvas.drawText(text, attribs.pos.x, attribs.pos.y, paint);
	}

	@Override
	public void LateUpdate(final float dt){
	}

	@Override
	public void SpecialRender(final Canvas canvas){
		canvas.drawText(text, attribs.pos.x, attribs.pos.y, paint);
	}

	@Override
	public void Collided(EntityAbstract other){
	}

	public static EntityTextOnScreen Create(final String key, final AssetManager assets, final String fPath){
		EntityTextOnScreen result = new EntityTextOnScreen(assets, fPath);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetColor(final Color color){
		this.color = color;
		paint.setARGB((int)(color.a * 255.0f), (int)(color.r * 255.0f), (int)(color.g * 255.0f), (int)(color.b * 255.0f));
	}

	public void SetStrokeWidth(final float strokeWidth){
		this.strokeWidth = strokeWidth;
		paint.setStrokeWidth(strokeWidth);
	}

	public void SetTextSize(final float textSize){
		this.textSize = textSize;
		paint.setTextSize(textSize);
	}

	public void SetText(final String text){
		this.text = text;
	}

	private Color color;
	private float strokeWidth;
	private final Paint paint;

	private float textSize;
	private String text;
}