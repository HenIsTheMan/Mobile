package sg.diploma.product.entity.entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.graphics.Color;

public final class EntityPlat extends EntityAbstract{
	private EntityPlat(){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.Plat;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		color = new Color(0.2f, 0.2f, 0.2f, 0.6f);
		strokeWidth = 50.0f;
		paintStyle = Paint.Style.FILL;
		paint = new Paint();

		paint.setARGB(153, 51, 51, 51);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(paintStyle);
	}

	@Override
	public void Update(final float dt){
	}

	@Override
	public void Render(final Canvas canvas){
		canvas.drawRect(
			attribs.pos.x - attribs.scale.x * 0.5f,
			attribs.pos.y - attribs.scale.y * 0.5f,
			attribs.pos.x + attribs.scale.x * 0.5f,
			attribs.pos.y + attribs.scale.y * 0.5f,
			paint
		);
	}

	@Override
	public void Collided(){
		SetColor(new Color(1.0f, 1.0f, 0.0f, 0.8f));
	}

	public static EntityPlat Create(final String key){
		EntityPlat result = new EntityPlat();
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

	public void SetPaintStyle(final Paint.Style paintStyle){
		this.paintStyle = paintStyle;
		paint.setStyle(paintStyle);
	}

	private Color color;
	private float strokeWidth;
	private Paint.Style paintStyle;
	private final Paint paint;
}